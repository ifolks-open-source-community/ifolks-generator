package org.ifolks.generator.skeletons.core.commands.population.configuration;

import java.io.File;

import org.ifolks.generator.model.domain.Project;
import org.ifolks.generator.model.metadata.files.FileType;
import org.ifolks.generator.skeletons.commands.impl.templatized.ProjectTemplatizedFileWriteCommand;

public class LogbackPopulatorFileWriteCommand extends ProjectTemplatizedFileWriteCommand {

	public LogbackPopulatorFileWriteCommand(Project project) {
		super(project.workspaceFolder + File.separator + project.model.populatorArtefactName + File.separator + project.model.resourcesFolder, "logback-spring", FileType.XML, project);
	}
}