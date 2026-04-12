package org.ifolks.generator.services.impl;

import org.ifolks.generator.model.domain.Project;
import org.ifolks.generator.services.interfaces.CodeGenerator;
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
public class CodeGeneratorImpl implements CodeGenerator {
	
	private static final Logger logger = LoggerFactory.getLogger(CodeGeneratorImpl.class);
	
	
	@Override
	public void initResources(Project project) {

		Skeleton skeleton = SkeletonResolver.getSkeleton(project);
		
		for (Layer layer : skeleton.getLayers(project)) {			
			FileWriteCommandTreeNode root = layer.getResourcesNode(project);
			if (root != null) {
				logger.info("start copying resources for layer : " + layer.getName());
				
				root.execute();
			}
		}		
	}

	
	@Override
	public void initConfiguration(Project project) {

		Skeleton skeleton = SkeletonResolver.getSkeleton(project);
		
		for (Layer layer : skeleton.getLayers(project)) {			
			FileWriteCommandTreeNode root = layer.getConfigurationNode(project);
			if (root != null) {
				logger.info("start creating configuration for layer : " + layer.getName());
				
				root.execute();
			}
		}
	}


	@Override
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
