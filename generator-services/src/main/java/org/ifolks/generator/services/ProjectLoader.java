package org.ifolks.generator.services;

import org.ifolks.generator.components.metadata.factory.interfaces.ProjectFactory;
import org.ifolks.generator.model.domain.Project;
import org.ifolks.generator.model.metadata.ProjectMetaData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;


@Component
public class ProjectLoader {

	/*
	 * logger
	 */
	private static final Logger logger = LoggerFactory.getLogger(ProjectLoader.class);
	
	/*
	 * properties injected by spring
	 */
	@Autowired
	@Qualifier("javaProjectFactory")
	private ProjectFactory projectFactory;
	

	public Project loadProject(ProjectMetaData projectMetaData) {
		
		logger.info("start building project : " + projectMetaData.getProjectName());
		Project project = projectFactory.buildProject(projectMetaData);
		logger.info("end building project : " + projectMetaData.getProjectName());
		return project;
	}

	
}
