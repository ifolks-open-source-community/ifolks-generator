package org.ifolks.generator.skeletons.core.commands.components.mapper;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.ifolks.generator.model.domain.business.Bean;
import org.ifolks.generator.model.domain.business.Property;
import org.ifolks.generator.model.domain.ui.ViewProperty;
import org.ifolks.generator.skeletons.commands.impl.typed.JavaFileWriteCommand;

public class BaseFormMapperFileWriteCommand extends JavaFileWriteCommand {

	private Bean bean;
	private Set<String> daoSet;

	/*
	 * constructor
	 */
	public BaseFormMapperFileWriteCommand(Bean bean) {
		super(bean.myPackage.model.project.workspaceFolder + File.separator + bean.myPackage.model.componentsArtefactName + File.separator + bean.myPackage.model.javaSourcesFolder + File.separator
				+ bean.myPackage.baseFormMapperPackageName.replace(".", File.separator), bean.formBean.baseMapperClassName);

		this.bean = bean;
	}

	@Override
	protected void fetchSpecificImports() {

		this.daoSet = new HashSet<>();

		javaImports.add("import java.time.OffsetDateTime;");
		javaImports.add("import java.time.LocalDate;");
		javaImports.add("import java.math.BigDecimal;");
		javaImports.add("import org.springframework.beans.factory.annotation.Autowired;");
		javaImports.add("import org.springframework.stereotype.Component;");
		javaImports.add("import org.ifolks.commons.api.exception.repository.ObjectNotFoundException;");
		javaImports.add("import " + this.bean.myPackage.omPackageName + "." + this.bean.className + ";");
		javaImports.add("import " + this.bean.myPackage.formsPackageName + "." + this.bean.formBean.className + ";");

		for (Property property : this.bean.properties) {
			if (property.referenceBean != null && property.visibility.isDetailVisible() && !property.relation.isComponentLink()) {
				if (!property.embedded) {
					boolean test = this.daoSet.add(property.referenceBean.daoObjectName);
					if (test) {
						javaImports.add("import " + property.referenceBean.myPackage.DAOInterfacePackageName + "." + property.referenceBean.daoInterfaceName + ";");
					}
				} else {
					javaImports.add("import " + property.referenceBean.myPackage.omPackageName + "." + property.referenceBean.className + ";");
					
					for (Property embeddedProperty:property.referenceBean.properties) {
						if (embeddedProperty.referenceBean != null && embeddedProperty.visibility.isDetailVisible()) {
							boolean test = this.daoSet.add(embeddedProperty.referenceBean.daoObjectName);
							if (test) {
								javaImports.add("import " + embeddedProperty.referenceBean.myPackage.DAOInterfacePackageName + "." + embeddedProperty.referenceBean.daoInterfaceName + ";");
							}
						}
					}
				}
			}
		}
	}
		

	@Override
	protected void writeContent() throws IOException {

		writeLine("package " + this.bean.myPackage.baseFormMapperPackageName + ";");
		skipLine();
		
		writeImports();
		skipLine();
		
		writeLine("/**");
		writeLine(" * auto generated base mapper class file");
		writeLine(" * <br/>no modification should be done to this file");
		writeLine(" * <br/>processed by ifolks-generator");
		writeLine(" */");
		writeLine("@Component");
		writeLine("public class " + this.bean.formBean.baseMapperClassName + " {");
		skipLine();
		skipLine();

		writeLine("/*");
		writeLine(" * properties");
		writeLine(" */");
		
		this.daoSet = new HashSet<>();

		for (Property property : this.bean.properties) {
			if (property.referenceBean != null && property.visibility.isDetailVisible() && !property.relation.isComponentLink()) {
				if (!property.embedded) {
					boolean test = this.daoSet.add(property.referenceBean.daoObjectName);
					if (test) {
						writeLine("@Autowired");
						writeLine("protected " + property.referenceBean.daoInterfaceName + " " + property.referenceBean.daoObjectName + ";");
					}
				} else {
					for (Property embeddedProperty:property.referenceBean.properties) {
						if (embeddedProperty.referenceBean != null && embeddedProperty.visibility.isDetailVisible()) {
							boolean test = this.daoSet.add(embeddedProperty.referenceBean.daoObjectName);
							if (test) {
								writeLine("@Autowired");
								writeLine("protected " + embeddedProperty.referenceBean.daoInterfaceName + " " + embeddedProperty.referenceBean.daoObjectName + ";");
							}
						}
					}
				}
			}
		}

		skipLine();

		createObjectArryToForm();
		createToForm();
		createToEntity();

		writeLine("}");

	}

	private void createObjectArryToForm() {
		writeLine("/**");
		writeLine(" * mapping object arry to form");
		writeLine(" */");
		writeLine("public " + this.bean.formBean.className + " toForm(Object[] args) {");

        skipLine();
		writeLine("return new " + this.bean.formBean.className + " (");
        
        boolean first = true;
        int i = 0;
        for (ViewProperty property:this.bean.formBean.properties) {
        	if (first) {
        		first = false;
        	} else {
        		writeLine(",");
        	}
        	write("(" + property.javaType + ")args[" + i + "]" );
        	i++;
        }
        writeLine(");");

		writeLine("}");
		skipLine();
		
		
	}

