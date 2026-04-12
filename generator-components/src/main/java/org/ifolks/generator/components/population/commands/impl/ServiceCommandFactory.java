package org.ifolks.generator.components.population.commands.impl;

import org.ifolks.generator.components.population.commands.interfaces.ServiceCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * searches for a generated {@link ServiceCommand} in the populator spring context
 * 
 * @author Nicolas Thibault
 *
 */
@Component
public class ServiceCommandFactory {

	@Autowired
	private ApplicationContext applicationContext;


	/**
	 * create the appropriate command depending on what table is used for
	 * population.
	 */
	public ServiceCommand getCommand(String classSimpleName) {

		return (ServiceCommand) applicationContext.getBean(classSimpleName);
	}
	
	public ServiceCommand getCommand(Class<?> commandClass) {

		return (ServiceCommand) applicationContext.getBean(commandClass);
	}
}
