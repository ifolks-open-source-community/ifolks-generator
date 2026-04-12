package org.ifolks.generator.components.population.checks;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.sql.DataSource;

import org.ifolks.generator.model.domain.Package;
import org.ifolks.generator.model.domain.Project;
import org.ifolks.generator.model.domain.database.Table;
import org.ifolks.generator.model.population.check.PopulationPlanPostExecutionWarning;
import org.ifolks.generator.model.population.check.PopulationPlanWarningType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * After a population plan, checks if the targeted database has empty tables
 * @author Nicolas Thibault
 */
@Component
public class PopulationPostExecutionChecker {
	
	private static final Logger logger = LoggerFactory.getLogger(PopulationPostExecutionChecker.class);
	
	@Autowired
	private TableNotEmptyChecker tableNotEmptyChecker;

	public List<PopulationPlanPostExecutionWarning> checkPlan(DataSource dataSource, Project project, Set<String> tables) {
		
		List<PopulationPlanPostExecutionWarning> result = new ArrayList<>();
		
		for (Package myPackage:project.model.packages) {
			for (Table table:myPackage.tables) {
				if (tables == null || tables.contains(table.originalName)) {

					logger.info("start post checking table : " + table.name);
					if(tableNotEmptyChecker.isTableEmpty(dataSource, table)){
						result.add(new PopulationPlanPostExecutionWarning(PopulationPlanWarningType.EMPTY_TABLE, table));
					}
				} else {
					logger.info("table : " + table.name + " skipped");
				}
			}
		}
		
		return result;
	}
}
