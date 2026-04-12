package org.ifolks.generator.components.population.datasources;

import java.util.Map;

import org.apache.commons.dbcp.BasicDataSource;
import org.ifolks.generator.model.exception.DataSourceNotFoundException;

/**
 * This class is used in your /data-model/datasource-context.xml<br/>
 * given a name, it gives a declared {@link BasicDataSource} that can be built then populated
 * @author Nicolas Thibault
 *
 */
public class OutputDataSourceProvider {

	/*
	 * properties
	 */
	private Map<String, BasicDataSource> dataSources;
	
	
	/*
	 * constructor
	 */
	public OutputDataSourceProvider(Map<String, BasicDataSource> dataSources) {
		this.dataSources = dataSources;
	}

	
	public BasicDataSource getDataSource(String dataSourceName) throws DataSourceNotFoundException {

		BasicDataSource result = dataSources.get(dataSourceName);
		
		if (result == null) {
			throw new DataSourceNotFoundException("Unable to find output DataSource : " + dataSourceName);
		}
		
		return result;
	}
}
