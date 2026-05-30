package org.ifolks.generator.skeletons.angular;

import java.util.ArrayList;
import java.util.List;

import org.ifolks.generator.components.database.DatabaseHandlerDiscovery;
import org.ifolks.generator.model.domain.Project;
import org.ifolks.generator.skeletons.Skeleton;
import org.ifolks.generator.skeletons.angular.layers.TypeScriptComponentsLayer;
import org.ifolks.generator.skeletons.angular.layers.TypeScriptModelLayer;
import org.ifolks.generator.skeletons.angular.layers.TypeScriptServicesLayer;
import org.ifolks.generator.skeletons.core.layers.ApiLayer;
import org.ifolks.generator.skeletons.core.layers.BusinessComponentLayer;
import org.ifolks.generator.skeletons.core.layers.BusinessModelLayer;
import org.ifolks.generator.skeletons.core.layers.CommandsLayer;
import org.ifolks.generator.skeletons.core.layers.JunitLayer;
import org.ifolks.generator.skeletons.core.layers.PopulatorLayer;
import org.ifolks.generator.skeletons.core.layers.RepositoryLayer;
import org.ifolks.generator.skeletons.core.layers.ServiceLayer;
import org.ifolks.generator.skeletons.database.DatabaseHandler;
import org.ifolks.generator.skeletons.layers.Layer;
import org.ifolks.generator.skeletons.rest.layers.SpringRestControllerLayer;
import org.ifolks.generator.skeletons.rest.layers.SpringRestRootLayer;


public class SpringHibernateAngularSkeleton implements Skeleton {

	@Override
	public String getName() {
		return "SPRING_REST_ANGULAR";
	}
	
	@Override
	public List<Layer> getLayers(Project project) {
		List<Layer> layers = new ArrayList<>();
	
		for (DatabaseHandler handler:DatabaseHandlerDiscovery.handlers) {
			layers.add(handler.getLayer());
		}
		layers.add(new SpringRestRootLayer());
		layers.add(new ApiLayer());
		layers.add(new BusinessModelLayer());
		layers.add(new RepositoryLayer());
		layers.add(new BusinessComponentLayer());
		layers.add(new ServiceLayer());
		layers.add(new SpringRestControllerLayer());
		layers.add(new CommandsLayer());
		layers.add(new PopulatorLayer());
		layers.add(new JunitLayer());
		layers.add(new TypeScriptModelLayer());
		layers.add(new TypeScriptServicesLayer());
		layers.add(new TypeScriptComponentsLayer());
		
		return layers;
	}	
}
