package org.ifolks.generator.skeletons.core.commands.junit.configuration;

import java.io.File;

import org.ifolks.generator.model.domain.Project;
import org.ifolks.generator.model.metadata.files.FileType;
import org.ifolks.generator.skeletons.commands.impl.templatized.ProjectTemplatizedFileWriteCommand;

public class SpringJUnitPersistenceConfigFileWriteCommand extends ProjectTemplatizedFileWriteCommand {

	public SpringJUnitPersistenceConfigFileWriteCommand(Project project) {
		super(project.workspaceFolder + File.separator + project.model.persistenceArtefactName + File.separator + project.model.javaSourcesFolder + File.separator + project.model.persistencePackageName.replace(".", File.separator), "JUnitPersistenceConfig", FileType.JAVA, project);
	}
}
