package org.ifolks.generator.skeletons.database;

import org.ifolks.generator.model.metadata.datasources.DataSourceMetaData;
import org.ifolks.generator.skeletons.layers.Layer;

public interface DatabaseHandler {

	String getName();
	
	String getDriverClassName();
	
	String getDialect();
	
	String getUrl(DataSourceMetaData datasource);
	
	String rename(String name);

	Layer getLayer();
	
}
