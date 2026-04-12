package org.ifolks.generator.skeletons.core.commands.api.configuration;

import java.io.File;

import org.ifolks.generator.model.domain.Project;
import org.ifolks.generator.model.metadata.files.FileType;
import org.ifolks.generator.skeletons.commands.impl.templatized.ProjectTemplatizedFileWriteCommand;

public class ApiPomFileWriteCommand extends ProjectTemplatizedFileWriteCommand {

	public ApiPomFileWriteCommand(Project project) {
		super(project.workspaceFolder + File.separator + project.model.apiArtefactName, "pom", FileType.XML, project);
	}
}