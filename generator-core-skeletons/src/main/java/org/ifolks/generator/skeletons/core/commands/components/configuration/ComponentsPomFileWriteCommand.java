package org.ifolks.generator.skeletons.core.commands.components.configuration;

import java.io.File;

import org.ifolks.generator.model.domain.Project;
import org.ifolks.generator.model.metadata.files.FileType;
import org.ifolks.generator.skeletons.commands.impl.templatized.ProjectTemplatizedFileWriteCommand;

public class ComponentsPomFileWriteCommand extends ProjectTemplatizedFileWriteCommand {

	public ComponentsPomFileWriteCommand(Project project) {
		super(project.workspaceFolder + File.separator + project.model.componentsArtefactName, "pom", FileType.XML, project);
	}
}
