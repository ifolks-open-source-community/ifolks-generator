package org.ifolks.generator.skeletons.core.commands.commands.configuration;

import java.io.File;

import org.ifolks.generator.model.domain.Project;
import org.ifolks.generator.model.metadata.files.FileType;
import org.ifolks.generator.skeletons.commands.impl.templatized.ProjectTemplatizedFileWriteCommand;

public class CommandsPomFileWriteCommand extends ProjectTemplatizedFileWriteCommand {

	public CommandsPomFileWriteCommand(Project project) {
		super(project.workspaceFolder + File.separator + project.model.commandsArtefactName, "pom", FileType.XML, project);
	}
}
