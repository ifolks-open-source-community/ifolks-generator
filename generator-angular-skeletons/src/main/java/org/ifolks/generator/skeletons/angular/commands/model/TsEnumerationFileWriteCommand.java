package org.ifolks.generator.skeletons.angular.commands.model;

import java.io.File;
import java.io.IOException;

import org.ifolks.generator.model.domain.business.EnumItem;
import org.ifolks.generator.model.domain.business.Enumeration;
import org.ifolks.generator.model.util.naming.JavaClassNaming;
import org.ifolks.generator.skeletons.commands.impl.typed.TsFileWriteCommand;

/**
 * Template for Angular TypeScript enumeration file
 * processed by ifolks-generator
 */
public class TsEnumerationFileWriteCommand extends TsFileWriteCommand {

	private Enumeration enumeration;

	public TsEnumerationFileWriteCommand(Enumeration enumeration) {
		super(enumeration.myPackage.model.project.workspaceFolder + File.separator
				+ enumeration.myPackage.model.tsUiArtefactName + File.separator
				+ enumeration.myPackage.tsModelsPath + File.separator + "enums", enumeration.className);
		this.enumeration = enumeration;
	}

	@Override
	protected void fetchSpecificImports() {
		// Enums do not require external imports
	}

	@Override
	protected void writeContent() throws IOException {
		writeImports();

		writeLine("/**");
		writeLine(" * auto generated enumeration ts file");
		writeLine(" * <br/>write modifications between specific code marks");
		writeLine(" * <br/>processed by ifolks-generator");
		writeLine(" */");
		writeLine("export enum " + this.enumeration.className + " {");
		if (this.enumeration.items != null) {
			for (int i = 0; i < this.enumeration.items.size(); i++) {
				EnumItem item = this.enumeration.items.get(i);
				String line = "  " + item.name + " = '" + item.name + "'";
				if (i < this.enumeration.items.size() - 1) {
					line += ",";
				}
				writeLine(line);
			}
		}
		writeLine("}");
		skipLine();

		String optionsConst = JavaClassNaming.toDatabaseName(this.enumeration.className) + "_OPTIONS";
		writeLine("export const " + optionsConst + ": " + this.enumeration.className + "[] = [");
		if (this.enumeration.items != null) {
			for (int i = 0; i < this.enumeration.items.size(); i++) {
				EnumItem item = this.enumeration.items.get(i);
				String line = "  " + this.enumeration.className + "." + item.name;
				if (i < this.enumeration.items.size() - 1) {
					line += ",";
				}
				writeLine(line);
			}
		}
		writeLine("];");
		skipLine();

		writeNotOverridableContent();
	}
}
