package org.ifolks.generator.skeletons.rest.layers;

import org.ifolks.generator.model.domain.Project;
import org.ifolks.generator.skeletons.layers.AbstractLayer;
import org.ifolks.generator.skeletons.rest.commands.configuration.SpringRestRootPomFileWriteCommand;
import org.ifolks.generator.skeletons.tree.FileWriteCommandTreeNode;

public class SpringRestRootLayer extends AbstractLayer {
	
	public SpringRestRootLayer() {
		super("Spring REST Root layer");
	}
	
	@Override
	public FileWriteCommandTreeNode getResourcesNode(Project project) {
		
		FileWriteCommandTreeNode resourcesTreeNode = new FileWriteCommandTreeNode();
		return resourcesTreeNode;
	}

	@Override
	public FileWriteCommandTreeNode getConfigurationNode(Project project) {
		
		FileWriteCommandTreeNode configurationTreeNode = new FileWriteCommandTreeNode();
		
		FileWriteCommandTreeNode rootPomTreeNode = new FileWriteCommandTreeNode(new SpringRestRootPomFileWriteCommand(project));
		configurationTreeNode.add(rootPomTreeNode);
		
		return configurationTreeNode;
	}

	@Override
	public FileWriteCommandTreeNode getGenerationNode(Project project) {
		
		FileWriteCommandTreeNode treeNode = new FileWriteCommandTreeNode();
		return treeNode;
	}

}