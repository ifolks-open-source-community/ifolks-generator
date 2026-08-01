package org.ifolks.generator.skeletons.core.layers;

import org.ifolks.generator.model.domain.Project;
import org.ifolks.generator.skeletons.commands.impl.DirectoryResourceCopyCommand;
import org.ifolks.generator.skeletons.layers.AbstractLayer;
import org.ifolks.generator.skeletons.tree.FileWriteCommandTreeNode;

public class JunitLayer extends AbstractLayer {
	
	public JunitLayer() {
		super("JUnit tests");
	}

	@Override
	public FileWriteCommandTreeNode getInitializationNode(Project project) {
		FileWriteCommandTreeNode initTreeNode = new FileWriteCommandTreeNode();
		initTreeNode.add(new FileWriteCommandTreeNode(new DirectoryResourceCopyCommand(project, "/tests/pom.xml.vm", project.projectName + "-tests")));
		initTreeNode.add(new FileWriteCommandTreeNode(new DirectoryResourceCopyCommand(project, "/tests/resources", project.projectName + "-tests/" + project.model.resourcesFolder)));
		
		String junitPackagePath = project.model.junitPackageName.replace('.', '/');
		initTreeNode.add(new FileWriteCommandTreeNode(new DirectoryResourceCopyCommand(project, "/tests/java", project.projectName + "-tests/" + project.model.javaSourcesFolder + "/" + junitPackagePath)));
		initTreeNode.add(new FileWriteCommandTreeNode(new DirectoryResourceCopyCommand(project, "/tests/test-java", project.projectName + "-tests/" + project.model.testJavaSourcesFolder + "/" + junitPackagePath)));
		return initTreeNode;
	}
	
	@Override
	public FileWriteCommandTreeNode getGenerationNode(Project project) {

		return null;
	}
}
