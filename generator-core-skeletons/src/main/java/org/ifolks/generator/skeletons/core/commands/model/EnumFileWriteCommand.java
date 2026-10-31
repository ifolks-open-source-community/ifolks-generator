package org.ifolks.generator.skeletons.core.commands.model;

import java.io.File;
import java.io.IOException;

import org.ifolks.generator.model.domain.business.EnumItem;
import org.ifolks.generator.model.domain.business.Enumeration;
import org.ifolks.generator.skeletons.commands.impl.typed.JavaFileWriteCommand;

/**
 * Template for Java enum class file
 * processed by ifolks-generator
 */
public class EnumFileWriteCommand extends JavaFileWriteCommand {

	private Enumeration enumeration;

	public EnumFileWriteCommand(Enumeration enumeration) {
		super(enumeration.myPackage.model.project.workspaceFolder + File.separator
				+ enumeration.myPackage.model.modelArtefactName + File.separator
				+ enumeration.myPackage.model.javaSourcesFolder + File.separator
				+ enumeration.myPackage.omPackageName.replace(".", File.separator), enumeration.className);
		this.enumeration = enumeration;
	}

	@Override
	protected void fetchSpecificImports() {
		// Enums do not require specific imports by default
	}

	@Override
	protected void writeContent() throws IOException {
		writeLine("package " + this.enumeration.myPackage.omPackageName + ";");
		skipLine();

		writeImports();
		skipLine();

		writeLine("/**");
		writeLine(" * auto generated enumeration class file");
		writeLine(" * <br/>write modifications between specific code marks");
		writeLine(" * <br/>processed by ifolks-generator");
		writeLine(" */");

		writeLine("public enum " + this.enumeration.className + " {");
		skipLine();

		if (enumeration.items != null) {
			for (int i = 0; i < enumeration.items.size(); i++) {
				EnumItem item = enumeration.items.get(i);
				String line = "\t" + item.name;
				if (i < enumeration.items.size() - 1) {
					line += ",";
				} else {
					line += ";";
				}
				writeLine(line);
			}
		}

		skipLine();
		this.writeNotOverridableContent();

		write("}");
	}
}
