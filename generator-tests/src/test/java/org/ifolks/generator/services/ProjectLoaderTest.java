package org.ifolks.generator.services;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Set;

import org.ifolks.generator.model.domain.Project;
import org.ifolks.generator.model.domain.business.Bean;
import org.ifolks.generator.model.domain.business.Enumeration;
import org.ifolks.generator.model.domain.business.Property;
import org.ifolks.generator.model.metadata.DataType;
import org.ifolks.generator.model.domain.ui.FilterProperty;
import org.ifolks.generator.model.domain.ui.ViewProperty;
import org.ifolks.generator.skeletons.angular.commands.model.TsEnumerationFileWriteCommand;
import org.ifolks.generator.skeletons.angular.commands.pages.list.TsListComponentFileWriteCommand;
import org.ifolks.generator.skeletons.angular.commands.pages.modal.TsModalComponentFileWriteCommand;
import org.ifolks.generator.skeletons.commands.impl.typed.TsFileWriteCommand;
import org.ifolks.generator.model.metadata.ProjectMetaData;
import org.ifolks.generator.model.metadata.validation.ProjectValidationReport;
import org.ifolks.generator.skeletons.commands.impl.typed.JavaFileWriteCommand;
import org.ifolks.generator.skeletons.core.commands.persistence.SpecificationFileWriteCommand;
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

		// Verify ViewProperty and FilterProperty enum metadata
		ViewProperty roleViewProp = orgBean.basicViewBean.properties.stream().filter(p -> "role".equals(p.name)).findFirst().orElse(null);
		Assertions.assertNotNull(roleViewProp);
		Assertions.assertEquals("OrganizationRole", roleViewProp.enumName);
		Assertions.assertEquals(DataType.ENUM, roleViewProp.dataType);

		FilterProperty roleFilterProp = orgBean.basicViewBean.filter.properties.stream().filter(p -> "role".equals(p.name)).findFirst().orElse(null);
		Assertions.assertNotNull(roleFilterProp);
		Assertions.assertEquals("OrganizationRole", roleFilterProp.enumName);
		Assertions.assertEquals(DataType.ENUM, roleFilterProp.dataType);

		// Verify Angular TsEnumerationFileWriteCommand
		TsEnumerationFileWriteCommand enumCmd = new TsEnumerationFileWriteCommand(roleEnum);
		Assertions.assertNotNull(enumCmd);

		// Verify Angular TsModalComponentFileWriteCommand imports enum and options
		try {
			TsModalComponentFileWriteCommand modalCmd = new TsModalComponentFileWriteCommand(orgBean);
			Method mModal = TsModalComponentFileWriteCommand.class.getDeclaredMethod("fetchSpecificImports");
			mModal.setAccessible(true);
			mModal.invoke(modalCmd);
			Field fImports = TsFileWriteCommand.class.getDeclaredField("imports");
			fImports.setAccessible(true);
			@SuppressWarnings("unchecked")
			Set<String> tsImports = (Set<String>) fImports.get(modalCmd);
			assertTrue(tsImports.stream().anyMatch(i -> i.contains("OrganizationRole") && i.contains("ORGANIZATION_ROLE_OPTIONS")), "Missing OrganizationRole options import in OrganizationModalComponent");

			TsListComponentFileWriteCommand listCmd = new TsListComponentFileWriteCommand(orgBean);
			Method mList = TsListComponentFileWriteCommand.class.getDeclaredMethod("fetchSpecificImports");
			mList.setAccessible(true);
			mList.invoke(listCmd);
			@SuppressWarnings("unchecked")
			Set<String> listImports = (Set<String>) fImports.get(listCmd);
			assertTrue(listImports.stream().anyMatch(i -> i.contains("OrganizationRole") && i.contains("ORGANIZATION_ROLE_OPTIONS")), "Missing OrganizationRole options import in OrganizationListComponent");
		} catch (Exception e) {
			Assertions.fail("Reflection error during Angular command import test: " + e.getMessage());
		}
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testNestedComponentImportsInSpecification() throws Exception {
		ProjectMetaData project = service.loadProjectMetaData("src/test/resources/projects/success/1");
		Project result = loader.loadProject(project);
		Bean cityBean = result.model.findBean("CITY");

		SpecificationFileWriteCommand cmd = new SpecificationFileWriteCommand(cityBean);
		Method m = SpecificationFileWriteCommand.class.getDeclaredMethod("fetchSpecificImports");
		m.setAccessible(true);
		m.invoke(cmd);

		Field f = JavaFileWriteCommand.class.getDeclaredField("javaImports");
		f.setAccessible(true);
		Set<String> imports = (Set<String>) f.get(cmd);

		assertTrue(imports.stream().anyMatch(i -> i.contains("Country;")), "Missing Country entity import in CitySpecification");
		assertTrue(imports.stream().anyMatch(i -> i.contains("Country_;")), "Missing Country_ metamodel import in CitySpecification");
		assertTrue(imports.stream().anyMatch(i -> i.contains("Region;")), "Missing Region entity import in CitySpecification");
		assertTrue(imports.stream().anyMatch(i -> i.contains("Region_;")), "Missing Region_ metamodel import in CitySpecification");
	}
}
