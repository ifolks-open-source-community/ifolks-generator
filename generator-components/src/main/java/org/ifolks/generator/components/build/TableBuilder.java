package org.ifolks.generator.components.build;

import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.ifolks.generator.components.build.commands.JdbcRawCommand;
import org.ifolks.generator.model.domain.Project;
import org.ifolks.generator.model.domain.database.Table;
import org.ifolks.generator.model.exception.InvalidFileException;
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
