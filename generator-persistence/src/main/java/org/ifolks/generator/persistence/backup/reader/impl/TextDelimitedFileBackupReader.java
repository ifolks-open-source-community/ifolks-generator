package org.ifolks.generator.persistence.backup.reader.impl;

import java.io.IOException;
import java.util.List;

import org.ifolks.generator.model.exception.InvalidFileException;
import org.ifolks.generator.model.exception.ReadBackupFailureException;
import org.ifolks.generator.persistence.backup.file.interfaces.TextDelimitedFileParser;
import org.ifolks.generator.persistence.backup.file.model.TextDelimitedFile;
import org.ifolks.generator.persistence.backup.reader.interfaces.BackupArgumentReader;
import org.ifolks.generator.persistence.backup.reader.model.BackupArguments;


public abstract class TextDelimitedFileBackupReader implements BackupArgumentReader {

	/*
	 * properties
	 */
	private TextDelimitedFileParser fileParser;
	
	/*
	 * constructor
	 */
	public TextDelimitedFileBackupReader(TextDelimitedFileParser fileParser) {
		this.fileParser = fileParser;
	}
	
	public BackupArguments readBackupArgs(String backupFilePath)  {
		BackupArguments result = new BackupArguments();
		result.setArguments(readArgs(backupFilePath));
		result.setArgumentsTyped(false);
		return result;
	}
	

	private List<Object[]> readArgs(String backupFilePath){
		try {
			TextDelimitedFile csvFile = fileParser.readData(backupFilePath);
			
			return csvFile.getData();
			
		} catch (IOException | InvalidFileException e) {
			throw new ReadBackupFailureException("failed to read backup at " + backupFilePath + " : " + e.getClass().getName() + " - " + e.getMessage(), e);
		}
	}
}
