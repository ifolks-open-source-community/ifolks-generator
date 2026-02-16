package org.ifolks.generator.components.metadata.factory.interfaces;

import org.ifolks.generator.model.domain.business.Bean;
import org.ifolks.generator.model.domain.business.OneToMany;
import org.ifolks.generator.model.domain.ui.BasicViewBean;

public interface BasicViewBeanFactory {

	BasicViewBean getBasicViewBean(Bean bean);
	
	BasicViewBean getBasicViewBean(OneToMany oneToMany);
}
