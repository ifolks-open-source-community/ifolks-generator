package org.ifolks.generator.skeletons.rest.commands.configuration;

import java.io.File;

import org.ifolks.generator.model.domain.Project;
import org.ifolks.generator.model.metadata.files.FileType;
import org.ifolks.generator.skeletons.commands.impl.templatized.ProjectTemplatizedFileWriteCommand;

public class SpringRestApplicationStarterFileWriteCommand extends ProjectTemplatizedFileWriteCommand {

	public SpringRestApplicationStarterFileWriteCommand(Project project) {
		super(project.workspaceFolder + File.separator + project.model.restArtefactName + File.separator + project.model.javaSourcesFolder + File.separator + project.model.restControllerPackageName.replace(".", File.separator), "ApplicationStarter", FileType.JAVA, project);
	}
}
