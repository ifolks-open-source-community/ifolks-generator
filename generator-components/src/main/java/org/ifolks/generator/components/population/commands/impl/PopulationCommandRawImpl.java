package org.ifolks.generator.components.population.commands.impl;

import java.io.IOException;

import org.ifolks.generator.components.commands.Command;
import org.ifolks.generator.components.population.commands.interfaces.PopulationCommand;
import org.ifolks.generator.components.population.files.SimpleScriptFileReader;
import org.ifolks.generator.model.domain.business.Bean;
import org.ifolks.generator.model.exception.InvalidFileException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Implementation of a {@link PopulationCommand} that 
 * <li>reads a recordName in the file
 * <li>finds the corresponding component in a spring context
 * <li>execute the component as a Command
 * 
 * @author Nicolas Thibault
 *
 */
@Component
public class PopulationCommandRawImpl implements PopulationCommand, ApplicationContextAware {
	
	@Autowired
	private SimpleScriptFileReader reader;
	
	private ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	
	@Override
	public void execute(String path, Bean bean) {
		
		try {
			String className = reader.readScript(path);
			Class<?> clazz = Class.forName(className);
		
			((Command)applicationContext.getBean(clazz)).execute();
			
		} catch (IOException | ClassNotFoundException e) {
			throw new InvalidFileException("failed to read raw file at path : " + path, e);
		}		
	}
}
