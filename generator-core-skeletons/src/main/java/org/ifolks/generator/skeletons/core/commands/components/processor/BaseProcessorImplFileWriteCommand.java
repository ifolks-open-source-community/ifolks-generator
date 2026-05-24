package org.ifolks.generator.skeletons.core.commands.components.processor;

import java.io.File;
import java.io.IOException;

import org.ifolks.generator.model.domain.business.Bean;
import org.ifolks.generator.model.domain.business.OneToManyComponent;
import org.ifolks.generator.model.domain.business.OneToOneComponent;
import org.ifolks.generator.skeletons.commands.impl.typed.JavaFileWriteCommand;

public class BaseProcessorImplFileWriteCommand extends JavaFileWriteCommand {

	private Bean bean;

	/*
	 * constructor
	 */
	public BaseProcessorImplFileWriteCommand(Bean bean) {
		super(bean.myPackage.model.project.workspaceFolder + File.separator + bean.myPackage.model.componentsArtefactName + File.separator + bean.myPackage.model.javaSourcesFolder + File.separator
				+ bean.myPackage.baseProcessorImplPackageName.replace(".", File.separator), bean.baseProcessorClassName);

		this.bean = bean;
	}

	@Override
	protected void fetchSpecificImports() {

		javaImports.add("import org.springframework.beans.factory.annotation.Autowired;");
		javaImports.add("import " + this.bean.myPackage.omPackageName + "." + this.bean.className + ";");
        javaImports.add("import " + this.bean.myPackage.DAOInterfacePackageName + "." + this.bean.daoInterfaceName + ";");   
        
        for (OneToOneComponent oneToOneComponent : this.bean.oneToOneComponentList)
        {
            Bean currentBean = oneToOneComponent.referenceBean;
            javaImports.add("import " + currentBean.myPackage.omPackageName + "." + currentBean.className + ";");
        }

        for (OneToManyComponent oneToManyComponent : this.bean.oneToManyComponentList)
        {
            Bean currentBean = oneToManyComponent.referenceBean;
            javaImports.add("import " + currentBean.myPackage.omPackageName + "." + currentBean.className + ";");
            javaImports.add("import " + currentBean.myPackage.DAOInterfacePackageName + "." + currentBean.daoInterfaceName + ";");
        }		
	}

