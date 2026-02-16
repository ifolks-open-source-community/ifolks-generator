package org.ifolks.generator.skeletons.core.commands.components.configuration;

import java.io.File;

import org.ifolks.generator.skeletons.commands.impl.templatized.ProjectTemplatizedFileWriteCommand;
import org.ifolks.generator.model.domain.Project;
import org.ifolks.generator.model.metadata.files.FileType;

public class SpringComponentsConfigFileWriteCommand extends ProjectTemplatizedFileWriteCommand {

	public SpringComponentsConfigFileWriteCommand(Project project) {
		super(project.workspaceFolder + File.separator + project.model.componentsArtefactName + File.separator + project.model.javaSourcesFolder + File.separator + project.model.componentsPackageName.replace(".", File.separator), "ComponentsConfig", FileType.JAVA, project);
	}
}