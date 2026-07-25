package org.ifolks.generator.model.domain.ui;

import org.ifolks.generator.model.metadata.DataType;
import org.ifolks.generator.model.metadata.FilterRangeType;

public class FilterProperty {

	public String name;
	public String baseName;
	public FilterRangeType rangeType = FilterRangeType.NONE;
	public DataType dataType;
	public String tsType;
	
}
