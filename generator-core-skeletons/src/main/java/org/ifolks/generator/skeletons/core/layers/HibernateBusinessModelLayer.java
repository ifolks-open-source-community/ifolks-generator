package org.ifolks.generator.skeletons.core.layers;

import org.ifolks.generator.skeletons.core.commands.model.EntityBeanFileWriteCommand;
import org.ifolks.generator.skeletons.core.commands.model.EntityMetaModelFileWriteCommand;
import org.ifolks.generator.skeletons.core.commands.model.configuration.ModelPomFileWriteCommand;
import org.ifolks.generator.skeletons.core.commands.model.resources.AuditEntityFileWriteCommand;
import org.ifolks.generator.skeletons.core.commands.model.resources.AuditListenerFileWriteCommand;
import org.ifolks.generator.skeletons.layers.AbstractLayer;
import org.ifolks.generator.skeletons.tree.FileWriteCommandTreeNode;
import org.ifolks.generator.model.domain.Package;
import org.ifolks.generator.model.domain.Project;
import org.ifolks.generator.model.domain.business.Bean;

public class HibernateBusinessModelLayer extends AbstractLayer {
	
	public HibernateBusinessModelLayer() {
		super("Business Model / Hibernate mappings");
	}

	@Override
	public FileWriteCommandTreeNode getResourcesNode(Project project) {
		
		FileWriteCommandTreeNode resourcesTreeNode = new FileWriteCommandTreeNode();
		
		if (project.audited) {
			FileWriteCommandTreeNode auditEntityTreeNode = new FileWriteCommandTreeNode(new AuditEntityFileWriteCommand(project));
			resourcesTreeNode.add(auditEntityTreeNode);
		
			FileWriteCommandTreeNode auditListenerTreeNode = new FileWriteCommandTreeNode(new AuditListenerFileWriteCommand(project));
			resourcesTreeNode.add(auditListenerTreeNode);
		}
		
		return resourcesTreeNode;
	}

	@Override
	public FileWriteCommandTreeNode getConfigurationNode(Project project) {
		
		FileWriteCommandTreeNode configurationTreeNode = new FileWriteCommandTreeNode();
		
		FileWriteCommandTreeNode businessModelPomTreeNode = new FileWriteCommandTreeNode(new ModelPomFileWriteCommand(project));
		configurationTreeNode.add(businessModelPomTreeNode);
		
		return configurationTreeNode;
	}
	
	@Override
	public FileWriteCommandTreeNode getGenerationNode(Project project) {

		FileWriteCommandTreeNode businessModelTreeNode = new FileWriteCommandTreeNode();

		for (Package myPackage : project.model.packages) {
			FileWriteCommandTreeNode packageTreeNode = new FileWriteCommandTreeNode(myPackage.name);
			businessModelTreeNode.add(packageTreeNode);

			FileWriteCommandTreeNode omTreeNode = new FileWriteCommandTreeNode("Entities");
			packageTreeNode.add(omTreeNode);
			for (Bean bean : myPackage.beans) {
				FileWriteCommandTreeNode beanTreeNode = new FileWriteCommandTreeNode(new EntityBeanFileWriteCommand(bean));
				omTreeNode.add(beanTreeNode);
				
				FileWriteCommandTreeNode beanMetaModelTreeNode = new FileWriteCommandTreeNode(new EntityMetaModelFileWriteCommand(bean));
				omTreeNode.add(beanMetaModelTreeNode);
			}
		}

		return businessModelTreeNode;
	}
}
