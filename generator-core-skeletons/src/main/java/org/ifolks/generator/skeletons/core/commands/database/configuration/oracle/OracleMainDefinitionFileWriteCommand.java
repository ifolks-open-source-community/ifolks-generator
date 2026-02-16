package org.ifolks.generator.skeletons.core.commands.database.configuration.oracle;

import java.io.File;

import org.ifolks.generator.components.resolvers.DatabaseHandlerDiscovery;
import org.ifolks.generator.skeletons.commands.impl.templatized.ProjectTemplatizedFileWriteCommand;
import org.ifolks.generator.skeletons.core.database.OracleHandler;
import org.ifolks.generator.model.domain.Project;
import org.ifolks.generator.model.metadata.files.FileType;

public class OracleMainDefinitionFileWriteCommand extends ProjectTemplatizedFileWriteCommand {

	public OracleMainDefinitionFileWriteCommand(Project project) {
		super(project.workspaceFolder + File.separator + DatabaseHandlerDiscovery.getBuildScriptFolder(OracleHandler.NAME), "MAIN", FileType.SQL, project);
	}
}
