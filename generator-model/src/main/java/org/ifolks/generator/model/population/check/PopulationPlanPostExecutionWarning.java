package org.ifolks.generator.model.population.check;

import org.ifolks.generator.model.domain.database.Table;

/**
 * A warning that can occur after a population plan
 * @author Nicolas Thibault
 *
 */
public class PopulationPlanPostExecutionWarning {
	
	private PopulationPlanWarningType type;
	private Table table;
	
	public PopulationPlanPostExecutionWarning(PopulationPlanWarningType type, Table table) {
		super();
		this.type = type;
		this.table = table;
	}
	
	public PopulationPlanWarningType getType() {
		return type;
	}
	public Table getTable() {
		return table;
	}
}
