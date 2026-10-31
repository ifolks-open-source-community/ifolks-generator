package org.ifolks.generator.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

import org.ifolks.generator.model.domain.Project;
import org.ifolks.generator.model.domain.business.Bean;
import org.ifolks.generator.model.domain.business.Enumeration;
import org.ifolks.generator.model.domain.database.Table;
import org.ifolks.generator.model.metadata.ProjectMetaData;
import org.ifolks.generator.skeletons.core.commands.database.oracle.OracleTableDefinitionFileWriteCommand;
import org.ifolks.generator.skeletons.core.commands.database.postgresql.PostgresqlTableDefinitionFileWriteCommand;
import org.ifolks.generator.skeletons.core.commands.api.model.BasicViewBeanFileWriteCommand;
import org.ifolks.generator.skeletons.core.commands.api.model.FilterFileWriteCommand;
import org.ifolks.generator.skeletons.core.commands.api.model.FormBeanFileWriteCommand;
import org.ifolks.generator.skeletons.core.commands.model.EntityBeanFileWriteCommand;
import org.ifolks.generator.skeletons.core.commands.model.EntityMetaModelFileWriteCommand;
import org.ifolks.generator.skeletons.core.commands.model.EnumFileWriteCommand;
import org.ifolks.generator.skeletons.core.commands.persistence.SpecificationFileWriteCommand;
import org.ifolks.generator.skeletons.core.layers.BusinessModelLayer;
import org.ifolks.generator.skeletons.tree.FileWriteCommandTreeNode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig(ApplicationConfig.class)
public class EnumGenerationTest {

	@Autowired
	private ProjectMetaDataService service;

	@Autowired
	private ProjectLoader loader;

	@Test
	public void testBusinessModelLayerRegistersEnumerations() {
		ProjectMetaData projectMetaData = service.loadProjectMetaData("src/test/resources/projects/success/2");
		Project project = loader.loadProject(projectMetaData);

		BusinessModelLayer layer = new BusinessModelLayer();
		FileWriteCommandTreeNode rootNode = layer.getGenerationNode(project);
		Assertions.assertNotNull(rootNode);

		boolean foundEnumCommand = false;
		for (int i = 0; i < rootNode.getChildCount(); i++) {
			FileWriteCommandTreeNode packageNode = (FileWriteCommandTreeNode) rootNode.getChildAt(i);
			for (int j = 0; j < packageNode.getChildCount(); j++) {
				FileWriteCommandTreeNode sectionNode = (FileWriteCommandTreeNode) packageNode.getChildAt(j);
				org.ifolks.generator.skeletons.tree.FileWriteCommandExecutor sectionExecutor = 
						(org.ifolks.generator.skeletons.tree.FileWriteCommandExecutor) sectionNode.getUserObject();
				if ("Enumerations".equals(sectionExecutor.getLabel())) {
					for (int k = 0; k < sectionNode.getChildCount(); k++) {
						FileWriteCommandTreeNode leafNode = (FileWriteCommandTreeNode) sectionNode.getChildAt(k);
						org.ifolks.generator.skeletons.tree.FileWriteCommandExecutor leafExecutor = 
								(org.ifolks.generator.skeletons.tree.FileWriteCommandExecutor) leafNode.getUserObject();
						if (leafExecutor.getCommand() instanceof EnumFileWriteCommand) {
							foundEnumCommand = true;
						}
					}
				}
			}
		}
		Assertions.assertTrue(foundEnumCommand, "BusinessModelLayer should register EnumFileWriteCommand");
	}

	@Test
	public void testEnumFileWriteCommandExecution() throws IOException {
		Path tempDir = Files.createTempDirectory("ifolks-enum-test");
		try {
			ProjectMetaData projectMetaData = service.loadProjectMetaData("src/test/resources/projects/success/2");
			Project project = loader.loadProject(projectMetaData);
			project.workspaceFolder = tempDir.toAbsolutePath().toString();

			Enumeration roleEnum = project.model.findEnumeration("OrganizationRole");
			EnumFileWriteCommand writeCommand = new EnumFileWriteCommand(roleEnum);
			writeCommand.execute();

			File targetFile = new File(writeCommand.getLabel());
			Assertions.assertTrue(targetFile.exists(), "Enum file should be generated on disk");

			String content = Files.readString(targetFile.toPath());
			Assertions.assertTrue(content.contains("package org.ifolks.demo.model.organizations;"));
			Assertions.assertTrue(content.contains("public enum OrganizationRole {"));
			Assertions.assertTrue(content.contains("HEADQUARTERS,"));
			Assertions.assertTrue(content.contains("SUBSIDIARY,"));
			Assertions.assertTrue(content.contains("BRANCH;"));
			Assertions.assertTrue(content.contains("/* Specific Code Start */"));
			Assertions.assertTrue(content.contains("/* Specific Code End */"));
		} finally {
			Files.walk(tempDir)
					.sorted(Comparator.reverseOrder())
					.map(Path::toFile)
					.forEach(File::delete);
		}
	}

