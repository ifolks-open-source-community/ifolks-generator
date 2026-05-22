package org.ifolks.generator.skeletons.core.commands.commands;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.ifolks.generator.model.domain.business.Bean;
import org.ifolks.generator.model.domain.business.OneToManyComponent;
import org.ifolks.generator.model.domain.ui.ViewProperty;
import org.ifolks.generator.skeletons.commands.impl.typed.JavaFileWriteCommand;

public class OneToManyComponentPopulatorCommandFileWriteCommand extends JavaFileWriteCommand {

	private OneToManyComponent oneToManyComponent;
	private Bean referenceBean;
    private Bean parentBean;

    public OneToManyComponentPopulatorCommandFileWriteCommand(OneToManyComponent oneToManyComponent){
        super(oneToManyComponent.referenceBean.myPackage.model.project.workspaceFolder + File.separator + oneToManyComponent.referenceBean.myPackage.model.commandsArtefactName + File.separator + oneToManyComponent.referenceBean.myPackage.model.javaSourcesFolder + File.separator + oneToManyComponent.referenceBean.myPackage.commandPackageName.replace(".",File.separator),
        		oneToManyComponent.referenceBean.className + "Command");
        
        		this.oneToManyComponent = oneToManyComponent;
        		referenceBean = oneToManyComponent.referenceBean;
        	    parentBean = oneToManyComponent.parentBean;
    }

	@Override
	protected void fetchSpecificImports() {
		
		javaImports.add("import java.util.Arrays;");
		javaImports.add("import java.util.List;");
		javaImports.add("import java.time.OffsetDateTime;");
		javaImports.add("import java.time.LocalDate;");
		javaImports.add("import java.math.BigDecimal;");
		
		javaImports.add("import org.ifolks.generator.components.population.commands.interfaces.ServiceCommand;");
		
		javaImports.add("import org.slf4j.Logger;");
		javaImports.add("import org.slf4j.LoggerFactory;");
		
		javaImports.add("import org.springframework.beans.factory.annotation.Autowired;");
		javaImports.add("import org.springframework.stereotype.Component;");
		
        javaImports.add("import " + referenceBean.myPackage.fullViewsPackageName + "." + referenceBean.fullViewBean.className + ";");
        javaImports.add("import " + referenceBean.myPackage.formsPackageName + "." + referenceBean.formBean.className + ";");
        javaImports.add("import " + parentBean.myPackage.fullViewsPackageName + "." + parentBean.fullViewBean.className + ";");
        javaImports.add("import " + parentBean.myPackage.serviceInterfacePackageName + "." + parentBean.serviceInterfaceName + ";");
        javaImports.add("import " + this.referenceBean.myPackage.formMapperPackageName + "." + referenceBean.formBean.mapperClassName + ";");
	}

	@Override
	protected void writeContent() throws IOException {
		
		writeLine("package " + oneToManyComponent.referenceBean.myPackage.commandPackageName + ";");
        skipLine();

        writeImports();
        skipLine();

        writeLine("/**");
        writeLine(" * auto generated bean populator command class file");
        writeLine(" * <br/>no modification should be done to this file");
		writeLine(" * <br/>processed by ifolks-generator");
		writeLine(" */");
		writeLine("@Component");
        writeLine("public class " + oneToManyComponent.referenceBean.className + "Command implements ServiceCommand {");
        skipLine();
        
        writeLine("/*");
        writeLine(" * logger");
        writeLine(" */");
        writeLine("private static final Logger logger = LoggerFactory.getLogger(" + oneToManyComponent.referenceBean.className + "Command.class);");
        skipLine();
    
        writeLine("@Autowired");
        writeLine("private " + oneToManyComponent.parentBean.serviceInterfaceName + " " + parentBean.serviceObjectName + ";");
        skipLine();
        
        writeLine("@Autowired");
        writeLine("private " + this.oneToManyComponent.referenceBean.formBean.mapperClassName + " " + this.oneToManyComponent.referenceBean.formBean.mapperObjectName + ";");
        skipLine();
        
        writeLine("@Override");
        writeLine("public void execute(List<Object[]> data) {");
        
        writeLine("for (Object[] args:data) {");
        writeLine("String message = " + CHAR_34 + "execute " + parentBean.serviceObjectName + ".save - args : " + CHAR_34 + ";");
                
        writeLine("for (Object arg:args) {");
        writeLine("message += " + CHAR_34 + "[" + CHAR_34 + " + arg + " + CHAR_34 + "]" + CHAR_34 + ";");
        writeLine("}");
        writeLine("logger.info(message);");
        skipLine();
                
        writeLine("try {");
        
        List<ViewProperty> properties = parentBean.referenceViewProperties;
        
        writeLine(referenceBean.formBean.className + " " + referenceBean.formBean.objectName + " = " + this.oneToManyComponent.referenceBean.formBean.mapperObjectName + ".toForm(Arrays.copyOfRange(args," + properties.size() + ",args.length));");
        skipLine();               
        
        int i = 0;
        for (ViewProperty property:properties) {
        	String type = property.javaType;        	
        	writeLine(type + " arg" + i + " = (" + type + ")args[" + i + "];");
        	i++;
        }
        
        write(parentBean.fullViewBean.className + " " + parentBean.fullViewBean.objectName + " = " + parentBean.serviceObjectName + ".find(arg0");
        
        for (i=1;i<properties.size();i++) {
            write(", arg" + i);
        }
        writeLine(");");
        skipLine();
        
        writeLine("this." + parentBean.serviceObjectName + ".save" + referenceBean.className + "(" + parentBean.fullViewBean.objectName + ".id(), " + referenceBean.formBean.objectName + ");");
        writeLine("} catch (Exception e) {");
        writeLine("logger.error(message + " + CHAR_34 + "failed : " + CHAR_34 + " + e.getClass().getSimpleName() + " + CHAR_34 + " - " + CHAR_34 + " + e.getMessage(), e);");
        writeLine("}");
        writeLine("}");
        writeLine("}");
        writeLine("}");
        
	}
}
