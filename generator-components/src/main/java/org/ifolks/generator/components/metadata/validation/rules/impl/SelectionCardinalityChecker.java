package org.ifolks.generator.components.metadata.validation.rules.impl;

import org.ifolks.generator.components.metadata.validation.rules.ProjectMetaDataRuleChecker;
import org.ifolks.generator.model.metadata.PackageMetaData;
import org.ifolks.generator.model.metadata.ProjectMetaData;
import org.ifolks.generator.model.metadata.TableMetaData;
import org.ifolks.generator.model.metadata.validation.ProjectValidationReport;

public class SelectionCardinalityChecker implements ProjectMetaDataRuleChecker {

	@Override
	public ProjectValidationReport checkRules(ProjectMetaData project, ProjectValidationReport report) {
		
		for (PackageMetaData packageMetaData:project.getAllPackages()) {
			if (packageMetaData.getTables()!=null) {
				for (TableMetaData table:packageMetaData.getTables()) {
					if (table.getSelectionBehavior() != null) {
						if (table.getCardinality() != 1) {
							report.addError(table, null, "Selection behavior is only allowed for master tables with cardinality = 1");
						}
					}
				}
			}
		}
		
		return report;
	}
}
