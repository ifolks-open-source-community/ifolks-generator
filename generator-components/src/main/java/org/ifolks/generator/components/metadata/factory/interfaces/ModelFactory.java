package org.ifolks.generator.components.metadata.factory.interfaces;

import org.ifolks.generator.model.domain.Model;
import org.ifolks.generator.model.domain.Project;
import org.ifolks.generator.model.metadata.ProjectMetaData;


/**
 * 
 * @author Nicolas Thibault
 *
 */
public interface ModelFactory {

	Model buildModel(ProjectMetaData projectMetaData, Project project);
}
