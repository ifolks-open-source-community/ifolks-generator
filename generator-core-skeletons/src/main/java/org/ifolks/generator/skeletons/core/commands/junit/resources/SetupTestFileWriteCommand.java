package org.ifolks.generator.skeletons.core.commands.junit.resources;

import java.io.File;

import org.ifolks.generator.model.domain.Project;
import org.ifolks.generator.model.metadata.files.FileType;
import org.ifolks.generator.skeletons.commands.impl.templatized.ProjectTemplatizedFileWriteCommand;

public class SetupTestFileWriteCommand extends ProjectTemplatizedFileWriteCommand {

	public SetupTestFileWriteCommand(Project project) {
		super(project.workspaceFolder + File.separator + project.model.testsArtefactName + File.separator + project.model.testJavaSourcesFolder + File.separator + project.model.junitPackageName.replace(".", File.separator), "SetupTest", FileType.JAVA, project);
	}
}