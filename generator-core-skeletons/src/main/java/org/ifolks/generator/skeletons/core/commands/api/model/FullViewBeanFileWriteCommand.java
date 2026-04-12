package org.ifolks.generator.skeletons.core.commands.api.model;

import java.io.File;
import java.io.IOException;

import org.ifolks.generator.model.domain.business.Bean;
import org.ifolks.generator.skeletons.commands.impl.typed.JavaFileWriteCommand;


public class FullViewBeanFileWriteCommand extends JavaFileWriteCommand {

	private Bean bean;
	/*
	 * constructor
	 */
	public FullViewBeanFileWriteCommand(Bean bean) {
        
		super(bean.myPackage.model.project.workspaceFolder + File.separator + bean.myPackage.model.apiArtefactName + File.separator + bean.myPackage.model.javaSourcesFolder + File.separator + bean.myPackage.fullViewsPackageName.replace(".",File.separator), bean.fullViewBean.className);
		
		this.bean = bean;
		
	}
	
	@Override
	protected void fetchSpecificImports() {
		javaImports.add("import java.util.Date;");
		javaImports.add("import java.time.LocalDate;");
		javaImports.add("import java.math.BigDecimal;");
		javaImports.add("import java.io.Serializable;");
		javaImports.add("import javax.validation.constraints.NotNull;");
		javaImports.add("import " + this.bean.myPackage.formsPackageName + "." + this.bean.formBean.className + ";");
	}
	
	
	@Override
	protected void writeContent() throws IOException {

		writeLine("package " + bean.myPackage.fullViewsPackageName + ";");
		skipLine();

		writeImports();
		skipLine();

		writeLine("/**");
		writeLine(" * auto generated full view record");
		writeLine(" * <br/>basic representation of what is going to be considered as model in MVC patterns");
		writeLine(" * <br/>write modifications between specific code marks");
		writeLine(" * <br/>processed by ifolks-generator");
		writeLine(" */");
		writeLine("public record " + this.bean.fullViewBean.className + " (" + bean.idType + " id, boolean canUpdate, boolean canDelete, " + bean.formBean.className + " form) {");
		skipLine();
		
		writeNotOverridableContent();
		
		writeLine("}");

    }
}
