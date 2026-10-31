package org.ifolks.generator.components.metadata.validation.rules.impl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import org.ifolks.generator.components.metadata.validation.rules.ProjectMetaDataRuleChecker;
import org.ifolks.generator.model.metadata.EnumItemMetaData;
import org.ifolks.generator.model.metadata.EnumerationMetaData;
import org.ifolks.generator.model.metadata.PackageMetaData;
import org.ifolks.generator.model.metadata.ProjectMetaData;
import org.ifolks.generator.model.metadata.validation.ProjectValidationReport;

public class InvalidEnumerationChecker implements ProjectMetaDataRuleChecker {

	private static final Pattern ENUM_NAME_PATTERN = Pattern.compile("^[A-Z][a-zA-Z0-9_]*$");
	private static final Pattern ITEM_NAME_PATTERN = Pattern.compile("^[A-Z][A-Z0-9_]*$");

	private static final Set<String> RESERVED_KEYWORDS = new HashSet<>(Arrays.asList(
		"DEFAULT", "NULL", "TRUE", "FALSE", "CLASS", "CASE", "ORDER", "SELECT", "FROM", "WHERE", "GROUP", "BY",
		"PUBLIC", "PRIVATE", "PROTECTED", "STATIC", "FINAL", "VOID", "INT", "LONG", "SHORT", "BYTE", "FLOAT", "DOUBLE", "BOOLEAN"
	));

	@Override
	public ProjectValidationReport checkRules(ProjectMetaData project, ProjectValidationReport report) {

		Set<String> declaredEnumNames = new HashSet<>();

		for (PackageMetaData packageMetaData : project.getAllPackages()) {
			if (packageMetaData.getEnumerations() != null) {
				for (EnumerationMetaData enumeration : packageMetaData.getEnumerations()) {
					String enumName = enumeration.getName();

					if (enumName == null || enumName.trim().isEmpty()) {
						report.addError(null, null, "Enumeration name cannot be blank");
						continue;
					}

					if (!ENUM_NAME_PATTERN.matcher(enumName).matches()) {
						report.addError(null, null, "Enumeration name '" + enumName + "' must follow PascalCase identifier pattern [A-Z][a-zA-Z0-9_]*");
					}

					if (declaredEnumNames.contains(enumName)) {
						report.addError(null, null, "Duplicate enumeration declaration: '" + enumName + "'");
					} else {
						declaredEnumNames.add(enumName);
					}

					if (enumeration.getItems() == null || enumeration.getItems().isEmpty()) {
						report.addError(null, null, "Enumeration '" + enumName + "' must contain at least one item");
						continue;
					}

					Set<String> declaredItemNames = new HashSet<>();
					for (EnumItemMetaData item : enumeration.getItems()) {
						String itemName = item.getName();

						if (itemName == null || itemName.trim().isEmpty()) {
							report.addError(null, null, "Enumeration item name cannot be blank in enum '" + enumName + "'");
							continue;
						}

						if (!ITEM_NAME_PATTERN.matcher(itemName).matches()) {
							report.addError(null, null, "Enumeration item '" + itemName + "' in enum '" + enumName + "' must follow uppercase constant pattern [A-Z][A-Z0-9_]*");
						}

						if (itemName.length() > 32) {
							report.addError(null, null, "Enumeration item '" + itemName + "' in enum '" + enumName + "' exceeds maximum length of 32 characters");
						}

						if (RESERVED_KEYWORDS.contains(itemName.toUpperCase())) {
							report.addError(null, null, "Enumeration item '" + itemName + "' in enum '" + enumName + "' uses a forbidden reserved keyword");
						}

						if (declaredItemNames.contains(itemName)) {
							report.addError(null, null, "Duplicate item '" + itemName + "' in enumeration '" + enumName + "'");
						} else {
							declaredItemNames.add(itemName);
						}
					}
				}
			}
		}

		return report;
	}
}
