package org.ifolks.generator.skeletons.commands.impl.typed;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.ifolks.generator.model.metadata.files.FileType;
import org.ifolks.generator.skeletons.commands.impl.SingleFileWriteCommand;

/**
 * Extension of a generic file write command to handle specificities of TypeScript files
 * <br/>Handles conservation and deduplication of imports
 * @author Nicolas Thibault
 *
 */
public abstract class TsFileWriteCommand extends SingleFileWriteCommand {

	private static final Pattern NAMED_IMPORT_PATTERN = Pattern.compile(
			"^import\\s+(?:type\\s+)?\\{\\s*([^}]+)\\s*\\}\\s+from\\s+['\"]([^'\"]+)['\"]\\s*;?$",
			Pattern.DOTALL);

	private static final Pattern MODULE_FROM_PATTERN = Pattern.compile("from\\s+['\"]([^'\"]+)['\"]");
	private static final Pattern SIDE_EFFECT_PATTERN = Pattern.compile("^import\\s+['\"]([^'\"]+)['\"]\\s*;?$");

	/*
	 * properties
	 */
	protected Set<String> imports;

	/*
	 * constructor
	 */
	public TsFileWriteCommand(String folderName, String fileName) {
		super(folderName, fileName, FileType.TS);
		this.imports = new TreeSet<String>();
	}

	/**
	 * adds a named import with multiple symbols for a module
	 * @param modulePath module path (e.g. "@angular/core")
	 * @param symbols symbols to import (e.g. "Component", "OnInit")
	 */
	public void addImport(String modulePath, String... symbols) {
		if (modulePath != null && symbols != null && symbols.length > 0) {
			imports.add("import { " + String.join(", ", symbols) + " } from '" + modulePath + "';");
		}
	}

	private boolean isImportComplete(String str) {
		if (str == null) {
			return false;
		}
		String trimmed = str.trim();
		if (trimmed.contains("{") && !trimmed.contains("}")) {
			return false;
		}
		return trimmed.endsWith(";") || trimmed.matches(".*['\"][^'\"]+['\"]\\s*;?$");
	}

	private void fetchImports() throws IOException {
		if (Files.exists(filePath)) {
			List<String> lines = Files.readAllLines(filePath, fileType.getEncoding());
			StringBuilder currentImport = null;

			for (String line : lines) {
				String trimmed = line.trim();
				if (currentImport != null) {
					currentImport.append(" ").append(trimmed);
					if (isImportComplete(currentImport.toString())) {
						imports.add(currentImport.toString());
						currentImport = null;
					}
				} else if (trimmed.startsWith("import ") || trimmed.startsWith("import{")) {
					if (isImportComplete(trimmed)) {
						imports.add(trimmed);
					} else {
						currentImport = new StringBuilder(trimmed);
					}
				} else if (!trimmed.isEmpty() && !trimmed.startsWith("//") && !trimmed.startsWith("/*") && !trimmed.startsWith("*")) {
					// Reached first code line that is not an import, comment, or blank line
					break;
				}
			}
			if (currentImport != null) {
				imports.add(currentImport.toString());
			}
		}
	}

	protected abstract void fetchSpecificImports();

	/**
	 * Organizes, merges, and deduplicates TypeScript import statements.
	 * Named imports from the same module are combined with sorted symbols and single quotes.
	 *
	 * @param rawImports set of raw import lines
	 * @return sorted list of canonical import statements
	 */
	public List<String> organizeImports(Set<String> rawImports) {
		Map<String, SortedSet<String>> namedImports = new TreeMap<>();
		Set<String> otherImports = new LinkedHashSet<>();

		for (String raw : rawImports) {
			if (raw == null || raw.trim().isEmpty()) {
				continue;
			}
			String cleaned = raw.replaceAll("\\s+", " ").trim();
			Matcher m = NAMED_IMPORT_PATTERN.matcher(cleaned);
			if (m.matches()) {
				String symbolsStr = m.group(1);
				String modulePath = m.group(2);
				SortedSet<String> symbols = namedImports.computeIfAbsent(modulePath, k -> new TreeSet<>());
				for (String sym : symbolsStr.split(",")) {
					String trimmedSym = sym.trim();
					if (!trimmedSym.isEmpty()) {
						symbols.add(trimmedSym);
					}
				}
			} else {
				// Normalize quotes to single quotes and ensure semicolon
				String normalized = cleaned.replaceAll("\"([^\"]+)\"", "'$1'");
				if (!normalized.endsWith(";")) {
					normalized += ";";
				}
				otherImports.add(normalized);
			}
		}

		List<String> result = new ArrayList<>();
		for (Map.Entry<String, SortedSet<String>> entry : namedImports.entrySet()) {
			result.add("import { " + String.join(", ", entry.getValue()) + " } from '" + entry.getKey() + "';");
		}
		result.addAll(otherImports);

		// Sort all imports deterministically by their module path
		Collections.sort(result, new Comparator<String>() {
			@Override
			public int compare(String a, String b) {
				String moduleA = extractModulePath(a);
				String moduleB = extractModulePath(b);
				int modCmp = moduleA.compareTo(moduleB);
				return modCmp != 0 ? modCmp : a.compareTo(b);
			}
		});

		return result;
	}

	private String extractModulePath(String importStatement) {
		Matcher m = MODULE_FROM_PATTERN.matcher(importStatement);
		if (m.find()) {
			return m.group(1);
		}
		Matcher mSide = SIDE_EFFECT_PATTERN.matcher(importStatement);
		if (mSide.find()) {
			return mSide.group(1);
		}
		return importStatement;
	}

	/**
	 * determines whether existing imports should be preserved from disk.
	 * Only files that support user-defined specific code blocks preserve imports across generations.
	 * Base classes and other 100% generated classes return false.
	 * 
	 * @return true if existing imports should be read from disk
	 */
	protected boolean shouldFetchExistingImports() {
		return supportsSpecificCode();
	}

	protected final void writeImports() throws IOException {
		if (shouldFetchExistingImports()) {
			fetchImports();
		}
		fetchSpecificImports();

		List<String> organized = organizeImports(imports);
		for (String imported : organized) {
			writeLine(imported);
		}
	}
}
