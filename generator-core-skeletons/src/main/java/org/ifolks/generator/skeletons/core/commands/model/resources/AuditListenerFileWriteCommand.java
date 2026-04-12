package org.ifolks.generator.skeletons.core.commands.model.resources;

import java.io.File;

import org.ifolks.generator.model.domain.Project;
import org.ifolks.generator.model.metadata.files.FileType;
import org.ifolks.generator.skeletons.commands.impl.templatized.ProjectTemplatizedFileWriteCommand;

public class AuditListenerFileWriteCommand extends ProjectTemplatizedFileWriteCommand {

	public AuditListenerFileWriteCommand(Project project) {
		super(project.workspaceFolder + File.separator + project.model.modelArtefactName + File.separator + project.model.javaSourcesFolder + File.separator + project.model.enversPackageName.replace(".", File.separator), "AuditListener", FileType.JAVA, project);
	}

}
