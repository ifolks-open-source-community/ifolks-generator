package org.ifolks.generator.model.domain.business;

import org.ifolks.generator.model.domain.ui.BasicViewBean;
import org.ifolks.generator.model.domain.ui.FormBean;

public class OneToMany {

	public Bean referenceBean;
    public Property referenceProperty;
    public Bean parentBean;
    public String collectionName;
    public String collectionGetterName;
    public String collectionSetterName;
    
    public BasicViewBean basicViewBean;
    public FormBean formBean;
    
    
}
