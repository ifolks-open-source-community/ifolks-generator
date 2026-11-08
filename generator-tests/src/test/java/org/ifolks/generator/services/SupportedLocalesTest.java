package org.ifolks.generator.services;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.ifolks.generator.model.domain.Project;
import org.ifolks.generator.model.metadata.ProjectMetaData;
import org.ifolks.generator.model.metadata.validation.ProjectValidationReport;
import org.ifolks.generator.skeletons.angular.commands.i18n.I18nConfigFileWriteCommand;
import org.ifolks.generator.skeletons.angular.commands.i18n.I18nModelJsonFileWriteCommand;
import org.ifolks.generator.skeletons.angular.commands.i18n.I18nOverlayJsonFileWriteCommand;
import org.ifolks.generator.skeletons.angular.layers.TypeScriptComponentsLayer;
import org.ifolks.generator.skeletons.commands.interfaces.FileWriteCommand;
import org.ifolks.generator.skeletons.tree.FileWriteCommandExecutor;
import org.ifolks.generator.skeletons.tree.FileWriteCommandTreeNode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig(ApplicationConfig.class)
public class SupportedLocalesTest {

	@Autowired
	private ProjectMetaDataService service;

	@Autowired
	private ProjectLoader loader;

	@Test
	public void testSupportedLocalesMetadataParsing() {
		ProjectMetaData projectMetaData = service.loadProjectMetaData("src/test/resources/projects/success/3");
		Assertions.assertNotNull(projectMetaData);
		Assertions.assertNotNull(projectMetaData.getSupportedLocales());
		Assertions.assertEquals("en_US", projectMetaData.getSupportedLocales().getDefaultLocale());
		Assertions.assertNotNull(projectMetaData.getSupportedLocales().getLocales());
		Assertions.assertEquals(2, projectMetaData.getSupportedLocales().getLocales().size());

		Assertions.assertEquals("en_US", projectMetaData.getSupportedLocales().getLocales().get(0).getCode());
		Assertions.assertEquals("English (US)", projectMetaData.getSupportedLocales().getLocales().get(0).getLabel());
		Assertions.assertEquals("fr_FR", projectMetaData.getSupportedLocales().getLocales().get(1).getCode());
		Assertions.assertEquals("Français (France)", projectMetaData.getSupportedLocales().getLocales().get(1).getLabel());

		ProjectValidationReport report = service.validate(projectMetaData);
		Assertions.assertFalse(report.hasErrors, "Validation must pass for supportedLocales");
	}

	@Test
	public void testProjectLoaderMapsSupportedLocales() {
		ProjectMetaData projectMetaData = service.loadProjectMetaData("src/test/resources/projects/success/3");
		Project project = loader.loadProject(projectMetaData);

		Assertions.assertNotNull(project.supportedLocales);
		Assertions.assertEquals(2, project.supportedLocales.size());
		Assertions.assertEquals("en_US", project.defaultLocale);

		Assertions.assertEquals("en_US", project.supportedLocales.get(0).code);
		Assertions.assertEquals("English (US)", project.supportedLocales.get(0).label);
		Assertions.assertEquals("fr_FR", project.supportedLocales.get(1).code);
		Assertions.assertEquals("Français (France)", project.supportedLocales.get(1).label);
	}

	@Test
	public void testProjectWithoutSupportedLocales() {
		ProjectMetaData projectMetaData = service.loadProjectMetaData("src/test/resources/projects/success/2");
		Project project = loader.loadProject(projectMetaData);

		Assertions.assertTrue(project.supportedLocales.isEmpty());
		Assertions.assertNull(project.defaultLocale);
	}

