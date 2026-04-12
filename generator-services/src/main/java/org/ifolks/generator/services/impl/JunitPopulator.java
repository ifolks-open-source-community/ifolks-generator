package org.ifolks.generator.services.impl;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;

import org.ifolks.generator.components.population.commands.impl.PopulationCommandFactory;
import org.ifolks.generator.components.population.commands.interfaces.PopulationCommand;
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
 * The JunitPopulator is very similar to the @{link Populator}
 * The only differences are :
 * <li>We can restrict by packages and not tables : giving a list of tables would too tedious
 * <li>We don't consider any datasource to connect to except the project one. The only {@link PersistenceMode} to consider is CSV
 * 
 * @author Nicolas Thibault
 *
 */
@Component
public class JunitPopulator {

	private static final Logger logger = LoggerFactory.getLogger(JunitPopulator.class);
	
	@Autowired
	private PopulationCommandFactory commandFactory;
	
	@Autowired
	private PopulationFileLocator backupLocator;
	
	
	public void populate(Project project, Set<String> packages, String backupPath) {

		logger.info("start populating database");

		int maxSteps = FolderUtil.resolveMaxStep(backupPath);

		for (int step = 1; step <= maxSteps; step++) {
			logger.info("start bulding step " + step);
			for (Package myPackage : project.model.packages) {

				if (packages == null || packages.isEmpty() || packages.contains(myPackage.name)) {

					logger.info("start populating package : " + myPackage.name);

					for (Bean bean : myPackage.beans) {
						
						if (!bean.isEmbedded) {

							logger.info("start populating table : " + bean.table.name);
	
							String path = backupLocator.getBackupFilePath(backupPath, step, bean.table, PersistenceMode.CSV);
							
							if (Files.exists(Paths.get(path))) {
								PopulationCommand command = commandFactory.getPopulationCommand(bean, PersistenceMode.CSV, null);
								command.execute(path, bean);
								logger.info("populating table : " + bean.table.name + " completed");
							} else {
								logger.warn("populating table : " + bean.table.name + " : no backup found");
							}
						}
					}
					logger.info("populating package " + myPackage.name + " completed");
				} else {
					logger.info("package : " + myPackage.name + " skipped");
				}				
			}
		}
		logger.info("populating database completed");
	}
}
