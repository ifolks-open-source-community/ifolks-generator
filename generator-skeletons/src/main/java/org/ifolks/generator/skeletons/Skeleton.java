package org.ifolks.generator.skeletons;

import java.util.List;

import org.ifolks.generator.model.domain.Project;
import org.ifolks.generator.skeletons.layers.Layer;

/**
 * This interface represents a Application skeleton as a List of {@link Layer}
 * @author Nicolas Thibault
 *
 */
public interface Skeleton {
	
	String getName();

	List<Layer> getLayers(Project project);
}
