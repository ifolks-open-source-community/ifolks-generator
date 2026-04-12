package org.ifolks.generator.skeletons.core.commands.model.configuration;

import java.io.File;

import org.ifolks.generator.model.domain.Project;
import org.ifolks.generator.model.metadata.files.FileType;
import org.ifolks.generator.skeletons.commands.impl.templatized.ProjectTemplatizedFileWriteCommand;

public class ModelPomFileWriteCommand extends ProjectTemplatizedFileWriteCommand {

	public ModelPomFileWriteCommand(Project project) {
		super(project.workspaceFolder + File.separator + project.model.modelArtefactName, "pom", FileType.XML, project);
	}

}
