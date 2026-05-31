package org.ifolks.generator.skeletons.core.commands.database.oracle;

import java.io.File;
import java.io.IOException;

import org.ifolks.generator.components.database.DatabaseHandlerDiscovery;
import org.ifolks.generator.model.domain.database.Column;
import org.ifolks.generator.model.domain.database.Table;
import org.ifolks.generator.model.metadata.DataType;
import org.ifolks.generator.model.metadata.IdGeneratorType;
import org.ifolks.generator.skeletons.commands.impl.typed.SqlFileWriteCommand;
import org.ifolks.generator.skeletons.core.database.OracleHandler;

public class OracleTableDefinitionFileWriteCommand extends SqlFileWriteCommand {

	private Table table;
	

	/*
	 * constructor
	 */
	public OracleTableDefinitionFileWriteCommand(Table table) {

		super(table.myPackage.model.project.workspaceFolder + File.separator + DatabaseHandlerDiscovery.getBuildScriptFolder(OracleHandler.NAME, table.myPackage.model.project.projectName) + File.separator + "1" + File.separator + table.myPackage.name.toUpperCase().replace(".", File.separator), table.originalName);

		this.table = table;
	}

	@Override
	public void writeContent() throws IOException {
		
		createTable();
		


		writeNotOverridableContent();

		skipLine();
	}

	private void createTable() {
		
		writeLine("-- create table --");
		writeLine("CREATE TABLE " + table.name);
		writeLine("(");
		write("ID " + getOracleType(table.idType));

		for (Column column:table.columns) {
			writeLine(",");
			write(column.name + " " + getOracleType(column.dataType));
			if (column.nullable) {
				write(" NULL");
			} else {
				write(" NOT NULL");
			}
		}

		writeLine(",");
		
		
		write("CONSTRAINT PK_" + table.name + " PRIMARY KEY (ID)");
		write(" USING INDEX (CREATE INDEX IDX_" + table.name + "_PK ON " + table.name + "(ID)");
		
		if (table.myPackage.model.project.indexesTableSpace != null) {
			write(" TABLESPACE " + table.myPackage.model.project.indexesTableSpace);
		}
		skipLine();
		write(")");
		if (table.myPackage.model.project.tablesTableSpace != null) {
			write(" TABLESPACE " + table.myPackage.model.project.tablesTableSpace);
		}
		write(")");
		skipLine();
		writeLine("/");
		skipLine();

		if (table.idGeneratorType.equals(IdGeneratorType.SEQUENCE)) {
			writeLine("-- create sequence --");
			writeLine("CREATE SEQUENCE " + table.sequenceName + " MINVALUE 0 NOMAXVALUE START WITH 0 INCREMENT BY 1 NOCYCLE");
			writeLine("/");
			skipLine();
		}
	}
	

	
	
	private String getOracleType(DataType type) {
		switch (type) {
			case TEXT:
				return "CLOB";
	
			case STRING:
				return "VARCHAR2(255)";
	
			case SHORT:
				return "NUMBER(5,0)";
				
			case INTEGER:
				return "NUMBER(10,0)";
			
			case LONG:
				return "NUMBER(19,0)";
	
			case DOUBLE:
				return "FLOAT(24)";
				
			case BIG_DECIMAL:
				return "NUMBER";
	
			case DATE:
				return "DATE";
			
			case DATETIME:
				return "TIMESTAMP WITH TIME ZONE";
	
			case BOOLEAN:
				return "NUMBER(1,0)";
	
			default:
				throw new IllegalArgumentException("Unhandled data type " + this);
		}
	}
}
