package org.ifolks.generator.skeletons.angular.commands.pages;

import org.ifolks.generator.model.domain.business.Bean;
import org.ifolks.generator.model.domain.ui.FilterProperty;
import org.ifolks.generator.model.domain.ui.ViewProperty;
import org.ifolks.generator.model.metadata.SelectionMode;
import org.ifolks.generator.skeletons.commands.impl.typed.HtmlFileWriteCommand;

public abstract class AngularHtmlFileWriteCommand extends HtmlFileWriteCommand {

	public AngularHtmlFileWriteCommand(String folderName, String fileName) {
		super(folderName, fileName);
	}

	protected void writeListComponent(ViewProperty property, Bean bean) {
		writeLine("<ng-container matColumnDef=\"" + property.name + "\">");
		writeLine("<mat-header-cell *matHeaderCellDef mat-sort-header class=\"table-header\">{{ '" + bean.objectName + property.capName + "' | i18n }}</mat-header-cell>");
		
		switch (property.dataType) {
		case BOOLEAN:
			if (property.nullable) {
				writeLine("<mat-cell *matCellDef=\"let element\"> {{element." + property.name + "}} </mat-cell>");
			} else {
				writeLine("<mat-cell *matCellDef=\"let element\"> {{element." + property.name + "}} </mat-cell>");
			}
			break;
			
		case DATE:
			writeLine("<mat-cell *matCellDef=\"let element\"> {{element." + property.name + "}} </mat-cell>");

			break;

		case DATETIME:
			writeLine("<mat-cell *matCellDef=\"let element\"> {{element." + property.name + "| date:'yyyy-MM-ddTHH:mm:ssZ':'GMT'}} </mat-cell>");

			break;

		case DOUBLE:
			writeLine("<mat-cell *matCellDef=\"let element\"> {{element." + property.name + "}} </mat-cell>");
			break;
			
		case BIG_DECIMAL:
			writeLine("<mat-cell *matCellDef=\"let element\"> {{element." + property.name + "}} </mat-cell>");
			break;

		case SHORT:
		case INTEGER:
		case LONG:
			writeLine("<mat-cell *matCellDef=\"let element\"> {{element." + property.name + "}} </mat-cell>");
			break;

		case STRING:
			writeLine("<mat-cell *matCellDef=\"let element\"> {{element." + property.name + "}} </mat-cell>");
			break;

		case TEXT:
			writeLine("<mat-cell *matCellDef=\"let element\"> {{element." + property.name + "}} </mat-cell>");
			break;
		}
		
		writeLine("</ng-container>");
	}
	
	
	protected void writeInput(ViewProperty property, Bean bean){
		
		if (property.selectableBean != null) {
			if (property.selectableBean.selectionBehavior.selectionMode.equals(SelectionMode.DROPDOWN_OPTIONS)) {
				writeCombobox(property, bean);
			} else {
				writeAutocomplete(property, bean);
			}
		} else {
		
			switch (property.dataType) {
				case BOOLEAN:
					writeBooleanInput(property, bean);
					break;
				case DATE:
					writeDateInput(property, bean);
					break;
				case DATETIME:
					writeDateTimeInput(property, bean);
					break;
				case DOUBLE:
					writeBigDecimalInput(property, bean);
					break;
				case BIG_DECIMAL:
					writeBigDecimalInput(property, bean);
					break;
				case SHORT:
				case INTEGER:
				case LONG:
					writeLongInput(property, bean);
					break;
				case STRING:
					writeStringInput(property, bean);
					break;
				case TEXT:
					writeTextInput(property, bean);
					break;
			}
		}
        skipLine();
	}
	
	private void writeCombobox(ViewProperty property, Bean bean){
		
		writeLine("<p>");
		writeLine("<mat-form-field appearance=\"outline\" floatLabel=\"always\">");
		writeLine("<mat-label>{{ '" + bean.objectName + property.capName + "' | i18n }}</mat-label>");
		if (!property.editable) {
			writeLine("<input matInput placeholder=\"{{ '" + bean.objectName + property.capName + "' | i18n }}\" formControlName=\"" + property.name + "\"/>");
		} else {
			writeLine("<mat-select placeholder=\"{{ '" + bean.objectName + property.capName + "' | i18n }}\" formControlName=\"" + property.name + "\" >");
			writeLine("<mat-option [value]=\"null\"></mat-option>");
			writeLine("@for (option of " + property.name + "Options; track option.key) {");
			writeLine("  <mat-option [value]=\"option.key\">{{option.label}}</mat-option>");
			writeLine("}");
			writeLine("</mat-select>");
		}
		writeLine("</mat-form-field>");
		writeLine("</p>");
	}
	
