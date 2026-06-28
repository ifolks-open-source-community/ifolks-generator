package org.ifolks.generator.skeletons.core.commands.junit.resources;

import java.io.File;

import org.ifolks.generator.model.domain.Project;
import org.ifolks.generator.model.metadata.files.FileType;
import org.ifolks.generator.skeletons.commands.impl.templatized.ProjectTemplatizedFileWriteCommand;

public class JUnitDataInitializerFileWriteCommand extends ProjectTemplatizedFileWriteCommand {

	public JUnitDataInitializerFileWriteCommand(Project project) {
		super(project.workspaceFolder + File.separator + project.model.testsArtefactName + File.separator + project.model.javaSourcesFolder + File.separator + project.model.junitPackageName.replace(".", File.separator), "JUnitDataInitializer", FileType.JAVA, project);
	}
}
