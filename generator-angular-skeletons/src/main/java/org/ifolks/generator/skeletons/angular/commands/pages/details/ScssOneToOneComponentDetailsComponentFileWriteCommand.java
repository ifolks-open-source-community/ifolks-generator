package org.ifolks.generator.skeletons.angular.commands.pages.details;

import java.io.File;
import java.io.IOException;

import org.ifolks.generator.model.domain.business.OneToOneComponent;
import org.ifolks.generator.skeletons.commands.impl.typed.ScssFileWriteCommand;


public class ScssOneToOneComponentDetailsComponentFileWriteCommand extends ScssFileWriteCommand {

	private OneToOneComponent oneToOneComponent;
	
	/*
	 * constructor
	 */
	public ScssOneToOneComponentDetailsComponentFileWriteCommand(OneToOneComponent oneToOneComponent) {
        
		super(oneToOneComponent.parentBean.myPackage.model.project.workspaceFolder + File.separator + oneToOneComponent.parentBean.myPackage.model.tsUiArtefactName + File.separator + oneToOneComponent.parentBean.myPackage.tsComponentsPath + File.separator + oneToOneComponent.parentBean.urlPiece + File.separator + oneToOneComponent.referenceBean.urlPiece + File.separator + "details", oneToOneComponent.referenceBean.urlPiece + "-details.component");
		
		this.oneToOneComponent = oneToOneComponent;
		
	}
	
	
	
	@Override
	protected void writeContent() throws IOException {

        writeLine("/**");
        writeLine(" * auto generated one to one component details component scss file");
        writeLine(" * <br/>write modifications between specific code marks");
        writeLine(" * <br/>processed by ifolks-generator");
        writeLine(" */");
        
        writeNotOverridableContent();
        
	}
}
