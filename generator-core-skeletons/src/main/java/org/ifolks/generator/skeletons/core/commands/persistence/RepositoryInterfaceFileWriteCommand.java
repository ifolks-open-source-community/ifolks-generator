package org.ifolks.generator.skeletons.core.commands.persistence;

import java.io.File;
import java.io.IOException;

import org.ifolks.generator.model.domain.business.Bean;
import org.ifolks.generator.skeletons.commands.impl.typed.JavaFileWriteCommand;

public class RepositoryInterfaceFileWriteCommand extends JavaFileWriteCommand {
	
	private Bean bean;

	public RepositoryInterfaceFileWriteCommand(Bean bean) {
		super(bean.myPackage.model.project.workspaceFolder + File.separator + bean.myPackage.model.persistenceArtefactName + File.separator + bean.myPackage.model.javaSourcesFolder + File.separator
				+ bean.myPackage.DAOInterfacePackageName.replace(".", File.separator), bean.daoInterfaceName);

		this.bean = bean;
	}

	@Override
	protected void fetchSpecificImports() {
		javaImports.add("import org.springframework.stereotype.Repository;");
		javaImports.add("import " + bean.myPackage.baseDAOInterfacePackageName + "." + this.bean.baseDaoInterfaceName + ";");
	}

	@Override
	protected void writeContent() throws IOException {
		writeLine("package " + bean.myPackage.DAOInterfacePackageName + ";");
		skipLine();

		writeImports();
		skipLine();

		writeLine("/**");
		writeLine(" * auto generated repository interface file");
		writeLine(" * <br/>write modifications between specific code marks");
		writeLine(" * <br/>processed by ifolks-generator");
		writeLine(" */");
		writeLine("@Repository");
		writeLine("public interface " + bean.daoInterfaceName + " extends " + bean.baseDaoInterfaceName + " {");
		skipLine();

		this.writeNotOverridableContent();

		writeLine("}");
	}
}
