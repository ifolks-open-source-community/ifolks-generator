package org.ifolks.generator.skeletons.core.commands.api.model;

import java.io.File;
import java.io.IOException;

import org.ifolks.generator.model.domain.business.Bean;
import org.ifolks.generator.model.domain.ui.ViewProperty;
import org.ifolks.generator.skeletons.commands.impl.typed.JavaFileWriteCommand;


public class FormBeanFileWriteCommand extends JavaFileWriteCommand {

	private Bean bean;
	/*
	 * constructor
	 */
	public FormBeanFileWriteCommand(Bean bean) {
        
		super(bean.myPackage.model.project.workspaceFolder + File.separator + bean.myPackage.model.apiArtefactName + File.separator + bean.myPackage.model.javaSourcesFolder + File.separator + bean.myPackage.formsPackageName.replace(".",File.separator), bean.formBean.className);
		
		this.bean = bean;
		
	}
	
	@Override
	protected void fetchSpecificImports() {
		javaImports.add("import java.util.Date;");
		javaImports.add("import java.time.LocalDate;");
		javaImports.add("import java.math.BigDecimal;");
        javaImports.add("import java.io.Serializable;");
        javaImports.add("import javax.validation.constraints.NotNull;");
	}
	
	
	@Override
	protected void writeContent() throws IOException {

		writeLine("package " + bean.myPackage.formsPackageName + ";");
        skipLine();

        writeImports();
        skipLine();

        writeLine("/**");
        writeLine(" * auto generated form bean record file");
        writeLine(" * <br/>basic representation of what is going to be considered as model in MVC patterns");
        writeLine(" * <br/>write modifications between specific code marks");
        writeLine(" * <br/>processed by ifolks-generator");
        writeLine(" */");
        writeLine("public record " + this.bean.formBean.className + " (");
        
        createProperties();
        
        skipLine();
        writeLine(") {");
        skipLine();

        writeNotOverridableContent();
        
        writeLine("}");

    }

    private void createProperties() {
        boolean first = true;
        for (ViewProperty property:this.bean.formBean.properties) {
        	if (first) {
        		first = false;
        	} else {
        		writeLine(",");
        	}
        	if (!property.nullable) {
        		write("@NotNull ");
        	}
        	write(property.javaType + " " + property.name);
        }
    }
}
