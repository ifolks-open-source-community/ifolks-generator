package org.ifolks.generator.skeletons.core.commands.components.rightmanager;

import java.io.File;
import java.io.IOException;

import org.ifolks.generator.skeletons.commands.impl.typed.JavaFileWriteCommand;
import org.ifolks.generator.model.domain.business.Bean;

public class RightsManagerImplFileWriteCommand extends JavaFileWriteCommand {

	private Bean bean;

	/*
	 * constructor
	 */
	public RightsManagerImplFileWriteCommand(Bean bean) {
		super(bean.myPackage.model.project.workspaceFolder + File.separator + bean.myPackage.model.componentsArtefactName + File.separator + bean.myPackage.model.javaSourcesFolder + File.separator
				+ bean.myPackage.rightsManagerImplPackageName.replace(".", File.separator), bean.rightsManagerClassName);

		this.bean = bean;
	}

	@Override
	protected void fetchSpecificImports() {

		javaImports.add("import org.springframework.stereotype.Component;");
        javaImports.add("import " + this.bean.myPackage.baseRightsManagerImplPackageName + "." + this.bean.baseRightsManagerClassName + ";");
		
	}

	@Override
	protected void writeContent() throws IOException {

		writeLine("package " + this.bean.myPackage.rightsManagerImplPackageName + ";");
        skipLine();
        
        writeImports();
        skipLine();

        writeLine("/**");
        writeLine(" * auto generated rights manager class file");
        writeLine(" * <br/>write modifications between specific code marks");
        writeLine(" * <br/>processed by ifolks-generator");
        writeLine(" */");
        skipLine();

        writeLine("@Component(" + CHAR_34 + bean.myPackage.model.project.projectName.toLowerCase() + this.bean.rightsManagerClassName + CHAR_34 + ")");
        writeLine("public class " + this.bean.rightsManagerClassName + " extends " + this.bean.baseRightsManagerClassName + " {");
        skipLine();
        
        this.writeNotOverridableContent();

        writeLine("}");
	}
}
