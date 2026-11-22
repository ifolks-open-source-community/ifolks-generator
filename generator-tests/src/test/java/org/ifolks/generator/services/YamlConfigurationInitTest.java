package org.ifolks.generator.services;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.ifolks.generator.model.domain.Project;
import org.ifolks.generator.model.metadata.ProjectMetaData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

/**
 * GENERATOR-55 : verify YAML configuration and logback-spring.xml initialization across generated modules
 */
@SpringJUnitConfig(ApplicationConfig.class)
public class YamlConfigurationInitTest {

	@Autowired
	private ProjectMetaDataService service;

	@Autowired
	private ProjectLoader loader;

	@Autowired
	private CodeGenerator codeGenerator;

	@Test
	public void testInitGeneratesModularYamlConfigurations(@TempDir Path tempDir) throws IOException {
		ProjectMetaData projectMetaData = service.loadProjectMetaData("src/test/resources/projects/success/3");
		Project project = loader.loadProject(projectMetaData);
		project.workspaceFolder = tempDir.toString();
		project.dataSource = new org.ifolks.generator.model.metadata.datasources.DataSourceMetaData();
		project.dataSource.setDriverClassName("org.postgresql.Driver");
		project.dataSource.setUrl("jdbc:postgresql://localhost:5432/demo");
		project.dataSource.setUserName("postgres");
		project.dataSource.setPassword("postgres");
		project.dataSource.setDialect("org.hibernate.dialect.PostgreSQLDialect");

		codeGenerator.initProject(project);

		String prefix = project.projectName;

		// 1. Components module
		Path componentsRes = tempDir.resolve(prefix + "-components/src/main/resources");
		assertTrue(Files.exists(componentsRes.resolve("components.yml")), "components.yml must be initialized");
		assertTrue(Files.exists(componentsRes.resolve("components-local.yml")), "components-local.yml must be initialized");
		String componentsLocal = Files.readString(componentsRes.resolve("components-local.yml"));
		assertTrue(componentsLocal.contains("traceSentBody: true"));
		assertTrue(componentsLocal.contains("traceReceivedBody: true"));
		assertTrue(componentsLocal.contains("printErrorStack: true"));

		// 2. Persistence module
		Path persistenceRes = tempDir.resolve(prefix + "-persistence/src/main/resources");
		assertTrue(Files.exists(persistenceRes.resolve("persistence.yml")), "persistence.yml must be initialized");
		assertTrue(Files.exists(persistenceRes.resolve("persistence-local.yml")), "persistence-local.yml must be initialized");
		String persistenceLocal = Files.readString(persistenceRes.resolve("persistence-local.yml"));
		assertTrue(persistenceLocal.contains("driverClassName:"));
		assertTrue(persistenceLocal.contains("hibernate:"));
		assertFalse(persistenceLocal.contains("${project."), "Velocity placeholders must be resolved");

		// 3. REST module
		Path restRes = tempDir.resolve(prefix + "-rest/src/main/resources");
		assertTrue(Files.exists(restRes.resolve("application.yml")), "rest application.yml must be initialized");
		assertTrue(Files.exists(restRes.resolve("application-local.yml")), "rest application-local.yml must be initialized");
		assertTrue(Files.exists(restRes.resolve("logback-spring.xml")), "rest logback-spring.xml must be initialized");
		assertFalse(Files.exists(restRes.resolve("application.properties")), "rest application.properties must be removed");
		assertFalse(Files.exists(restRes.resolve("application-local.properties")), "rest application-local.properties must be removed");

		String restApp = Files.readString(restRes.resolve("application.yml"));
		assertTrue(restApp.contains("classpath:components.yml"));
		assertTrue(restApp.contains("classpath:persistence.yml"));
		assertTrue(restApp.contains("context-path: /" + prefix + "-rest"));

		String restLocal = Files.readString(restRes.resolve("application-local.yml"));
		assertTrue(restLocal.contains("traceRequestBody: true"));
		assertTrue(restLocal.contains("traceResponseBody: true"));
		assertFalse(restLocal.contains("driverClassName"), "DB config must live in persistence-local.yml, not rest");

		// 4. Populator module
		Path populatorRes = tempDir.resolve(prefix + "-populator/src/main/resources");
		assertTrue(Files.exists(populatorRes.resolve("application.yml")), "populator application.yml must be initialized");
		assertTrue(Files.exists(populatorRes.resolve("application-local.yml")), "populator application-local.yml must be initialized");
		assertTrue(Files.exists(populatorRes.resolve("logback-spring.xml")), "populator logback-spring.xml must be initialized");
		assertFalse(Files.exists(populatorRes.resolve("application-local.properties")), "populator application-local.properties must be removed");

		String populatorApp = Files.readString(populatorRes.resolve("application.yml"));
		assertTrue(populatorApp.contains("classpath:components.yml"));
		assertTrue(populatorApp.contains("classpath:persistence.yml"));

		String populatorLocal = Files.readString(populatorRes.resolve("application-local.yml"));
		assertTrue(populatorLocal.contains("engine: " + project.databaseEngine));
		assertFalse(populatorLocal.contains("driverClassName"), "DB connection config must live in persistence-local.yml, not populator");

		// 5. Tests module
		Path testsRes = tempDir.resolve(prefix + "-tests/src/main/resources");
		assertTrue(Files.exists(testsRes.resolve("application.yml")), "tests application.yml must be initialized");
		assertTrue(Files.exists(testsRes.resolve("application-junit.yml")), "tests application-junit.yml must be initialized");
		assertTrue(Files.exists(testsRes.resolve("logback-spring.xml")), "tests logback-spring.xml must be initialized");
		assertFalse(Files.exists(testsRes.resolve("application.properties")), "tests application.properties must be removed");
		assertFalse(Files.exists(testsRes.resolve("logback.xml")), "tests logback.xml must be replaced by logback-spring.xml");

		String testsApp = Files.readString(testsRes.resolve("application.yml"));
		assertTrue(testsApp.contains("active: junit"));
		assertTrue(testsApp.contains("classpath:components.yml"));

		Path testsAppConfig = tempDir.resolve(prefix + "-tests/src/main/java/"
				+ project.model.junitPackageName.replace('.', '/') + "/ApplicationConfig.java");
		String testsAppConfigContent = Files.readString(testsAppConfig);
		assertTrue(testsAppConfigContent.contains("@SpringBootConfiguration"));
		assertFalse(testsAppConfigContent.contains("@PropertySource"));

		Path setupTest = tempDir.resolve(prefix + "-tests/src/test/java/"
				+ project.model.junitPackageName.replace('.', '/') + "/SetupTest.java");
		String setupTestContent = Files.readString(setupTest);
		assertTrue(setupTestContent.contains("@SpringBootTest(classes = ApplicationConfig.class)"));
	}
}
