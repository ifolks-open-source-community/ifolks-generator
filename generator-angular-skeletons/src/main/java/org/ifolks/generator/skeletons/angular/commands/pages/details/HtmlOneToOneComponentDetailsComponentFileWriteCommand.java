package org.ifolks.generator.skeletons.angular.commands.pages.details;

import java.io.File;
import java.io.IOException;

import org.ifolks.generator.model.domain.business.Bean;
import org.ifolks.generator.model.domain.business.OneToOneComponent;
import org.ifolks.generator.model.domain.ui.ViewProperty;
import org.ifolks.generator.skeletons.angular.commands.pages.AngularHtmlFileWriteCommand;


public class HtmlOneToOneComponentDetailsComponentFileWriteCommand extends AngularHtmlFileWriteCommand {

	private OneToOneComponent oneToOneComponent;
	private Bean parentBean;
	private Bean referenceBean;
	
	/*
	 * constructor
	 */
	public HtmlOneToOneComponentDetailsComponentFileWriteCommand(OneToOneComponent oneToOneComponent) {
        
		super(oneToOneComponent.parentBean.myPackage.model.project.workspaceFolder + File.separator + oneToOneComponent.parentBean.myPackage.model.tsUiArtefactName + File.separator + oneToOneComponent.parentBean.myPackage.tsComponentsPath + File.separator + oneToOneComponent.parentBean.urlPiece + File.separator + oneToOneComponent.referenceBean.urlPiece + File.separator + "details", oneToOneComponent.referenceBean.urlPiece + "-details.component");
		
		this.oneToOneComponent = oneToOneComponent;
		this.parentBean = oneToOneComponent.parentBean;
		this.referenceBean = oneToOneComponent.referenceBean;
		
	}
	
	
	
	@Override
	protected void writeContent() throws IOException {

        
		writeLine("<!-- -->");
		writeLine("<!-- auto generated one to one component details component html file -->");
		writeLine("<!-- write modifications between specific code marks -->");
		writeLine("<!-- processed by skeleton generator -->");
		writeLine("<!-- -->");
		skipLine();
		
		writeLine("<app-private-template>");
		writeLine("<app-" + parentBean.urlPiece + "-menu  #menu [activePath]=\"activePath\" [id]=\"id\"></app-" + parentBean.urlPiece + "-menu>");
		skipLine();
		
		writeLine("<h2>");
		writeLine("{{ '" + referenceBean.objectName + "Details' | i18n }}");
		writeLine("</h2>");
		skipLine();
		
		writeLine("<div class=\"details-form\" >");
		writeLine("<form [formGroup]=\"form\" (ngSubmit)=\"saveOrUpdate()\">");
		
		for (ViewProperty property:referenceBean.formBean.properties) {
			writeInput(property, referenceBean);
		}
		
		writeLine("<mat-dialog-actions>");
		if (referenceBean.createEnabled) {
			writeLine("@if (view.id == null) {");
			writeLine("  <button mat-raised-button color=\"primary\" type=\"submit\" [disabled]=\"form.invalid\">{{ 'save' | i18n }}</button>");
			writeLine("}");
		}
		if (referenceBean.updateEnabled ) {
			writeLine("@if (view.id != null && view.canUpdate) {");
			writeLine("  <button mat-raised-button color=\"primary\" type=\"submit\" [disabled]=\"form.invalid\">{{ 'update' | i18n }}</button>");
			writeLine("}");
		}
		if (referenceBean.deleteEnabled ) {
			writeLine("@if (view.id != null && view.canDelete) {");
			writeLine("  <button mat-raised-button color=\"warn\" type=\"button\" (click)=\"delete()\">{{ 'delete' | i18n }}</button>");
			writeLine("}");
		}
		writeLine("</mat-dialog-actions>");
		writeLine("</form>");
		
        writeNotOverridableContent();
        
        writeLine("</div>");
        writeLine("</app-private-template>");
    }
}
