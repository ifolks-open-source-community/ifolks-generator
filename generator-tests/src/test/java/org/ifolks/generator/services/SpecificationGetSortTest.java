package org.ifolks.generator.services;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.ifolks.generator.model.domain.Project;
import org.ifolks.generator.model.domain.business.Bean;
import org.ifolks.generator.model.metadata.ProjectMetaData;
import org.ifolks.generator.skeletons.core.commands.api.model.BasicViewBeanFileWriteCommand;
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

	@Test
	public void testSpecificationStaticImportsWithoutWildcard() throws IOException {
		ProjectMetaData projectMetaData = service.loadProjectMetaData("src/test/resources/projects/success/1");
		Project project = loader.loadProject(projectMetaData);

		Bean countryBean = project.model.findBean("COUNTRY");
		assertNotNull(countryBean);

		SpecificationFileWriteCommand specCmd = new SpecificationFileWriteCommand(countryBean);
		String specContent = specCmd.generateContent();

		assertFalse(specContent.contains("import static org.ifolks.commons.jpa.JpaCriteriaUtils.*;"),
				"Specification must not use wildcard import which prevents Spotless unused import removal");
		assertTrue(specContent.contains("import static org.ifolks.commons.jpa.JpaCriteriaUtils.addEqualsRestriction;"),
				"Specification must explicitly import static JpaCriteriaUtils methods");
		assertTrue(specContent.contains("import static org.ifolks.commons.jpa.JpaCriteriaUtils.addStringStartsWithRestriction;"),
				"Specification must explicitly import static JpaCriteriaUtils methods");
	}

	@Test
	public void testBasicViewSpacingWithoutUnusedImports() throws IOException {
		ProjectMetaData projectMetaData = service.loadProjectMetaData("src/test/resources/projects/success/1");
		Project project = loader.loadProject(projectMetaData);

		Bean countryBean = project.model.findBean("COUNTRY");
		assertNotNull(countryBean);

		BasicViewBeanFileWriteCommand basicViewCmd = new BasicViewBeanFileWriteCommand(countryBean);
		String basicViewContent = basicViewCmd.generateContent();

		// Country has no Date, DateTime, BigDecimal or Enum -> must not import them
		assertFalse(basicViewContent.contains("import java.time.LocalDate;"), "CountryBasicView must not import LocalDate");
		assertFalse(basicViewContent.contains("import java.time.OffsetDateTime;"), "CountryBasicView must not import OffsetDateTime");
		assertFalse(basicViewContent.contains("import java.math.BigDecimal;"), "CountryBasicView must not import BigDecimal");

		// When there are no imports, there should not be 2 blank lines (i.e. \n\n\n) between package and javadoc
		assertFalse(basicViewContent.contains(";\n\n\n/**") || basicViewContent.contains(";\r\n\r\n\r\n/**"),
				"BasicView without imports must have only a single blank line between package and javadoc");

		// CalendarDayOff has dayOffDate (DATETIME) -> must import OffsetDateTime
		Bean calendarDayOffBean = project.model.findBean("CALENDAR_DAY_OFF");
		if (calendarDayOffBean != null) {
			BasicViewBeanFileWriteCommand calDayOffCmd = new BasicViewBeanFileWriteCommand(calendarDayOffBean);
			String calDayOffContent = calDayOffCmd.generateContent();
			assertTrue(calDayOffContent.contains("import java.time.OffsetDateTime;"), "CalendarDayOffBasicView must import OffsetDateTime");
		}

		// Fool in project 2 has STATUS (ENUM) but no Date/DateTime/BigDecimal
		ProjectMetaData projectMetaData2 = service.loadProjectMetaData("src/test/resources/projects/success/2");
		Project project2 = loader.loadProject(projectMetaData2);
		Bean foolBean = project2.model.findBean("FOOL");
		if (foolBean != null) {
			BasicViewBeanFileWriteCommand foolCmd = new BasicViewBeanFileWriteCommand(foolBean);
			String foolContent = foolCmd.generateContent();
			assertTrue(foolContent.contains("import org.ifolks.demo.api.model.dummy.enums.FoolStatus;"), "FoolBasicView must import FoolStatus");
			assertFalse(foolContent.contains("import java.time.LocalDate;"), "FoolBasicView must not import LocalDate when not used");
			assertFalse(foolContent.contains("import java.math.BigDecimal;"), "FoolBasicView must not import BigDecimal when not used");
		}
	}
}
