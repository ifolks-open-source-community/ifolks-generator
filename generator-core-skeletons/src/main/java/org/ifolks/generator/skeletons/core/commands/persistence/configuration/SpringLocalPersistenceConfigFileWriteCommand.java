package org.ifolks.generator.skeletons.core.commands.persistence.configuration;

import java.io.File;

import org.ifolks.generator.skeletons.commands.impl.templatized.ProjectTemplatizedFileWriteCommand;
import org.ifolks.generator.model.domain.Project;
import org.ifolks.generator.model.metadata.files.FileType;

public class SpringLocalPersistenceConfigFileWriteCommand extends ProjectTemplatizedFileWriteCommand {

	public SpringLocalPersistenceConfigFileWriteCommand(Project project) {
		super(project.workspaceFolder + File.separator + project.model.persistenceArtefactName + File.separator + project.model.javaSourcesFolder + File.separator + project.model.persistencePackageName.replace(".", File.separator), "LocalPersistenceConfig", FileType.JAVA, project);
	}
}