package org.ifolks.generator.skeletons.core.layers;

import org.ifolks.generator.model.domain.Package;
import org.ifolks.generator.model.domain.Project;
import org.ifolks.generator.model.domain.business.Bean;
import org.ifolks.generator.skeletons.core.commands.services.BaseServiceImplFileWriteCommand;
import org.ifolks.generator.skeletons.core.commands.services.ServiceImplFileWriteCommand;
import org.ifolks.generator.skeletons.commands.impl.DirectoryResourceCopyCommand;
import org.ifolks.generator.skeletons.layers.AbstractLayer;
import org.ifolks.generator.skeletons.tree.FileWriteCommandTreeNode;

public class ServiceLayer extends AbstractLayer {
	
	public ServiceLayer() {
		super("Services / Backend implementation");
	}

	@Override
	public FileWriteCommandTreeNode getInitializationNode(Project project) {
		FileWriteCommandTreeNode initTreeNode = new FileWriteCommandTreeNode();
		initTreeNode.add(new FileWriteCommandTreeNode(new DirectoryResourceCopyCommand(project, "/services/pom.xml.vm", project.projectName + "-services")));
		
		String servicesPackagePath = project.model.servicesPackageName.replace('.', '/');
		initTreeNode.add(new FileWriteCommandTreeNode(new DirectoryResourceCopyCommand(project, "/services/java", project.projectName + "-services/" + project.model.javaSourcesFolder + "/" + servicesPackagePath)));
		return initTreeNode;
	}
	
	@Override
	public FileWriteCommandTreeNode getGenerationNode(Project project) {

		FileWriteCommandTreeNode serviceLayerTreeNode = new FileWriteCommandTreeNode();

		for (Package myPackage : project.model.packages) {
			FileWriteCommandTreeNode packageTreeNode = new FileWriteCommandTreeNode(myPackage.name);
			serviceLayerTreeNode.add(packageTreeNode);
			
			for (Bean bean : myPackage.beans) {
				if (!bean.isComponent) {
					FileWriteCommandTreeNode baseBeanTreeNode = new FileWriteCommandTreeNode(new BaseServiceImplFileWriteCommand(bean));
					packageTreeNode.add(baseBeanTreeNode);
					
					FileWriteCommandTreeNode beanTreeNode = new FileWriteCommandTreeNode(new ServiceImplFileWriteCommand(bean));
					packageTreeNode.add(beanTreeNode);
				}
			}
		}
		
		return serviceLayerTreeNode;
	}
}
