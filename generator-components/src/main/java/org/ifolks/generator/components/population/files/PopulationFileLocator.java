package org.ifolks.generator.components.population.files;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.ifolks.generator.model.domain.database.Table;
import org.ifolks.generator.model.metadata.PersistenceMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

@Component
public class PopulationFileLocator {

	@Autowired
	private ResourceLoader resourceLoader;

	public int resolveMaxStep(String rootPath) {
		if (rootPath.startsWith("classpath:")) {
			int maxStep = 0;
			while (resourceLoader.getResource(rootPath + "/" + (maxStep + 1)).exists()) {
				maxStep++;
			}
			return maxStep;
		} else {
			return org.ifolks.generator.model.util.folder.FolderUtil.resolveMaxStep(rootPath);
		}
	}

	public PersistenceMode resolvePersistenceModeOrNull(String backupPath, int step, Table table) {
		if (existsFileForType(backupPath, step, table, PersistenceMode.CMD)) {
			return PersistenceMode.CMD;
		} else if (existsFileForType(backupPath, step, table, PersistenceMode.XML)) {
			return PersistenceMode.XML;
		} else if (existsFileForType(backupPath, step, table, PersistenceMode.CSV)) {
			return PersistenceMode.CSV;
		} else {
			return null;
		}
	}

	public String getBackupFilePath(String backupPath, int step, Table table, PersistenceMode mode) {
		return getPathPrefix(backupPath, step, table) + mode.getExtension();
	}

	
	public boolean existsFileForTable(String backupPath, int maxStep, Table table) {
		for (int step = 1; step <= maxStep; step++) {
			for (PersistenceMode mode : PersistenceMode.values()) {
				if (existsFileForType(backupPath, step, table, mode)) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean existsFileForType(String backupPath, int step, Table table, PersistenceMode type) {

		String backupFilePath = getBackupFilePath(backupPath, step, table, type);
		
		if (backupFilePath.startsWith("classpath:")) {
			return resourceLoader.getResource(backupFilePath).exists();
		} else {
			Path path = Paths.get(backupFilePath);
			return Files.exists(path);
		}
	}

	private String getPathPrefix(String backupPath, int step, Table table) {
		if (backupPath.startsWith("classpath:")) {
			return backupPath + "/" + step + "/"
					+ table.myPackage.name.toUpperCase().replace(".", "/") + "/" + table.originalName;
		} else {
			return backupPath + File.separator + step + File.separator
					+ table.myPackage.name.toUpperCase().replace(".", File.separator) + File.separator + table.originalName;
		}
	}
}
