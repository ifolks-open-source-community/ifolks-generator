package org.ifolks.generator.components.metadata.factory.interfaces;

import org.ifolks.generator.model.domain.Project;
import org.ifolks.generator.model.metadata.ProjectMetaData;


/**
 * General factory interface to build a full project given its MetaData representation
 * @author Nicolas Thibault
 *
 */
public interface ProjectFactory {
	
	/**
	 * builds a full project definition given its MetaData<br/>
	 * @Throws {@link IllegalArgumentException} if convertions from String to enum fails
	 * @param projectMetaData
	 * @return
	 */
	Project buildProject(ProjectMetaData projectMetaData);

}
