package org.ifolks.generator.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={ApplicationConfig.class})
public class SetupTest {

	/*
	 * logger
	 */
	private final Logger logger = LoggerFactory.getLogger(SetupTest.class);
	
	
	@Test
	public void testSetup() {
		logger.info("Your test environment is ready to use !");			
	}
}
