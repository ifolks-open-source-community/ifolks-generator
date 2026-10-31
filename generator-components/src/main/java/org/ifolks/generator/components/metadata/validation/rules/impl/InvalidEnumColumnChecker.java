package org.ifolks.generator.components.metadata.validation.rules.impl;

import java.util.HashSet;
import java.util.Set;

import org.ifolks.generator.components.metadata.validation.rules.ProjectMetaDataRuleChecker;
import org.ifolks.generator.model.metadata.ColumnMetaData;
import org.ifolks.generator.model.metadata.DataType;
import org.ifolks.generator.model.metadata.EnumerationMetaData;
import org.ifolks.generator.model.metadata.PackageMetaData;
import org.ifolks.generator.model.metadata.ProjectMetaData;
import org.ifolks.generator.model.metadata.TableMetaData;
import org.ifolks.generator.model.metadata.validation.ProjectValidationReport;

public class InvalidEnumColumnChecker implements ProjectMetaDataRuleChecker {

	@Override
	public ProjectValidationReport checkRules(ProjectMetaData project, ProjectValidationReport report) {

		Set<String> declaredEnumNames = new HashSet<>();

		for (PackageMetaData packageMetaData : project.getAllPackages()) {
			if (packageMetaData.getEnumerations() != null) {
				for (EnumerationMetaData enumeration : packageMetaData.getEnumerations()) {
					if (enumeration.getName() != null) {
						declaredEnumNames.add(enumeration.getName());
					}
				}
			}
		}

		for (PackageMetaData packageMetaData : project.getAllPackages()) {
			if (packageMetaData.getTables() != null) {
				for (TableMetaData table : packageMetaData.getTables()) {
					if (table.getColumns() != null) {
						for (ColumnMetaData column : table.getColumns()) {
							if (column.getDataType() != null && column.getDataType().equals(DataType.ENUM)) {

								if (column.getEnumName() == null || column.getEnumName().trim().isEmpty()) {
									report.addError(table, column, "Column with dataType ENUM must declare a non-empty enumName");
								} else if (!declaredEnumNames.contains(column.getEnumName())) {
									report.addError(table, column, "Unknown enum reference: '" + column.getEnumName() + "'");
								}

								if (column.getReferenceTableName() != null) {
									report.addError(table, column, "Enum column cannot define referenceTableName");
								}

								if (column.getReferenceTableRelation() != null) {
									report.addError(table, column, "Enum column cannot define referenceTableRelation");
								}

								if (column.getTextFilterType() != null) {
									report.addError(table, column, "Enum column cannot define textFilterType");
								}

							} else {
								if (column.getEnumName() != null) {
									report.addError(table, column, "enumName attribute is only permitted when dataType is ENUM");
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
