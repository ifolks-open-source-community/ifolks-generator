package org.ifolks.generator.services;

import org.ifolks.generator.model.domain.Project;
import org.ifolks.generator.model.domain.business.Bean;
import org.ifolks.generator.model.domain.business.Enumeration;
import org.ifolks.generator.model.domain.business.Property;
import org.ifolks.generator.model.metadata.DataType;
import org.ifolks.generator.model.metadata.ProjectMetaData;
import org.ifolks.generator.model.metadata.validation.ProjectValidationReport;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig(ApplicationConfig.class)
public class ProjectLoaderTest {
	
	@Autowired
	private ProjectMetaDataService service;
	
	@Autowired
	private ProjectLoader loader;

	@Test
	public void testLoadSuccess() {
		ProjectMetaData project = service.loadProjectMetaData("src/test/resources/projects/success/1");
		ProjectValidationReport report = service.validate(project);
		Project result = loader.loadProject(project);
	}

	@Test
	public void testLoadSuccessWithEnumerations() {
		ProjectMetaData projectMetaData = service.loadProjectMetaData("src/test/resources/projects/success/2");
		ProjectValidationReport report = service.validate(projectMetaData);
		Assertions.assertFalse(report.hasErrors);
		Assertions.assertFalse(report.hasWarnings);

		Project project = loader.loadProject(projectMetaData);
		Assertions.assertNotNull(project);
		Assertions.assertNotNull(project.model);

		// Verify domain enumerations
		Enumeration roleEnum = project.model.findEnumeration("OrganizationRole");
		Assertions.assertNotNull(roleEnum);
		Assertions.assertEquals("OrganizationRole", roleEnum.name);
		Assertions.assertEquals(3, roleEnum.items.size());
		Assertions.assertEquals("HEADQUARTERS", roleEnum.items.get(0).name);
		Assertions.assertEquals("SUBSIDIARY", roleEnum.items.get(1).name);
		Assertions.assertEquals("BRANCH", roleEnum.items.get(2).name);

		Enumeration foolEnum = project.model.findEnumeration("FoolStatus");
		Assertions.assertNotNull(foolEnum);
		Assertions.assertEquals("FoolStatus", foolEnum.name);
		Assertions.assertEquals(3, foolEnum.items.size());

		// Verify bean properties
		Bean orgBean = project.model.findBean("ORGANIZATION");
		Property roleProp = orgBean.findPropertyByColumnName("ROLE");
		Assertions.assertNotNull(roleProp);
		Assertions.assertEquals(DataType.ENUM, roleProp.dataType);
		Assertions.assertEquals("OrganizationRole", roleProp.enumName);
		Assertions.assertEquals("OrganizationRole", roleProp.javaType);
		Assertions.assertEquals("string", roleProp.tsType);
		Assertions.assertFalse(roleProp.nullable);

		Bean foolBean = project.model.findBean("FOOL");
		Property statusProp = foolBean.findPropertyByColumnName("STATUS");
		Assertions.assertNotNull(statusProp);
		Assertions.assertEquals(DataType.ENUM, statusProp.dataType);
		Assertions.assertEquals("FoolStatus", statusProp.enumName);
		Assertions.assertEquals("FoolStatus", statusProp.javaType);
		Assertions.assertEquals("string", statusProp.tsType);
		Assertions.assertTrue(statusProp.nullable);

		// Verify strict fail-fast behavior
		Assertions.assertNull(project.model.findEnumeration(null));
		Assertions.assertNull(project.model.findEnumeration(""));
		Assertions.assertThrows(
				org.ifolks.generator.model.exception.EnumerationNotFoundException.class,
				() -> project.model.findEnumeration("UnknownEnum")
		);
	}
}
