package org.ifolks.generator.skeletons.core.commands.junit.configuration;

import java.io.File;

import org.ifolks.generator.skeletons.commands.impl.templatized.ProjectTemplatizedFileWriteCommand;
import org.ifolks.generator.model.domain.Project;
import org.ifolks.generator.model.metadata.files.FileType;

public class TestsPropertiesFileWriteCommand extends ProjectTemplatizedFileWriteCommand {

	public TestsPropertiesFileWriteCommand(Project project) {
		super(project.workspaceFolder + File.separator + project.model.testsArtefactName + File.separator + project.model.resourcesFolder, "application", FileType.PROPERTIES, project);
	}
}