package org.ifolks.generator.skeletons.rest.commands.configuration;

import java.io.File;

import org.ifolks.generator.model.domain.Project;
import org.ifolks.generator.model.metadata.files.FileType;
import org.ifolks.generator.skeletons.commands.impl.templatized.ProjectTemplatizedFileWriteCommand;

public class SpringRestExecutablePomFileWriteCommand extends ProjectTemplatizedFileWriteCommand {

	public SpringRestExecutablePomFileWriteCommand(Project project) {
		super(project.workspaceFolder + File.separator + project.model.restArtefactName, "pom", FileType.XML, project);
	}
}
