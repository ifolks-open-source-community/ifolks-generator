package org.ifolks.generator.persistence.backup.reader.impl;

import org.ifolks.generator.persistence.backup.file.impl.SemiColonExcelCsvFileParserImpl;


public class StandardCsvFileBackupReader extends TextDelimitedFileBackupReader {
	
	/*
	 * constructor
	 */
	public StandardCsvFileBackupReader() {
		super(new SemiColonExcelCsvFileParserImpl());
	}
}
