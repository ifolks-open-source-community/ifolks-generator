package org.ifolks.generator.components.metadata.factory.interfaces;

import org.ifolks.generator.model.domain.Model;
import org.ifolks.generator.model.domain.business.Bean;
import org.ifolks.generator.model.domain.database.Table;
import org.ifolks.generator.model.metadata.TableMetaData;

public interface BeanFactory {

	Bean scanBean(TableMetaData tableMetaData, Table table);
	
	Bean fillBean(TableMetaData tableMetaData, Table table, Model model);
	

}
