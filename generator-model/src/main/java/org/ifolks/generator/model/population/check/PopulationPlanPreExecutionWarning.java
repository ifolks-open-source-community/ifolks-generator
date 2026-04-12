package org.ifolks.generator.model.population.check;

import org.ifolks.generator.model.domain.database.Table;

/**
 * A warning that can occur when checking a population plan before execution
 * @author Mounir Regragui
 *
 */
public class PopulationPlanPreExecutionWarning {
	
	private PopulationPlanWarningType type;
	private int step;
	private Table table;
	
	public PopulationPlanPreExecutionWarning(PopulationPlanWarningType type, int step, Table table) {
		super();
		this.type = type;
		this.step = step;
		this.table = table;
	}
	
	public PopulationPlanWarningType getType() {
		return type;
	}
	public int getStep() {
		return step;
	}
	public Table getTable() {
		return table;
	}
	
	public static final int NO_STEP = -1;
}
