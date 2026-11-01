package org.ifolks.generator.services;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Set;

import org.ifolks.generator.model.domain.Project;
import org.ifolks.generator.model.domain.business.Bean;
import org.ifolks.generator.model.metadata.ProjectMetaData;
import org.ifolks.generator.model.metadata.validation.ProjectValidationReport;
import org.ifolks.generator.skeletons.commands.impl.typed.JavaFileWriteCommand;
import org.ifolks.generator.skeletons.core.commands.persistence.SpecificationFileWriteCommand;
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
