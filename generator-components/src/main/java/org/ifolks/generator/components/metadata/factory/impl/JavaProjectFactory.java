package org.ifolks.generator.components.metadata.factory.impl;

import org.ifolks.generator.components.metadata.factory.interfaces.ModelFactory;
import org.ifolks.generator.components.metadata.factory.interfaces.ProjectFactory;
import org.ifolks.generator.model.domain.Locale;
import org.ifolks.generator.model.domain.Project;
import org.ifolks.generator.model.metadata.LocaleMetaData;
import org.ifolks.generator.model.metadata.ProjectMetaData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;


/**
 * 
 * @author Nicolas Thibault
 *
 */
@Component(value="javaProjectFactory")
public class JavaProjectFactory implements ProjectFactory {
	
	/*
	 * logger
	 */
	private static final Logger logger = LoggerFactory.getLogger(ProjectFactory.class);
	
	/*
	 * properties injected by spring
	 */
	@Autowired
	@Qualifier("javaModelFactory")
	private ModelFactory modelFactory;
	

	public Project buildProject(ProjectMetaData projectMetaData) {
		
		Project project = new Project();
		
		project.domainName = projectMetaData.getDomainName();
		project.projectName = projectMetaData.getProjectName();
		project.sourceFolder = projectMetaData.getSourceFolder();
		project.workspaceFolder = projectMetaData.getWorkspaceFolder();
		project.skeleton = projectMetaData.getSkeleton();
		project.databaseEngine = projectMetaData.getDatabaseEngine();
		project.tablesTableSpace = projectMetaData.tablesTableSpace;
		project.indexesTableSpace = projectMetaData.indexesTableSpace;
		project.dataSource = projectMetaData.getDataSource();

		if (projectMetaData.getSupportedLocales() != null) {
			if (projectMetaData.getSupportedLocales().getDefaultLocale() != null && !projectMetaData.getSupportedLocales().getDefaultLocale().trim().isEmpty()) {
				project.defaultLocale = projectMetaData.getSupportedLocales().getDefaultLocale().trim();
			}
			if (projectMetaData.getSupportedLocales().getLocales() != null) {
				for (LocaleMetaData locMeta : projectMetaData.getSupportedLocales().getLocales()) {
					if (locMeta.getCode() != null && !locMeta.getCode().trim().isEmpty()) {
						String label = (locMeta.getLabel() != null && !locMeta.getLabel().trim().isEmpty()) ? locMeta.getLabel().trim() : locMeta.getCode().trim();
						project.supportedLocales.add(new Locale(locMeta.getCode().trim(), label));
					}
				}
			}
			if (project.defaultLocale == null && !project.supportedLocales.isEmpty()) {
				project.defaultLocale = project.supportedLocales.get(0).code;
			}
		}
		
		logger.trace("start building model");
		project.model = modelFactory.buildModel(projectMetaData, project);
		logger.trace("end building model");
		
		return project;
	}

}
