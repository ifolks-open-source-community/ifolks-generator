package org.ifolks.generator.components.population.datasources;

import java.util.Map;

import javax.sql.DataSource;

import org.ifolks.generator.model.exception.DataSourceNotFoundException;

/**
 * This class is used in your /data-model/datasource-context.xml<br/>
 * given a name, it gives a declared {@link BasicDataSource} that can be accessed to fecth data that will further be injected in your project datasource
 * @author Nicolas Thibault
 *
 */
public class InputDataSourceProvider {

	/*
	 * properties
	 */
	private Map<String, DataSource> dataSources;
	
	
	/*
	 * constructor
	 */
	public InputDataSourceProvider(Map<String, DataSource> dataSources) {
		this.dataSources = dataSources;
	}


	public DataSource getDataSource(String dataSourceName) throws DataSourceNotFoundException {

		DataSource result = dataSources.get(dataSourceName);
		
		if (result == null) {
			throw new DataSourceNotFoundException("Unable to find input DataSource : " + dataSourceName);
		}
		
		return result;
	}
}
