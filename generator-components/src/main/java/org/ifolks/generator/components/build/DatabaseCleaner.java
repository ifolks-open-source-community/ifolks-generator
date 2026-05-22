package org.ifolks.generator.components.build;

import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.ifolks.generator.components.build.commands.JdbcRawCommand;
import org.ifolks.generator.model.domain.Project;
import org.ifolks.generator.model.exception.InvalidFileException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

@Component
public class DatabaseCleaner {
	
	@Autowired
	private ResourceLoader resourceLoader;

	public void cleanDatabase(DataSource dataSource, Project project, String engineName) throws IOException, InvalidFileException, SQLException {
		
		String scriptFilePath = "classpath:scripts/SQL/" + engineName + "/build/MAIN.sql";
		
		String script = StreamUtils.copyToString(resourceLoader.getResource(scriptFilePath).getInputStream(), Charset.defaultCharset());
			
		new JdbcRawCommand(dataSource, script).execute();
	}
}
