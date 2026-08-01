package org.ifolks.generator.skeletons.layers;

import org.ifolks.generator.model.domain.Project;
import org.ifolks.generator.skeletons.tree.FileWriteCommandTreeNode;

public abstract class AbstractLayer implements Layer {

	private String name;

	public AbstractLayer(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public FileWriteCommandTreeNode getInitializationNode(Project project) {
		return null;
	}
}
