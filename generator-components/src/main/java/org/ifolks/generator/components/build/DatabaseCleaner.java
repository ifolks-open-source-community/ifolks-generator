package org.ifolks.generator.components.build;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import org.apache.commons.dbcp.BasicDataSource;
import org.ifolks.generator.components.resolvers.DatabaseHandlerDiscovery;
import org.ifolks.generator.persistence.backup.file.interfaces.SimpleScriptFileReader;
import org.ifolks.generator.persistence.build.JdbcRawCommand;
import org.ifolks.generator.model.domain.Project;
import org.ifolks.generator.model.exception.InvalidFileException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DatabaseCleaner {
	
	@Autowired
	private SimpleScriptFileReader scriptFileReader;	

	public void cleanDatabase(BasicDataSource dataSource, Project project) throws IOException, InvalidFileException, SQLException {
		
		String scriptFilePath = project.workspaceFolder + File.separator + DatabaseHandlerDiscovery.getBuildScriptFolder(dataSource) + File.separator + "MAIN.sql";
		String script = scriptFileReader.readScript(scriptFilePath);
			
		new JdbcRawCommand(dataSource, script).execute();
	}
}
