package org.ifolks.generator.skeletons.core.layers;

import org.ifolks.generator.model.domain.Package;
import org.ifolks.generator.model.domain.Project;
import org.ifolks.generator.model.domain.business.Bean;
import org.ifolks.generator.skeletons.commands.impl.DirectoryResourceCopyCommand;
import org.ifolks.generator.skeletons.core.commands.api.interfaces.BaseServiceInterfaceFileWriteCommand;
import org.ifolks.generator.skeletons.core.commands.api.interfaces.ServiceInterfaceFileWriteCommand;
import org.ifolks.generator.skeletons.core.commands.api.model.BasicViewBeanFileWriteCommand;
import org.ifolks.generator.skeletons.core.commands.api.model.FilterFileWriteCommand;
import org.ifolks.generator.skeletons.core.commands.api.model.FormBeanFileWriteCommand;
import org.ifolks.generator.skeletons.core.commands.api.model.FullViewBeanFileWriteCommand;
import org.ifolks.generator.skeletons.core.commands.api.model.SortingFileWriteCommand;
import org.ifolks.generator.skeletons.layers.AbstractLayer;
import org.ifolks.generator.skeletons.tree.FileWriteCommandTreeNode;

public class ApiLayer extends AbstractLayer {
	
	public ApiLayer() {
		super("API");
	}

	@Override
	public FileWriteCommandTreeNode getInitializationNode(Project project) {
		FileWriteCommandTreeNode initTreeNode = new FileWriteCommandTreeNode();
		initTreeNode.add(new FileWriteCommandTreeNode(new DirectoryResourceCopyCommand(project, "/api", project.projectName + "-api")));
		return initTreeNode;
	}

	@Override
	public FileWriteCommandTreeNode getGenerationNode(Project project) {

		FileWriteCommandTreeNode apiTreeNode = new FileWriteCommandTreeNode();
		
		FileWriteCommandTreeNode interfacesTreeNode = new FileWriteCommandTreeNode("Interfaces");
		apiTreeNode.add(interfacesTreeNode);
		
		for (Package myPackage : project.model.packages) {
			FileWriteCommandTreeNode packageTreeNode = new FileWriteCommandTreeNode(myPackage.name);
			interfacesTreeNode.add(packageTreeNode);		
		
			for (Bean bean : myPackage.beans) {
				if (!bean.isComponent) {
					FileWriteCommandTreeNode baseBeanTreeNode = new FileWriteCommandTreeNode(new BaseServiceInterfaceFileWriteCommand(bean));
					interfacesTreeNode.add(baseBeanTreeNode);
					
					FileWriteCommandTreeNode beanTreeNode = new FileWriteCommandTreeNode(new ServiceInterfaceFileWriteCommand(bean));
					interfacesTreeNode.add(beanTreeNode);
				}
			}
		}
		
		
		FileWriteCommandTreeNode modelTreeNode = new FileWriteCommandTreeNode("Model");
		apiTreeNode.add(modelTreeNode);

		for (Package myPackage : project.model.packages) {
			FileWriteCommandTreeNode packageTreeNode = new FileWriteCommandTreeNode(myPackage.name);
			modelTreeNode.add(packageTreeNode);

			FileWriteCommandTreeNode ovTreeNode = new FileWriteCommandTreeNode("Views");
			packageTreeNode.add(ovTreeNode);
			for (Bean bean : myPackage.beans) {
				if (!bean.isEmbedded) {
					FileWriteCommandTreeNode basicViewTreeNode = new FileWriteCommandTreeNode(new BasicViewBeanFileWriteCommand(bean));
					ovTreeNode.add(basicViewTreeNode);
					
					FileWriteCommandTreeNode fullViewTreeNode = new FileWriteCommandTreeNode(new FullViewBeanFileWriteCommand(bean));
					ovTreeNode.add(fullViewTreeNode);
					
					FileWriteCommandTreeNode formTreeNode = new FileWriteCommandTreeNode(new FormBeanFileWriteCommand(bean));
					ovTreeNode.add(formTreeNode);
				}
			}
			
			FileWriteCommandTreeNode filterTreeNode = new FileWriteCommandTreeNode("Filters");
			packageTreeNode.add(filterTreeNode);		

			for (Bean bean : myPackage.beans) {
				if (bean.isListable()) {
					FileWriteCommandTreeNode beanTreeNode = new FileWriteCommandTreeNode(new FilterFileWriteCommand(bean));
					filterTreeNode.add(beanTreeNode);
				}
			}
			
			FileWriteCommandTreeNode oerderingTreeNode = new FileWriteCommandTreeNode("Orderings");
			packageTreeNode.add(oerderingTreeNode);		

			for (Bean bean : myPackage.beans) {
				if (bean.isListable()) {
					FileWriteCommandTreeNode beanTreeNode = new FileWriteCommandTreeNode(new SortingFileWriteCommand(bean));
					filterTreeNode.add(beanTreeNode);
				}
			}
		}

		
		

		return apiTreeNode;
	}
}
