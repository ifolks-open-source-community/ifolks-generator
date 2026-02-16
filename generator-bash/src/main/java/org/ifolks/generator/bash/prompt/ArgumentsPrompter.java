package org.ifolks.generator.bash.prompt;

import java.util.List;

/**
 * When using a IGEN command, you can be prompted some questions
 * A prompter will collect your inputs
 * @author Nicolas Thibault
 *
 */
public interface ArgumentsPrompter {	

	List<String> promptForArguments();
}
