package org.ifolks.generator.skeletons.rest.layers;

import org.ifolks.generator.skeletons.rest.commands.BaseRestClientFileWriteCommand;
import org.ifolks.generator.skeletons.rest.commands.RestClientFileWriteCommand;
import org.ifolks.generator.skeletons.rest.commands.configuration.SpringRestClientConfigFileWriteCommand;
import org.ifolks.generator.skeletons.rest.commands.configuration.SpringRestClientPomFileWriteCommand;
import org.ifolks.generator.model.domain.Package;
import org.ifolks.generator.model.domain.Project;
import org.ifolks.generator.model.domain.business.Bean;
import org.ifolks.generator.skeletons.layers.AbstractLayer;
import org.ifolks.generator.skeletons.tree.FileWriteCommandTreeNode;

public class SpringRestClientLayer extends AbstractLayer {
	
	public SpringRestClientLayer() {
		super("Spring REST Clients");
	}
	
	@Override
	public FileWriteCommandTreeNode getResourcesNode(Project project) {
		
		FileWriteCommandTreeNode resourcesTreeNode = new FileWriteCommandTreeNode();
		
		
		return resourcesTreeNode;
	}

	@Override
	public FileWriteCommandTreeNode getConfigurationNode(Project project) {
		
		FileWriteCommandTreeNode configurationTreeNode = new FileWriteCommandTreeNode();
				
		FileWriteCommandTreeNode pomTreeNode = new FileWriteCommandTreeNode(new SpringRestClientPomFileWriteCommand(project));
		configurationTreeNode.add(pomTreeNode);
		
		FileWriteCommandTreeNode springContextTreeNode = new FileWriteCommandTreeNode(new SpringRestClientConfigFileWriteCommand(project));
		configurationTreeNode.add(springContextTreeNode);
		
		return configurationTreeNode;
	}

	@Override
	public FileWriteCommandTreeNode getGenerationNode(Project project) {
		
		FileWriteCommandTreeNode mainTreeNode = new FileWriteCommandTreeNode();

		FileWriteCommandTreeNode baseRestClientTreeNode = new FileWriteCommandTreeNode("Base REST clients");
		mainTreeNode.add(baseRestClientTreeNode);

		for (Package myPackage : project.model.packages) {
			FileWriteCommandTreeNode packageTreeNode = new FileWriteCommandTreeNode(myPackage.name);
			baseRestClientTreeNode.add(packageTreeNode);
			
			for (Bean bean : myPackage.beans) {
				if (!bean.isComponent) {
					FileWriteCommandTreeNode beanBaseRestClientTreeNode = new FileWriteCommandTreeNode(new BaseRestClientFileWriteCommand(bean));
					packageTreeNode.add(beanBaseRestClientTreeNode);
				}
			}
		}
		
		FileWriteCommandTreeNode controllerTreeNode = new FileWriteCommandTreeNode("REST clients");
		mainTreeNode.add(controllerTreeNode);

		for (Package myPackage : project.model.packages) {
			FileWriteCommandTreeNode packageTreeNode = new FileWriteCommandTreeNode(myPackage.name);
			controllerTreeNode.add(packageTreeNode);
			
			for (Bean bean : myPackage.beans) {
				if (!bean.isComponent) {
					FileWriteCommandTreeNode beanRestClientTreeNode = new FileWriteCommandTreeNode(new RestClientFileWriteCommand(bean));
					packageTreeNode.add(beanRestClientTreeNode);
				}
			}
		}
		
		return mainTreeNode;
	}

}