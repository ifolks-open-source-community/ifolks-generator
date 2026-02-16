package org.ifolks.generator.skeletons.core.layers;

import org.ifolks.generator.skeletons.core.commands.components.configuration.ComponentsPomFileWriteCommand;
import org.ifolks.generator.skeletons.core.commands.components.configuration.SpringComponentsConfigFileWriteCommand;
import org.ifolks.generator.skeletons.core.commands.components.mapper.BaseBasicViewMapperFileWriteCommand;
import org.ifolks.generator.skeletons.core.commands.components.mapper.BaseFormMapperFileWriteCommand;
import org.ifolks.generator.skeletons.core.commands.components.mapper.BaseFullViewMapperFileWriteCommand;
import org.ifolks.generator.skeletons.core.commands.components.mapper.BasicViewMapperFileWriteCommand;
import org.ifolks.generator.skeletons.core.commands.components.mapper.FormMapperFileWriteCommand;
import org.ifolks.generator.skeletons.core.commands.components.mapper.FullViewMapperFileWriteCommand;
import org.ifolks.generator.skeletons.core.commands.components.processor.BaseProcessorImplFileWriteCommand;
import org.ifolks.generator.skeletons.core.commands.components.processor.ProcessorImplFileWriteCommand;
import org.ifolks.generator.skeletons.core.commands.components.rightmanager.BaseRightsManagerImplFileWriteCommand;
import org.ifolks.generator.skeletons.core.commands.components.rightmanager.RightsManagerImplFileWriteCommand;
import org.ifolks.generator.skeletons.core.commands.components.statemanager.BaseStateManagerImplFileWriteCommand;
import org.ifolks.generator.skeletons.core.commands.components.statemanager.StateManagerImplFileWriteCommand;
import org.ifolks.generator.skeletons.layers.AbstractLayer;
import org.ifolks.generator.skeletons.tree.FileWriteCommandTreeNode;
import org.ifolks.generator.model.domain.Package;
import org.ifolks.generator.model.domain.Project;
import org.ifolks.generator.model.domain.business.Bean;

public class BusinessComponentLayer extends AbstractLayer {
	
	public BusinessComponentLayer() {
		super("Business Components");
	}
	
	@Override
	public FileWriteCommandTreeNode getResourcesNode(Project project) {
		return null;
	}

	@Override
	public FileWriteCommandTreeNode getConfigurationNode(Project project) {
		
		FileWriteCommandTreeNode configurationTreeNode = new FileWriteCommandTreeNode();
		
		FileWriteCommandTreeNode businessComponentPomTreeNode = new FileWriteCommandTreeNode(new ComponentsPomFileWriteCommand(project));
		configurationTreeNode.add(businessComponentPomTreeNode);
		
		FileWriteCommandTreeNode springBusinessComponentTreeNode = new FileWriteCommandTreeNode(new SpringComponentsConfigFileWriteCommand(project));
		configurationTreeNode.add(springBusinessComponentTreeNode);
		
		return configurationTreeNode;
	}
	
