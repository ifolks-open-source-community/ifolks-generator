package org.ifolks.generator.components.build.prompt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BuildPrompter {
	
	private static final Logger logger = LoggerFactory.getLogger(BuildPrompter.class);
	
	public static void promptForConfirmation() throws IOException {
		System.out.println("Do you wish to rebuild your db? [Y/n]");
		BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
		String input = bufferRead.readLine();
		
		if(!("Y".equals(input)||input.isEmpty())){
			logger.warn("Aborting ...");
			System.exit(0);
		}
	}
}
