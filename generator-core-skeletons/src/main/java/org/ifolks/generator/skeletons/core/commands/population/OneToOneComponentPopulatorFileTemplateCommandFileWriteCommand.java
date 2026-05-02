package org.ifolks.generator.skeletons.core.commands.population;

import java.io.File;
import java.io.IOException;

import org.ifolks.generator.model.domain.business.Bean;
import org.ifolks.generator.model.domain.business.OneToOneComponent;
import org.ifolks.generator.model.domain.ui.ViewProperty;
import org.ifolks.generator.skeletons.commands.impl.typed.CsvFileWriteCommand;

public class OneToOneComponentPopulatorFileTemplateCommandFileWriteCommand extends CsvFileWriteCommand {

	private Bean referenceBean;
    private Bean parentBean;

    public OneToOneComponentPopulatorFileTemplateCommandFileWriteCommand(OneToOneComponent oneToOneComponent){
    	super(oneToOneComponent.referenceBean.myPackage.model.project.workspaceFolder + File.separator + oneToOneComponent.referenceBean.myPackage.model.commandsArtefactName + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "population" + File.separator + "templates" + File.separator + oneToOneComponent.referenceBean.table.myPackage.name.toUpperCase().replace(".", File.separator),
    			oneToOneComponent.referenceBean.table.originalName);
    			referenceBean = oneToOneComponent.referenceBean;
    			parentBean = oneToOneComponent.parentBean;
    }


    @Override
	protected void writeContent() throws IOException {
		
    	boolean start = true;
    	for (ViewProperty property:parentBean.referenceViewProperties) {
			if (start) {
				start = false;
			} else {
				write(";");
			}
			write("\"" + property.rendering + "\"");
		}
		for (ViewProperty property:referenceBean.formBean.properties) {
			write(";");
			write("\"" + property.rendering + "\"");
		}
	}
}
