package org.ifolks.generator.skeletons.commands.impl.typed;

import org.ifolks.generator.model.metadata.files.FileType;
import org.ifolks.generator.skeletons.commands.impl.SingleFileWriteCommand;

public abstract class XmlFileWriteCommand extends SingleFileWriteCommand {

	/*
	 * constructor
	 */
	public XmlFileWriteCommand(String folderName, String fileName) {
		super(folderName, fileName, FileType.XML);
	}
}
