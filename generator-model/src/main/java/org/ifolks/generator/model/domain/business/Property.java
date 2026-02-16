package org.ifolks.generator.model.domain.business;

import java.util.List;

import org.ifolks.generator.model.domain.database.Column;
import org.ifolks.generator.model.metadata.DataType;
import org.ifolks.generator.model.metadata.RelationType;
import org.ifolks.generator.model.metadata.TextFilterType;
import org.ifolks.generator.model.metadata.Visibility;

public class Property {

	public Column column;
	public String name;
	public String capName;
	public String getterName;
	public String setterName;
	
	public String javaType;
	public String tsType;
	public DataType dataType;
	public boolean nullable;
	public boolean unique;
	public Bean referenceBean;
	public RelationType relation;
	public boolean embedded;
	public boolean editable;
	public boolean filterable;
	public TextFilterType textFilterType;
	public Visibility visibility;
	public String rendering;
	public List<String> annotations;

}
