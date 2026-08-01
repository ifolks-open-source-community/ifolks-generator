package org.ifolks.generator.skeletons.commands.impl;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Iterator;

import org.ifolks.generator.model.domain.Project;
import org.ifolks.generator.skeletons.commands.impl.templatized.TemplatizedReourceCopier;
import org.ifolks.generator.skeletons.commands.interfaces.FileWriteCommand;

/**
 * An implementation of the file write command based on recursive directory copy of classpath resources.
 * During the copy, Velocity will perform variable resolution for .vm files.
 * @author Nicolas Thibault
 *
 */
public class DirectoryResourceCopyCommand implements FileWriteCommand {

	private Class<?> clazz;
	private String resourcesRoot;
	private String targetRootPath;
	private Project project;
	private TemplatizedReourceCopier copier;
	private static final String separator = "/";

	public DirectoryResourceCopyCommand(Project project, String resourcesRoot, String targetRootPath) {
		this(project, DirectoryResourceCopyCommand.class, resourcesRoot, targetRootPath);
	}

	public DirectoryResourceCopyCommand(Project project, Class<?> clazz, String resourcesRoot, String targetRootPath) {
		this.project = project;
		this.clazz = clazz != null ? clazz : DirectoryResourceCopyCommand.class;
		this.resourcesRoot = resourcesRoot;
		this.targetRootPath = targetRootPath;
		this.copier = new TemplatizedReourceCopier(project);
	}

	public static DirectoryResourceCopyCommand of(Project project, String resourcesRoot, String targetRootPath) {
		return new DirectoryResourceCopyCommand(project, resourcesRoot, targetRootPath);
	}

	public static DirectoryResourceCopyCommand of(Project project, Class<?> clazz, String resourcesRoot, String targetRootPath) {
		return new DirectoryResourceCopyCommand(project, clazz, resourcesRoot, targetRootPath);
	}

	@Override
	public String getLabel() {
		return "resources copied to " + targetRootPath;
	}

	@Override
	public void execute() throws IOException, URISyntaxException {

		URL url = clazz.getResource(resourcesRoot);
		if (url == null) {
			url = Thread.currentThread().getContextClassLoader().getResource(resourcesRoot.startsWith("/") ? resourcesRoot.substring(1) : resourcesRoot);
		}
		if (url == null) {
			throw new IOException("Resource root path not found on classpath: " + resourcesRoot);
		}

		Path targetPath = Paths.get(project.workspaceFolder + File.separator + targetRootPath);
		targetPath = Files.createDirectories(targetPath);

		if (url.getProtocol().equals("file")) {
			Path resourcesPath = Paths.get(url.toURI());
			if (Files.isDirectory(resourcesPath)) {
				copyRecursively(resourcesRoot, resourcesPath, targetPath);
			} else {
				copySingleFile(resourcesRoot, resourcesPath, targetPath);
			}
		} else {

			File jar;
			try {
				jar = new File(clazz.getProtectionDomain().getCodeSource().getLocation().toURI());
			} catch (URISyntaxException e) {
				throw new IOException("failed to find jar path", e);
			}

			try (FileSystem jarMount = FileSystems.newFileSystem(jar.toPath())) {
				Path resourcesPath = jarMount.getPath(resourcesRoot);
				if (Files.isDirectory(resourcesPath)) {
					copyRecursively(resourcesRoot, resourcesPath, targetPath);
				} else {
					copySingleFile(resourcesRoot, resourcesPath, targetPath);
				}
			}
		}
	}

	private void copySingleFile(String resourceLocation, Path resourcePath, Path targetPath) throws IOException {
		String fileName = resourcePath.getFileName().toString().replace(separator, "");
		String resolvedName = resolveName(fileName);
		String targetFileName = getTargetFileName(resolvedName);

		Path targetFile = targetPath;
		if (Files.isDirectory(targetPath) || !targetPath.toString().endsWith(targetFileName)) {
			targetFile = Paths.get(targetPath.toString(), targetFileName);
		}

		if (isTemplatized(fileName)) {
			copier.resolveAndCopy(resourceLocation, targetFile);
		} else {
			Files.copy(resourcePath, targetFile, StandardCopyOption.REPLACE_EXISTING);
		}
	}

	private void copyRecursively(String root, Path resourcesPath, Path targetPath) throws IOException {

		try (DirectoryStream<Path> stream = Files.newDirectoryStream(resourcesPath)) {
			Iterator<Path> iterator = stream.iterator();
			while (iterator.hasNext()) {
				Path childResourcesPath = iterator.next();
				String childResourceFileName = childResourcesPath.getFileName().toString().replace(separator, "");
				String childRelativeLocation = root + separator + childResourceFileName;

				String resolvedName = resolveName(childResourceFileName);
				String targetFileName = getTargetFileName(resolvedName);
				File childTargetFile = new File(targetPath.toFile().getPath() + File.separator + targetFileName);
				Path childTargetPath = childTargetFile.toPath();

				if (Files.isDirectory(childResourcesPath)) {
					childTargetPath = Files.createDirectories(childTargetPath);
					copyRecursively(childRelativeLocation, childResourcesPath, childTargetPath);
				} else {
					if (isTemplatized(childResourceFileName)) {
						copier.resolveAndCopy(childRelativeLocation, childTargetPath);
					} else {
						Files.copy(childResourcesPath, childTargetPath, StandardCopyOption.REPLACE_EXISTING);
					}
				}
			}
		}
	}

	private String resolveName(String name) {
		if (project != null) {
			if (project.domainName != null && (name.contains("$domainName") || name.contains("${domainName}"))) {
				name = name.replace("${domainName}", project.domainName).replace("$domainName", project.domainName);
			}
			if (project.projectName != null && (name.contains("$projectName") || name.contains("${projectName}"))) {
				name = name.replace("${projectName}", project.projectName).replace("$projectName", project.projectName);
			}
		}
		return name;
	}

	private boolean isTemplatized(String resourceFileName) {
		return resourceFileName.endsWith(".vm");
	}

	private String getTargetFileName(String resourceFileName) {

		if (resourceFileName.endsWith(".vm")) {
			return resourceFileName.substring(0, resourceFileName.length() - 3);
		}
		return resourceFileName;
	}

	@Override
	public int getRowCount() {
		return 0;
	}

}
