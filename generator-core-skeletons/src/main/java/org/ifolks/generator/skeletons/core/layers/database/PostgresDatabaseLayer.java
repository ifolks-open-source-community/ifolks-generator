package org.ifolks.generator.skeletons.core.layers.database;

import org.ifolks.generator.model.domain.Package;
import org.ifolks.generator.model.domain.Project;
import org.ifolks.generator.model.domain.database.Table;
import org.ifolks.generator.skeletons.core.commands.database.configuration.postgresql.PostgresqlMainDefinitionFileWriteCommand;
import org.ifolks.generator.skeletons.core.commands.database.postgresql.PostgresqlTableDefinitionFileWriteCommand;
import org.ifolks.generator.skeletons.core.commands.database.postgresql.PostgresqlTableFkDefinitionFileWriteCommand;
import org.ifolks.generator.skeletons.layers.AbstractLayer;
import org.ifolks.generator.skeletons.tree.FileWriteCommandTreeNode;

public class PostgresDatabaseLayer extends AbstractLayer {
	
	public PostgresDatabaseLayer() {
		super("Database definition");
	}
	
	@Override
	public FileWriteCommandTreeNode getResourcesNode(Project project) {
		return null;
	}

	@Override
	public FileWriteCommandTreeNode getConfigurationNode(Project project) {
		
		FileWriteCommandTreeNode configurationTreeNode = new FileWriteCommandTreeNode();
		
		FileWriteCommandTreeNode mainFileTreeNode = new FileWriteCommandTreeNode(new PostgresqlMainDefinitionFileWriteCommand(project));
		configurationTreeNode.add(mainFileTreeNode);
		
		return configurationTreeNode;
	}

	@Override
	public FileWriteCommandTreeNode getGenerationNode(Project project) {

		FileWriteCommandTreeNode databaseTreeNode = new FileWriteCommandTreeNode();

		FileWriteCommandTreeNode definitionFilesTreeNode = new FileWriteCommandTreeNode("Definition Files");
		databaseTreeNode.add(definitionFilesTreeNode);

		for (Package myPackage : project.model.packages) {
			FileWriteCommandTreeNode packageTreeNode = new FileWriteCommandTreeNode(myPackage.name);
			definitionFilesTreeNode.add(packageTreeNode);
			for (Table table : myPackage.tables) {
				packageTreeNode.add(new FileWriteCommandTreeNode(new PostgresqlTableDefinitionFileWriteCommand(table)));
				packageTreeNode.add(new FileWriteCommandTreeNode(new PostgresqlTableFkDefinitionFileWriteCommand(table)));
			}
		}
		
		return databaseTreeNode;
	}
}
