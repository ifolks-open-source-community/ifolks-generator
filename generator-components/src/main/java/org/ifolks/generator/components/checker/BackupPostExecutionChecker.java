package org.ifolks.generator.components.checker;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.sql.DataSource;

import org.ifolks.generator.persistence.backup.check.TableNotEmptyChecker;
import org.ifolks.generator.model.backup.check.BackupPlanPostExecutionWarning;
import org.ifolks.generator.model.backup.check.BackupPlanWarningType;
import org.ifolks.generator.model.domain.Package;
import org.ifolks.generator.model.domain.Project;
import org.ifolks.generator.model.domain.database.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * After a population plan, checks if the targeted database has empty tables
 * @author Nicolas Thibault
 */
@Component
public class BackupPostExecutionChecker {
	
	private static final Logger logger = LoggerFactory.getLogger(BackupPostExecutionChecker.class);
	
	@Autowired
	private TableNotEmptyChecker tableNotEmptyChecker;

	public List<BackupPlanPostExecutionWarning> checkPlan(DataSource dataSource, Project project, Set<String> tables) {
		
		List<BackupPlanPostExecutionWarning> result = new ArrayList<>();
		
		for (Package myPackage:project.model.packages) {
			for (Table table:myPackage.tables) {
				if (tables == null || tables.contains(table.originalName)) {

					logger.info("start post checking table : " + table.name);
					if(tableNotEmptyChecker.isTableEmpty(dataSource, table)){
						result.add(new BackupPlanPostExecutionWarning(BackupPlanWarningType.EMPTY_TABLE, table));
					}
				} else {
					logger.info("table : " + table.name + " skipped");
				}
			}
		}
		
		return result;
	}
}
