package org.ifolks.generator.components.metadata.validation.rules.impl;

import java.util.HashSet;
import java.util.Set;

import org.ifolks.generator.components.metadata.validation.rules.ProjectMetaDataRuleChecker;
import org.ifolks.generator.model.metadata.ColumnMetaData;
import org.ifolks.generator.model.metadata.DataType;
import org.ifolks.generator.model.metadata.IndexMetaData;
import org.ifolks.generator.model.metadata.PackageMetaData;
import org.ifolks.generator.model.metadata.ProjectMetaData;
import org.ifolks.generator.model.metadata.TableMetaData;
import org.ifolks.generator.model.metadata.UniqueConstraintMetaData;
import org.ifolks.generator.model.metadata.validation.ProjectValidationReport;

public class InvalidTextConstraintChecker implements ProjectMetaDataRuleChecker {

	@Override
	public ProjectValidationReport checkRules(ProjectMetaData project, ProjectValidationReport report) {

		for (PackageMetaData packageMetaData : project.getAllPackages()) {
			if (packageMetaData.getTables() != null) {
				for (TableMetaData table : packageMetaData.getTables()) {
					if (table.getColumns() != null) {
						Set<String> textColumnNames = new HashSet<>();

						for (int i = 0; i < table.getColumns().size(); i++) {
							ColumnMetaData column = table.getColumns().get(i);
							if (DataType.TEXT.equals(column.getDataType())) {
								textColumnNames.add(column.getName());

								// Check 1: Direct unique attribute on column
								if (Boolean.TRUE.equals(column.getUnique())) {
									report.addError(table, column, "Column " + column.getName() + " of type TEXT cannot have a unique constraint");
								}

								// Check 2: Column included in business key (cardinality)
								if (i < table.getCardinality()) {
									report.addError(table, column, "Column " + column.getName() + " of type TEXT cannot be part of the business key (cardinality)");
								}
							}
						}

						// Check 3: Unique constraint including a TEXT column
						if (table.getUniqueConstraints() != null) {
							for (UniqueConstraintMetaData uc : table.getUniqueConstraints()) {
								if (uc.getFields() != null) {
									for (String fieldName : uc.getFields()) {
										if (textColumnNames.contains(fieldName)) {
											report.addError(table, null, "Unique constraint " + uc.getName() + " cannot include column " + fieldName + " of type TEXT");
										}
									}
								}
							}
						}

						// Check 4: Index including a TEXT column
						if (table.getIndexes() != null) {
							for (IndexMetaData index : table.getIndexes()) {
								if (index.getFields() != null) {
									for (String fieldName : index.getFields()) {
										if (textColumnNames.contains(fieldName)) {
											report.addError(table, null, "Index " + index.getName() + " cannot include column " + fieldName + " of type TEXT");
										}
									}
								}
							}
						}
					}
				}
			}
		}

		return report;
	}
}
