package org.ifolks.generator.skeletons.rest.commands.configuration;

import org.ifolks.generator.model.domain.Project;
import org.ifolks.generator.model.metadata.files.FileType;
import org.ifolks.generator.skeletons.commands.impl.templatized.ProjectTemplatizedFileWriteCommand;

public class SpringRestRootPomFileWriteCommand extends ProjectTemplatizedFileWriteCommand {

	public SpringRestRootPomFileWriteCommand(Project project) {
		super(project.workspaceFolder, "pom", FileType.XML, project);
	}
}
