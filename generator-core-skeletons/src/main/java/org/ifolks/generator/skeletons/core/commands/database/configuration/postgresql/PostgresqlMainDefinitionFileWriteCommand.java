package org.ifolks.generator.skeletons.core.commands.database.configuration.postgresql;

import java.io.File;

import org.ifolks.generator.components.resolvers.DatabaseHandlerDiscovery;
import org.ifolks.generator.skeletons.commands.impl.templatized.ProjectTemplatizedFileWriteCommand;
import org.ifolks.generator.skeletons.core.database.PostgresqlHandler;
import org.ifolks.generator.model.domain.Project;
import org.ifolks.generator.model.metadata.files.FileType;

public class PostgresqlMainDefinitionFileWriteCommand extends ProjectTemplatizedFileWriteCommand {

	public PostgresqlMainDefinitionFileWriteCommand(Project project) {
		super(project.workspaceFolder + File.separator + DatabaseHandlerDiscovery.getBuildScriptFolder(PostgresqlHandler.NAME), "MAIN", FileType.SQL, project);
	}
}