	@Test
	public void testTypeScriptComponentsLayerGenerationWithLocales() {
		ProjectMetaData projectMetaData = service.loadProjectMetaData("src/test/resources/projects/success/3");
		Project project = loader.loadProject(projectMetaData);

		TypeScriptComponentsLayer layer = new TypeScriptComponentsLayer();
		FileWriteCommandTreeNode genNode = layer.getGenerationNode(project);

		List<FileWriteCommand> allCommands = collectCommands(genNode);

		boolean hasConfigCommand = allCommands.stream().anyMatch(cmd -> cmd instanceof I18nConfigFileWriteCommand);
		boolean hasModelCommand = allCommands.stream().anyMatch(cmd -> cmd instanceof I18nModelJsonFileWriteCommand);

		Assertions.assertTrue(hasConfigCommand, "I18nConfigFileWriteCommand must be present in generation node");
		Assertions.assertTrue(hasModelCommand, "I18nModelJsonFileWriteCommand must be present in generation node");

		// Overlay commands should only be created for non-default locales (fr_FR, not en_US)
		boolean hasModelFr = false;
		boolean hasMainFr = false;
		boolean hasModelEn = false;
		boolean hasMainEn = false;

		for (FileWriteCommand cmd : allCommands) {
			if (cmd instanceof I18nOverlayJsonFileWriteCommand) {
				String label = cmd.getLabel().replace('\\', '/');
				if (label.endsWith("model_fr_FR.json")) {
					hasModelFr = true;
				}
				if (label.endsWith("main_fr_FR.json")) {
					hasMainFr = true;
				}
				if (label.endsWith("model_en_US.json")) {
					hasModelEn = true;
				}
				if (label.endsWith("main_en_US.json")) {
					hasMainEn = true;
				}
			}
		}

		Assertions.assertTrue(hasModelFr, "model_fr_FR.json overlay command must be present in generation node");
		Assertions.assertTrue(hasMainFr, "main_fr_FR.json overlay command must be present in generation node");
		Assertions.assertFalse(hasModelEn, "model_en_US.json overlay command must NOT be present for default locale");
		Assertions.assertFalse(hasMainEn, "main_en_US.json overlay command must NOT be present for default locale");
	}

	@Test
	public void testTypeScriptComponentsLayerGenerationWithoutLocales() {
		ProjectMetaData projectMetaData = service.loadProjectMetaData("src/test/resources/projects/success/2");
		Project project = loader.loadProject(projectMetaData);

		TypeScriptComponentsLayer layer = new TypeScriptComponentsLayer();
		FileWriteCommandTreeNode genNode = layer.getGenerationNode(project);

		List<FileWriteCommand> allCommands = collectCommands(genNode);

		boolean hasConfigCommand = allCommands.stream().anyMatch(cmd -> cmd instanceof I18nConfigFileWriteCommand);
		Assertions.assertTrue(hasConfigCommand, "I18nConfigFileWriteCommand must be present in generation node even without locales");

		long overlayCommandCount = allCommands.stream()
				.filter(cmd -> cmd instanceof I18nOverlayJsonFileWriteCommand)
				.count();

		Assertions.assertEquals(0, overlayCommandCount, "No overlay commands should be created without supportedLocales");
	}

	@Test
	public void testTypeScriptComponentsLayerInitializationHasNoOverlays() {
		ProjectMetaData projectMetaData = service.loadProjectMetaData("src/test/resources/projects/success/3");
		Project project = loader.loadProject(projectMetaData);

		TypeScriptComponentsLayer layer = new TypeScriptComponentsLayer();
		FileWriteCommandTreeNode initNode = layer.getInitializationNode(project);

		List<FileWriteCommand> allCommands = collectCommands(initNode);
		long overlayCommandCount = allCommands.stream()
				.filter(cmd -> cmd instanceof I18nOverlayJsonFileWriteCommand)
				.count();

		Assertions.assertEquals(0, overlayCommandCount, "Initialization node must NEVER contain overlay commands");
	}

