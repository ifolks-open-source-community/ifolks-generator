package org.ifolks.generator.skeletons.angular.commands.i18n;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.ifolks.generator.model.domain.Project;
import org.ifolks.generator.skeletons.commands.impl.typed.JsonFileWriteCommand;

public class I18nOverlayJsonFileWriteCommand extends JsonFileWriteCommand {

	public I18nOverlayJsonFileWriteCommand(Project project, String fileName) {
		super(project.workspaceFolder + File.separator + (project.model != null && project.model.tsUiArtefactName != null ? project.model.tsUiArtefactName : project.projectName + "-ui") + File.separator + "src/assets/i18n", fileName);
	}

	@Override
	public void execute() throws IOException {
		if (Files.exists(filePath)) {
			return;
		}
		super.execute();
	}

	@Override
	protected void writeContent() throws IOException {
		writeLine("{}");
	}
}
