package org.ifolks.generator.skeletons.core.commands.components.mapper;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.ifolks.generator.model.domain.business.Bean;
import org.ifolks.generator.model.domain.business.Property;
import org.ifolks.generator.model.domain.ui.ViewProperty;
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

		for (Property property : this.bean.properties) {
			if (property.visibility.isListVisible() && !property.relation.isComponentLink()) {
				if (property.referenceBean != null) {
					if (property.embedded) {
						writeMapEmbeddedToView(property);
					} else {
						writeMapReferenceToView(property);
					}
				} else {
					writeLine(property.javaType + " " + property.name + " = " + this.bean.objectName + "."
							+ property.getterName + "();");
				}
			}
		}

		skipLine();
		writeLine("return new " + this.bean.basicViewBean.recordName + " (");
		writeLine("id,");
		writeLine("selected,");
		write("canDelete");

		for (Property property : this.bean.properties) {
			if (property.visibility.isListVisible() && !property.relation.isComponentLink()) {
				if (property.referenceBean != null) {
					if (property.embedded) {

						Bean embeddedBean = property.referenceBean;

						for (Property embeddedProperty : embeddedBean.properties) {
							if (embeddedProperty.visibility.isListVisible()) {
								if (embeddedProperty.referenceBean != null) {
									List<ViewProperty> referencePropertyList = embeddedProperty.referenceBean.referenceViewProperties;
									for (ViewProperty viewProperty : referencePropertyList) {
										writeLine(",");
										write(embeddedProperty.name + viewProperty.capName);
									}
								} else {
									writeLine(",");
									write(embeddedProperty.name);
								}
							}
						}

					} else {
						List<ViewProperty> referencePropertyList = property.referenceBean.referenceViewProperties;
						for (ViewProperty viewProperty : referencePropertyList) {
							writeLine(",");
							write(property.name + viewProperty.capName);
						}
					}
				} else {
					writeLine(",");
					write(property.name);
				}
			}
		}

		writeLine(");");

		writeLine("}");
		skipLine();
	}

	private void writeMapReferenceToView(Property property) {

		List<ViewProperty> referencePropertyList = property.referenceBean.referenceViewProperties;
		if (property.nullable) {
			for (ViewProperty viewProperty : referencePropertyList) {
				writeLine(viewProperty.javaType + " " + property.name + viewProperty.capName + " = null;");
			}
			writeLine("if (" + this.bean.objectName + "." + property.getterName + "() != null) {");
			for (ViewProperty viewProperty : referencePropertyList) {
				writeLine(property.name + viewProperty.capName + " = " + this.bean.objectName + "."
						+ property.getterName + "()." + viewProperty.mappingPath + ";");
			}
			writeLine("}");

		} else {
			for (ViewProperty viewProperty : referencePropertyList) {
				writeLine(viewProperty.javaType + " " + property.name + viewProperty.capName + " = "
						+ this.bean.objectName + "." + property.getterName + "()." + viewProperty.mappingPath + ";");
			}
		}
	}

	private void writeMapEmbeddedToView(Property property) {

		Bean embeddedBean = property.referenceBean;

		for (Property embeddedProperty : embeddedBean.properties) {
			if (embeddedProperty.visibility.isListVisible()) {
				if (embeddedProperty.referenceBean != null) {
					List<ViewProperty> referencePropertyList = embeddedProperty.referenceBean.referenceViewProperties;
					if (embeddedProperty.nullable) {
						for (ViewProperty viewProperty : referencePropertyList) {
							writeLine(viewProperty.javaType + " " + embeddedProperty.name + viewProperty.capName
									+ " = null;");
						}
						writeLine("if (" + this.bean.objectName + "." + property.getterName + "()."
								+ embeddedProperty.getterName + "() != null) {");
						for (ViewProperty viewProperty : referencePropertyList) {
							writeLine(embeddedProperty.name + viewProperty.capName + " = " + this.bean.objectName + "."
									+ property.getterName + "()." + embeddedProperty.getterName + "()."
									+ viewProperty.mappingPath + ";");
						}
						writeLine("}");

					} else {
						for (ViewProperty viewProperty : referencePropertyList) {
							writeLine(viewProperty.javaType + " " + embeddedProperty.name + viewProperty.capName + " = "
									+ this.bean.objectName + "." + property.getterName + "()."
									+ embeddedProperty.getterName + "()." + viewProperty.mappingPath + ";");
						}
					}
				} else {
					writeLine(embeddedProperty.javaType + " " + embeddedProperty.name + " = " + this.bean.objectName
							+ "." + property.getterName + "()." + embeddedProperty.getterName + "();");
				}
			}
		}
	}
}
