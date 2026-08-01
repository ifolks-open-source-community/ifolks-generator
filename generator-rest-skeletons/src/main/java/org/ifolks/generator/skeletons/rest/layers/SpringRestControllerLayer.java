package org.ifolks.generator.skeletons.rest.layers;

import org.ifolks.generator.model.domain.Package;
import org.ifolks.generator.model.domain.Project;
import org.ifolks.generator.model.domain.business.Bean;
import org.ifolks.generator.skeletons.commands.impl.DirectoryResourceCopyCommand;
import org.ifolks.generator.skeletons.layers.AbstractLayer;
import org.ifolks.generator.skeletons.rest.commands.SpringRestBaseControllerCommand;
import org.ifolks.generator.skeletons.rest.commands.SpringRestControllerCommand;
import org.ifolks.generator.skeletons.tree.FileWriteCommandTreeNode;

public class SpringRestControllerLayer extends AbstractLayer {
	
	public SpringRestControllerLayer() {
		super("Spring REST Controllers");
	}
	
	@Override
	public FileWriteCommandTreeNode getInitializationNode(Project project) {
		FileWriteCommandTreeNode initTreeNode = new FileWriteCommandTreeNode();
		initTreeNode.add(new FileWriteCommandTreeNode(new DirectoryResourceCopyCommand(project, "/rest/pom.xml.vm", project.projectName + "-rest")));
		initTreeNode.add(new FileWriteCommandTreeNode(new DirectoryResourceCopyCommand(project, "/rest/resources", project.projectName + "-rest/" + project.model.resourcesFolder)));
		
		String restPackagePath = project.model.restControllerPackageName.replace('.', '/');
		initTreeNode.add(new FileWriteCommandTreeNode(new DirectoryResourceCopyCommand(project, "/rest/java", project.projectName + "-rest/" + project.model.javaSourcesFolder + "/" + restPackagePath)));
		return initTreeNode;
	}

	@Override
	public FileWriteCommandTreeNode getGenerationNode(Project project) {
		
		FileWriteCommandTreeNode controllersLayerTreeNode = new FileWriteCommandTreeNode();

		FileWriteCommandTreeNode baseControllerTreeNode = new FileWriteCommandTreeNode("REST Base Controllers");
		controllersLayerTreeNode.add(baseControllerTreeNode);

		for (Package myPackage : project.model.packages) {
			FileWriteCommandTreeNode packageTreeNode = new FileWriteCommandTreeNode(myPackage.name);
			baseControllerTreeNode.add(packageTreeNode);
			
			for (Bean bean : myPackage.beans) {
				if (!bean.isComponent) {
					FileWriteCommandTreeNode beanBaseControllerTreeNode = new FileWriteCommandTreeNode(new SpringRestBaseControllerCommand(bean));
					packageTreeNode.add(beanBaseControllerTreeNode);
				}
			}
		}
		
		FileWriteCommandTreeNode controllerTreeNode = new FileWriteCommandTreeNode("REST Controllers");
		controllersLayerTreeNode.add(controllerTreeNode);

		for (Package myPackage : project.model.packages) {
			FileWriteCommandTreeNode packageTreeNode = new FileWriteCommandTreeNode(myPackage.name);
			controllerTreeNode.add(packageTreeNode);
			
			for (Bean bean : myPackage.beans) {
				if (!bean.isComponent) {
					FileWriteCommandTreeNode beanControllerTreeNode = new FileWriteCommandTreeNode(new SpringRestControllerCommand(bean));
					packageTreeNode.add(beanControllerTreeNode);
				}
			}
		}
		
		return controllersLayerTreeNode;
	}

}
