package org.ifolks.generator.skeletons.core.layers;

import org.ifolks.generator.model.domain.Package;
import org.ifolks.generator.model.domain.Project;
import org.ifolks.generator.model.domain.business.Bean;
import org.ifolks.generator.skeletons.core.commands.persistence.BaseRepositoryInterfaceFileWriteCommand;
import org.ifolks.generator.skeletons.core.commands.persistence.RepositoryInterfaceFileWriteCommand;
import org.ifolks.generator.skeletons.core.commands.persistence.SpecificationFileWriteCommand;
import org.ifolks.generator.skeletons.core.commands.persistence.configuration.PersistencePomFileWriteCommand;
import org.ifolks.generator.skeletons.core.commands.persistence.configuration.SpringLocalPersistenceConfigFileWriteCommand;
import org.ifolks.generator.skeletons.layers.AbstractLayer;
import org.ifolks.generator.skeletons.tree.FileWriteCommandTreeNode;

public class RepositoryLayer extends AbstractLayer {
	
	public RepositoryLayer() {
		super("Hibernate based repositories");
	}
	
	@Override
	public FileWriteCommandTreeNode getResourcesNode(Project project) {
		return null;
	}

	@Override
	public FileWriteCommandTreeNode getConfigurationNode(Project project) {
		
		FileWriteCommandTreeNode configurationTreeNode = new FileWriteCommandTreeNode();
		
		FileWriteCommandTreeNode repositoryPomTreeNode = new FileWriteCommandTreeNode(new PersistencePomFileWriteCommand(project));
		configurationTreeNode.add(repositoryPomTreeNode);
		
		FileWriteCommandTreeNode springRepositoryTreeNode = new FileWriteCommandTreeNode(new SpringLocalPersistenceConfigFileWriteCommand(project));
		configurationTreeNode.add(springRepositoryTreeNode);
		
		return configurationTreeNode;
	}

	@Override
	public FileWriteCommandTreeNode getGenerationNode(Project project) {

		FileWriteCommandTreeNode persistenceTreeNode = new FileWriteCommandTreeNode();

		FileWriteCommandTreeNode baseRepositoryTreeNode = new FileWriteCommandTreeNode("Base Repositories");
		persistenceTreeNode.add(baseRepositoryTreeNode);

		for (Package myPackage : project.model.packages) {
			FileWriteCommandTreeNode packageTreeNode = new FileWriteCommandTreeNode(myPackage.name);
			baseRepositoryTreeNode.add(packageTreeNode);

			FileWriteCommandTreeNode interfacesTreeNode = new FileWriteCommandTreeNode("interfaces");
			packageTreeNode.add(interfacesTreeNode);
			
			for (Bean bean : myPackage.beans) {
				if (bean.isListable()) {
					FileWriteCommandTreeNode beanTreeNode = new FileWriteCommandTreeNode(new BaseRepositoryInterfaceFileWriteCommand(bean));
					interfacesTreeNode.add(beanTreeNode);
				}
			}
		}

		FileWriteCommandTreeNode repositoryTreeNode = new FileWriteCommandTreeNode("Repositories");
		persistenceTreeNode.add(repositoryTreeNode);

		for (Package myPackage : project.model.packages) {
			FileWriteCommandTreeNode packageTreeNode = new FileWriteCommandTreeNode(myPackage.name);
			repositoryTreeNode.add(packageTreeNode);

			FileWriteCommandTreeNode interfacesTreeNode = new FileWriteCommandTreeNode("interfaces");
			packageTreeNode.add(interfacesTreeNode);

			for (Bean bean : myPackage.beans) {
				if (bean.isListable()) {
					FileWriteCommandTreeNode beanTreeNode = new FileWriteCommandTreeNode(new RepositoryInterfaceFileWriteCommand(bean));
					interfacesTreeNode.add(beanTreeNode);
				}
			}
		}

		FileWriteCommandTreeNode specTreeNode = new FileWriteCommandTreeNode("Specifications");
		persistenceTreeNode.add(specTreeNode);

		for (Package myPackage : project.model.packages) {
			FileWriteCommandTreeNode packageTreeNode = new FileWriteCommandTreeNode(myPackage.name);
			specTreeNode.add(packageTreeNode);

			FileWriteCommandTreeNode specFolderTreeNode = new FileWriteCommandTreeNode("specifications");
			packageTreeNode.add(specFolderTreeNode);

			for (Bean bean : myPackage.beans) {
				if (bean.isListable()) {
					FileWriteCommandTreeNode beanTreeNode = new FileWriteCommandTreeNode(new SpecificationFileWriteCommand(bean));
					specFolderTreeNode.add(beanTreeNode);
				}
			}
		}
		
		return persistenceTreeNode;
	}
}