	private void createToForm() {
		writeLine("/**");
		writeLine(" * mapping entity to form");
		writeLine(" */");
		writeLine("public " + this.bean.formBean.className + " toForm(" + this.bean.className + " " + this.bean.objectName + ") {");
		
		for (Property property : this.bean.properties) {			
			if (property.visibility.isDetailVisible() && !property.relation.isComponentLink()) {
				if (property.referenceBean != null) {
                    if (property.embedded) {
                        writeMapEmbeddedToView(property);
                    } else {
                        writeMapReferenceToView(property);
                    }					
                } else {
                    writeLine(property.javaType + " " + property.name + " = " + this.bean.objectName + "." + property.getterName + "();");
                }
			}
		}

        skipLine();
		writeLine("return new " + this.bean.formBean.className + " (");
        
        boolean first = true;
        for (ViewProperty property:this.bean.formBean.properties) {
        	if (first) {
        		first = false;
        	} else {
        		writeLine(",");
        	}
        	write(property.name);
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
			if (embeddedProperty.visibility.isDetailVisible()) {
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
	
	private void createToEntity() {

		writeLine("/**");
		writeLine(" * mapping form to entity");
		writeLine(" */");
		writeLine("public " + this.bean.className + " toEntity(" + this.bean.formBean.className + " " + this.bean.formBean.objectName + ", " + this.bean.className + " "
				+ this.bean.objectName + ") {");

		for (Property property : this.bean.properties) {
			if (property.visibility.isDetailVisible() && !property.relation.isComponentLink()) {
                if (property.referenceBean == null) {
                    writeLine(this.bean.objectName + "." + property.setterName + "(" + this.bean.formBean.objectName + "." + property.name + "());");
                } else if (property.embedded) {
					writeMapEmbeddedToObject(property);
				} else {
					writeMapReferenceToObject(property);
				}
			}
		}

		writeLine("return " + this.bean.objectName + ";");
		writeLine("}");
		skipLine();
	}

	

	private void writeMapReferenceToObject(Property property) {
		if (property.nullable) {
			write("if (");
			boolean start = true;
			for (ViewProperty refProperty : property.referenceBean.referenceViewProperties) {
				if (start) start = false; else write(" && ");
				write(this.bean.formBean.objectName + "." + property.name + refProperty.capName + "() == null");
			}
			writeLine(") {");
			writeLine(this.bean.objectName + "." + property.setterName + "(null);");
			writeLine("} else {");
		}

		boolean start = true;
		write(this.bean.objectName + "." + property.setterName + "(" + property.referenceBean.daoObjectName + ".find(");
		for (ViewProperty refProperty:property.referenceBean.referenceViewProperties) {
			if (start) start = false; else write(", ");
			write(this.bean.formBean.objectName + "." + property.name + refProperty.capName + "()");
		}
		writeLine(").orElseThrow(() -> new ObjectNotFoundException(\"" + property.referenceBean.className + ".notFound\")));");		

		if (property.nullable) {
			writeLine("}");
		}
	}

	private void writeMapEmbeddedToObject(Property property) {
		
		Bean embeddedBean = property.referenceBean;
		
		writeLine(embeddedBean.className + " " + embeddedBean.objectName + " = " + bean.objectName + "." + property.getterName + "();");
		writeLine("if (" + embeddedBean.objectName + " == null) {");
		writeLine(embeddedBean.objectName + " = new " + embeddedBean.className + "();");
		writeLine(bean.objectName + "." + property.setterName + "(" + embeddedBean.objectName + ");");
		writeLine("}");
		
		for (Property embeddedProperty : embeddedBean.properties) {
			if (embeddedProperty.visibility.isDetailVisible()) {
				if (embeddedProperty.referenceBean != null) {
					
					List<ViewProperty> referencePropertyList = embeddedProperty.referenceBean.referenceViewProperties;
					if (embeddedProperty.nullable) {
						write("if (");
						boolean start = true;
						for (ViewProperty refProperty : referencePropertyList) {
							if (start) start = false; else write(" && ");
							write(this.bean.formBean.objectName + "." + embeddedProperty.name + refProperty.capName + "() == null");
						}
						writeLine(") {");
						writeLine(embeddedBean.objectName + "." + embeddedProperty.setterName + "(null);");
						writeLine("} else {");
					}

					write(embeddedBean.objectName + "." + embeddedProperty.setterName + "(" + embeddedProperty.referenceBean.daoObjectName + ".find(");
					boolean start = true;
					for (ViewProperty refProperty : referencePropertyList) {
						if (start) start = false; else write(", ");
						write(this.bean.formBean.objectName + "." + embeddedProperty.name + refProperty.capName + "()");
					}
					writeLine(").orElseThrow(() -> new ObjectNotFoundException(\"" + embeddedProperty.referenceBean.className + ".notFound\")));");
					
					if (embeddedProperty.nullable) {
						writeLine("}");
					}
					
				} else {
					writeLine(embeddedBean.objectName + "." + embeddedProperty.setterName + "(" + bean.formBean.objectName + "." + embeddedProperty.name + "());");
				}
			}
		}
	}
}
