package org.ifolks.generator.bash.launcher;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Launches help command<br>
 * reads a file where the text will be displayed
 * @author Nicolas Thibault
 *
 */
public class HelpLauncher {
	
	/*
	 * logger
	 */
	private static final Logger logger = LoggerFactory.getLogger(HelpLauncher.class);
	
	public static void main(String[] args) {
		String version = getGeneratorVersion();
		String content = getHelpContent();
		content = content.replace("{0}", version);
		System.out.println(content);
	}

	public static String getGeneratorVersion() {
		try (InputStream stream = HelpLauncher.class.getResourceAsStream("/META-INF/maven/org.ifolks.generator/generator-bash/pom.properties")) {
			if (stream != null) {
				Properties props = new Properties();
				props.load(stream);
				String version = props.getProperty("version");
				if (version != null && !version.isBlank()) {
					return version;
				}
			}
		} catch (Exception e) {
			logger.debug("Could not read version from pom.properties", e);
		}

		String pkgVersion = HelpLauncher.class.getPackage().getImplementationVersion();
		if (pkgVersion != null && !pkgVersion.isBlank()) {
			return pkgVersion;
		}

		return "1.1.0";
	}

	private static String getHelpContent() {
		try (InputStream stream = HelpLauncher.class.getResourceAsStream("HelpContent.txt")) {
			return IOUtils.toString(stream, StandardCharsets.UTF_8);
		} catch (Exception e) {
			logger.error("failed to get help content : " + e.getMessage(), e);
			return "";
		}
	}
}
