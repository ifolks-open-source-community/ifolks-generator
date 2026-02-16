package org.ifolks.generator.skeletons.core.commands.population.configuration;

import java.io.File;

import org.ifolks.generator.skeletons.commands.impl.templatized.ProjectTemplatizedFileWriteCommand;
import org.ifolks.generator.model.domain.Project;
import org.ifolks.generator.model.metadata.files.FileType;

public class PopulatorPomFileWriteCommand extends ProjectTemplatizedFileWriteCommand {

	public PopulatorPomFileWriteCommand(Project project) {
		super(project.workspaceFolder + File.separator + project.model.populatorArtefactName, "pom", FileType.XML, project);
	}
}