package org.ifolks.generator.skeletons.rest;

import java.util.ArrayList;
import java.util.List;

import org.ifolks.generator.components.resolvers.DatabaseHandlerDiscovery;
import org.ifolks.generator.skeletons.core.layers.ApiLayer;
import org.ifolks.generator.skeletons.core.layers.BusinessComponentLayer;
import org.ifolks.generator.skeletons.core.layers.HibernateBusinessModelLayer;
import org.ifolks.generator.skeletons.core.layers.HibernateDaoLayer;
import org.ifolks.generator.skeletons.core.layers.JunitLayer;
import org.ifolks.generator.skeletons.core.layers.PopulatorLayer;
import org.ifolks.generator.skeletons.core.layers.ServiceLayer;
import org.ifolks.generator.skeletons.rest.layers.SpringRestControllerLayer;
import org.ifolks.generator.skeletons.rest.layers.SpringRestRootLayer;
import org.ifolks.generator.model.domain.Project;
import org.ifolks.generator.skeletons.Skeleton;
import org.ifolks.generator.skeletons.database.DatabaseHandler;
import org.ifolks.generator.skeletons.layers.Layer;


public class SpringHibernateRestSkeleton implements Skeleton {

	@Override
	public String getName() {
		return "SPRING_HIBERNATE_REST";
	}
	
	@Override
	public List<Layer> getLayers(Project project) {
		List<Layer> layers = new ArrayList<>();
	
		for (DatabaseHandler handler:DatabaseHandlerDiscovery.handlers) {
			layers.add(handler.getLayer());
		}
		layers.add(new SpringRestRootLayer());
		layers.add(new ApiLayer());
		layers.add(new HibernateBusinessModelLayer());
		layers.add(new HibernateDaoLayer());
		layers.add(new BusinessComponentLayer());
		layers.add(new ServiceLayer());		
		layers.add(new SpringRestControllerLayer());
		layers.add(new PopulatorLayer());
		layers.add(new JunitLayer());
		
		return layers;
	}	
}
