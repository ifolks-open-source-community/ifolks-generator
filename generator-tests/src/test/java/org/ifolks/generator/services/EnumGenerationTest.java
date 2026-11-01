package org.ifolks.generator.services;

import java.io.IOException;

import org.ifolks.generator.model.domain.Project;
import org.ifolks.generator.model.domain.business.Bean;
import org.ifolks.generator.model.domain.business.Enumeration;
import org.ifolks.generator.model.domain.database.Table;
import org.ifolks.generator.model.metadata.ProjectMetaData;
import org.ifolks.generator.skeletons.core.commands.api.interfaces.BaseServiceInterfaceFileWriteCommand;
import org.ifolks.generator.skeletons.core.commands.api.model.BasicViewBeanFileWriteCommand;
import org.ifolks.generator.skeletons.core.commands.api.model.FilterFileWriteCommand;
import org.ifolks.generator.skeletons.core.commands.api.model.FormBeanFileWriteCommand;
import org.ifolks.generator.skeletons.core.commands.components.mapper.BaseBasicViewMapperFileWriteCommand;
import org.ifolks.generator.skeletons.core.commands.components.mapper.BaseFormMapperFileWriteCommand;
import org.ifolks.generator.skeletons.core.commands.database.oracle.OracleTableDefinitionFileWriteCommand;
import org.ifolks.generator.skeletons.core.commands.database.postgresql.PostgresqlTableDefinitionFileWriteCommand;
import org.ifolks.generator.skeletons.core.commands.model.EntityBeanFileWriteCommand;
import org.ifolks.generator.skeletons.core.commands.model.EntityMetaModelFileWriteCommand;
import org.ifolks.generator.skeletons.core.commands.model.EnumFileWriteCommand;
import org.ifolks.generator.skeletons.core.commands.persistence.BaseRepositoryInterfaceFileWriteCommand;
import org.ifolks.generator.skeletons.core.commands.persistence.SpecificationFileWriteCommand;
import org.ifolks.generator.skeletons.core.commands.services.BaseServiceImplFileWriteCommand;
import org.ifolks.generator.skeletons.core.layers.ApiLayer;
import org.ifolks.generator.skeletons.rest.commands.SpringRestBaseControllerCommand;
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
	public void testApiLayerRegistersEnumerations() {
		ProjectMetaData projectMetaData = service.loadProjectMetaData("src/test/resources/projects/success/2");
		Project project = loader.loadProject(projectMetaData);

		ApiLayer layer = new ApiLayer();
		FileWriteCommandTreeNode rootNode = layer.getGenerationNode(project);
		Assertions.assertNotNull(rootNode);

		boolean foundEnumCommand = false;
		for (int i = 0; i < rootNode.getChildCount(); i++) {
			FileWriteCommandTreeNode firstLevelNode = (FileWriteCommandTreeNode) rootNode.getChildAt(i);
			org.ifolks.generator.skeletons.tree.FileWriteCommandExecutor firstExecutor = 
					(org.ifolks.generator.skeletons.tree.FileWriteCommandExecutor) firstLevelNode.getUserObject();
			if ("Model".equals(firstExecutor.getLabel())) {
				for (int j = 0; j < firstLevelNode.getChildCount(); j++) {
					FileWriteCommandTreeNode packageNode = (FileWriteCommandTreeNode) firstLevelNode.getChildAt(j);
					for (int k = 0; k < packageNode.getChildCount(); k++) {
						FileWriteCommandTreeNode sectionNode = (FileWriteCommandTreeNode) packageNode.getChildAt(k);
						org.ifolks.generator.skeletons.tree.FileWriteCommandExecutor sectionExecutor = 
								(org.ifolks.generator.skeletons.tree.FileWriteCommandExecutor) sectionNode.getUserObject();
						if ("Enumerations".equals(sectionExecutor.getLabel())) {
							for (int l = 0; l < sectionNode.getChildCount(); l++) {
								FileWriteCommandTreeNode leafNode = (FileWriteCommandTreeNode) sectionNode.getChildAt(l);
								org.ifolks.generator.skeletons.tree.FileWriteCommandExecutor leafExecutor = 
										(org.ifolks.generator.skeletons.tree.FileWriteCommandExecutor) leafNode.getUserObject();
								if (leafExecutor.getCommand() instanceof EnumFileWriteCommand) {
									foundEnumCommand = true;
								}
							}
						}
					}
				}
			}
		}
		Assertions.assertTrue(foundEnumCommand, "ApiLayer should register EnumFileWriteCommand under Model -> <Package> -> Enumerations");
	}

	@Test
	public void testEnumFileWriteCommandExecution() throws IOException {
		ProjectMetaData projectMetaData = service.loadProjectMetaData("src/test/resources/projects/success/2");
		Project project = loader.loadProject(projectMetaData);

		Enumeration roleEnum = project.model.findEnumeration("OrganizationRole");
		EnumFileWriteCommand writeCommand = new EnumFileWriteCommand(roleEnum);
		String content = writeCommand.generateContent();

		Assertions.assertTrue(content.contains("package org.ifolks.demo.api.model.organizations.enums;"));
		Assertions.assertTrue(content.contains("public enum OrganizationRole {"));
		Assertions.assertTrue(content.contains("HEADQUARTERS,"));
		Assertions.assertTrue(content.contains("SUBSIDIARY,"));
		Assertions.assertTrue(content.contains("BRANCH;"));
		Assertions.assertTrue(content.contains("/* Specific Code Start */"));
		Assertions.assertTrue(content.contains("/* Specific Code End */"));
	}

	@Test
	public void testEntityBeanWithEnumGeneration() throws IOException {
		ProjectMetaData projectMetaData = service.loadProjectMetaData("src/test/resources/projects/success/2");
		Project project = loader.loadProject(projectMetaData);

		Bean organizationBean = project.model.findBean("ORGANIZATION");
		EntityBeanFileWriteCommand beanCommand = new EntityBeanFileWriteCommand(organizationBean);
		String content = beanCommand.generateContent();

		Assertions.assertTrue(content.contains("import org.ifolks.demo.api.model.organizations.enums.OrganizationRole;"));
		Assertions.assertTrue(content.contains("import jakarta.persistence.Enumerated;"));
		Assertions.assertTrue(content.contains("import jakarta.persistence.EnumType;"));
		Assertions.assertTrue(content.contains("@Enumerated(EnumType.STRING)"));
		Assertions.assertTrue(content.contains("@Column(name = \"ROLE\", length = 32, nullable = false)"));
		Assertions.assertTrue(content.contains("private OrganizationRole role;"));

		// Metamodel
		EntityMetaModelFileWriteCommand metaModelCommand = new EntityMetaModelFileWriteCommand(organizationBean);
		String metaContent = metaModelCommand.generateContent();
		Assertions.assertTrue(metaContent.contains("import org.ifolks.demo.api.model.organizations.enums.OrganizationRole;"));
		Assertions.assertTrue(metaContent.contains("public static volatile SingularAttribute<Organization, OrganizationRole> role;"));
	}

	@Test
	public void testTableDefinitionWithEnumSqlGeneration() throws IOException {
		ProjectMetaData projectMetaData = service.loadProjectMetaData("src/test/resources/projects/success/2");
		Project project = loader.loadProject(projectMetaData);

		Table organizationTable = project.model.findTable("ORGANIZATION");
		Table foolTable = project.model.findTable("FOOL");

		// PostgreSQL
		PostgresqlTableDefinitionFileWriteCommand pgOrgCommand = new PostgresqlTableDefinitionFileWriteCommand(organizationTable);
		String pgOrgContent = pgOrgCommand.generateContent();
		Assertions.assertTrue(pgOrgContent.contains("ROLE VARCHAR(32) NOT NULL"));

		PostgresqlTableDefinitionFileWriteCommand pgFoolCommand = new PostgresqlTableDefinitionFileWriteCommand(foolTable);
		String pgFoolContent = pgFoolCommand.generateContent();
		Assertions.assertTrue(pgFoolContent.contains("STATUS VARCHAR(32) NULL"));

		// Oracle
		OracleTableDefinitionFileWriteCommand oraOrgCommand = new OracleTableDefinitionFileWriteCommand(organizationTable);
		String oraOrgContent = oraOrgCommand.generateContent();
		Assertions.assertTrue(oraOrgContent.contains("ROLE VARCHAR2(32) NOT NULL"));

		OracleTableDefinitionFileWriteCommand oraFoolCommand = new OracleTableDefinitionFileWriteCommand(foolTable);
		String oraFoolContent = oraFoolCommand.generateContent();
		Assertions.assertTrue(oraFoolContent.contains("STATUS VARCHAR2(32) NULL"));
	}

	@Test
	public void testSpecificationWithEnumGeneration() throws IOException {
		ProjectMetaData projectMetaData = service.loadProjectMetaData("src/test/resources/projects/success/2");
		Project project = loader.loadProject(projectMetaData);

		Bean organizationBean = project.model.findBean("ORGANIZATION");
		SpecificationFileWriteCommand specCommand = new SpecificationFileWriteCommand(organizationBean);
		String content = specCommand.generateContent();

		Assertions.assertTrue(content.contains("import org.ifolks.demo.api.model.organizations.enums.OrganizationRole;"));
		Assertions.assertTrue(content.contains("addEqualsIfNotNullRestriction(cb, predicates, root.get(Organization_.role), filter.getRole());"));
	}

	@Test
	public void testDtoWithEnumGeneration() throws IOException {
		ProjectMetaData projectMetaData = service.loadProjectMetaData("src/test/resources/projects/success/2");
		Project project = loader.loadProject(projectMetaData);

		Bean organizationBean = project.model.findBean("ORGANIZATION");

		// BasicView
		BasicViewBeanFileWriteCommand basicViewCmd = new BasicViewBeanFileWriteCommand(organizationBean);
		String basicViewContent = basicViewCmd.generateContent();
		Assertions.assertTrue(basicViewContent.contains("import org.ifolks.demo.api.model.organizations.enums.OrganizationRole;"));
		Assertions.assertTrue(basicViewContent.contains("OrganizationRole role"));

		// Form
		FormBeanFileWriteCommand formCmd = new FormBeanFileWriteCommand(organizationBean);
		String formContent = formCmd.generateContent();
		Assertions.assertTrue(formContent.contains("import org.ifolks.demo.api.model.organizations.enums.OrganizationRole;"));
		Assertions.assertTrue(formContent.contains("@NotNull OrganizationRole role"));

		// Filter
		FilterFileWriteCommand filterCmd = new FilterFileWriteCommand(organizationBean);
		String filterContent = filterCmd.generateContent();
		Assertions.assertTrue(filterContent.contains("import org.ifolks.demo.api.model.organizations.enums.OrganizationRole;"));
		Assertions.assertTrue(filterContent.contains("private OrganizationRole role;"));
		Assertions.assertTrue(filterContent.contains("public OrganizationRole getRole() {"));
		Assertions.assertTrue(filterContent.contains("public void setRole(OrganizationRole role) {"));
	}

	@Test
	public void testMappersAndServicesWithEnumGeneration() throws IOException {
		ProjectMetaData projectMetaData = service.loadProjectMetaData("src/test/resources/projects/success/2");
		Project project = loader.loadProject(projectMetaData);

		Bean organizationBean = project.model.findBean("ORGANIZATION");

		// BaseBasicViewMapper
		BaseBasicViewMapperFileWriteCommand bvmCmd = new BaseBasicViewMapperFileWriteCommand(organizationBean);
		String bvmContent = bvmCmd.generateContent();
		Assertions.assertTrue(bvmContent.contains("import org.ifolks.demo.api.model.organizations.enums.OrganizationRole;"));
		Assertions.assertTrue(bvmContent.contains("OrganizationRole role = organization.getRole();"));

		// BaseFormMapper
		BaseFormMapperFileWriteCommand bfmCmd = new BaseFormMapperFileWriteCommand(organizationBean);
		String bfmContent = bfmCmd.generateContent();
		Assertions.assertTrue(bfmContent.contains("import org.ifolks.demo.api.model.organizations.enums.OrganizationRole;"));
		Assertions.assertTrue(bfmContent.contains("organization.setRole(organizationForm.role());"));
		Assertions.assertTrue(bfmContent.contains("OrganizationRole role = organization.getRole();"));
		Assertions.assertTrue(bfmContent.contains("OrganizationRole.valueOf(args["));

		// BaseRepositoryInterface
		BaseRepositoryInterfaceFileWriteCommand repoCmd = new BaseRepositoryInterfaceFileWriteCommand(organizationBean);
		String repoContent = repoCmd.generateContent();
		Assertions.assertNotNull(repoContent);
		Assertions.assertTrue(repoContent.contains("public interface OrganizationBaseRepository"));

		// BaseServiceInterface
		BaseServiceInterfaceFileWriteCommand srvIntfCmd = new BaseServiceInterfaceFileWriteCommand(organizationBean);
		String srvIntfContent = srvIntfCmd.generateContent();
		Assertions.assertNotNull(srvIntfContent);
		Assertions.assertTrue(srvIntfContent.contains("public interface OrganizationBaseService"));

		// BaseServiceImpl
		BaseServiceImplFileWriteCommand srvImplCmd = new BaseServiceImplFileWriteCommand(organizationBean);
		String srvImplContent = srvImplCmd.generateContent();
		Assertions.assertNotNull(srvImplContent);
		Assertions.assertTrue(srvImplContent.contains("public class OrganizationBaseServiceImpl"));

		// SpringRestBaseController
		SpringRestBaseControllerCommand restCmd = new SpringRestBaseControllerCommand(organizationBean);
		String restContent = restCmd.generateContent();
		Assertions.assertNotNull(restContent);
		Assertions.assertTrue(restContent.contains("public class OrganizationBaseController"));
	}
}
