package org.ifolks.generator.skeletons.core.commands.population;

import java.io.File;
import java.io.IOException;

import org.ifolks.generator.model.domain.business.Bean;
import org.ifolks.generator.skeletons.commands.impl.typed.JavaFileWriteCommand;

public class BeanPopulatorCommandFileWriteCommand extends JavaFileWriteCommand {

	private Bean bean;

    public BeanPopulatorCommandFileWriteCommand(Bean bean){
        super(bean.myPackage.model.project.workspaceFolder + File.separator + bean.myPackage.model.populatorArtefactName + File.separator + bean.myPackage.model.javaSourcesFolder + File.separator + bean.myPackage.commandPackageName.replace(".",File.separator),
        		bean.className + "Command");
        
        		this.bean = bean;
    }

	@Override
	protected void fetchSpecificImports() {
		
		javaImports.add("import java.util.List;");
		javaImports.add("import java.util.Date;");
		javaImports.add("import java.time.LocalDate;");
		javaImports.add("import java.math.BigDecimal;");
		
		javaImports.add("import org.ifolks.generator.components.population.commands.interfaces.ServiceCommand;");
		
		javaImports.add("import org.slf4j.Logger;");
		javaImports.add("import org.slf4j.LoggerFactory;");		
		
		javaImports.add("import org.springframework.beans.factory.annotation.Autowired;");
		javaImports.add("import org.springframework.stereotype.Component;");
		
        javaImports.add("import " + this.bean.myPackage.formsPackageName + "." + this.bean.formBean.className + ";");
        javaImports.add("import " + this.bean.myPackage.formMapperPackageName + "." + this.bean.formBean.mapperClassName + ";");
        javaImports.add("import " + this.bean.myPackage.serviceInterfacePackageName + "." + this.bean.serviceInterfaceName + ";");
        

	}

	@Override
	protected void writeContent() throws IOException {
		
		writeLine("package " + this.bean.myPackage.commandPackageName + ";");
        skipLine();

        writeImports();
        skipLine();

        writeLine("/**");
        writeLine(" * auto generated bean populator command class file");
        writeLine(" * <br/>no modification should be done to this file");
		writeLine(" * <br/>processed by ifolks-generator");
		writeLine(" */");
		writeLine("@Component");
        writeLine("public class " + bean.className + "Command implements ServiceCommand {");
        skipLine();
        
        writeLine("/*");
        writeLine(" * logger");
        writeLine(" */");
        writeLine("private static final Logger logger = LoggerFactory.getLogger(" + bean.className + "Command.class);");
        skipLine();
        
        writeLine("@Autowired");
        writeLine("private " + this.bean.serviceInterfaceName + " " + this.bean.serviceObjectName + ";");
        skipLine();
        
        writeLine("@Autowired");
        writeLine("private " + this.bean.formBean.mapperClassName + " " + this.bean.formBean.mapperObjectName + ";");
        skipLine();

        writeLine("@Override");
        writeLine("public void execute(List<Object[]> data) {");
        
        writeLine("for (Object[] args : data) {");
        writeLine("String message = " + CHAR_34 + "execute " + bean.serviceObjectName + ".save - args : " + CHAR_34 + ";");
        writeLine("for (Object arg:args) {");
        writeLine("message += " + CHAR_34 + "[" + CHAR_34 + " + arg + " + CHAR_34 + "]" + CHAR_34 + ";");
        writeLine("}");
        writeLine("logger.info(message);");
        skipLine();
                
        writeLine("try {");
        writeLine(bean.formBean.className + " " + bean.formBean.objectName + " = " + this.bean.formBean.mapperObjectName + ".toForm(args);");
        skipLine();
        
        writeLine("this." + bean.serviceObjectName + ".save(" + this.bean.formBean.objectName + ");");
        writeLine("} catch (Exception e) {");
        writeLine("logger.error(message + " + CHAR_34 + "failed : " + CHAR_34 + " + e.getClass().getSimpleName() + " + CHAR_34 + " - " + CHAR_34 + " + e.getMessage(), e);");
        writeLine("}");
        writeLine("}");
        writeLine("}");
        writeLine("}");
        
	}
}
