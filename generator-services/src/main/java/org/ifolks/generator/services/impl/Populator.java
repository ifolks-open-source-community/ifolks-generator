package org.ifolks.generator.services.impl;

import java.util.Set;

import org.ifolks.generator.components.population.commands.impl.PopulationCommandFactory;
import org.ifolks.generator.components.population.commands.interfaces.PopulationCommand;
import org.ifolks.generator.components.population.datasources.InputDataSourceProvider;
import org.ifolks.generator.components.population.files.PopulationFileLocator;
import org.ifolks.generator.model.domain.Package;
import org.ifolks.generator.model.domain.Project;
import org.ifolks.generator.model.domain.business.Bean;
import org.ifolks.generator.model.metadata.PersistenceMode;
import org.ifolks.generator.model.util.folder.FolderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * The populator aims at populating the project database following a plan given in the backupPath<br>
 * The plan consists of a root folder withs several steps<br>
 * Each step will execute several {@link PopulationCommand}, potentially one per table<br>
 * We can give a list of the tables to be considered if we want to restrict the population to these tables<br>
 * 
 * @author Nicolas Thibault
 *
 */
@Component
public class Populator {

	private static final Logger logger = LoggerFactory.getLogger(Populator.class);
	
	@Autowired
	private PopulationCommandFactory commandFactory;
	
	@Autowired
	private PopulationFileLocator fileLocator;
	
	
	public void populate(InputDataSourceProvider inputDataSourceProvider, Project project, Set<String> tables, String backupPath) {

		logger.info("start populating database");

		int maxSteps = FolderUtil.resolveMaxStep(backupPath);

		for(int step=1; step<=maxSteps; step++){
			logger.info("start bulding step " + step);
			for (Package myPackage:project.model.packages) {
				logger.info("start populating package : " + myPackage.name);

				for (Bean bean:myPackage.beans) {

					if (!bean.isEmbedded && (tables == null || tables.contains(bean.table.originalName))) {

						logger.info("start populating table : " + bean.table.name);
						
						PersistenceMode mode = fileLocator.resolvePersistenceModeOrNull(backupPath, step, bean.table);
						
						if (mode != null) {							
							PopulationCommand command = commandFactory.getPopulationCommand(bean, mode, inputDataSourceProvider);								
							String path = fileLocator.getBackupFilePath(backupPath, step, bean.table, mode);										
							command.execute(path, bean);								
							logger.info("populating table : " + bean.table.name + " completed");
							
						} else {
							logger.warn("populating table : " + bean.table.name + " : no backup found");
						}
					} else {
						logger.info("table : " + bean.table.name + " skipped");
					}
				}
				logger.info("populating package " + myPackage.name + " completed");
			}
		}
		logger.info("populating database completed");

	}
}
