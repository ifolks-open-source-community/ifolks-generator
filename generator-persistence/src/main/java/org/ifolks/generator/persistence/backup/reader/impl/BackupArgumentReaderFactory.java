package org.ifolks.generator.persistence.backup.reader.impl;

import org.ifolks.generator.model.exception.UnhandledPersistenceModeException;
import org.ifolks.generator.model.metadata.PersistenceMode;
import org.ifolks.generator.persistence.backup.datasource.interfaces.InputDataSourceProvider;
import org.ifolks.generator.persistence.backup.reader.interfaces.BackupArgumentReader;
import org.springframework.stereotype.Component;

/**
 * get a {@link BackupArgumentReader} dependeing on the {@link PersistenceMode}
 * <li>CSV : {@link TextDelimitedFileBackupReader}
 * <li>XML : {@link XmlFileBackupReader}
 * 
 * @author Nicolas Thibault
 *
 */
@Component
public class BackupArgumentReaderFactory {

	public BackupArgumentReader getBackupArgumentReader(PersistenceMode type, InputDataSourceProvider inputSourceProvider) {
		switch (type) {
		case TXT:
			return new LegacyTextDelimitedFileBackupReader();
		case CSV:
			return new StandardCsvFileBackupReader();
		case XML:
			return new XmlFileBackupReader(inputSourceProvider);
		default:
			throw new UnhandledPersistenceModeException("Unhandled persistenceMode " + type + " for reading backup arguments");
		}
	}
}
