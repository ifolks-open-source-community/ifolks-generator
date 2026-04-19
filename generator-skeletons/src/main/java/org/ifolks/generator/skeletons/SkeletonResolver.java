package org.ifolks.generator.skeletons;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import org.ifolks.generator.model.domain.Project;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;

/**
 * a static builder to get the {@link Skeleton} o use depending on the project's skeleton
 * @author Nicolas Thibault
 *
 */
public class SkeletonResolver {
	
	public static Map<String, Skeleton> skeletons = new HashMap<>();
	
	static {
		ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
		provider.addIncludeFilter(new AssignableTypeFilter(Skeleton.class));
		
		String[] packagesToScan = ResourceBundle.getBundle("scan").getString("skeletons.path").split(",");
		
		Set<BeanDefinition> defs = new HashSet<>();
		
		for (String packageToScan:packagesToScan) {
			defs.addAll(provider.findCandidateComponents(packageToScan.trim()));
		}
		
		for (BeanDefinition def:defs) {
			try {
				Skeleton handler = (Skeleton) Class.forName(def.getBeanClassName()).getConstructor().newInstance();
				skeletons.put(handler.getName(), handler);			
			} catch (Exception e) {
				throw new IllegalArgumentException("Invalid SkeletonHandler : " + def.getBeanClassName(), e);
			}
		}
	}

	public static Skeleton getSkeleton(Project project) {
		return skeletons.get(project.getSkeleton());
	}
}
