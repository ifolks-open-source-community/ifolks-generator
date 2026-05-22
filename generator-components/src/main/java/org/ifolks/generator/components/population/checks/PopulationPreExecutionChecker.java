package org.ifolks.generator.components.population.checks;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.ifolks.generator.components.population.datasources.InputDataSourceProvider;
import org.ifolks.generator.components.population.files.PopulationFileLocator;
import org.ifolks.generator.model.domain.Package;
import org.ifolks.generator.model.domain.Project;
import org.ifolks.generator.model.domain.database.Table;
import org.ifolks.generator.model.metadata.PersistenceMode;
import org.ifolks.generator.model.population.check.PopulationPlanPreExecutionWarning;
import org.ifolks.generator.model.population.check.PopulationPlanWarningType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Checks if the backup scripts are
 * <ul>
 * <li>reading data from a source that is not the production source</li>
 * <li>reading data from hardcoded values</li>
 * <li>absent !
 * </ul>
 * 
 * Configuration pre-requisite : The productionSourceReference needs to be defined in the {@link InputDataSourceProvider}
 * If these conditions are not met, a warning is presented to the user
 * @author Mounir Regragui
 *
 */
@Component
public class PopulationPreExecutionChecker {
	
	private static final Logger logger = LoggerFactory.getLogger(PopulationPreExecutionChecker.class);
	
	@Autowired
	private PopulationFileLocator backupLocator;
	
	
	public List<PopulationPlanPreExecutionWarning> checkPlan(InputDataSourceProvider inputDataSourceProvider, Project project, Set<String> tables, String backupPath) throws IOException{
		
		List<PopulationPlanPreExecutionWarning> result = new ArrayList<>();
		
		int maxStep = backupLocator.resolveMaxStep(backupPath);
		
		for (Package myPackage:project.model.packages) {
			for (Table table:myPackage.tables) {
				if (tables == null || tables.contains(table.originalName)) {
					logger.info("start pre checking table : " + table.name);

					PopulationPlanPreExecutionWarning noPlanWarning = checkTableHasPlan(backupPath, maxStep, table);
					
					if (noPlanWarning != null) {
						result.add(noPlanWarning);
					} else {
						result.addAll(checkHardCodedBackup(backupPath, maxStep, table, inputDataSourceProvider));
					}
				} else {
					logger.info("table : " + table.name + " skipped");
				}
			}
		}
		
		return result;
		
	}


	private PopulationPlanPreExecutionWarning checkTableHasPlan(String backupPath, int maxStep, Table table){
				
		if(!tableHasPlan(backupPath, maxStep, table)){
			return new PopulationPlanPreExecutionWarning(PopulationPlanWarningType.NO_PLAN, PopulationPlanPreExecutionWarning.NO_STEP, table);
		}
		return null;
	}
	
	
	private boolean tableHasPlan(String backupPath, int maxSteps, Table table) {
		return backupLocator.existsFileForTable(backupPath, maxSteps, table);
	}
	
	
	private List<PopulationPlanPreExecutionWarning> checkHardCodedBackup(String backupPath, int maxStep, Table table, InputDataSourceProvider inputDataSourceProvider) throws IOException {
		
		List<PopulationPlanPreExecutionWarning> result = new LinkedList<>();
		
		for(int step=1; step<=maxStep; step++){
			result.addAll(checkHardCodedBackupForStep(backupPath, step, table, inputDataSourceProvider));
		}
		
		return result;
	}
	

	private List<PopulationPlanPreExecutionWarning> checkHardCodedBackupForStep(String backupPath, int step, Table table, InputDataSourceProvider inputDataSourceProvider) throws IOException {
		List<PopulationPlanPreExecutionWarning> result = new LinkedList<>();

		PersistenceMode mode = backupLocator.resolvePersistenceModeOrNull(backupPath, step, table);
		if(mode!=null){
			switch(mode){
				case CSV : 
					result.add(new PopulationPlanPreExecutionWarning(PopulationPlanWarningType.HARDCODED_VALUES, step, table));
					break;
				default:
					break;
					
			}
		}
		return result;
	}
}
