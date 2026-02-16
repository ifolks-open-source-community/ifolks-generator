package org.ifolks.generator.components.metadata.factory.interfaces;

import org.ifolks.generator.model.domain.Model;
import org.ifolks.generator.model.domain.Package;
import org.ifolks.generator.model.domain.database.Table;
import org.ifolks.generator.model.metadata.TableMetaData;

public interface TableFactory {
	
	Table scanTable(TableMetaData tableMetaData, Package myPackage);
	
	Table fillTable(TableMetaData tableMetaData, Model model);
}
