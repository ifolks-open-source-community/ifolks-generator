package org.ifolks.generator.components.metadata.validation.rules.impl;

import org.ifolks.generator.components.metadata.validation.rules.ProjectMetaDataRuleChecker;
import org.ifolks.generator.model.metadata.PackageMetaData;
import org.ifolks.generator.model.metadata.ProjectMetaData;
import org.ifolks.generator.model.metadata.TableMetaData;
import org.ifolks.generator.model.metadata.validation.ProjectValidationReport;

public class InvalidCardinalityChecker implements ProjectMetaDataRuleChecker {
		
	@Override
	public ProjectValidationReport checkRules(ProjectMetaData project, ProjectValidationReport report) {
		
		for (PackageMetaData packageMetaData:project.getAllPackages()) {
			if (packageMetaData.getTables()!=null) {
				for (TableMetaData table:packageMetaData.getTables()) {
					if (table.getCardinality() < 0) {
						report.addError(table, null, "Cardinality must be positive");
					}
					if (table.getCardinality() > table.getColumns().size()) {
						report.addError(table, null, "Cardinality must be lower or equal to the number of columns");
					}
				}
			}
		}
		
		return report;		
	}
}