	@Test
	public void testEntityBeanWithEnumGeneration() throws IOException {
		Path tempDir = Files.createTempDirectory("ifolks-entity-enum-test");
		try {
			ProjectMetaData projectMetaData = service.loadProjectMetaData("src/test/resources/projects/success/2");
			Project project = loader.loadProject(projectMetaData);
			project.workspaceFolder = tempDir.toAbsolutePath().toString();

			Bean organizationBean = project.model.findBean("ORGANIZATION");
			EntityBeanFileWriteCommand beanCommand = new EntityBeanFileWriteCommand(organizationBean);
			beanCommand.execute();

			File targetFile = new File(beanCommand.getLabel());
			Assertions.assertTrue(targetFile.exists());

			String content = Files.readString(targetFile.toPath());
			Assertions.assertTrue(content.contains("import jakarta.persistence.Enumerated;"));
			Assertions.assertTrue(content.contains("import jakarta.persistence.EnumType;"));
			Assertions.assertTrue(content.contains("@Enumerated(EnumType.STRING)"));
			Assertions.assertTrue(content.contains("@Column(name = \"ROLE\", length = 32, nullable = false)"));
			Assertions.assertTrue(content.contains("private OrganizationRole role;"));

			// Metamodel
			EntityMetaModelFileWriteCommand metaModelCommand = new EntityMetaModelFileWriteCommand(organizationBean);
			metaModelCommand.execute();
			File metaModelFile = new File(metaModelCommand.getLabel());
			Assertions.assertTrue(metaModelFile.exists());
			String metaContent = Files.readString(metaModelFile.toPath());
			Assertions.assertTrue(metaContent.contains("public static volatile SingularAttribute<Organization, OrganizationRole> role;"));
		} finally {
			Files.walk(tempDir)
					.sorted(Comparator.reverseOrder())
					.map(Path::toFile)
					.forEach(File::delete);
		}
	}

	@Test
	public void testTableDefinitionWithEnumSqlGeneration() throws IOException {
		Path tempDir = Files.createTempDirectory("ifolks-sql-enum-test");
		try {
			ProjectMetaData projectMetaData = service.loadProjectMetaData("src/test/resources/projects/success/2");
			Project project = loader.loadProject(projectMetaData);
			project.workspaceFolder = tempDir.toAbsolutePath().toString();

			Table organizationTable = project.model.findTable("ORGANIZATION");
			Table foolTable = project.model.findTable("FOOL");

			// PostgreSQL
			PostgresqlTableDefinitionFileWriteCommand pgOrgCommand = new PostgresqlTableDefinitionFileWriteCommand(organizationTable);
			pgOrgCommand.execute();
			String pgOrgContent = Files.readString(new File(pgOrgCommand.getLabel()).toPath());
			Assertions.assertTrue(pgOrgContent.contains("ROLE VARCHAR(32) NOT NULL"));

			PostgresqlTableDefinitionFileWriteCommand pgFoolCommand = new PostgresqlTableDefinitionFileWriteCommand(foolTable);
			pgFoolCommand.execute();
			String pgFoolContent = Files.readString(new File(pgFoolCommand.getLabel()).toPath());
			Assertions.assertTrue(pgFoolContent.contains("STATUS VARCHAR(32) NULL"));

			// Oracle
			OracleTableDefinitionFileWriteCommand oraOrgCommand = new OracleTableDefinitionFileWriteCommand(organizationTable);
			oraOrgCommand.execute();
			String oraOrgContent = Files.readString(new File(oraOrgCommand.getLabel()).toPath());
			Assertions.assertTrue(oraOrgContent.contains("ROLE VARCHAR2(32) NOT NULL"));

			OracleTableDefinitionFileWriteCommand oraFoolCommand = new OracleTableDefinitionFileWriteCommand(foolTable);
			oraFoolCommand.execute();
			String oraFoolContent = Files.readString(new File(oraFoolCommand.getLabel()).toPath());
			Assertions.assertTrue(oraFoolContent.contains("STATUS VARCHAR2(32) NULL"));
		} finally {
			Files.walk(tempDir)
					.sorted(Comparator.reverseOrder())
					.map(Path::toFile)
					.forEach(File::delete);
		}
	}

	@Test
	public void testSpecificationWithEnumGeneration() throws IOException {
		Path tempDir = Files.createTempDirectory("ifolks-spec-enum-test");
		try {
			ProjectMetaData projectMetaData = service.loadProjectMetaData("src/test/resources/projects/success/2");
			Project project = loader.loadProject(projectMetaData);
			project.workspaceFolder = tempDir.toAbsolutePath().toString();

			Bean organizationBean = project.model.findBean("ORGANIZATION");
			SpecificationFileWriteCommand specCommand = new SpecificationFileWriteCommand(organizationBean);
			specCommand.execute();

			File targetFile = new File(specCommand.getLabel());
			Assertions.assertTrue(targetFile.exists());

			String content = Files.readString(targetFile.toPath());
			Assertions.assertTrue(content.contains("import org.ifolks.demo.model.organizations.OrganizationRole;"));
			Assertions.assertTrue(content.contains("addEqualsIfNotNullRestriction(cb, predicates, root.get(Organization_.role), filter.getRole());"));
		} finally {
			Files.walk(tempDir)
					.sorted(Comparator.reverseOrder())
					.map(Path::toFile)
					.forEach(File::delete);
		}
	}

