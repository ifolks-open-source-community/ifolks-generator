package org.ifolks.generator.components.population.prompt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.ifolks.generator.model.population.check.PopulationPlanPostExecutionWarning;
import org.ifolks.generator.model.population.check.PopulationPlanPreExecutionWarning;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PopulatorPrompter {
	
	private static final Logger logger = LoggerFactory.getLogger(PopulatorPrompter.class);
	
	public static void printPreExecutionWarnings(List<PopulationPlanPreExecutionWarning> warnings){
		logger.warn(warnings.size() + " warnings have been generated");
		
		for(PopulationPlanPreExecutionWarning w : warnings){
			logger.warn(printPreExecutionWarning(w));
		}
		
	}
	
	public static void printPostExecutionWarnings(List<PopulationPlanPostExecutionWarning> warnings){
		logger.warn(warnings.size() + " warnings have been generated");
		
		for(PopulationPlanPostExecutionWarning w : warnings){
			logger.warn(printPostExecutionWarning(w));
		}
		
	}
	
	public static void promptForConfirmation() throws IOException{
		System.out.println("Do you wish to continue? [Y/n]");
		BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
		String input = bufferRead.readLine();
		
		if(!("Y".equals(input)||input.isEmpty())){
			logger.warn("Aborting ...");
			System.exit(0);
		}
	}
	
	private static String printPreExecutionWarning(PopulationPlanPreExecutionWarning w){
		String firstPart = w.getType().getDescription() + w.getTable().originalName;
		if(w.getStep()==PopulationPlanPreExecutionWarning.NO_STEP){
			return firstPart;
		}else{
			return firstPart + " on step " + w.getStep();
		}
	}
	
	private static String printPostExecutionWarning(PopulationPlanPostExecutionWarning w){
		return w.getType().getDescription() + w.getTable().originalName;
	}
}
