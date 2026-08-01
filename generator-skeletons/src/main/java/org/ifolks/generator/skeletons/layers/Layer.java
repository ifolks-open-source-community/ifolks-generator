package org.ifolks.generator.skeletons.layers;

import org.ifolks.generator.model.domain.Project;
import org.ifolks.generator.skeletons.tree.FileWriteCommandTreeNode;

/**
 * An Application Skeleton is divided into several Layers which takes care of a particular role<br>
 * Each Layer can have initialization files (skeleton & resources) and generated files
 * @author Nicolas Thibault
 *
 */
public interface Layer {

	/**
	 * get the displayed name of the layer
	 * @return
	 */
	String getName();

	/**
	 * get the initialization node (resources, configs, skeleton) to be written when initializing a {@link Project}
	 * @param project
	 * @return
	 */
	FileWriteCommandTreeNode getInitializationNode(Project project);

	/**
	 * get the files to be written when generating code for a {@link Project}
	 * @param project
	 * @return
	 */
	FileWriteCommandTreeNode getGenerationNode(Project project);
}
