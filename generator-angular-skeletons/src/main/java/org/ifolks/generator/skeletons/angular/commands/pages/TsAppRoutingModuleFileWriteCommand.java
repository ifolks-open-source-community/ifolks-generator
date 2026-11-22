package org.ifolks.generator.skeletons.angular.commands.pages;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
		imports.add("import { IndexComponent } from './features/components/index/index.component';");
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
        
        List<Bean> routedBeans = new ArrayList<>();
        for (Package myPackage : project.model.packages) {
			for (Bean bean : myPackage.beans) {
				if (!bean.isComponent) {
					routedBeans.add(bean);
				}
			}
        }
        
        /*
         * Each generated route ends with a trailing comma, so that formatters such as Prettier
         * and the next generation agree on the same layout (no comma moved across the specific code mark).
         * Specific code blocks written with the legacy leading-comma convention are still supported:
         * in that case, the last generated route is written without trailing comma to avoid an array hole.
         */
        boolean legacyLeadingComma = getNotOverridableContent().trim().startsWith(",");
        
        writeLine("export const routes: Routes = [");
        for (int i = 0; i < routedBeans.size(); i++) {
        	Bean bean = routedBeans.get(i);
        	boolean last = i == routedBeans.size() - 1;
        	writeLine("{path:'" + bean.urlPiece + "', loadChildren:()=>import('" + bean.myPackage.tsComponentsSourcePath + "/" + bean.urlPiece + "/" + bean.urlPiece + ".routes').then(m=>m.routes), canActivate: [AuthGuard] }" + (last && legacyLeadingComma ? "" : ","));
        }
        writeNotOverridableContent();
        writeLine("];");

	}
}