	private void writeAutocomplete(ViewProperty property, Bean bean){
		
		writeLine("<p>");
		writeLine("<mat-form-field appearance=\"outline\" floatLabel=\"always\" class=\"autocomplete\">");
		writeLine("<mat-label>{{ '" + bean.objectName + property.capName + "' | i18n }}</mat-label>");
		if (!property.editable) {
			writeLine("<input matInput placeholder=\"{{ '" + bean.objectName + property.capName + "' | i18n }}\" formControlName=\"" + property.name + "\"/>");
		} else {
			writeLine("<input matInput placeholder=\"{{ '" + bean.objectName + property.capName + "' | i18n }}\" formControlName=\"" + property.name + "\" [matAutocomplete]=\"" + property.name + "AutoComplete\"/>");
			writeLine("<mat-autocomplete #" + property.name + "AutoComplete=\"matAutocomplete\">");
			writeLine("@for (option of " + property.name + "Options | async; track option.key) {");
			writeLine("  <mat-option [value]=\"option.key\">{{option.label}}</mat-option>");
			writeLine("}");
			writeLine("</mat-autocomplete>");
		}
		writeLine("</mat-form-field>");
		writeLine("</p>");
	}
	
	
	private void writeStringInput(ViewProperty property, Bean bean){
		writeLine("<p>");
		writeLine("<mat-form-field appearance=\"outline\" floatLabel=\"always\">");
		writeLine("<mat-label>{{ '" + bean.objectName + property.capName + "' | i18n }}</mat-label>");
		writeLine("<input matInput placeholder=\"{{ '" + bean.objectName + property.capName + "' | i18n }}\" formControlName=\"" + property.name + "\"/>");
		writeLine("</mat-form-field>");
		writeLine("</p>");
	}
	
	private void writeTextInput(ViewProperty property, Bean bean){
		writeLine("<p>");
		writeLine("<mat-form-field appearance=\"outline\" floatLabel=\"always\">");
		writeLine("<mat-label>{{ '" + bean.objectName + property.capName + "' | i18n }}</mat-label>");
		writeLine("<textarea matInput placeholder=\"{{ '" + bean.objectName + property.capName + "' | i18n }}\" formControlName=\"" + property.name + "\" cdkTextareaAutosize></textarea>");
		writeLine("</mat-form-field>");
		writeLine("</p>");
	}
	
	private void writeBooleanInput(ViewProperty property, Bean bean){
		if (!property.nullable) {
			writeLine("<p>");
			writeLine("<mat-checkbox color=\"primary\" formControlName=\"" +  property.name + "\">");
			writeLine("{{ '" + bean.objectName + property.capName + "' | i18n }}");
			writeLine("</mat-checkbox>");
			writeLine("</p>");
		} else {
			writeLine("<p>");
			writeLine("<mat-form-field appearance=\"outline\" floatLabel=\"always\">");
			writeLine("<mat-label>{{ '" + bean.objectName + property.capName + "' | i18n }}</mat-label>");
			if (!property.editable) {
				writeLine("<input matInput placeholder=\"{{ '" + bean.objectName + property.capName + "' | i18n }}\" formControlName=\"" + property.name + "\"/>");
			} else {
				writeLine("<mat-select placeholder=\"{{ '" + bean.objectName + property.capName + "' | i18n }}\" formControlName=\"" + property.name + "\">");
				writeLine("<mat-option [value]=\"\"></mat-option>");
				writeLine("<mat-option [value]=\"true\">{{ 'trueLabel' | i18n }}</mat-option>");
				writeLine("<mat-option [value]=\"false\">{{ 'falseLabel' | i18n }}</mat-option>");
				writeLine("</mat-select>");
			}
			writeLine("</mat-form-field>");
			writeLine("<p>");
		}
	}
	
	private void writeBigDecimalInput(ViewProperty property, Bean bean){
		writeLine("<p>");
		writeLine("<mat-form-field appearance=\"outline\" floatLabel=\"always\">");
		writeLine("<mat-label>{{ '" + bean.objectName + property.capName + "' | i18n }}</mat-label>");
		writeLine("<input matInput type=\"decimal\" placeholder=\"{{ '" + bean.objectName + property.capName + "' | i18n }}\" formControlName=\"" + property.name + "\"/>");
		writeLine("</mat-form-field>");
		writeLine("</p>");
	}
	
	private void writeLongInput(ViewProperty property, Bean bean){
		writeLine("<p>");
		writeLine("<mat-form-field appearance=\"outline\" floatLabel=\"always\">");
		writeLine("<mat-label>{{ '" + bean.objectName + property.capName + "' | i18n }}</mat-label>");
		writeLine("<input type=\"number\" matInput placeholder=\"{{ '" + bean.objectName + property.capName + "' | i18n }}\" formControlName=\"" + property.name + "\"/>");
		writeLine("</mat-form-field>");
		writeLine("</p>");
	}
	
