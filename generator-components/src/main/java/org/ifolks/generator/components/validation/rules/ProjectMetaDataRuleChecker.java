package org.ifolks.generator.components.validation.rules;

import org.ifolks.generator.model.metadata.ProjectMetaData;
import org.ifolks.generator.model.metadata.validation.ProjectValidationReport;

public interface ProjectMetaDataRuleChecker {

	ProjectValidationReport checkRules(ProjectMetaData project, ProjectValidationReport report);
}
