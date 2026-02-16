package org.ifolks.generator.persistence.backup.command.interfaces;

import org.ifolks.generator.persistence.backup.reader.model.BackupArguments;

public interface BackupArgumentsCommand {

	void execute(BackupArguments arguments);
}
