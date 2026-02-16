package org.ifolks.generator.bl.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.ifolks.generator.model.domain.Project;
import org.ifolks.generator.model.metadata.ProjectMetaData;
import org.ifolks.generator.model.metadata.validation.ProjectValidationReport;
import org.ifolks.generator.services.interfaces.ProjectLoader;
import org.ifolks.generator.services.interfaces.ProjectMetaDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext-generator-test.xml" })
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
}
