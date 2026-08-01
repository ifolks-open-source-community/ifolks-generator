package org.ifolks.generator.services;

import org.ifolks.generator.model.domain.Project;
import org.ifolks.generator.skeletons.Skeleton;
import org.ifolks.generator.skeletons.SkeletonResolver;
import org.ifolks.generator.skeletons.layers.Layer;
import org.ifolks.generator.skeletons.tree.FileWriteCommandTreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 
 * @author Nicolas Thibault
 *
 */
@Component
public class CodeGenerator {

	private static final Logger logger = LoggerFactory.getLogger(CodeGenerator.class);

	public void initProject(Project project) {
		Skeleton skeleton = SkeletonResolver.getSkeleton(project);

		for (Layer layer : skeleton.getLayers(project)) {
			FileWriteCommandTreeNode root = layer.getInitializationNode(project);
			if (root != null) {
				logger.info("start initializing layer : " + layer.getName());
				root.execute();
			}
		}
	}

	public void generateCode(Project project) {
		Skeleton skeleton = SkeletonResolver.getSkeleton(project);

		for (Layer layer : skeleton.getLayers(project)) {
			FileWriteCommandTreeNode root = layer.getGenerationNode(project);
			if (root != null) {
				logger.info("start generating layer : " + layer.getName());
				root.execute();
			}
		}
	}
}