	@Override
	protected void writeContent() throws IOException {

		writeLine("package " + this.bean.myPackage.baseProcessorImplPackageName + ";");
        skipLine();
        
        writeImports();
        skipLine();

        writeLine("/**");
        writeLine(" * auto generated base processor class file");
        writeLine(" * <br/>no modification should be done to this file");
		writeLine(" * <br/>processed by ifolks-generator");
        writeLine(" */");

        writeLine("public class " + this.bean.baseProcessorClassName + " {");
        skipLine();

        writeLine("/*"); 
        writeLine(" * properties injected by spring");
        writeLine(" */");
        
        writeLine("@Autowired");
        writeLine("protected " + this.bean.daoInterfaceName + " " + this.bean.daoObjectName + ";");
        


        for (OneToManyComponent oneToManyComponent : this.bean.oneToManyComponentList) {
            Bean currentBean = oneToManyComponent.referenceBean;
            writeLine("@Autowired");
            writeLine("protected " + currentBean.daoInterfaceName + " " + currentBean.daoObjectName + ";");
        }
        skipLine();
        
        writeLine("/**");
        writeLine(" * process save");
        writeLine(" */");
        writeLine("public " + bean.idType + " save(" + this.bean.className + " " + this.bean.objectName + ") {");
        writeLine("return " + this.bean.daoObjectName + ".saveAndFlush(" + this.bean.objectName + ").getId();");
        writeLine("}");
        skipLine();
        
        for (OneToOneComponent oneToOneComponent : this.bean.oneToOneComponentList)
        {
            Bean currentBean = oneToOneComponent.referenceBean;
            writeLine("/**");
            writeLine(" * process save one to one component " + currentBean.className);
            writeLine(" */");
            writeLine("public void save" + currentBean.className + "(" + currentBean.className + " " + currentBean.objectName + "," + this.bean.className + " " + this.bean.objectName + ") {");
            writeLine(this.bean.objectName + ".set" + currentBean.className + "(" + currentBean.objectName + ");");
            writeLine(currentBean.objectName + ".set" + bean.className + "(" + this.bean.objectName + ");");
            writeLine("this." + this.bean.daoObjectName + ".save(" + this.bean.objectName + ");");
            writeLine("}");
            skipLine();
        }

        for (OneToManyComponent oneToManyComponent : this.bean.oneToManyComponentList)
        {
        	Bean currentBean = oneToManyComponent.referenceBean;
        	writeLine("/**");
            writeLine(" * process save one to many component " + currentBean.className);
            writeLine(" */");
            writeLine("public void save" + currentBean.className + "(" + currentBean.className + " " + currentBean.objectName + "," + this.bean.className + " " + this.bean.objectName + ") {");
            writeLine(currentBean.objectName + ".set" + bean.className + "(" + this.bean.objectName + ");");
            writeLine("this." + currentBean.daoObjectName + ".save(" + currentBean.objectName + ");");
            writeLine("}");
            skipLine();
        }
        

        writeLine("/**");
        writeLine(" * process update");
        writeLine(" */");
        writeLine("public void update(" + this.bean.className + " " + this.bean.objectName + ") {");
        writeLine("// Empty by default. Can be overridden");
        writeLine("}");
        skipLine();

        for (OneToOneComponent oneToOneComponent : this.bean.oneToOneComponentList)
        {
            Bean currentBean = oneToOneComponent.referenceBean;
            writeLine("/**");
            writeLine(" * process update one to one component " + currentBean.className);
            writeLine(" */");
            writeLine("public void update" + currentBean.className + "(" + currentBean.className + " " + currentBean.objectName + ") {");
            writeLine("// Empty by default. Can be overridden");
            writeLine("}");
            skipLine();
        }

        for (OneToManyComponent oneToManyComponent : this.bean.oneToManyComponentList)
        {
            Bean currentBean = oneToManyComponent.referenceBean;
            writeLine("/**");
            writeLine(" * process update one to many component " + currentBean.className);
            writeLine(" */");
            writeLine("public void update" + currentBean.className + "(" + currentBean.className + " " + currentBean.objectName + ") {");
            writeLine("// Empty by default. Can be overridden");
            writeLine("}");
            skipLine();
        }

        writeLine("/**");
        writeLine(" * process delete");
        writeLine(" */");
        writeLine("public void delete(" + this.bean.className + " " + this.bean.objectName + ") {");
        writeLine(this.bean.daoObjectName + ".delete(" + this.bean.objectName + ");");
        writeLine("}");
        skipLine();

        for (OneToOneComponent oneToOneComponent : this.bean.oneToOneComponentList)
        {
            Bean currentBean = oneToOneComponent.referenceBean;
            writeLine("/**");
            writeLine(" * process delete one to one component " + currentBean.className);
            writeLine(" */");
            writeLine("public void delete" + currentBean.className + "(" + currentBean.className + " " + currentBean.objectName + ") {");
            writeLine(this.bean.className + " " + this.bean.objectName + " = " + currentBean.objectName + ".get" + bean.className + "();");
            writeLine("if (" + this.bean.objectName + " != null) {");
            writeLine("    " + this.bean.objectName + ".set" + currentBean.className + "(null);");
            writeLine("    " + currentBean.objectName + ".set" + bean.className + "(null);");
            writeLine("    this." + this.bean.daoObjectName + ".save(" + this.bean.objectName + ");");
            writeLine("}");
            writeLine("}");
            skipLine();
        }
        
        for (OneToManyComponent oneToManyComponent : this.bean.oneToManyComponentList)
        {
            Bean currentBean = oneToManyComponent.referenceBean;
            writeLine("/**");
            writeLine(" * process delete one to many component " + currentBean.className);
            writeLine(" */");
            writeLine("public void delete" + currentBean.className + "(" + currentBean.className + " " + currentBean.objectName + ") {");
            writeLine(currentBean.objectName + ".get" + bean.className + "().get" + currentBean.className + "Collection().remove(" + currentBean.objectName + ");");
            writeLine("this." + currentBean.daoObjectName + ".delete(" + currentBean.objectName + ");");
            writeLine("}");
            skipLine();
        }

        writeLine("}");

    }
}
