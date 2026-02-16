package org.ifolks.generator.components.metadata.factory.interfaces;

import org.ifolks.generator.model.domain.business.Bean;
import org.ifolks.generator.model.domain.business.OneToMany;
import org.ifolks.generator.model.domain.ui.FormBean;

public interface FormBeanFactory {

	FormBean getFormBean(Bean bean);
	
	FormBean getFormBean(OneToMany oneToMany);
}
