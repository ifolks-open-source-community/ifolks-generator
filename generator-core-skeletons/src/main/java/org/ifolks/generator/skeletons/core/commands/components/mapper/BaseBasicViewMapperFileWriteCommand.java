package org.ifolks.generator.skeletons.core.commands.components.mapper;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.ifolks.generator.model.domain.business.Bean;
import org.ifolks.generator.model.domain.business.Enumeration;
import org.ifolks.generator.model.domain.business.Property;
import org.ifolks.generator.model.domain.ui.ViewProperty;
import org.ifolks.generator.model.metadata.DataType;
import org.ifolks.generator.skeletons.commands.impl.typed.JavaFileWriteCommand;

public class BaseBasicViewMapperFileWriteCommand extends JavaFileWriteCommand {

	private Bean bean;

	/*
	 * constructor
	 */
	public BaseBasicViewMapperFileWriteCommand(Bean bean) {
		super(bean.myPackage.model.project.workspaceFolder + File.separator
				+ bean.myPackage.model.componentsArtefactName + File.separator + bean.myPackage.model.javaSourcesFolder
				+ File.separator
				+ bean.myPackage.baseBasicViewMapperPackageName.replace(".", File.separator),
				bean.basicViewBean.baseMapperClassName);

		this.bean = bean;
	}

		@Override
	public boolean supportsSpecificCode() {
		return false;
	}

	@Override
	protected void fetchSpecificImports() {
		javaImports.add("import java.time.OffsetDateTime;");
		javaImports.add("import java.time.LocalDate;");
		javaImports.add("import java.math.BigDecimal;");
		javaImports.add("import org.springframework.beans.factory.annotation.Autowired;");
		javaImports.add("import " + this.bean.myPackage.omPackageName + "." + this.bean.className + ";");
		javaImports.add(
				"import " + this.bean.myPackage.basicViewsPackageName + "." + this.bean.basicViewBean.recordName + ";");
		if (this.bean.isComponent) {
			javaImports.add("import " + this.bean.parentBean.myPackage.rightsManagerImplPackageName + "."
					+ this.bean.parentBean.rightsManagerClassName + ";");
			javaImports.add("import " + this.bean.parentBean.myPackage.stateManagerImplPackageName + "."
					+ this.bean.parentBean.stateManagerClassName + ";");
		} else {
			javaImports.add("import " + this.bean.myPackage.rightsManagerImplPackageName + "."
					+ this.bean.rightsManagerClassName + ";");
			javaImports.add("import " + this.bean.myPackage.stateManagerImplPackageName + "."
					+ this.bean.stateManagerClassName + ";");
		}
		for (Property property : this.bean.properties) {
			if (property.dataType != null && property.dataType.equals(DataType.ENUM) && property.javaType != null) {
				Enumeration enumeration = this.bean.myPackage.model.findEnumeration(property.javaType);
				if (enumeration != null) {
					javaImports.add("import " + enumeration.myPackage.enumsPackageName + "." + enumeration.className + ";");
				}
			}
		}
		if (this.bean.basicViewBean != null && this.bean.basicViewBean.properties != null) {
			for (ViewProperty property : this.bean.basicViewBean.properties) {
				if (property.dataType != null && property.dataType.equals(DataType.ENUM) && property.javaType != null) {
					Enumeration enumeration = this.bean.myPackage.model.findEnumeration(property.javaType);
					if (enumeration != null) {
						javaImports.add("import " + enumeration.myPackage.enumsPackageName + "." + enumeration.className + ";");
					}
				}
			}
		}
	}

	@Override
	protected void writeContent() throws IOException {

		writeLine("package " + this.bean.myPackage.baseBasicViewMapperPackageName + ";");
		skipLine();

		writeImports();
		skipLine();

		writeLine("/**");
		writeLine(" * auto generated base basic view mapper class file");
		writeLine(" * <br/>no modification should be done to this file");
		writeLine(" * <br/>processed by ifolks-generator");
		writeLine(" */");
		write("public class " + this.bean.basicViewBean.baseMapperClassName + " {");
		skipLine();

		writeLine("/*");
		writeLine(" * injections");
		writeLine(" */");

		if (this.bean.isComponent) {
			writeLine("@Autowired");
			writeLine("protected " + this.bean.parentBean.rightsManagerClassName + " "
					+ this.bean.parentBean.rightsManagerObjectName + ";");

			writeLine("@Autowired");
			writeLine("protected " + this.bean.parentBean.stateManagerClassName + " "
					+ this.bean.parentBean.stateManagerObjectName + ";");
		} else {
			writeLine("@Autowired");
			writeLine("protected " + this.bean.rightsManagerClassName + " " + this.bean.rightsManagerObjectName + ";");

			writeLine("@Autowired");
			writeLine("protected " + this.bean.stateManagerClassName + " " + this.bean.stateManagerObjectName + ";");
		}

		skipLine();

		createToView();

		writeLine("}");

	}

	private void createToView() {
		writeLine("/**");
		writeLine(" * mapping entity to view");
		writeLine(" */");
		writeLine("public " + this.bean.basicViewBean.recordName + " toView(" + this.bean.className + " "
				+ this.bean.objectName + ") {");
		skipLine();

		writeLine(this.bean.idType + " id = " + bean.objectName + ".getId();");
		writeLine("boolean selected = false;");
		if (this.bean.isComponent) {
			writeLine("boolean canDelete = "
					+ this.bean.parentBean.rightsManagerObjectName + ".canDelete" + bean.className + "("
					+ this.bean.objectName + ")"
					+ " && "
					+ this.bean.parentBean.stateManagerObjectName + ".canDelete" + bean.className + "("
					+ this.bean.objectName + ");");
		} else {
			writeLine("boolean canDelete = "
					+ this.bean.rightsManagerObjectName + ".canDelete(" + this.bean.objectName + ")"
					+ " && "
					+ this.bean.stateManagerObjectName + ".canDelete(" + this.bean.objectName + ");");
		}

		for (ViewProperty property : this.bean.basicViewBean.properties) {
			writeLine(property.javaType + " " + property.name + " = "
					+ buildSafeGetterExpression(this.bean.objectName, property.mappingPath) + ";");
		}

		skipLine();
		writeLine("return new " + this.bean.basicViewBean.recordName + " (");
		writeLine("id,");
		writeLine("selected,");
		write("canDelete");

		for (ViewProperty property : this.bean.basicViewBean.properties) {
			writeLine(",");
			write(property.name);
		}

		writeLine(");");

		writeLine("}");
		skipLine();
	}
}