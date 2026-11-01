package org.ifolks.generator.components.metadata.validation.rules.impl;

import org.ifolks.generator.components.metadata.validation.rules.ProjectMetaDataRuleChecker;
import org.ifolks.generator.model.metadata.ColumnMetaData;
import org.ifolks.generator.model.metadata.PackageMetaData;
import org.ifolks.generator.model.metadata.ProjectMetaData;
import org.ifolks.generator.model.metadata.TableMetaData;
import org.ifolks.generator.model.metadata.validation.ProjectValidationReport;

public class RedundantUniqueConstraintChecker implements ProjectMetaDataRuleChecker {

	@Override
	public ProjectValidationReport checkRules(ProjectMetaData project, ProjectValidationReport report) {

		for (PackageMetaData packageMetaData : project.getAllPackages()) {
			if (packageMetaData.getTables() != null) {
				for (TableMetaData table : packageMetaData.getTables()) {
					if (table.getCardinality() == 1 && table.getColumns() != null && !table.getColumns().isEmpty()) {
						ColumnMetaData firstColumn = table.getColumns().get(0);
						if (Boolean.TRUE.equals(firstColumn.getUnique())) {
							report.addError(table, firstColumn, "Column " + firstColumn.getName()
									+ " in table " + table.getName()
									+ " cannot declare unique=\"true\" because it already constitutes the business key (cardinality=1)");
						}
					}
				}
			}
		}

		return report;
	}
}
