package org.ifolks.generator.components.validation.rules.impl;

import java.util.HashMap;
import java.util.Map;

import org.ifolks.generator.components.validation.rules.ProjectMetaDataRuleChecker;
import org.ifolks.generator.model.metadata.ColumnMetaData;
import org.ifolks.generator.model.metadata.PackageMetaData;
import org.ifolks.generator.model.metadata.ProjectMetaData;
import org.ifolks.generator.model.metadata.TableMetaData;
import org.ifolks.generator.model.metadata.validation.ProjectValidationReport;

public class InvalidReferenceTableChecker implements ProjectMetaDataRuleChecker {

	@Override
	public ProjectValidationReport checkRules(ProjectMetaData project, ProjectValidationReport report) {
		
		Map<String, TableMetaData> tablesMap = new HashMap<>();
		
		for (PackageMetaData packageMetaData:project.getAllPackages()) {
			if (packageMetaData.getTables()!=null) {
				for (TableMetaData table:packageMetaData.getTables()) {
					tablesMap.put(table.getName(), table);
				}
			}
		}
		
		for (PackageMetaData packageMetaData:project.getAllPackages()) {
			if (packageMetaData.getTables()!=null) {
				for (TableMetaData table:packageMetaData.getTables()) {
					for (ColumnMetaData column:table.getColumns()) {
						if (column.getReferenceTableName() != null) {
							if (!tablesMap.containsKey(column.getReferenceTableName())) {
								report.addError(table, column, "Invalid table reference");
							}
						}
					}				
				}
			}
		}
		
		return report;
	}
}
