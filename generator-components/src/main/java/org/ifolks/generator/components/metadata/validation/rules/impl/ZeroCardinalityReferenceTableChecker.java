package org.ifolks.generator.components.metadata.validation.rules.impl;

import java.util.HashMap;
import java.util.Map;

import org.ifolks.generator.components.metadata.validation.rules.ProjectMetaDataRuleChecker;
import org.ifolks.generator.model.metadata.ColumnMetaData;
import org.ifolks.generator.model.metadata.PackageMetaData;
import org.ifolks.generator.model.metadata.ProjectMetaData;
import org.ifolks.generator.model.metadata.RelationType;
import org.ifolks.generator.model.metadata.TableMetaData;
import org.ifolks.generator.model.metadata.validation.ProjectValidationReport;

public class ZeroCardinalityReferenceTableChecker implements ProjectMetaDataRuleChecker {

	@Override
	public ProjectValidationReport checkRules(ProjectMetaData project, ProjectValidationReport report) {
		
		Map<String, TableMetaData> tablesMap = new HashMap<>();
		
		for (PackageMetaData packageMetaData:project.getPackages()) {
			if (packageMetaData.getTables()!=null) {
			for (TableMetaData table:packageMetaData.getTables()) {
				tablesMap.put(table.getName(), table);
			}
			}
		}
		
		for (PackageMetaData packageMetaData:project.getPackages()) {
			if (packageMetaData.getTables()!=null) {
				for (TableMetaData table:packageMetaData.getTables()) {
					for (ColumnMetaData column:table.getColumns()) {
						if (column.getReferenceTableName() != null) {
							TableMetaData referenceTable = tablesMap.get(column.getReferenceTableName());
							if (referenceTable != null && referenceTable.getCardinality() == 0 && !RelationType.EMBEDDED.equals(column.getReferenceTableRelation())) {
								report.addError(table, column, "Reference to a zero cardinality table");
							}
						}
					}				
				}
			}
		}
		
		return report;		
	}
}
