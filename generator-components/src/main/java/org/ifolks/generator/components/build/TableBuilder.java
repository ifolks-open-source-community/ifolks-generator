package org.ifolks.generator.components.build;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import org.apache.commons.dbcp.BasicDataSource;
import org.ifolks.generator.components.build.commands.JdbcRawCommand;
import org.ifolks.generator.components.database.DatabaseHandlerDiscovery;
import org.ifolks.generator.components.population.files.SimpleScriptFileReader;
import org.ifolks.generator.model.domain.Project;
import org.ifolks.generator.model.domain.database.Table;
import org.ifolks.generator.model.exception.InvalidFileException;


public class TableBuilder {

	/*
	 * properties
	 */
	private BasicDataSource dataSource;
	private SimpleScriptFileReader scriptFileReader;
	private String scriptRootPath;
	
	/*
	 * constructor
	 */
	public TableBuilder(Project project, BasicDataSource dataSource) {
		this.dataSource = dataSource;
		this.scriptFileReader = new SimpleScriptFileReader();
		scriptRootPath = project.workspaceFolder + File.separator + DatabaseHandlerDiscovery.getBuildScriptFolder(dataSource);
	}
	

	public void buildTable(Table table, int step) throws IOException, InvalidFileException, SQLException {
		
		String scriptFilePath = scriptRootPath + File.separator + step + File.separator + table.myPackage.name.toUpperCase().replace(".", File.separator) + File.separator + table.originalName + ".sql";
		
		String script = scriptFileReader.readScript(scriptFilePath);
			
		new JdbcRawCommand(dataSource, script).execute();
	}
}