	@Test
	public void testI18nOverlayExecutionDoesNotOverwriteExistingFile(@TempDir Path tempDir) throws IOException {
		Project project = new Project();
		project.projectName = "testapp";
		project.workspaceFolder = tempDir.toString();

		Path i18nDir = tempDir.resolve("testapp-ui").resolve("src").resolve("assets").resolve("i18n");
		Files.createDirectories(i18nDir);

		// Pre-existing overlay file with custom content
		Path existingFile = i18nDir.resolve("model_fr_FR.json");
		String customContent = "{\"customKey\": \"Custom Value\"}";
		Files.writeString(existingFile, customContent, StandardCharsets.UTF_8);

		I18nOverlayJsonFileWriteCommand cmd = new I18nOverlayJsonFileWriteCommand(project, "model_fr_FR");
		cmd.execute();

		String contentAfter = Files.readString(existingFile, StandardCharsets.UTF_8);
		Assertions.assertEquals(customContent, contentAfter, "Existing overlay file content must NOT be overwritten");

		// Non-existing overlay file should be created with empty JSON object
		Path newFile = i18nDir.resolve("main_fr_FR.json");
		Assertions.assertFalse(Files.exists(newFile));

		I18nOverlayJsonFileWriteCommand cmd2 = new I18nOverlayJsonFileWriteCommand(project, "main_fr_FR");
		cmd2.execute();

		Assertions.assertTrue(Files.exists(newFile));
		String createdContent = Files.readString(newFile, StandardCharsets.UTF_8).trim();
		Assertions.assertEquals("{}", createdContent, "New overlay file must contain empty JSON object");
	}

	@Test
	public void testI18nConfigFileWriteCommandWithLocales(@TempDir Path tempDir) throws IOException {
		ProjectMetaData projectMetaData = service.loadProjectMetaData("src/test/resources/projects/success/3");
		Project project = loader.loadProject(projectMetaData);
		project.workspaceFolder = tempDir.toString();

		I18nConfigFileWriteCommand cmd = new I18nConfigFileWriteCommand(project);
		cmd.execute();

		Path configFile = tempDir.resolve(project.model.tsUiArtefactName).resolve("src").resolve("app").resolve("core").resolve("services").resolve("i18n.config.ts");
		Assertions.assertTrue(Files.exists(configFile), "i18n.config.ts must be created");

		String content = Files.readString(configFile, StandardCharsets.UTF_8);
		Assertions.assertTrue(content.contains("import { I18nConfig } from './I18nService';"));
		Assertions.assertTrue(content.contains("defaultLocale: 'en_US'"));
		Assertions.assertTrue(content.contains("{ code: 'en_US', label: 'English (US)' }"));
		Assertions.assertTrue(content.contains("{ code: 'fr_FR', label: 'Français (France)' }"));
	}

	@Test
	public void testI18nConfigFileWriteCommandWithoutLocales(@TempDir Path tempDir) throws IOException {
		ProjectMetaData projectMetaData = service.loadProjectMetaData("src/test/resources/projects/success/2");
		Project project = loader.loadProject(projectMetaData);
		project.workspaceFolder = tempDir.toString();

		I18nConfigFileWriteCommand cmd = new I18nConfigFileWriteCommand(project);
		cmd.execute();

		Path configFile = tempDir.resolve(project.model.tsUiArtefactName).resolve("src").resolve("app").resolve("core").resolve("services").resolve("i18n.config.ts");
		Assertions.assertTrue(Files.exists(configFile), "i18n.config.ts must be created");

		String content = Files.readString(configFile, StandardCharsets.UTF_8);
		Assertions.assertTrue(content.contains("import { I18nConfig } from './I18nService';"));
		Assertions.assertTrue(content.contains("export const i18nConfig: I18nConfig = {};"));
	}

	@Test
	public void testMainTsTemplateUsesI18nConfig() throws IOException {
		String mainTsContent = readClasspathResource("/ui/src/main.ts");
		Assertions.assertNotNull(mainTsContent);
		Assertions.assertTrue(mainTsContent.contains("import { i18nConfig } from './app/core/services/i18n.config';"), "main.ts must import i18nConfig");
		Assertions.assertTrue(mainTsContent.contains("return () => i18nService.init(i18nConfig);"), "main.ts must pass i18nConfig to i18nService.init");
	}

