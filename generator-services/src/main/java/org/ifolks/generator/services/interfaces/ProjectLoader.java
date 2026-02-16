package org.ifolks.generator.services.interfaces;

import org.ifolks.generator.model.domain.Project;
import org.ifolks.generator.model.metadata.ProjectMetaData;

/**
 * Defines the contract of a project loader : get a {@link Project} representation from its persistent {@link ProjectMetaData} representation
 * @author Nicolas Thibault
 *
 */
public interface ProjectLoader {

	Project loadProject(ProjectMetaData projectMetaData);
}
