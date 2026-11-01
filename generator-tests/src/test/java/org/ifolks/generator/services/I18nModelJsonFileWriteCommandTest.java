package org.ifolks.generator.services;

import java.io.IOException;

import org.ifolks.generator.model.domain.Project;
import org.ifolks.generator.model.metadata.ProjectMetaData;
import org.ifolks.generator.skeletons.angular.commands.i18n.I18nModelJsonFileWriteCommand;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig(ApplicationConfig.class)
public class I18nModelJsonFileWriteCommandTest {

	@Autowired
	private ProjectMetaDataService service;

	@Autowired
	private ProjectLoader loader;

	@Test
	public void testModelJsonContentIncludesEnumerations() throws IOException {
		ProjectMetaData projectMetaData = service.loadProjectMetaData("src/test/resources/projects/success/2");
		Project project = loader.loadProject(projectMetaData);

		I18nModelJsonFileWriteCommand cmd = new I18nModelJsonFileWriteCommand(project);
		String content = cmd.generateContent();

		Assertions.assertNotNull(content);
		Assertions.assertTrue(content.trim().startsWith("{"));
		Assertions.assertTrue(content.trim().endsWith("}"));

		// Verify bean entries
		Assertions.assertTrue(content.contains("\"countryList\": \"Countries\""));
		Assertions.assertTrue(content.contains("\"countryDetails\": \"Country details\""));

		// Verify enumeration entries
		Assertions.assertTrue(content.contains("\"OrganizationRole.HEADQUARTERS\": \"HEADQUARTERS\""));
		Assertions.assertTrue(content.contains("\"OrganizationRole.SUBSIDIARY\": \"SUBSIDIARY\""));
		Assertions.assertTrue(content.contains("\"OrganizationRole.BRANCH\": \"BRANCH\""));

		Assertions.assertTrue(content.contains("\"FoolStatus.DRAFT\": \"DRAFT\""));
		Assertions.assertTrue(content.contains("\"FoolStatus.ACTIVE\": \"ACTIVE\""));
		Assertions.assertTrue(content.contains("\"FoolStatus.ARCHIVED\": \"ARCHIVED\""));
	}
}
