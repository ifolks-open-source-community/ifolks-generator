package org.ifolks.generator.skeletons.core.commands.database.configuration.postgresql;

import java.io.File;

import org.ifolks.generator.components.database.DatabaseHandlerDiscovery;
import org.ifolks.generator.model.domain.Project;
import org.ifolks.generator.model.metadata.files.FileType;
import org.ifolks.generator.skeletons.commands.impl.templatized.ProjectTemplatizedFileWriteCommand;
import org.ifolks.generator.skeletons.core.database.PostgresqlHandler;

public class PostgresqlMainDefinitionFileWriteCommand extends ProjectTemplatizedFileWriteCommand {

	public PostgresqlMainDefinitionFileWriteCommand(Project project) {
		super(project.workspaceFolder + File.separator + DatabaseHandlerDiscovery.getBuildScriptFolder(PostgresqlHandler.NAME, project.projectName), "MAIN", FileType.SQL, project);
	}
}
