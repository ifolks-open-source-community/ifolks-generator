package org.ifolks.generator.skeletons.angular.layers;

import org.ifolks.generator.skeletons.angular.commands.model.TsBasicViewBeanFileWriteCommand;
import org.ifolks.generator.skeletons.angular.commands.model.TsFilterFileWriteCommand;
import org.ifolks.generator.skeletons.angular.commands.model.TsFormBeanFileWriteCommand;
import org.ifolks.generator.skeletons.angular.commands.model.TsFullViewBeanFileWriteCommand;
import org.ifolks.generator.skeletons.angular.commands.model.TsSortingFileWriteCommand;
import org.ifolks.generator.model.domain.Package;
import org.ifolks.generator.model.domain.Project;
import org.ifolks.generator.model.domain.business.Bean;
import org.ifolks.generator.skeletons.layers.AbstractLayer;
import org.ifolks.generator.skeletons.tree.FileWriteCommandTreeNode;

public class TypeScriptModelLayer extends AbstractLayer {
	
	public TypeScriptModelLayer() {
		super("TypeScript Model");
	}

	@Override
	public FileWriteCommandTreeNode getResourcesNode(Project project) {
		return null;
	}

	@Override
	public FileWriteCommandTreeNode getConfigurationNode(Project project) {
		
		return null;
	}

	@Override
	public FileWriteCommandTreeNode getGenerationNode(Project project) {

		FileWriteCommandTreeNode modelTreeNode = new FileWriteCommandTreeNode();
		
		
		for (Package myPackage : project.model.packages) {
			FileWriteCommandTreeNode packageTreeNode = new FileWriteCommandTreeNode(myPackage.name);
			modelTreeNode.add(packageTreeNode);

			FileWriteCommandTreeNode basicViewsTreeNode = new FileWriteCommandTreeNode("Basic views");
			packageTreeNode.add(basicViewsTreeNode);
			for (Bean bean : myPackage.beans) {
				if (!bean.isEmbedded) {
					FileWriteCommandTreeNode basicViewTreeNode = new FileWriteCommandTreeNode(new TsBasicViewBeanFileWriteCommand(bean));
					basicViewsTreeNode.add(basicViewTreeNode);
				}
			}
			
			FileWriteCommandTreeNode formsTreeNode = new FileWriteCommandTreeNode("Forms");
			packageTreeNode.add(formsTreeNode);
			for (Bean bean : myPackage.beans) {
				if (!bean.isEmbedded) {
					FileWriteCommandTreeNode formTreeNode = new FileWriteCommandTreeNode(new TsFormBeanFileWriteCommand(bean));
					formsTreeNode.add(formTreeNode);
				}
			}
			
			FileWriteCommandTreeNode fullViewsTreeNode = new FileWriteCommandTreeNode("Full views");
			packageTreeNode.add(fullViewsTreeNode);
			for (Bean bean : myPackage.beans) {
				if (!bean.isEmbedded) {
					FileWriteCommandTreeNode fullViewTreeNode = new FileWriteCommandTreeNode(new TsFullViewBeanFileWriteCommand(bean));
					fullViewsTreeNode.add(fullViewTreeNode);
				}
			}
			
			FileWriteCommandTreeNode filtersTreeNode = new FileWriteCommandTreeNode("Filters");
			packageTreeNode.add(filtersTreeNode);
			for (Bean bean : myPackage.beans) {
				if (!bean.isEmbedded) {
					FileWriteCommandTreeNode filterTreeNode = new FileWriteCommandTreeNode(new TsFilterFileWriteCommand(bean));
					filtersTreeNode.add(filterTreeNode);
				}
			}
			
			FileWriteCommandTreeNode sortingsTreeNode = new FileWriteCommandTreeNode("Sortings");
			packageTreeNode.add(sortingsTreeNode);
			for (Bean bean : myPackage.beans) {
				if (!bean.isEmbedded) {
					FileWriteCommandTreeNode sortringTreeNode = new FileWriteCommandTreeNode(new TsSortingFileWriteCommand(bean));
					sortingsTreeNode.add(sortringTreeNode);
				}
			}
		}

		return modelTreeNode;
	}
}
