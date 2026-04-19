package org.ifolks.generator.components.build;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.ifolks.generator.components.build.commands.JdbcRawCommand;
import org.ifolks.generator.components.database.DatabaseHandlerDiscovery;
import org.ifolks.generator.components.population.files.SimpleScriptFileReader;
import org.ifolks.generator.model.domain.Project;
import org.ifolks.generator.model.exception.InvalidFileException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DatabaseCleaner {
	
	@Autowired
	private SimpleScriptFileReader scriptFileReader;	

	public void cleanDatabase(DataSource dataSource, Project project, String engineName) throws IOException, InvalidFileException, SQLException {
		
		String scriptFilePath = project.workspaceFolder + File.separator + DatabaseHandlerDiscovery.getBuildScriptFolder(engineName) + File.separator + "MAIN.sql";
		String script = scriptFileReader.readScript(scriptFilePath);
			
		new JdbcRawCommand(dataSource, script).execute();
	}
}
