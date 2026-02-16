package org.ifolks.generator.model.domain.database;

import java.util.List;

import org.ifolks.generator.model.metadata.DataType;
import org.ifolks.generator.model.metadata.RelationType;
import org.ifolks.generator.model.metadata.TextFilterType;
import org.ifolks.generator.model.metadata.Visibility;

public class Column {
	
	public String name;
    public String originalName;
    public DataType dataType;
    public boolean nullable;
    public boolean unique;
    public Table referenceTable;
    public RelationType relation;
    public boolean deleteCascade;

    public boolean editable;
    public boolean filterable;
    public TextFilterType textFilterType;
    public Visibility visibility;
    public String rendering;
    public List<String> annotations;

}
