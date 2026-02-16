package org.ifolks.generator.skeletons.core.commands.services.configuration;

import java.io.File;

import org.ifolks.generator.skeletons.commands.impl.templatized.ProjectTemplatizedFileWriteCommand;
import org.ifolks.generator.model.domain.Project;
import org.ifolks.generator.model.metadata.files.FileType;

public class ServicesPomFileWriteCommand extends ProjectTemplatizedFileWriteCommand {

	public ServicesPomFileWriteCommand(Project project) {
		super(project.workspaceFolder + File.separator + project.model.servicesArtefactName, "pom", FileType.XML, project);
	}
}