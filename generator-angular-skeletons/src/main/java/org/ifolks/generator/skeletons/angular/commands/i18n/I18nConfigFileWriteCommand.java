package org.ifolks.generator.skeletons.angular.commands.i18n;

import java.io.File;
import java.io.IOException;

import org.ifolks.generator.model.domain.Locale;
import org.ifolks.generator.model.domain.Project;
import org.ifolks.generator.skeletons.commands.impl.typed.TsFileWriteCommand;

public class I18nConfigFileWriteCommand extends TsFileWriteCommand {

	private Project project;

	public I18nConfigFileWriteCommand(Project project) {
		super(project.workspaceFolder + File.separator + (project.model != null && project.model.tsUiArtefactName != null ? project.model.tsUiArtefactName : project.projectName + "-ui") + File.separator + "src/app/core/services", "i18n.config");
		this.project = project;
	}

	@Override
	protected void fetchSpecificImports() {
		imports.add("import { I18nConfig } from './I18nService';");
	}

	@Override
	protected void writeContent() throws IOException {
		writeImports();
		skipLine();

		boolean hasDefaultLocale = project.defaultLocale != null && !project.defaultLocale.trim().isEmpty();
		boolean hasLocales = project.supportedLocales != null && !project.supportedLocales.isEmpty();

		if (!hasDefaultLocale && !hasLocales) {
			writeLine("export const i18nConfig: I18nConfig = {};");
			return;
		}

		writeLine("export const i18nConfig: I18nConfig = {");

		if (hasDefaultLocale) {
			writeLine("  defaultLocale: '" + project.defaultLocale.trim() + "'" + (hasLocales ? "," : ""));
		}

		if (hasLocales) {
			writeLine("  supportedLocales: [");
			int count = project.supportedLocales.size();
			for (int i = 0; i < count; i++) {
				Locale locale = project.supportedLocales.get(i);
				String trailingComma = (i < count - 1) ? "," : "";
				if (locale.label != null && !locale.label.trim().isEmpty()) {
					writeLine("    { code: '" + locale.code + "', label: '" + escapeTs(locale.label) + "' }" + trailingComma);
				} else {
					writeLine("    '" + locale.code + "'" + trailingComma);
				}
			}
			writeLine("  ]");
		}

		writeLine("};");
	}

	private String escapeTs(String value) {
		if (value == null) {
			return "";
		}
		return value.replace("'", "\\'");
	}
}
