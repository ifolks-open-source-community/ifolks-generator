package org.ifolks.generator.skeletons.core.commands.junit.configuration;

import java.io.File;

import org.ifolks.generator.model.domain.Project;
import org.ifolks.generator.model.metadata.files.FileType;
import org.ifolks.generator.skeletons.commands.impl.templatized.ProjectTemplatizedFileWriteCommand;

public class TestsPomFileWriteCommand extends ProjectTemplatizedFileWriteCommand {

	public TestsPomFileWriteCommand(Project project) {
		super(project.workspaceFolder + File.separator + project.model.testsArtefactName, "pom", FileType.XML, project);
	}
}