	@Override
	public FileWriteCommandTreeNode getGenerationNode(Project project) {

		FileWriteCommandTreeNode bcTreeNode = new FileWriteCommandTreeNode();

		FileWriteCommandTreeNode baseStateManagerTreeNode = new FileWriteCommandTreeNode("Base state managers");
		bcTreeNode.add(baseStateManagerTreeNode);

		for (Package myPackage : project.model.packages) {
			FileWriteCommandTreeNode packageTreeNode = new FileWriteCommandTreeNode(myPackage.name);
			baseStateManagerTreeNode.add(packageTreeNode);
			
			for (Bean bean : myPackage.beans) {
				if (!bean.isComponent) {
					FileWriteCommandTreeNode beanTreeNode = new FileWriteCommandTreeNode(new BaseStateManagerImplFileWriteCommand(bean));
					baseStateManagerTreeNode.add(beanTreeNode);
				}
			}
		}

		FileWriteCommandTreeNode stateManagerTreeNode = new FileWriteCommandTreeNode("State managers");
		bcTreeNode.add(stateManagerTreeNode);

		for (Package myPackage : project.model.packages) {
			FileWriteCommandTreeNode packageTreeNode = new FileWriteCommandTreeNode(myPackage.name);
			stateManagerTreeNode.add(packageTreeNode);
			
			for (Bean bean : myPackage.beans) {
				if (!bean.isComponent) {
					FileWriteCommandTreeNode beanTreeNode = new FileWriteCommandTreeNode(new StateManagerImplFileWriteCommand(bean));
					packageTreeNode.add(beanTreeNode);
				}
			}
		}
		
		
		//Rights Managers
		FileWriteCommandTreeNode baseRightsManagerTreeNode = new FileWriteCommandTreeNode("Base rights managers");
		bcTreeNode.add(baseRightsManagerTreeNode );

		for (Package myPackage : project.model.packages) {
			FileWriteCommandTreeNode packageTreeNode = new FileWriteCommandTreeNode(myPackage.name);
			baseRightsManagerTreeNode .add(packageTreeNode);
			
			for (Bean bean : myPackage.beans) {
				if (!bean.isComponent) {
					FileWriteCommandTreeNode beanTreeNode = new FileWriteCommandTreeNode(new BaseRightsManagerImplFileWriteCommand(bean));
					baseRightsManagerTreeNode .add(beanTreeNode);
				}
			}
		}

		FileWriteCommandTreeNode rightsManagerTreeNode = new FileWriteCommandTreeNode("Rights managers");
		bcTreeNode.add(rightsManagerTreeNode);

		for (Package myPackage : project.model.packages) {
			FileWriteCommandTreeNode packageTreeNode = new FileWriteCommandTreeNode(myPackage.name);
			rightsManagerTreeNode.add(packageTreeNode);
			
			for (Bean bean : myPackage.beans) {
				if (!bean.isComponent) {
					FileWriteCommandTreeNode beanTreeNode = new FileWriteCommandTreeNode(new RightsManagerImplFileWriteCommand(bean));
					packageTreeNode.add(beanTreeNode);
				}
			}
		}
		
		
		
		FileWriteCommandTreeNode baseProcessorTreeNode = new FileWriteCommandTreeNode("Base processors");
		bcTreeNode.add(baseProcessorTreeNode);

		for (Package myPackage : project.model.packages) {
			FileWriteCommandTreeNode packageTreeNode = new FileWriteCommandTreeNode(myPackage.name);
			baseProcessorTreeNode.add(packageTreeNode);
			
			for (Bean bean : myPackage.beans) {
				if (!bean.isComponent) {
					FileWriteCommandTreeNode beanTreeNode = new FileWriteCommandTreeNode(new BaseProcessorImplFileWriteCommand(bean));
					baseProcessorTreeNode.add(beanTreeNode);
				}
			}
		}

		FileWriteCommandTreeNode processorTreeNode = new FileWriteCommandTreeNode("Processors");
		bcTreeNode.add(processorTreeNode);

		for (Package myPackage : project.model.packages) {
			FileWriteCommandTreeNode packageTreeNode = new FileWriteCommandTreeNode(myPackage.name);
			processorTreeNode.add(packageTreeNode);
			
			for (Bean bean : myPackage.beans) {
				if (!bean.isComponent) {
					FileWriteCommandTreeNode beanTreeNode = new FileWriteCommandTreeNode(new ProcessorImplFileWriteCommand(bean));
					packageTreeNode.add(beanTreeNode);
				}
			}
		}
		
		
		
		FileWriteCommandTreeNode baseMapperTreeNode = new FileWriteCommandTreeNode("Base mappers");
		bcTreeNode.add(baseMapperTreeNode);

		for (Package myPackage : project.model.packages) {
			FileWriteCommandTreeNode packageTreeNode = new FileWriteCommandTreeNode(myPackage.name);
			baseMapperTreeNode.add(packageTreeNode);
			
			for (Bean bean : myPackage.beans) {
				if (!bean.isEmbedded) {
					FileWriteCommandTreeNode basicMapperTreeNode = new FileWriteCommandTreeNode(new BaseBasicViewMapperFileWriteCommand(bean));
					packageTreeNode.add(basicMapperTreeNode);
					
					FileWriteCommandTreeNode formMapperTreeNode = new FileWriteCommandTreeNode(new BaseFormMapperFileWriteCommand(bean));
					packageTreeNode.add(formMapperTreeNode);
					
					FileWriteCommandTreeNode fullMapperTreeNode = new FileWriteCommandTreeNode(new BaseFullViewMapperFileWriteCommand(bean));
					packageTreeNode.add(fullMapperTreeNode);
				}
			}
		}

		FileWriteCommandTreeNode mapperTreeNode = new FileWriteCommandTreeNode("Mappers");
		bcTreeNode.add(mapperTreeNode);

		for (Package myPackage : project.model.packages) {
			FileWriteCommandTreeNode packageTreeNode = new FileWriteCommandTreeNode(myPackage.name);
			mapperTreeNode.add(packageTreeNode);
			
			for (Bean bean : myPackage.beans) {
				if (!bean.isEmbedded) {
					FileWriteCommandTreeNode basicMapperTreeNode = new FileWriteCommandTreeNode(new BasicViewMapperFileWriteCommand(bean));
					packageTreeNode.add(basicMapperTreeNode);
					
					FileWriteCommandTreeNode formMapperTreeNode = new FileWriteCommandTreeNode(new FormMapperFileWriteCommand(bean));
					packageTreeNode.add(formMapperTreeNode);
					
					FileWriteCommandTreeNode fullMapperTreeNode = new FileWriteCommandTreeNode(new FullViewMapperFileWriteCommand(bean));
					packageTreeNode.add(fullMapperTreeNode);
				}
			}
		}
		
		return bcTreeNode;
	}
}
