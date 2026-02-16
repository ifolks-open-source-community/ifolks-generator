package org.ifolks.generator.skeletons.rest.commands.configuration;

import java.io.File;

import org.ifolks.generator.model.domain.Project;
import org.ifolks.generator.model.metadata.files.FileType;
import org.ifolks.generator.skeletons.commands.impl.templatized.ProjectTemplatizedFileWriteCommand;

public class SpringRestClientConfigFileWriteCommand extends ProjectTemplatizedFileWriteCommand {

	public SpringRestClientConfigFileWriteCommand(Project project) {
		super(project.workspaceFolder + File.separator + project.model.restClientArtefactName + File.separator + project.model.javaSourcesFolder + File.separator + project.model.restClientPackageName.replace(".", File.separator), "RestClientConfig", FileType.JAVA, project);
	}
}