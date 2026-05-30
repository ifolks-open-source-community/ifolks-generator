package org.ifolks.generator.util;

import org.ifolks.generator.model.util.naming.JavaClassNaming;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class JavaClassNamingTest {

	@Test
	public void testClassToDatabaseName() {
		String arg = "MyDummyClass";
		String expected = "MY_DUMMY_CLASS";
		String result = JavaClassNaming.toDatabaseName(arg);
		System.out.println(result);
		Assertions.assertEquals(expected, result);
	}
	
	@Test
	public void testObjectToDatabaseName() {
		String arg = "myDummyObject";
		String expected = "MY_DUMMY_OBJECT";
		String result = JavaClassNaming.toDatabaseName(arg);
		System.out.println(result);
		Assertions.assertEquals(expected, result);
	}
}

