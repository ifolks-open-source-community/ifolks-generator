package org.ifolks.generator.components.population.commands.impl;

import java.util.List;

import org.ifolks.generator.components.population.commands.interfaces.PopulationCommand;
import org.ifolks.generator.components.population.commands.interfaces.ServiceCommand;
import org.ifolks.generator.components.population.files.readers.interfaces.DataReader;
import org.ifolks.generator.model.domain.business.Bean;

/**
 * This implementation of a {@link PopulationCommand} has 
 * <li>a {@link DataReader}
 * <li>a {@link ServiceCommand}
 * 
 * It works this way :<br>
 * the reader fetches the {@link BackupArguments}<br>
 * the {@link ServiceCommand} executes with the fetched arguments
 * 
 * @author Nicolas Thibault
 *
 */
public class FromDataPopulationCommandImpl implements PopulationCommand {
	
	private DataReader dataReader;
	private ServiceCommand command;
	
		
	public FromDataPopulationCommandImpl(DataReader argumentReader,
			ServiceCommand command) {
		super();
		this.dataReader = argumentReader;
		this.command = command;
	}



	@Override
	public void execute(String path, Bean bean) {
		
		List<Object[]> data = dataReader.readData(path, bean);
		
		command.execute(data);
	}
}
