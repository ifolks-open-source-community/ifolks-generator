package org.ifolks.generator.persistence.backup.command.impl;

import org.ifolks.generator.persistence.backup.command.interfaces.BackupArgumentsCommand;
import org.ifolks.generator.persistence.backup.reader.impl.StandardCsvFileBackupReader;
import org.ifolks.generator.persistence.backup.reader.interfaces.BackupArgumentReader;
import org.ifolks.generator.persistence.backup.reader.model.BackupArguments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JUnitDataInjector {
	
	@Autowired
	private BackupArgumentsCommandFactory commandFactory;
		
	
	public void inject(String filePath, Class<?> commandClass) {
		BackupArgumentReader argumentReader = new StandardCsvFileBackupReader();
		BackupArguments commandArgs = argumentReader.readBackupArgs(filePath);
		
		BackupArgumentsCommand command = commandFactory.getCommand(commandClass);
		command.execute(commandArgs);
	}
}
