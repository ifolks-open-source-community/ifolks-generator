package org.ifolks.generator.components.population.files;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * simply converts a sript file to its content as a String
 * @author Nicolas Thibault
 *
 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

@Component
public class SimpleScriptFileReader {

	@Autowired
	private ResourceLoader resourceLoader;

	public String readScript(String scriptFilePath, Charset charset) throws IOException {
		if (scriptFilePath.startsWith("classpath:")) {
			return StreamUtils.copyToString(resourceLoader.getResource(scriptFilePath).getInputStream(), charset);
		} else {
			Path path  = Paths.get(scriptFilePath);
			byte[] bytes = Files.readAllBytes(path);
			return new String(bytes,charset);
		}
	}
	
	public String readScript(String scriptFilePath) throws IOException {
		return readScript(scriptFilePath, StandardCharsets.UTF_8);
	}
}
