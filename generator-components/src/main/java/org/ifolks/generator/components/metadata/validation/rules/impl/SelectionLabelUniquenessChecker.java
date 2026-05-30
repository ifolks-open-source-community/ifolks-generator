package org.ifolks.generator.components.metadata.validation.rules.impl;

import org.ifolks.generator.components.metadata.validation.rules.ProjectMetaDataRuleChecker;
import org.ifolks.generator.model.metadata.ColumnMetaData;
import org.ifolks.generator.model.metadata.PackageMetaData;
import org.ifolks.generator.model.metadata.ProjectMetaData;
import org.ifolks.generator.model.metadata.TableMetaData;
import org.ifolks.generator.model.metadata.UniqueConstraintMetaData;
import org.ifolks.generator.model.metadata.validation.ProjectValidationReport;

public class SelectionLabelUniquenessChecker implements ProjectMetaDataRuleChecker {

	@Override
	public ProjectValidationReport checkRules(ProjectMetaData project, ProjectValidationReport report) {
		
		for (PackageMetaData packageMetaData:project.getAllPackages()) {
			if (packageMetaData.getTables()!=null) {
				for (TableMetaData table:packageMetaData.getTables()) {
					if (table.getSelectionBehavior() != null) {
						String labelColumnName = table.getSelectionBehavior().getLabelColumn();
						ColumnMetaData labelColumn = null;
						
						if (labelColumnName != null) {
							for (ColumnMetaData col : table.getColumns()) {
								if (col.getName().equals(labelColumnName)) {
									labelColumn = col;
									break;
								}
							}
						} else if (table.getColumns() != null && !table.getColumns().isEmpty()) {
							labelColumn = table.getColumns().get(0);
						}
						
						if (labelColumn != null) {
							boolean isUnique = false;
							if (table.getColumns().indexOf(labelColumn) == 0 && table.getCardinality() == 1) {
								isUnique = true;
							} else if (labelColumn.getUnique() != null && labelColumn.getUnique()) {
								isUnique = true;
							} else if (table.getUniqueConstraints() != null) {
								for (UniqueConstraintMetaData constraint : table.getUniqueConstraints()) {
									if (constraint.getFields() != null && constraint.getFields().size() == 1 
											&& constraint.getFields().get(0).equals(labelColumn.getName())) {
										isUnique = true;
										break;
									}
								}
							}
							
							if (!isUnique) {
								report.addError(table, labelColumn, "The selection label column '" + labelColumn.getName() + "' must have a unique constraint");
							}
						}
					}
				}
			}
		}
		
		return report;
	}
}
