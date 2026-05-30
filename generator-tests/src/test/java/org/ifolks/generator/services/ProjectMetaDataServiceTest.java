package org.ifolks.generator.services;

import org.ifolks.generator.model.metadata.ProjectMetaData;
import org.ifolks.generator.model.metadata.validation.ProjectValidationReport;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig(ApplicationConfig.class)
public class ProjectMetaDataServiceTest {
	
	@Autowired
	private ProjectMetaDataService service;
	
	
	@Test
	public void testLoadSuccess() {
		ProjectMetaData project = service.loadProjectMetaData("src/test/resources/projects/success/1");
		ProjectValidationReport report = service.validate(project);
		Assertions.assertFalse(report.hasErrors);
		Assertions.assertFalse(report.hasWarnings);
	}
	
	
	@Test
	public void testLoadFailureOnDuplicateTables() {
		ProjectMetaData project = service.loadProjectMetaData("src/test/resources/projects/failure/1");
		ProjectValidationReport report = service.validate(project);
		report.print();
		Assertions.assertTrue(report.hasErrors);
		Assertions.assertFalse(report.hasWarnings);
	}
	
	
	@Test
	public void testLoadFailureOnInvalidTableReference() {
		ProjectMetaData project = service.loadProjectMetaData("src/test/resources/projects/failure/2");
		ProjectValidationReport report = service.validate(project);
		report.print();
		Assertions.assertTrue(report.hasErrors);
		Assertions.assertFalse(report.hasWarnings);
	}
	
	
	@Test
	public void testLoadFailureOnInvalidIdType() {
		ProjectMetaData project = service.loadProjectMetaData("src/test/resources/projects/failure/3");
		ProjectValidationReport report = service.validate(project);
		report.print();
		Assertions.assertTrue(report.hasErrors);
		Assertions.assertFalse(report.hasWarnings);
	}
}

