package org.ifolks.generator.skeletons.commands.impl.typed;

import org.ifolks.generator.model.metadata.files.FileType;
import org.ifolks.generator.skeletons.commands.impl.SingleFileWriteCommand;

public abstract class XhtmlFileWriteCommand extends SingleFileWriteCommand {

	/*
	 * constructor
	 */
	public XhtmlFileWriteCommand(String folderName, String fileName) {
		super(folderName, fileName, FileType.XHTML);
	}
}
