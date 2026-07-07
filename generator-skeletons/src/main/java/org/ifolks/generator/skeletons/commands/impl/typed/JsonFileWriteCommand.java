package org.ifolks.generator.skeletons.commands.impl.typed;

import org.ifolks.generator.model.metadata.files.FileType;
import org.ifolks.generator.skeletons.commands.impl.SingleFileWriteCommand;

public abstract class JsonFileWriteCommand extends SingleFileWriteCommand {

	/*
	 * constructor
	 */
	public JsonFileWriteCommand(String folderName, String fileName) {
		super(folderName, fileName, FileType.JSON);
	}
}