	@Test
	public void testDemoBusinessModelLayerGenerationAndCompilation() throws IOException, InterruptedException {
		File demoDir = new File("../../ifolks-demo");
		if (!demoDir.exists()) {
			demoDir = new File("c:/Users/nicol/dev/sources/ifolks-open-source/ifolks-demo");
		}
		if (!demoDir.exists()) {
			return;
		}

		ProjectMetaData projectMetaData = service.loadProjectMetaData(demoDir.getAbsolutePath());
		Project project = loader.loadProject(projectMetaData);
		project.workspaceFolder = demoDir.getAbsolutePath();

		BusinessModelLayer layer = new BusinessModelLayer();
		FileWriteCommandTreeNode root = layer.getGenerationNode(project);
		root.execute();

		// Check OrganizationRole.java was generated
		File orgRoleFile = new File(demoDir, "demo-model/src/main/java/org/ifolks/demo/model/organizations/OrganizationRole.java");
		Assertions.assertTrue(orgRoleFile.exists(), "OrganizationRole.java must exist");

		// Check FoolStatus.java was generated
		File foolStatusFile = new File(demoDir, "demo-model/src/main/java/org/ifolks/demo/model/dummy/FoolStatus.java");
		Assertions.assertTrue(foolStatusFile.exists(), "FoolStatus.java must exist");

		// Check Organization.java has @Enumerated
		File orgEntityFile = new File(demoDir, "demo-model/src/main/java/org/ifolks/demo/model/organizations/Organization.java");
		String orgEntityContent = Files.readString(orgEntityFile.toPath());
		Assertions.assertTrue(orgEntityContent.contains("@Enumerated(EnumType.STRING)"));
		Assertions.assertTrue(orgEntityContent.contains("private OrganizationRole role;"));

		// Check Fool.java has @Enumerated
		File foolEntityFile = new File(demoDir, "demo-model/src/main/java/org/ifolks/demo/model/dummy/Fool.java");
		String foolEntityContent = Files.readString(foolEntityFile.toPath());
		Assertions.assertTrue(foolEntityContent.contains("@Enumerated(EnumType.STRING)"));
		Assertions.assertTrue(foolEntityContent.contains("private FoolStatus status;"));

		// Verify maven compilation of demo-model
		String mvnCmd = System.getProperty("os.name").toLowerCase().contains("win") ? "mvn.cmd" : "mvn";
		ProcessBuilder pb = new ProcessBuilder(mvnCmd, "compile");
		pb.directory(new File(demoDir, "demo-model"));
		pb.redirectErrorStream(true);
		Process process = pb.start();
		String output = new String(process.getInputStream().readAllBytes());
		int exitCode = process.waitFor();
		Assertions.assertEquals(0, exitCode, "demo-model compilation failed:\n" + output);
	}

	@Test
	public void testDtoWithEnumGeneration() throws IOException {
		Path tempDir = Files.createTempDirectory("ifolks-dto-enum-test");
		try {
			ProjectMetaData projectMetaData = service.loadProjectMetaData("src/test/resources/projects/success/2");
			Project project = loader.loadProject(projectMetaData);
			project.workspaceFolder = tempDir.toAbsolutePath().toString();

			Bean organizationBean = project.model.findBean("ORGANIZATION");

			// BasicView
			BasicViewBeanFileWriteCommand basicViewCmd = new BasicViewBeanFileWriteCommand(organizationBean);
			basicViewCmd.execute();
			String basicViewContent = Files.readString(new File(basicViewCmd.getLabel()).toPath());
			Assertions.assertTrue(basicViewContent.contains("import org.ifolks.demo.model.organizations.OrganizationRole;"));
			Assertions.assertTrue(basicViewContent.contains("OrganizationRole role"));

			// Form
			FormBeanFileWriteCommand formCmd = new FormBeanFileWriteCommand(organizationBean);
			formCmd.execute();
			String formContent = Files.readString(new File(formCmd.getLabel()).toPath());
			Assertions.assertTrue(formContent.contains("import org.ifolks.demo.model.organizations.OrganizationRole;"));
			Assertions.assertTrue(formContent.contains("@NotNull OrganizationRole role"));

			// Filter
			FilterFileWriteCommand filterCmd = new FilterFileWriteCommand(organizationBean);
			filterCmd.execute();
			String filterContent = Files.readString(new File(filterCmd.getLabel()).toPath());
			Assertions.assertTrue(filterContent.contains("import org.ifolks.demo.model.organizations.OrganizationRole;"));
			Assertions.assertTrue(filterContent.contains("private OrganizationRole role;"));
			Assertions.assertTrue(filterContent.contains("public OrganizationRole getRole() {"));
			Assertions.assertTrue(filterContent.contains("public void setRole(OrganizationRole role) {"));
		} finally {
			Files.walk(tempDir)
					.sorted(Comparator.reverseOrder())
					.map(Path::toFile)
					.forEach(File::delete);
		}
	}
}
