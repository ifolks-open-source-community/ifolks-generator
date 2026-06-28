package org.ifolks.generator.skeletons.core.commands.persistence.configuration;

import java.io.File;

import org.ifolks.generator.model.domain.Project;
import org.ifolks.generator.model.metadata.files.FileType;
import org.ifolks.generator.skeletons.commands.impl.templatized.ProjectTemplatizedFileWriteCommand;

public class PersistencePomFileWriteCommand extends ProjectTemplatizedFileWriteCommand {

	public PersistencePomFileWriteCommand(Project project) {
		super(project.workspaceFolder + File.separator + project.model.persistenceArtefactName, "pom", FileType.XML, project);
	}
}
