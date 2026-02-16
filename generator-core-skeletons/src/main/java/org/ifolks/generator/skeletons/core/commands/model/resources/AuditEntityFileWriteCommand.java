package org.ifolks.generator.skeletons.core.commands.model.resources;

import java.io.File;

import org.ifolks.generator.skeletons.commands.impl.templatized.ProjectTemplatizedFileWriteCommand;
import org.ifolks.generator.model.domain.Project;
import org.ifolks.generator.model.metadata.files.FileType;

public class AuditEntityFileWriteCommand extends ProjectTemplatizedFileWriteCommand {

	public AuditEntityFileWriteCommand(Project project) {
		super(project.workspaceFolder + File.separator + project.model.modelArtefactName + File.separator + project.model.javaSourcesFolder + File.separator + project.model.enversPackageName.replace(".", File.separator), "AuditEntity", FileType.JAVA, project);
	}

}
