package org.ifolks.generator.skeletons.core.layers.database;

import org.ifolks.generator.model.domain.Package;
import org.ifolks.generator.model.domain.Project;
import org.ifolks.generator.model.domain.database.Table;
import org.ifolks.generator.skeletons.commands.impl.DirectoryResourceCopyCommand;
import org.ifolks.generator.skeletons.core.commands.database.oracle.OracleTableDefinitionFileWriteCommand;
import org.ifolks.generator.skeletons.core.commands.database.oracle.OracleTableFkDefinitionFileWriteCommand;
import org.ifolks.generator.skeletons.layers.AbstractLayer;
import org.ifolks.generator.skeletons.tree.FileWriteCommandTreeNode;

public class OracleDatabaseLayer extends AbstractLayer {
	
	public OracleDatabaseLayer() {
		super("Database definition");
	}
	
	@Override
	public FileWriteCommandTreeNode getInitializationNode(Project project) {
		FileWriteCommandTreeNode initTreeNode = new FileWriteCommandTreeNode();
		initTreeNode.add(new FileWriteCommandTreeNode(new DirectoryResourceCopyCommand(project, "/database/scripts/SQL/ORACLE", project.projectName + "-populator/" + project.model.resourcesFolder + "/scripts/SQL/ORACLE")));
		return initTreeNode;
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
				packageTreeNode.add(new FileWriteCommandTreeNode(new OracleTableDefinitionFileWriteCommand(table)));
				packageTreeNode.add(new FileWriteCommandTreeNode(new OracleTableFkDefinitionFileWriteCommand(table)));
			}
		}
		
		return databaseTreeNode;
	}
}
