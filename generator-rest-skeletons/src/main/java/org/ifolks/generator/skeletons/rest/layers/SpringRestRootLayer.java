package org.ifolks.generator.skeletons.rest.layers;

import org.ifolks.generator.model.domain.Project;
import org.ifolks.generator.skeletons.commands.impl.DirectoryResourceCopyCommand;
import org.ifolks.generator.skeletons.layers.AbstractLayer;
import org.ifolks.generator.skeletons.tree.FileWriteCommandTreeNode;

public class SpringRestRootLayer extends AbstractLayer {
	
	public SpringRestRootLayer() {
		super("Spring REST Root layer");
	}
	
	@Override
	public FileWriteCommandTreeNode getInitializationNode(Project project) {
		FileWriteCommandTreeNode initTreeNode = new FileWriteCommandTreeNode();
		initTreeNode.add(new FileWriteCommandTreeNode(new DirectoryResourceCopyCommand(project, "/root", "")));
		return initTreeNode;
	}

	@Override
	public FileWriteCommandTreeNode getGenerationNode(Project project) {
		FileWriteCommandTreeNode treeNode = new FileWriteCommandTreeNode();
		return treeNode;
	}
}
