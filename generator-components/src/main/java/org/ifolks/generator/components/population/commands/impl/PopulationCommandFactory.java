package org.ifolks.generator.components.population.commands.impl;

import org.ifolks.generator.components.population.commands.interfaces.PopulationCommand;
import org.ifolks.generator.components.population.commands.interfaces.ServiceCommand;
import org.ifolks.generator.components.population.datasources.InputDataSourceProvider;
import org.ifolks.generator.components.population.files.readers.impl.DataReaderFactory;
import org.ifolks.generator.components.population.files.readers.interfaces.DataReader;
import org.ifolks.generator.model.domain.business.Bean;
import org.ifolks.generator.model.metadata.PersistenceMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * return a PopulationCommand depending on which {@link PersistenceMode} will be discovered<br>
 * <li>CMD : uses a {@link PopulationCommandRawImpl}
 * <li>else : uses a {@link FromDataPopulationCommandImpl}
 * 
 * in the last case, we need to build it by injecting a {@link DataReader} and a {@link ServiceCommand}<br>
 * this is built with the corresponding factories : {@link DataReaderFactory} and {@link ServiceCommandFactory}
 * 
 * @author Nicolas Thibault
 *
 */
@Component
public class PopulationCommandFactory {
	
	@Autowired
	private DataReaderFactory readerFactory;
	
	@Autowired
	private ServiceCommandFactory backupArgumentsCommandFactory;
	
	@Autowired
	private PopulationCommandRawImpl backupCommandRawImpl;


	
	public PopulationCommand getPopulationCommand(Bean bean, PersistenceMode mode, InputDataSourceProvider inputDataSourceProvider) {
		
		switch (mode) {
			case CMD:
				return backupCommandRawImpl;
			default:
				return getFromDataPopulationCommand(bean, mode, inputDataSourceProvider);
		}
	}

	private PopulationCommand getFromDataPopulationCommand(Bean bean, PersistenceMode mode, InputDataSourceProvider inputDataSourceProvider) {
		
		DataReader dataReader = readerFactory.getDataReader(mode, inputDataSourceProvider);
		
		ServiceCommand command = backupArgumentsCommandFactory.getCommand(bean.objectName + "Command");
		
		return new FromDataPopulationCommandImpl(dataReader, command);
	}
}
