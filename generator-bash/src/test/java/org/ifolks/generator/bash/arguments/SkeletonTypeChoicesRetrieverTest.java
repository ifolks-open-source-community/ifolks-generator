package org.ifolks.generator.bash.arguments;

import org.junit.jupiter.api.Test;

public class SkeletonTypeChoicesRetrieverTest {

	@Test
	public void test() {
		System.out.println(new TrueFalseChoicesHelper("Please choose amongst").getFullMessage());
	}
}

