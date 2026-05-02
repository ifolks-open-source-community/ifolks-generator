package org.ifolks.generator.skeletons.core.commands.population.configuration;

import java.io.File;

import org.ifolks.generator.model.domain.Project;
import org.ifolks.generator.model.metadata.files.FileType;
import org.ifolks.generator.skeletons.commands.impl.templatized.ProjectTemplatizedFileWriteCommand;

public class PopulatorConfigFileWriteCommand extends ProjectTemplatizedFileWriteCommand {

	public PopulatorConfigFileWriteCommand(Project project) {
		super(project.workspaceFolder + File.separator + project.model.populatorArtefactName + File.separator + project.model.javaSourcesFolder + File.separator + project.model.populationPackageName.replace(".", File.separator) + File.separator, "ApplicationConfig", FileType.JAVA, project);
	}
}