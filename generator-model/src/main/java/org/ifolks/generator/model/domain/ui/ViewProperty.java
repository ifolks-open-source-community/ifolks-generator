package org.ifolks.generator.model.domain.ui;

import org.ifolks.generator.model.domain.business.Bean;
import org.ifolks.generator.model.metadata.DataType;
import org.ifolks.generator.model.metadata.SelectionMode;
import org.ifolks.generator.model.metadata.TextFilterType;
import org.ifolks.generator.model.metadata.Visibility;

public class ViewProperty {

	public String name;
	public String capName;

	public DataType dataType;
	public String javaType;
	public String tsType;
	public boolean nullable;
	public boolean editable;
	public boolean filterable;
	public TextFilterType textFilterType;
	public Visibility visibility;
	public String rendering;
	
	public Bean referenceBean;
	public Bean selectableBean;
	
	public String mappingPath;
	
	public String joinedAliasName;
	public String lastParentBeanClassName;
	public String lastPropertyName;
	
	
	public boolean isComboboxable( ) {
		return selectableBean!=null && selectableBean.selectionBehavior.selectionMode.equals(SelectionMode.DROPDOWN_OPTIONS);
	}	
}
