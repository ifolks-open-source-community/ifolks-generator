package org.ifolks.generator.components.population.files.readers.impl;

import org.ifolks.generator.components.population.datasources.InputDataSourceProvider;
import org.ifolks.generator.components.population.files.readers.interfaces.DataReader;
import org.ifolks.generator.model.exception.UnhandledPersistenceModeException;
import org.ifolks.generator.model.metadata.PersistenceMode;
import org.springframework.stereotype.Component;

/**
 * get a {@link DataReader} depending on the {@link PersistenceMode}
 * <li>CSV : {@link CsvFileDataReader}
 * <li>XML : {@link XmlFileSourceAndScriptDataReader}
 * 
 * @author Nicolas Thibault
 *
 */
@Component
public class DataReaderFactory {

	public DataReader getDataReader(PersistenceMode type, InputDataSourceProvider inputSourceProvider) {
		switch (type) {
		case CSV:
			return new CsvFileDataReader();
		case XML:
			return new XmlFileSourceAndScriptDataReader(inputSourceProvider);
		default:
			throw new UnhandledPersistenceModeException("Unhandled persistenceMode " + type + " for reading backup arguments");
		}
	}
}
