package org.ifolks.generator.skeletons.rest.commands.configuration;

import java.io.File;

import org.ifolks.generator.model.domain.Project;
import org.ifolks.generator.model.metadata.files.FileType;
import org.ifolks.generator.skeletons.commands.impl.templatized.ProjectTemplatizedFileWriteCommand;

public class SpringRestApplicationConfigFileWriteCommand extends ProjectTemplatizedFileWriteCommand {

	public SpringRestApplicationConfigFileWriteCommand(Project project) {
		super(project.workspaceFolder + File.separator + project.model.restArtefactName + File.separator + project.model.javaSourcesFolder + File.separator + project.model.restControllerPackageName.replace(".", File.separator), "ApplicationConfig", FileType.JAVA, project);
	}
}
