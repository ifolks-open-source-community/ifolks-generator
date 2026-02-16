package org.ifolks.generator.skeletons.core.commands.population.resources;

import java.io.File;

import org.ifolks.generator.skeletons.commands.impl.templatized.ProjectTemplatizedFileWriteCommand;
import org.ifolks.generator.model.domain.Project;
import org.ifolks.generator.model.metadata.files.FileType;

public class PopulatorLauncherFileWriteCommand extends ProjectTemplatizedFileWriteCommand {

	public PopulatorLauncherFileWriteCommand(Project project) {
		super(project.workspaceFolder + File.separator + project.model.populatorArtefactName + File.separator + project.model.javaSourcesFolder + File.separator + project.model.executorPackageName.replace(".", File.separator) + File.separator, "PopulatorLauncher", FileType.JAVA, project);
	}
}