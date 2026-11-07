package org.ifolks.generator.services;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.ifolks.generator.model.domain.Project;
import org.ifolks.generator.model.domain.business.Bean;
import org.ifolks.generator.model.metadata.ProjectMetaData;
import org.ifolks.generator.skeletons.core.commands.persistence.SpecificationFileWriteCommand;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig(ApplicationConfig.class)
public class SpecificationGetSortTest {

	@Autowired
	private ProjectMetaDataService service;

	@Autowired
	private ProjectLoader loader;

	@Test
	public void testGetSortUsesDotNotationPropertyPath() throws IOException {
		ProjectMetaData projectMetaData = service.loadProjectMetaData("src/test/resources/projects/success/1");
		Project project = loader.loadProject(projectMetaData);

		Bean cityBean = project.model.findBean("CITY");
		assertNotNull(cityBean);

		SpecificationFileWriteCommand citySpecCmd = new SpecificationFileWriteCommand(cityBean);
		String citySpecContent = citySpecCmd.generateContent();

		// Check root and chained relation sort paths
		assertTrue(citySpecContent.contains("new Sort.Order(sorting.getRegionCountryCodeOrderType() == OrderType.ASC ? Sort.Direction.ASC : Sort.Direction.DESC, \"region.country.code\")"),
				"CitySpecification getSort should sort by nested path region.country.code");
		assertTrue(citySpecContent.contains("new Sort.Order(sorting.getRegionCodeOrderType() == OrderType.ASC ? Sort.Direction.ASC : Sort.Direction.DESC, \"region.code\")"),
				"CitySpecification getSort should sort by path region.code");
		assertTrue(citySpecContent.contains("new Sort.Order(sorting.getCodeOrderType() == OrderType.ASC ? Sort.Direction.ASC : Sort.Direction.DESC, \"code\")"),
				"CitySpecification getSort should sort by code");
		assertTrue(citySpecContent.contains("new Sort.Order(sorting.getLabelOrderType() == OrderType.ASC ? Sort.Direction.ASC : Sort.Direction.DESC, \"label\")"),
				"CitySpecification getSort should sort by label");

		// Test component getSort on Calendar / CalendarDayOff
		Bean calendarBean = project.model.findBean("CALENDAR");
		assertNotNull(calendarBean);

		SpecificationFileWriteCommand calSpecCmd = new SpecificationFileWriteCommand(calendarBean);
		String calSpecContent = calSpecCmd.generateContent();

		assertTrue(calSpecContent.contains("new Sort.Order(sorting.getDayOffDateOrderType() == OrderType.ASC ? Sort.Direction.ASC : Sort.Direction.DESC, \"dayOffDate\")"),
				"CalendarSpecification getSort for CalendarDayOff should sort by dayOffDate");
	}
}
