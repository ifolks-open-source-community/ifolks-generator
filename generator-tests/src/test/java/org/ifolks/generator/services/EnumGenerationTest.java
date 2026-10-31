package org.ifolks.generator.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

import org.ifolks.generator.model.domain.Project;
import org.ifolks.generator.model.domain.business.Enumeration;
import org.ifolks.generator.model.metadata.ProjectMetaData;
import org.ifolks.generator.skeletons.core.commands.model.EnumFileWriteCommand;
import org.ifolks.generator.skeletons.core.layers.BusinessModelLayer;
import org.ifolks.generator.skeletons.tree.FileWriteCommandTreeNode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig(ApplicationConfig.class)
public class EnumGenerationTest {

	@Autowired
	private ProjectMetaDataService service;

	@Autowired
	private ProjectLoader loader;

	@Test
	public void testBusinessModelLayerRegistersEnumerations() {
		ProjectMetaData projectMetaData = service.loadProjectMetaData("src/test/resources/projects/success/2");
		Project project = loader.loadProject(projectMetaData);

		BusinessModelLayer layer = new BusinessModelLayer();
		FileWriteCommandTreeNode rootNode = layer.getGenerationNode(project);
		Assertions.assertNotNull(rootNode);

		boolean foundEnumCommand = false;
		for (int i = 0; i < rootNode.getChildCount(); i++) {
			FileWriteCommandTreeNode packageNode = (FileWriteCommandTreeNode) rootNode.getChildAt(i);
			for (int j = 0; j < packageNode.getChildCount(); j++) {
				FileWriteCommandTreeNode sectionNode = (FileWriteCommandTreeNode) packageNode.getChildAt(j);
				org.ifolks.generator.skeletons.tree.FileWriteCommandExecutor sectionExecutor = 
						(org.ifolks.generator.skeletons.tree.FileWriteCommandExecutor) sectionNode.getUserObject();
				if ("Enumerations".equals(sectionExecutor.getLabel())) {
					for (int k = 0; k < sectionNode.getChildCount(); k++) {
						FileWriteCommandTreeNode leafNode = (FileWriteCommandTreeNode) sectionNode.getChildAt(k);
						org.ifolks.generator.skeletons.tree.FileWriteCommandExecutor leafExecutor = 
								(org.ifolks.generator.skeletons.tree.FileWriteCommandExecutor) leafNode.getUserObject();
						if (leafExecutor.getCommand() instanceof EnumFileWriteCommand) {
							foundEnumCommand = true;
						}
					}
				}
			}
		}
		Assertions.assertTrue(foundEnumCommand, "BusinessModelLayer should register EnumFileWriteCommand");
	}

	@Test
	public void testEnumFileWriteCommandExecution() throws IOException {
		Path tempDir = Files.createTempDirectory("ifolks-enum-test");
		try {
			ProjectMetaData projectMetaData = service.loadProjectMetaData("src/test/resources/projects/success/2");
			Project project = loader.loadProject(projectMetaData);
			project.workspaceFolder = tempDir.toAbsolutePath().toString();

			Enumeration roleEnum = project.model.findEnumeration("OrganizationRole");
			EnumFileWriteCommand writeCommand = new EnumFileWriteCommand(roleEnum);
			writeCommand.execute();

			File targetFile = new File(writeCommand.getLabel());
			Assertions.assertTrue(targetFile.exists(), "Enum file should be generated on disk");

			String content = Files.readString(targetFile.toPath());
			Assertions.assertTrue(content.contains("package org.ifolks.demo.model.organizations;"));
			Assertions.assertTrue(content.contains("public enum OrganizationRole {"));
			Assertions.assertTrue(content.contains("HEADQUARTERS,"));
			Assertions.assertTrue(content.contains("SUBSIDIARY,"));
			Assertions.assertTrue(content.contains("BRANCH;"));
			Assertions.assertTrue(content.contains("/* Specific Code Start */"));
			Assertions.assertTrue(content.contains("/* Specific Code End */"));
		} finally {
			Files.walk(tempDir)
					.sorted(Comparator.reverseOrder())
					.map(Path::toFile)
					.forEach(File::delete);
		}
	}
}
