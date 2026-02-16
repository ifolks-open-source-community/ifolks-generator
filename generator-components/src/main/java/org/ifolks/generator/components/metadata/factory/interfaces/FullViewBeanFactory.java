package org.ifolks.generator.components.metadata.factory.interfaces;

import org.ifolks.generator.model.domain.business.Bean;
import org.ifolks.generator.model.domain.business.OneToMany;
import org.ifolks.generator.model.domain.ui.FullViewBean;

public interface FullViewBeanFactory {

	FullViewBean getFullViewBean(Bean bean);
	
	FullViewBean getFullViewBean(OneToMany oneToMany);
}
