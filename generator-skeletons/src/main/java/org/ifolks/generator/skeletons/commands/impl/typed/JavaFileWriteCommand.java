package org.ifolks.generator.skeletons.commands.impl.typed;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.ifolks.generator.model.metadata.files.FileType;
import org.ifolks.generator.skeletons.commands.impl.SingleFileWriteCommand;




/**
 * Extension of a generic file write command to handle specificities of java files
 * <br/>Handles conservation of imports
 * @author Nicolas Thibault
 *
 */
public abstract class JavaFileWriteCommand extends SingleFileWriteCommand {

	/*
	 * properties
	 */
	protected Set<String> javaImports;
	
	
	/*
	 * constructor
	 */
	public JavaFileWriteCommand(String folderName, String fileName) {
		super(folderName, fileName, FileType.JAVA);
		this.javaImports = new TreeSet<String>();
	}

	private final void fetchImports() throws IOException {
		if (Files.exists(filePath)) {
			List<String> lines = Files.readAllLines(filePath,
					fileType.getEncoding());
			
			for (String line : lines) {
				if (line.startsWith("import ")) {
					javaImports.add(line);
				}
			}
		}
	}
	
	protected abstract void fetchSpecificImports();
	
	protected final void writeImports() throws IOException{
		fetchImports();
		fetchSpecificImports();
		
		for (String javaImport:javaImports) {
			writeLine(javaImport);
		}
	}

	
	


	/**
	 * builds a null-safe getter expression for chained properties (e.g. city.getRegion().getCountry().getCode())
	 * @param rootObjectName root object name (e.g. "city")
	 * @param mappingPath dot-separated getter calls (e.g. "getRegion().getCountry().getCode()")
	 * @return null-safe Java expression
	 */
	protected String buildSafeGetterExpression(String rootObjectName, String mappingPath) {
		if (mappingPath == null || mappingPath.isEmpty()) {
			return rootObjectName;
		}
		String[] getters = mappingPath.split("\\.");
		if (getters.length <= 1) {
			return rootObjectName + "." + mappingPath;
		}

		StringBuilder condition = new StringBuilder();
		StringBuilder prefix = new StringBuilder(rootObjectName);

		for (int i = 0; i < getters.length - 1; i++) {
			prefix.append(".").append(getters[i]);
			if (i > 0) {
				condition.append(" && ");
			}
			condition.append(prefix).append(" != null");
		}

		String fullAccess = rootObjectName + "." + mappingPath;
		if (getters.length == 2) {
			return condition + " ? " + fullAccess + " : null";
		} else {
			return "(" + condition + ") ? " + fullAccess + " : null";
		}
	}
}