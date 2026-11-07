package org.ifolks.generator.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.ifolks.generator.model.domain.Project;
import org.ifolks.generator.model.domain.business.Bean;
import org.ifolks.generator.model.domain.business.OneToManyComponent;
import org.ifolks.generator.model.domain.ui.ViewProperty;
import org.ifolks.generator.model.metadata.ProjectMetaData;
import org.ifolks.generator.skeletons.core.commands.population.BeanPopulatorFileTemplateCommandFileWriteCommand;
import org.ifolks.generator.skeletons.core.commands.population.OneToManyComponentPopulatorFileTemplateCommandFileWriteCommand;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig(ApplicationConfig.class)
public class CsvPopulatorFileTemplateHeadersTest {

	@Autowired
	private ProjectMetaDataService service;

	@Autowired
	private ProjectLoader loader;

	@Test
	public void testCsvTemplateHeadersDotNotation() throws IOException {
		ProjectMetaData projectMetaData = service.loadProjectMetaData("src/test/resources/projects/success/1");
		Project project = loader.loadProject(projectMetaData);

		Bean cityBean = project.model.findBean("CITY");
		assertNotNull(cityBean);

		// Verify ViewProperty paths
		ViewProperty countryCodeProp = cityBean.formBean.properties.stream()
				.filter(p -> "region.country.code".equals(p.path))
				.findFirst()
				.orElse(null);
		assertNotNull(countryCodeProp, "Property path region.country.code should exist on City form bean");

		// Test Bean template command
		BeanPopulatorFileTemplateCommandFileWriteCommand beanCommand = new BeanPopulatorFileTemplateCommandFileWriteCommand(cityBean);
		String content = beanCommand.generateContent();

		assertTrue(content.contains("\"region.country.code\""), "Template header should contain dot-notation region.country.code");
		assertTrue(content.contains("\"region.code\""), "Template header should contain region.code");
		assertTrue(content.contains("\"code\""), "Template header should contain code");
		assertTrue(content.contains("\"label\""), "Template header should contain label");

		// Test Component template command
		Bean calendarBean = project.model.findBean("CALENDAR");
		assertNotNull(calendarBean);
		assertEquals(1, calendarBean.oneToManyComponentList.size());
		OneToManyComponent dayOffComponent = calendarBean.oneToManyComponentList.get(0);

		OneToManyComponentPopulatorFileTemplateCommandFileWriteCommand componentCommand =
				new OneToManyComponentPopulatorFileTemplateCommandFileWriteCommand(dayOffComponent);
		String componentContent = componentCommand.generateContent();

		// Parent attachment columns must be prefixed by component reference property (calendar)
		assertTrue(componentContent.contains("\"calendar.code\""), "Component template header should prefix parent reference property: calendar.code");
		assertTrue(componentContent.contains("\"dayOffDate\""), "Component template header should contain component properties");
	}
}
