package org.ifolks.generator.skeletons.angular.commands.i18n;

import java.io.File;
import java.io.IOException;

import org.ifolks.generator.model.domain.Package;
import org.ifolks.generator.model.domain.Project;
import org.ifolks.generator.model.domain.business.Bean;
import org.ifolks.generator.model.domain.ui.ViewProperty;
import org.ifolks.generator.skeletons.commands.impl.typed.JsonFileWriteCommand;

public class I18nModelJsonFileWriteCommand extends JsonFileWriteCommand {

	private Project project;

	public I18nModelJsonFileWriteCommand(Project project) {
		super(project.workspaceFolder + File.separator + project.model.tsUiArtefactName + File.separator + "src/assets/i18n", "model");
		this.project = project;
	}

	@Override
	protected void writeContent() throws IOException {
		writeLine("{");
		boolean first = true;
		
		for (Package myPackage : project.model.packages) {
			for (Bean bean : myPackage.beans) {
				if (!first) {
					writeLine(",");
				}
				first = false;
				writeLine("  \"" + bean.objectName + "List\": \"" + escapeJson(bean.listRendering) + "\",");
				write("  \"" + bean.objectName + "Details\": \"" + escapeJson(bean.detailRendering) + "\"");

				for (ViewProperty property : bean.formBean.properties) {
					writeLine(",");
					write("  \"" + bean.objectName + property.capName + "\": \"" + escapeJson(property.rendering) + "\"");
				}
			}
		}
		
		if (!first) {
			skipLine();
		}
		writeLine("}");
	}

	private String escapeJson(String value) {
		if (value == null) {
			return "";
		}
		return value.replace("\"", "\\\"");
	}
}
