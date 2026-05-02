package org.ifolks.generator.components.build;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.ifolks.generator.components.build.commands.JdbcRawCommand;
import org.ifolks.generator.components.database.DatabaseHandlerDiscovery;
import org.ifolks.generator.components.population.files.SimpleScriptFileReader;
import org.ifolks.generator.model.domain.Project;
import org.ifolks.generator.model.domain.database.Table;
import org.ifolks.generator.model.exception.InvalidFileException;


import java.nio.charset.Charset;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.StreamUtils;

public class TableBuilder {

	/*
	 * properties
	 */
	private DataSource dataSource;
	private ResourceLoader resourceLoader;
	private String scriptRootPath;
	
	/*
	 * constructor
	 */
	public TableBuilder(Project project, DataSource dataSource, String engineName, ResourceLoader resourceLoader) {
		this.dataSource = dataSource;
		this.resourceLoader = resourceLoader;
		scriptRootPath = "classpath:scripts/SQL/" + engineName + "/build";
	}
	

	public void buildTable(Table table, int step) throws IOException, InvalidFileException, SQLException {
		
		String scriptFilePath = scriptRootPath + "/" + step + "/" + table.myPackage.name.toUpperCase().replace(".", "/") + "/" + table.originalName + ".sql";
		
		String script = StreamUtils.copyToString(resourceLoader.getResource(scriptFilePath).getInputStream(), Charset.defaultCharset());
			
		new JdbcRawCommand(dataSource, script).execute();
	}
}
