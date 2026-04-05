package org.ifolks.generator.skeletons.core.commands.api.model;

import java.io.File;
import java.io.IOException;

import org.ifolks.generator.skeletons.commands.impl.typed.JavaFileWriteCommand;
import org.ifolks.generator.model.domain.business.Bean;
import org.ifolks.generator.model.domain.ui.ViewProperty;


public class BasicViewBeanFileWriteCommand extends JavaFileWriteCommand {

	private Bean bean;
	/*
	 * constructor
	 */
	public BasicViewBeanFileWriteCommand(Bean bean) {
        
		super(bean.myPackage.model.project.workspaceFolder + File.separator + bean.myPackage.model.apiArtefactName + File.separator + bean.myPackage.model.javaSourcesFolder + File.separator + bean.myPackage.basicViewsPackageName.replace(".",File.separator), bean.basicViewBean.recordName);
		
		this.bean = bean;
		
	}
	
	@Override
	protected void fetchSpecificImports() {
		javaImports.add("import java.util.Date;");
		javaImports.add("import java.time.LocalDate;");
		javaImports.add("import java.math.BigDecimal;");
	}
	
	
	@Override
	protected void writeContent() throws IOException {

		writeLine("package " + bean.myPackage.basicViewsPackageName + ";");
        skipLine();

        writeImports();
        skipLine();

        writeLine("/**");
        writeLine(" * auto generated basic view record");
        writeLine(" * <br/>basic representation of an entity, adapted to list screens");
        writeLine(" * <br/>write modifications between specific code marks");
        writeLine(" * <br/>processed by ifolks-generator");
        writeLine(" */");
        writeLine("public record " + this.bean.basicViewBean.recordName + " (");
        skipLine();

        createProperties();
        
        skipLine();
        writeLine(") {");

        writeNotOverridableContent();
        
        writeLine("}");

    }

    private void createProperties()
    {
        writeLine(bean.idType + " id,");
        writeLine("boolean selected,");
        write("boolean canDelete");

        for (ViewProperty property:this.bean.basicViewBean.properties) {
        	writeLine(",");
        	write(property.javaType + " " + property.name);
        }
        skipLine();

    }
}
