package org.ifolks.generator.services;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.ifolks.generator.components.build.DatabaseCleaner;
import org.ifolks.generator.components.build.TableBuilder;
import org.ifolks.generator.components.database.DatabaseHandlerDiscovery;
import org.ifolks.generator.model.domain.Package;
import org.ifolks.generator.model.domain.Project;
import org.ifolks.generator.model.domain.database.Table;
import org.ifolks.generator.model.exception.InvalidFileException;
import org.ifolks.generator.model.util.folder.FolderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DatabaseBuilder {
	
	private static final Logger logger = LoggerFactory.getLogger(DatabaseBuilder.class);
	
	@Autowired
	private DatabaseCleaner databaseCleaner;
	
	@Autowired
	private org.springframework.core.io.ResourceLoader resourceLoader;

	public void buildDatabase(DataSource dataSource, Project project, String engineName) throws InvalidFileException, IOException, SQLException {
		
		logger.info("start cleaning database");
		databaseCleaner.cleanDatabase(dataSource, project, engineName);
		logger.info("cleaning database completed");
		
		logger.info("start bulding database");		
		TableBuilder tableBuilder = new TableBuilder(project, dataSource, engineName, resourceLoader);		
		
		int maxStep = 0;
		while (resourceLoader.getResource("classpath:scripts/SQL/" + engineName + "/build/" + (maxStep + 1)).exists()) {
			maxStep++;
		}
		
		for (int step = 1; step <= maxStep; step++) {
			
			logger.info("start bulding step " + step);
		
			for (Package myPackage:project.model.packages) {
				logger.info("start building package : " + myPackage.name);
				
				for (Table table:myPackage.tables) {
					logger.info("start building table : " + table.name);
					tableBuilder.buildTable(table, step);					
					logger.info("building table : " + table.name + " completed");
				}
				logger.info("building package " + myPackage.name + " completed");
			}
			logger.info("bulding step " + step + " completed");
		}
		logger.info("bulding database completed");
	}
	


}
