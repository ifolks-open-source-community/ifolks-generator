package org.ifolks.generator.skeletons.core.commands.population.resources;

import java.io.File;

import org.ifolks.generator.model.domain.Project;
import org.ifolks.generator.model.metadata.files.FileType;
import org.ifolks.generator.skeletons.commands.impl.templatized.ProjectTemplatizedFileWriteCommand;

public class PopulatorStarterFileWriteCommand extends ProjectTemplatizedFileWriteCommand {

	public PopulatorStarterFileWriteCommand(Project project) {
		super(project.workspaceFolder + File.separator + project.model.populatorArtefactName + File.separator + project.model.javaSourcesFolder + File.separator + project.model.populationPackageName.replace(".", File.separator) + File.separator, "ApplicationStarter", FileType.JAVA, project);
	}
}