	@Test
	public void testHeaderTemplatesContainLanguageSwitcher() throws IOException {
		// Private header
		String privateHtml = readClasspathResource("/ui/src/app/templates/private/components/header/header.component.html");
		Assertions.assertTrue(privateHtml.contains("@if (supportedLocales.length > 1)"), "Private header must check supportedLocales.length > 1");
		Assertions.assertTrue(privateHtml.contains("svgIcon=\"translate\""), "Private header must use translate SVG icon");
		Assertions.assertTrue(privateHtml.contains("[matMenuTriggerFor]=\"langMenu\""), "Private header must trigger langMenu");
		Assertions.assertTrue(privateHtml.contains("@for (loc of supportedLocales; track loc.code)"), "Private header must loop over supportedLocales");
		Assertions.assertTrue(privateHtml.contains("(click)=\"changeLocale(loc.code)\""), "Private header must call changeLocale");
		Assertions.assertTrue(privateHtml.contains("{{ loc.label }}"), "Private header must display native label");

		String privateTs = readClasspathResource("/ui/src/app/templates/private/components/header/header.component.ts");
		Assertions.assertTrue(privateTs.contains("I18nService"), "Private header TS must import I18nService");
		Assertions.assertTrue(privateTs.contains("get supportedLocales()"), "Private header TS must have supportedLocales getter");
		Assertions.assertTrue(privateTs.contains("changeLocale(locale: string)"), "Private header TS must have changeLocale method");

		String privateScss = readClasspathResource("/ui/src/app/templates/private/components/header/header.component.scss");
		Assertions.assertTrue(privateScss.contains(".app-language-switcher"), "Private header SCSS must style .app-language-switcher");

		// Public header
		String publicHtml = readClasspathResource("/ui/src/app/templates/public/components/header/header.component.html");
		Assertions.assertTrue(publicHtml.contains("@if (supportedLocales.length > 1)"), "Public header must check supportedLocales.length > 1");
		Assertions.assertTrue(publicHtml.contains("svgIcon=\"translate\""), "Public header must use translate SVG icon");
		Assertions.assertTrue(publicHtml.contains("[matMenuTriggerFor]=\"langMenu\""), "Public header must trigger langMenu");
		Assertions.assertTrue(publicHtml.contains("@for (loc of supportedLocales; track loc.code)"), "Public header must loop over supportedLocales");
		Assertions.assertTrue(publicHtml.contains("(click)=\"changeLocale(loc.code)\""), "Public header must call changeLocale");
		Assertions.assertTrue(publicHtml.contains("{{ loc.label }}"), "Public header must display native label");

		String publicTs = readClasspathResource("/ui/src/app/templates/public/components/header/header.component.ts");
		Assertions.assertTrue(publicTs.contains("I18nService"), "Public header TS must import I18nService");
		Assertions.assertTrue(publicTs.contains("get supportedLocales()"), "Public header TS must have supportedLocales getter");
		Assertions.assertTrue(publicTs.contains("changeLocale(locale: string)"), "Public header TS must have changeLocale method");

		String publicScss = readClasspathResource("/ui/src/app/templates/public/components/header/header.component.scss");
		Assertions.assertTrue(publicScss.contains(".app-language-switcher"), "Public header SCSS must style .app-language-switcher");
	}

	private String readClasspathResource(String location) throws IOException {
		try (InputStream in = getClass().getResourceAsStream(location)) {
			Assertions.assertNotNull(in, "Resource not found on classpath: " + location);
			return new String(in.readAllBytes(), StandardCharsets.UTF_8);
		}
	}

	private List<FileWriteCommand> collectCommands(FileWriteCommandTreeNode node) {
		List<FileWriteCommand> commands = new ArrayList<>();
		if (node == null) {
			return commands;
		}
		if (node.getUserObject() instanceof FileWriteCommandExecutor) {
			FileWriteCommand cmd = ((FileWriteCommandExecutor) node.getUserObject()).getCommand();
			if (cmd != null) {
				commands.add(cmd);
			}
		}
		int count = node.getChildCount();
		for (int i = 0; i < count; i++) {
			commands.addAll(collectCommands((FileWriteCommandTreeNode) node.getChildAt(i)));
		}
		return commands;
	}
}
