package org.ifolks.generator.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.ifolks.generator.model.domain.Project;
import org.ifolks.generator.model.domain.business.Bean;
import org.ifolks.generator.model.domain.ui.ViewProperty;
import org.ifolks.generator.model.metadata.ProjectMetaData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig(ApplicationConfig.class)
public class ViewPropertiesFactoryTest {

	@Autowired
	private ProjectMetaDataService service;

	@Autowired
	private ProjectLoader loader;

	@Test
	public void testCityViewProperties() {
		ProjectMetaData projectMetaData = service.loadProjectMetaData("src/test/resources/projects/success/1");
		Project project = loader.loadProject(projectMetaData);

		Bean cityBean = project.model.findBean("CITY");
		assertNotNull(cityBean);

		List<ViewProperty> props = cityBean.basicViewBean.properties;
		assertNotNull(props);

		// Check count and each property
		ViewProperty regionCountryCode = props.stream().filter(p -> "regionCountryCode".equals(p.name)).findFirst().orElse(null);
		assertNotNull(regionCountryCode);
		assertEquals("region.country.code", regionCountryCode.path);
		assertEquals("getRegion().getCountry().getCode()", regionCountryCode.mappingPath);
		assertEquals("regionCountry", regionCountryCode.joinedAliasName);
		assertEquals("code", regionCountryCode.lastPropertyName);
		assertEquals("Country", regionCountryCode.lastParentBeanClassName);
		assertNotNull(regionCountryCode.referenceBean);
		assertEquals("Country", regionCountryCode.referenceBean.className);

		ViewProperty regionCode = props.stream().filter(p -> "regionCode".equals(p.name)).findFirst().orElse(null);
		assertNotNull(regionCode);
		assertEquals("region.code", regionCode.path);
		assertEquals("getRegion().getCode()", regionCode.mappingPath);
		assertEquals("region", regionCode.joinedAliasName);
		assertEquals("code", regionCode.lastPropertyName);
		assertEquals("Region", regionCode.lastParentBeanClassName);
		assertNotNull(regionCode.referenceBean);
		assertEquals("Region", regionCode.referenceBean.className);

		ViewProperty code = props.stream().filter(p -> "code".equals(p.name)).findFirst().orElse(null);
		assertNotNull(code);
		assertEquals("code", code.path);
		assertEquals("getCode()", code.mappingPath);
		assertEquals("", code.joinedAliasName);
		assertEquals("code", code.lastPropertyName);
		assertEquals("City", code.lastParentBeanClassName);

		ViewProperty label = props.stream().filter(p -> "label".equals(p.name)).findFirst().orElse(null);
		assertNotNull(label);
		assertEquals("label", label.path);
		assertEquals("getLabel()", label.mappingPath);
		assertEquals("", label.joinedAliasName);
	}

	@Test
	public void testEmbeddedViewProperties() {
		ProjectMetaData projectMetaData = service.loadProjectMetaData("src/test/resources/projects/success/1");
		Project project = loader.loadProject(projectMetaData);

		Bean orgBean = project.model.findBean("ORGANIZATION");
		assertNotNull(orgBean);

		List<ViewProperty> props = orgBean.basicViewBean.properties;
		assertNotNull(props);

		ViewProperty descProp = props.stream().filter(p -> "description".equals(p.name)).findFirst().orElse(null);
		assertNotNull(descProp);
		assertEquals("organizationDescription.description", descProp.path);
		assertEquals("getOrganizationDescription().getDescription()", descProp.mappingPath);
		assertEquals("organizationDescription", descProp.joinedAliasName);
		assertEquals("description", descProp.lastPropertyName);
		assertEquals("OrganizationDescription", descProp.lastParentBeanClassName);
	}
}
