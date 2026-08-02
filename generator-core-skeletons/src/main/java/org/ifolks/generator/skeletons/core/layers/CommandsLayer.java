package org.ifolks.generator.skeletons.core.layers;

import org.ifolks.generator.model.domain.Package;
import org.ifolks.generator.model.domain.Project;
import org.ifolks.generator.model.domain.business.Bean;
import org.ifolks.generator.model.domain.business.OneToManyComponent;
import org.ifolks.generator.model.domain.business.OneToOneComponent;
import org.ifolks.generator.skeletons.commands.impl.DirectoryResourceCopyCommand;
import org.ifolks.generator.skeletons.core.commands.commands.BeanPopulatorCommandFileWriteCommand;
import org.ifolks.generator.skeletons.core.commands.commands.OneToManyComponentPopulatorCommandFileWriteCommand;
import org.ifolks.generator.skeletons.core.commands.commands.OneToOneComponentPopulatorCommandFileWriteCommand;
import org.ifolks.generator.skeletons.layers.AbstractLayer;
import org.ifolks.generator.skeletons.tree.FileWriteCommandTreeNode;

public class CommandsLayer extends AbstractLayer {
	
	public CommandsLayer() {
		super("Commands");
	}
	
	@Override
	public FileWriteCommandTreeNode getInitializationNode(Project project) {
		FileWriteCommandTreeNode initTreeNode = new FileWriteCommandTreeNode();
		initTreeNode.add(new FileWriteCommandTreeNode(new DirectoryResourceCopyCommand(project, "/commands", project.projectName + "-commands")));
		return initTreeNode;
	}

	@Override
	public FileWriteCommandTreeNode getGenerationNode(Project project) {

		FileWriteCommandTreeNode layerTreeNode = new FileWriteCommandTreeNode();
        
        FileWriteCommandTreeNode commandTreeNode = new FileWriteCommandTreeNode("Population commands");
        layerTreeNode.add(commandTreeNode);

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
        
        return layerTreeNode;
	}
}
