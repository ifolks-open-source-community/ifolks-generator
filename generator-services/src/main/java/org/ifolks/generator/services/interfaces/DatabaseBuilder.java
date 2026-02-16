package org.ifolks.generator.services.interfaces;

import java.io.IOException;
import java.sql.SQLException;

import org.apache.commons.dbcp.BasicDataSource;
import org.ifolks.generator.model.domain.Project;
import org.ifolks.generator.model.exception.InvalidFileException;

public interface DatabaseBuilder {
	
	void buildDatabase(BasicDataSource dataSource, Project project) throws InvalidFileException, IOException, SQLException;

}
