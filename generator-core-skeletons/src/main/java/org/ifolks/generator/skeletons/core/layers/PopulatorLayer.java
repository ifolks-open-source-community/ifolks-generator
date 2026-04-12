package org.ifolks.generator.skeletons.core.layers;

import org.ifolks.generator.model.domain.Package;
import org.ifolks.generator.model.domain.Project;
import org.ifolks.generator.model.domain.business.Bean;
import org.ifolks.generator.model.domain.business.OneToManyComponent;
import org.ifolks.generator.model.domain.business.OneToOneComponent;
import org.ifolks.generator.skeletons.core.commands.population.BeanPopulatorCommandFileWriteCommand;
import org.ifolks.generator.skeletons.core.commands.population.BeanPopulatorFileTemplateCommandFileWriteCommand;
import org.ifolks.generator.skeletons.core.commands.population.OneToManyComponentPopulatorCommandFileWriteCommand;
import org.ifolks.generator.skeletons.core.commands.population.OneToManyComponentPopulatorFileTemplateCommandFileWriteCommand;
import org.ifolks.generator.skeletons.core.commands.population.OneToOneComponentPopulatorCommandFileWriteCommand;
import org.ifolks.generator.skeletons.core.commands.population.OneToOneComponentPopulatorFileTemplateCommandFileWriteCommand;
import org.ifolks.generator.skeletons.core.commands.population.configuration.LogbackPopulatorFileWriteCommand;
import org.ifolks.generator.skeletons.core.commands.population.configuration.PopulatorPomFileWriteCommand;
import org.ifolks.generator.skeletons.core.commands.population.configuration.PopulatorPropertiesFileWriteCommand;
import org.ifolks.generator.skeletons.core.commands.population.configuration.SpringPopulatorFileWriteCommand;
import org.ifolks.generator.skeletons.core.commands.population.configuration.SpringPopulatorRepositoryFileWriteCommand;
import org.ifolks.generator.skeletons.core.commands.population.resources.PopulatorConfigFileWriteCommand;
import org.ifolks.generator.skeletons.core.commands.population.resources.PopulatorLauncherFileWriteCommand;
import org.ifolks.generator.skeletons.layers.AbstractLayer;
import org.ifolks.generator.skeletons.tree.FileWriteCommandTreeNode;

public class PopulatorLayer extends AbstractLayer {
	
	public PopulatorLayer() {
		super("Population");
	}
	
	@Override
	public FileWriteCommandTreeNode getResourcesNode(Project project) {
		
		FileWriteCommandTreeNode resourcesTreeNode = new FileWriteCommandTreeNode();
		
		FileWriteCommandTreeNode populatorLauncherTreeNode = new FileWriteCommandTreeNode(new PopulatorLauncherFileWriteCommand(project));
		resourcesTreeNode.add(populatorLauncherTreeNode);
		
		FileWriteCommandTreeNode populatorConfigTreeNode = new FileWriteCommandTreeNode(new PopulatorConfigFileWriteCommand(project));
		resourcesTreeNode.add(populatorConfigTreeNode);
		
		return resourcesTreeNode;
	}

	@Override
	public FileWriteCommandTreeNode getConfigurationNode(Project project) {
		
		FileWriteCommandTreeNode configurationTreeNode = new FileWriteCommandTreeNode();
		
		FileWriteCommandTreeNode populatorPomTreeNode = new FileWriteCommandTreeNode(new PopulatorPomFileWriteCommand(project));
		configurationTreeNode.add(populatorPomTreeNode);
		
		FileWriteCommandTreeNode springPopulatorRepositoryTreeNode = new FileWriteCommandTreeNode(new SpringPopulatorRepositoryFileWriteCommand(project));
		configurationTreeNode.add(springPopulatorRepositoryTreeNode);
		
		FileWriteCommandTreeNode springPopulatorTreeNode = new FileWriteCommandTreeNode(new SpringPopulatorFileWriteCommand(project));
		configurationTreeNode.add(springPopulatorTreeNode);
		
		FileWriteCommandTreeNode logbackPopulatorTreeNode = new FileWriteCommandTreeNode(new LogbackPopulatorFileWriteCommand(project));
		configurationTreeNode.add(logbackPopulatorTreeNode);
		
		FileWriteCommandTreeNode populatorPropertiesTreeNode = new FileWriteCommandTreeNode(new PopulatorPropertiesFileWriteCommand(project));
		configurationTreeNode.add(populatorPropertiesTreeNode);
		
		return configurationTreeNode;
	}

	@Override
	public FileWriteCommandTreeNode getGenerationNode(Project project) {

		FileWriteCommandTreeNode populatorLayerTreeNode = new FileWriteCommandTreeNode();
        
        FileWriteCommandTreeNode commandTreeNode = new FileWriteCommandTreeNode("Population commands");
        populatorLayerTreeNode.add(commandTreeNode);

        for (Package myPackage : project.model.packages) {
            FileWriteCommandTreeNode packageTreeNode = new FileWriteCommandTreeNode(myPackage.name);
            commandTreeNode.add(packageTreeNode);

            for (Bean bean : myPackage.beans)
            {
                if (!bean.isComponent)
                {
                    FileWriteCommandTreeNode beanTreeNode = new FileWriteCommandTreeNode(new BeanPopulatorCommandFileWriteCommand(bean));
                    packageTreeNode.add(beanTreeNode);

                    for (OneToManyComponent oneToManyComponent : bean.oneToManyComponentList)
                    {
                        FileWriteCommandTreeNode oneToManyComponentTreeNode = new FileWriteCommandTreeNode(new OneToManyComponentPopulatorCommandFileWriteCommand(oneToManyComponent));
                        packageTreeNode.add(oneToManyComponentTreeNode);
                    }
                    
                    for (OneToOneComponent oneToOneComponent : bean.oneToOneComponentList)
                    {
                        FileWriteCommandTreeNode oneToOneComponentTreeNode = new FileWriteCommandTreeNode(new OneToOneComponentPopulatorCommandFileWriteCommand(oneToOneComponent));
                        packageTreeNode.add(oneToOneComponentTreeNode);
                    }
                }
            }
        }
        
        FileWriteCommandTreeNode dataCsvTemplates = new FileWriteCommandTreeNode("CSV templates");
        populatorLayerTreeNode.add(dataCsvTemplates);

        for (Package myPackage : project.model.packages)
        {
            FileWriteCommandTreeNode packageTreeNode = new FileWriteCommandTreeNode(myPackage.name);
            commandTreeNode.add(packageTreeNode);

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
        
        return populatorLayerTreeNode;
	}
}