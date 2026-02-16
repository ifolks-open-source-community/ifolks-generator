package org.ifolks.generator.skeletons.angular.commands.model;

import java.io.File;
import java.io.IOException;

import org.ifolks.generator.model.domain.business.Bean;
import org.ifolks.generator.model.domain.ui.FilterProperty;
import org.ifolks.generator.skeletons.commands.impl.typed.TsFileWriteCommand;


public class TsFilterFileWriteCommand extends TsFileWriteCommand {

	private Bean bean;
	/*
	 * constructor
	 */
	public TsFilterFileWriteCommand(Bean bean) {
        
		super(bean.myPackage.model.project.workspaceFolder + File.separator + bean.myPackage.model.tsUiArtefactName + File.separator + bean.myPackage.tsModelsPath + File.separator + "filters", bean.basicViewBean.filter.className);
		
		this.bean = bean;
		
	}
	
	@Override
	protected void fetchSpecificImports() {
		//
	}
	
	
	@Override
	protected void writeContent() throws IOException {

        writeImports();
        
        writeLine("/**");
        writeLine(" * auto generated filter ts file");
        writeLine(" * <br/>write modifications between specific code marks");
        writeLine(" * <br/>processed by ifolks-generator");
        writeLine(" */");
        writeLine("export class " + this.bean.basicViewBean.filter.className + " {");
        skipLine();

        createProperties();

        writeNotOverridableContent();
        
        writeLine("}");

    }

    private void createProperties() {
        
        for (FilterProperty property:this.bean.basicViewBean.filter.properties) {
			writeLine(property.name + ": " + property.tsType + ";");
        }
        skipLine();

    }
}
