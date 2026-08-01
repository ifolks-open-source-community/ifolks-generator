package org.ifolks.generator.skeletons.core.layers;

import org.ifolks.generator.model.domain.Package;
import org.ifolks.generator.model.domain.Project;
import org.ifolks.generator.model.domain.business.Bean;
import org.ifolks.generator.model.domain.business.OneToManyComponent;
import org.ifolks.generator.model.domain.business.OneToOneComponent;
import org.ifolks.generator.skeletons.core.commands.population.BeanPopulatorFileTemplateCommandFileWriteCommand;
import org.ifolks.generator.skeletons.core.commands.population.OneToManyComponentPopulatorFileTemplateCommandFileWriteCommand;
import org.ifolks.generator.skeletons.core.commands.population.OneToOneComponentPopulatorFileTemplateCommandFileWriteCommand;
import org.ifolks.generator.skeletons.commands.impl.DirectoryResourceCopyCommand;
import org.ifolks.generator.skeletons.layers.AbstractLayer;
import org.ifolks.generator.skeletons.tree.FileWriteCommandTreeNode;

public class PopulatorLayer extends AbstractLayer {
	
	public PopulatorLayer() {
		super("Population");
	}
	
	@Override
	public FileWriteCommandTreeNode getInitializationNode(Project project) {
		FileWriteCommandTreeNode initTreeNode = new FileWriteCommandTreeNode();
		initTreeNode.add(new FileWriteCommandTreeNode(new DirectoryResourceCopyCommand(project, "/populator/pom.xml.vm", project.projectName + "-populator")));
		initTreeNode.add(new FileWriteCommandTreeNode(new DirectoryResourceCopyCommand(project, "/populator/resources", project.projectName + "-populator/" + project.model.resourcesFolder)));
		
		String populatorPackagePath = project.model.populationPackageName.replace('.', '/');
		initTreeNode.add(new FileWriteCommandTreeNode(new DirectoryResourceCopyCommand(project, "/populator/java", project.projectName + "-populator/" + project.model.javaSourcesFolder + "/" + populatorPackagePath)));
		return initTreeNode;
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