	private void writeDateInput(ViewProperty property, Bean bean){
		writeLine("<p>");
		writeLine("<mat-form-field appearance=\"outline\" floatLabel=\"always\">");
		writeLine("<mat-label>{{ '" + bean.objectName + property.capName + "' | i18n }}</mat-label>");
		if (!property.editable) {
			writeLine("<input matInput placeholder=\"yyyy-MM-dd\" formControlName=\"" + property.name + "\"/>");
		} else {
			writeLine("<input matInput [matDatepicker]=\"" + property.name + "DatePicker\" placeholder=\"yyyy-MM-dd\" formControlName=\"" + property.name + "\"/>");
			writeLine("<mat-datepicker-toggle matSuffix [for]=\"" + property.name + "DatePicker\"></mat-datepicker-toggle>");
			writeLine("<mat-datepicker #" + property.name + "DatePicker></mat-datepicker>");
		}
		writeLine("</mat-form-field>");
		writeLine("</p>");
	}
	
	private void writeDateTimeInput(ViewProperty property, Bean bean){
		writeLine("<p>");
		writeLine("<mat-form-field appearance=\"outline\" floatLabel=\"always\">");
		writeLine("<mat-label>{{ '" + bean.objectName + property.capName + "' | i18n }}</mat-label>");
		writeLine("<input matInput placeholder=\"yyyy-MM-ddTHH:mm:ssZ\" formControlName=\"" + property.name + "\"/>");
		writeLine("</mat-form-field>");
		writeLine("</p>");
	}
	
	
	protected void writeFilter(FilterProperty property, Bean bean) {
		String capName = capitalize(property.name);
		switch (property.dataType) {
			case STRING:
			case TEXT:
				writeLine("<mat-form-field appearance=\"outline\" floatLabel=\"always\">");
				writeLine("<mat-label>{{ '" + bean.objectName + capName + "' | i18n }}</mat-label>");
				writeLine("<input matInput placeholder=\"{{ '" + bean.objectName + capName + "' | i18n }}\" formControlName=\"" + property.name + "\"/>");
				writeLine("</mat-form-field>");	
				break;
				
			case DATE:				
				writeLine("<mat-form-field appearance=\"outline\" floatLabel=\"always\">");
				writeLine("<mat-label>{{ '" + bean.objectName + capName + "' | i18n }}</mat-label>");
				writeLine("<input matInput [matDatepicker]=\"" + property.name + "DatePicker\" placeholder=\"yyyy-MM-dd\" formControlName=\"" + property.name + "\"/>");
				writeLine("<mat-datepicker-toggle matSuffix [for]=\"" + property.name + "DatePicker\"></mat-datepicker-toggle>");
				writeLine("<mat-datepicker #" + property.name + "DatePicker></mat-datepicker>");
				writeLine("</mat-form-field>");
				break;
				
			case DATETIME:				
				writeLine("<mat-form-field appearance=\"outline\" floatLabel=\"always\">");
				writeLine("<mat-label>{{ '" + bean.objectName + capName + "' | i18n }}</mat-label>");
				writeLine("<input matInput placeholder=\"yyyy-MM-ddTHH:mm:ssZ\" formControlName=\"" + property.name + "\"/>");
				writeLine("</mat-form-field>");
				break;
				
			case DOUBLE:
			case BIG_DECIMAL:
				writeLine("<mat-form-field appearance=\"outline\" floatLabel=\"always\">");
				writeLine("<mat-label>{{ '" + bean.objectName + capName + "' | i18n }}</mat-label>");
				writeLine("<input type=\"number\" matInput placeholder=\"{{ '" + bean.objectName + capName + "' | i18n }}\" formControlName=\"" + property.name + "\"/>");
				writeLine("</mat-form-field>");
				break;
			
			case SHORT:
			case INTEGER:
			case LONG:
				writeLine("<mat-form-field appearance=\"outline\" floatLabel=\"always\">");
				writeLine("<mat-label>{{ '" + bean.objectName + capName + "' | i18n }}</mat-label>");
				writeLine("<input type=\"number\" matInput placeholder=\"{{ '" + bean.objectName + capName + "' | i18n }}\" formControlName=\"" + property.name + "\"/>");
				writeLine("</mat-form-field>");
				break;
				
			case BOOLEAN:
				writeLine("<mat-form-field appearance=\"outline\" floatLabel=\"always\">");
				writeLine("<mat-label>{{ '" + bean.objectName + capName + "' | i18n }}</mat-label>");
				writeLine("<mat-select placeholder=\"{{ '" + bean.objectName + capName + "' | i18n }}\" formControlName=\"" + property.name + "\" >");
				writeLine("<mat-option [value]=\"\"></mat-option>");
				writeLine("<mat-option [value]=\"true\">{{ 'trueLabel' | i18n }}</mat-option>");
				writeLine("<mat-option [value]=\"false\">{{ 'falseLabel' | i18n }}</mat-option>");
				writeLine("</mat-select>");
				writeLine("</mat-form-field>");
				break;

		}
	}

	private String capitalize(String str) {
		if (str == null || str.isEmpty()) {
			return "";
		}
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}
}
