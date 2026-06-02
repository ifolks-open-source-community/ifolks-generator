package org.ifolks.generator.skeletons.angular.commands.pages;

import java.io.File;
import java.io.IOException;

import org.ifolks.generator.model.domain.Package;
import org.ifolks.generator.model.domain.Project;
import org.ifolks.generator.model.domain.business.Bean;
import org.ifolks.generator.skeletons.commands.impl.typed.TsFileWriteCommand;


public class TsAppRoutingModuleFileWriteCommand extends TsFileWriteCommand {

	private Project project;
	/*
	 * constructor
	 */
	public TsAppRoutingModuleFileWriteCommand(Project project) {
        
		super(project.workspaceFolder + File.separator + project.model.tsUiArtefactName + File.separator + "src" + File.separator + "app", "app.routes");
		this.project = project;
		
	}
	
	@Override
	protected void fetchSpecificImports() {
		imports.add("import { Routes } from '@angular/router';");
		imports.add("import { AuthGuard } from './core/services/AuthGuard';");
	}
	
	
	@Override
	protected void writeContent() throws IOException {

        writeImports();
        
        writeLine("/**");
        writeLine(" * auto generated app routes ts file");
        writeLine(" * <br/>write modifications between specific code marks");
        writeLine(" * <br/>processed by ifolks-generator");
        writeLine(" */");
        skipLine();
        
        writeLine("export const routes: Routes = [");
        boolean start = true;
        for (Package myPackage : project.model.packages) {
			for (Bean bean : myPackage.beans) {
				if (!bean.isComponent) {
					if (start) {
						start = false;
					} else {
						write(",");
					}
					writeLine("{path:'" + bean.urlPiece + "', loadChildren:()=>import('" + bean.myPackage.tsComponentsSourcePath + "/" + bean.urlPiece + "/" + bean.urlPiece + ".routes').then(m=>m.routes), canActivate: [AuthGuard] }");
				}
			}
        }
        writeNotOverridableContent();
        writeLine("];");

	}
}
