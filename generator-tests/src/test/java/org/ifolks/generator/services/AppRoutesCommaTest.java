package org.ifolks.generator.services;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.ifolks.generator.model.domain.Project;
import org.ifolks.generator.model.metadata.ProjectMetaData;
import org.ifolks.generator.skeletons.angular.commands.pages.TsAppRoutingModuleFileWriteCommand;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

/**
 * GENERATOR-54 : app.routes.ts must survive a Prettier pass followed by a new generation
 */
@SpringJUnitConfig(ApplicationConfig.class)
public class AppRoutesCommaTest {

	private static final String START = "/* Specific Code Start */";
	private static final String END = "/* Specific Code End */";

	@Autowired
	private ProjectMetaDataService service;

	@Autowired
	private ProjectLoader loader;

	private String generate(Path workspace, String existingSpecificCode) throws IOException {
		ProjectMetaData projectMetaData = service.loadProjectMetaData("src/test/resources/projects/success/1");
		Project project = loader.loadProject(projectMetaData);
		project.workspaceFolder = workspace.toString();

		if (existingSpecificCode != null) {
			Path appFolder = workspace.resolve(project.model.tsUiArtefactName).resolve("src").resolve("app");
			Files.createDirectories(appFolder);
			Files.writeString(appFolder.resolve("app.routes.ts"),
					"export const routes: Routes = [\n" + START + "\n" + existingSpecificCode + END + "\n];\n");
		}
		return new TsAppRoutingModuleFileWriteCommand(project).generateContent();
	}

	private String lineBefore(String content, String mark) {
		String head = content.substring(0, content.indexOf(mark)).stripTrailing();
		return head.substring(head.lastIndexOf('\n') + 1);
	}

	@Test
	public void testTrailingCommaAfterEachGeneratedRoute(@TempDir Path tempDir) throws IOException {
		// specific code as written by the initial skeleton, or as left by Prettier
		String content = generate(tempDir, "{path:'**', redirectTo: '404'},\n");

		assertTrue(lineBefore(content, START).endsWith("},"), "last generated route must end with a comma");
		assertFalse(content.contains(",\n" + START + "\n,"), "no array hole between generated and specific routes");
		assertTrue(content.contains(START + "\n{path:'**', redirectTo: '404'},\n" + END), "specific code must be preserved");
	}

	@Test
	public void testNoSpecificCodeYet(@TempDir Path tempDir) throws IOException {
		String content = generate(tempDir, null);
		assertTrue(lineBefore(content, START).endsWith("},"), "trailing comma is valid even without specific routes");
	}

	@Test
	public void testLegacyLeadingCommaSpecificCodeStillCompiles(@TempDir Path tempDir) throws IOException {
		// projects generated before GENERATOR-54 and never reformatted
		String content = generate(tempDir, ",{path:'**', redirectTo: '404'}\n");

		assertTrue(lineBefore(content, START).endsWith("}"), "last generated route must not end with a comma when specific code starts with one");
		assertFalse(lineBefore(content, START).endsWith(","));
	}
}
