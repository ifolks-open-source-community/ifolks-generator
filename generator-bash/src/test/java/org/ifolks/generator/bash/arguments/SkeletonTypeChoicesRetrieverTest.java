package org.ifolks.generator.bash.arguments;

import org.ifolks.generator.bash.arguments.TrueFalseChoicesHelper;
import org.junit.Test;

public class SkeletonTypeChoicesRetrieverTest {

	@Test
	public void test() {
		System.out.println(new TrueFalseChoicesHelper("Please choose amongst").getFullMessage());
	}
}
