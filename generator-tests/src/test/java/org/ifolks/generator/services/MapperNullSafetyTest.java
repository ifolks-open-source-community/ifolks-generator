package org.ifolks.generator.services;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.ifolks.generator.model.domain.Project;
import org.ifolks.generator.model.domain.business.Bean;
import org.ifolks.generator.model.metadata.ProjectMetaData;
import org.ifolks.generator.skeletons.core.commands.components.mapper.BaseBasicViewMapperFileWriteCommand;
import org.ifolks.generator.skeletons.core.commands.components.mapper.BaseFormMapperFileWriteCommand;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig(ApplicationConfig.class)
public class MapperNullSafetyTest {

	@Autowired
	private ProjectMetaDataService service;

	@Autowired
	private ProjectLoader loader;

	@Test
	public void testBasicViewMapperNullSafety() throws IOException {
		ProjectMetaData projectMetaData = service.loadProjectMetaData("src/test/resources/projects/success/1");
		Project project = loader.loadProject(projectMetaData);

		Bean cityBean = project.model.findBean("CITY");
		assertNotNull(cityBean);

		BaseBasicViewMapperFileWriteCommand cmd = new BaseBasicViewMapperFileWriteCommand(cityBean);
		String content = cmd.generateContent();

		// Check multi-level reference navigation null safety
		assertTrue(content.contains("String regionCountryCode = (city.getRegion() != null && city.getRegion().getCountry() != null) ? city.getRegion().getCountry().getCode() : null;"),
				"CityBasicViewBaseMapper should generate null-safe ternary for multi-level reference navigation (region.country.code)");

		// Check single-level reference navigation null safety
		assertTrue(content.contains("String regionCode = city.getRegion() != null ? city.getRegion().getCode() : null;"),
				"CityBasicViewBaseMapper should generate null-safe ternary for single-level reference navigation (region.code)");

		// Check direct property navigation
		assertTrue(content.contains("String code = city.getCode();"),
				"CityBasicViewBaseMapper should directly call getter for direct property (code)");
	}

	@Test
	public void testFormMapperNullSafety() throws IOException {
		ProjectMetaData projectMetaData = service.loadProjectMetaData("src/test/resources/projects/success/1");
		Project project = loader.loadProject(projectMetaData);

		Bean cityBean = project.model.findBean("CITY");
		assertNotNull(cityBean);

		BaseFormMapperFileWriteCommand cmd = new BaseFormMapperFileWriteCommand(cityBean);
		String content = cmd.generateContent();

		// Check multi-level reference navigation null safety
		assertTrue(content.contains("String regionCountryCode = (city.getRegion() != null && city.getRegion().getCountry() != null) ? city.getRegion().getCountry().getCode() : null;"),
				"CityFormBaseMapper should generate null-safe ternary for multi-level reference navigation (region.country.code)");

		// Check single-level reference navigation null safety
		assertTrue(content.contains("String regionCode = city.getRegion() != null ? city.getRegion().getCode() : null;"),
				"CityFormBaseMapper should generate null-safe ternary for single-level reference navigation (region.code)");

		// Check direct property navigation
		assertTrue(content.contains("String code = city.getCode();"),
				"CityFormBaseMapper should directly call getter for direct property (code)");
	}
}
