package org.ifolks.generator.skeletons.core.layers;

import org.ifolks.generator.model.domain.Package;
import org.ifolks.generator.model.domain.Project;
import org.ifolks.generator.model.domain.business.Bean;
import org.ifolks.generator.model.domain.business.OneToManyComponent;
import org.ifolks.generator.model.domain.business.OneToOneComponent;
import org.ifolks.generator.skeletons.core.commands.population.BeanPopulatorFileTemplateCommandFileWriteCommand;
import org.ifolks.generator.skeletons.core.commands.population.OneToManyComponentPopulatorFileTemplateCommandFileWriteCommand;
import org.ifolks.generator.skeletons.core.commands.population.OneToOneComponentPopulatorFileTemplateCommandFileWriteCommand;
import org.ifolks.generator.skeletons.core.commands.population.configuration.LogbackPopulatorFileWriteCommand;
import org.ifolks.generator.skeletons.core.commands.population.configuration.PopulatorConfigFileWriteCommand;
import org.ifolks.generator.skeletons.core.commands.population.configuration.PopulatorPomFileWriteCommand;
import org.ifolks.generator.skeletons.core.commands.population.configuration.PopulatorPropertiesFileWriteCommand;
import org.ifolks.generator.skeletons.core.commands.population.resources.PopulatorRunnerFileWriteCommand;
import org.ifolks.generator.skeletons.core.commands.population.resources.PopulatorStarterFileWriteCommand;
import org.ifolks.generator.skeletons.layers.AbstractLayer;
import org.ifolks.generator.skeletons.tree.FileWriteCommandTreeNode;

public class PopulatorLayer extends AbstractLayer {
	
	public PopulatorLayer() {
		super("Population");
	}
	
	@Override
	public FileWriteCommandTreeNode getResourcesNode(Project project) {
		
		FileWriteCommandTreeNode resourcesTreeNode = new FileWriteCommandTreeNode();
		
		FileWriteCommandTreeNode runnerTreeNode = new FileWriteCommandTreeNode(new PopulatorRunnerFileWriteCommand(project));
		resourcesTreeNode.add(runnerTreeNode);
		
		FileWriteCommandTreeNode starterTreeNode = new FileWriteCommandTreeNode(new PopulatorStarterFileWriteCommand(project));
		resourcesTreeNode.add(starterTreeNode);
		
		return resourcesTreeNode;
	}

	@Override
	public FileWriteCommandTreeNode getConfigurationNode(Project project) {
		
		FileWriteCommandTreeNode configurationTreeNode = new FileWriteCommandTreeNode();
		
		FileWriteCommandTreeNode populatorPomTreeNode = new FileWriteCommandTreeNode(new PopulatorPomFileWriteCommand(project));
		configurationTreeNode.add(populatorPomTreeNode);
		
		FileWriteCommandTreeNode logbackPopulatorTreeNode = new FileWriteCommandTreeNode(new LogbackPopulatorFileWriteCommand(project));
		configurationTreeNode.add(logbackPopulatorTreeNode);
		
		FileWriteCommandTreeNode populatorPropertiesTreeNode = new FileWriteCommandTreeNode(new PopulatorPropertiesFileWriteCommand(project));
		configurationTreeNode.add(populatorPropertiesTreeNode);
		
		FileWriteCommandTreeNode populatorConfigTreeNode = new FileWriteCommandTreeNode(new PopulatorConfigFileWriteCommand(project));
		configurationTreeNode.add(populatorConfigTreeNode);
		
		return configurationTreeNode;
	}

	@Override
	public FileWriteCommandTreeNode getGenerationNode(Project project) {
		FileWriteCommandTreeNode layerTreeNode = new FileWriteCommandTreeNode();
		
		FileWriteCommandTreeNode dataCsvTemplates = new FileWriteCommandTreeNode("CSV templates");
        layerTreeNode.add(dataCsvTemplates);

        for (Package myPackage : project.model.packages)
        {
            FileWriteCommandTreeNode packageTreeNode = new FileWriteCommandTreeNode(myPackage.name);
            dataCsvTemplates.add(packageTreeNode);

            for (Bean bean : myPackage.beans)
            {
                if (!bean.isComponent)
                {
                    FileWriteCommandTreeNode beanTreeNode = new FileWriteCommandTreeNode(new BeanPopulatorFileTemplateCommandFileWriteCommand(bean));
                    packageTreeNode.add(beanTreeNode);

                    for (OneToManyComponent oneToManyComponent : bean.oneToManyComponentList)
                    {
                        FileWriteCommandTreeNode oneToManyComponentTreeNode = new FileWriteCommandTreeNode(new OneToManyComponentPopulatorFileTemplateCommandFileWriteCommand(oneToManyComponent));
                        packageTreeNode.add(oneToManyComponentTreeNode);
                    }
                    
                    for (OneToOneComponent oneToOneComponent : bean.oneToOneComponentList)
                    {
                        FileWriteCommandTreeNode oneToOneComponentTreeNode = new FileWriteCommandTreeNode(new OneToOneComponentPopulatorFileTemplateCommandFileWriteCommand(oneToOneComponent));
                        packageTreeNode.add(oneToOneComponentTreeNode);
                    }
                }
            }
        }
		return layerTreeNode;
	}
}