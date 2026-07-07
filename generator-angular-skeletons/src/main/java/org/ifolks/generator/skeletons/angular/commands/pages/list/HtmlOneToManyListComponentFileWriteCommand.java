package org.ifolks.generator.skeletons.angular.commands.pages.list;

import java.io.File;
import java.io.IOException;

import org.ifolks.generator.model.domain.business.Bean;
import org.ifolks.generator.model.domain.business.OneToMany;
import org.ifolks.generator.model.domain.ui.FilterProperty;
import org.ifolks.generator.model.domain.ui.ViewProperty;
import org.ifolks.generator.model.metadata.DetailMode;
import org.ifolks.generator.skeletons.angular.commands.pages.AngularHtmlFileWriteCommand;


public class HtmlOneToManyListComponentFileWriteCommand extends AngularHtmlFileWriteCommand {

	private OneToMany oneToMany;
	private Bean referenceBean;
	private Bean parentBean;
	
	/*
	 * constructor
	 */
	public HtmlOneToManyListComponentFileWriteCommand(OneToMany oneToMany) {
        
		super(oneToMany.parentBean.myPackage.model.project.workspaceFolder + File.separator + oneToMany.parentBean.myPackage.model.tsUiArtefactName + File.separator + oneToMany.parentBean.myPackage.tsComponentsPath + File.separator + oneToMany.parentBean.urlPiece + File.separator + oneToMany.referenceBean.urlPiece + File.separator + "list", oneToMany.referenceBean.urlPiece + "-list.component");
		
		this.oneToMany = oneToMany;
		this.referenceBean = oneToMany.referenceBean;
		this.parentBean = oneToMany.parentBean;
		
	}
	
	
	
	@Override
	protected void writeContent() throws IOException {

        
		writeLine("<!-- -->");
		writeLine("<!-- auto generated one to many list component html file -->");
		writeLine("<!-- write modifications between specific code marks -->");
		writeLine("<!-- processed by skeleton generator -->");
		writeLine("<!-- -->");
		skipLine();
		
		writeLine("<app-private-template>");
		writeLine("<app-" + parentBean.urlPiece + "-menu  #menu [activePath]=\"activePath\" [id]=\"id\"></app-" + parentBean.urlPiece + "-menu>");
		skipLine();
		
		writeLine("<h2>");
		writeLine("{{ '" + referenceBean.objectName + "List' | i18n }}: {{scrollView.count}} / {{scrollView.size}}");
		writeLine("</h2>");
		skipLine();
		
		writeLine("<form [formGroup]=\"filter\">");
		for (FilterProperty property : oneToMany.basicViewBean.filter.properties) {
			writeFilter(property, referenceBean);
		}
		writeLine("</form>");
		
		writeLine("<mat-table [dataSource]=\"dataSource\" matSort>");

		for (ViewProperty property:oneToMany.basicViewBean.properties) {
			writeListComponent(property, referenceBean);
		}
		
		writeLine("<ng-container matColumnDef=\"Actions\">");
		writeLine("<mat-header-cell *matHeaderCellDef class=\"table-header\">{{ 'actions' | i18n }}</mat-header-cell>");
		writeLine("<mat-cell *matCellDef=\"let element\">");
		
		if (referenceBean.detailMode.equals(DetailMode.PAGE)) {
			writeLine("<a class=\"margin-10\" href=\"{{'/" + referenceBean.urlPiece + "/' + element.id}}\"><mat-icon aria-label=\"Edit\" svgIcon=\"table-edit\" class=\"text-success\"></mat-icon></a>");
		}
		writeLine("<a class=\"margin-10\" (click)=\"edit(element.id)\"><mat-icon aria-label=\"Edit\" svgIcon=\"pencil\" class=\"text-success\"></mat-icon></a>");
		writeLine("@if (element.canDelete) {");
		writeLine("  <a class=\"margin-10\" (click)=\"delete(element.id)\"><mat-icon aria-label=\"Delete\" svgIcon=\"delete\" class=\"text-warn\"></mat-icon></a>");
		writeLine("}");

		writeLine("</mat-cell>");
		writeLine("</ng-container>");

		writeLine("<mat-header-row *matHeaderRowDef=\"displayedColumns\"></mat-header-row>");
		writeLine("<mat-row *matRowDef=\"let row; columns: displayedColumns;\"></mat-row>");
		writeLine("</mat-table>");
		
		
		writeLine("<mat-paginator #paginator [length]=\"scrollView.count\"");
		writeLine("[pageSize]=\"scrollForm.elementsPerPage\"");
		writeLine("[showFirstLastButtons]=\"true\"");
		writeLine("[pageSizeOptions]=\"pageSizeOptions\">");
		writeLine("</mat-paginator>");
		skipLine();
		
		if (referenceBean.createEnabled) {
			writeLine("<button mat-raised-button (click)=\"create()\" color=\"primary\">");
			writeLine("{{ 'create' | i18n }}");
			writeLine("</button>");
		}

        writeNotOverridableContent();
        writeLine("</app-private-template>");
    }
}
