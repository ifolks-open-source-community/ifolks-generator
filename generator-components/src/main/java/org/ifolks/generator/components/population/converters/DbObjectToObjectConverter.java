package org.ifolks.generator.components.population.converters;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.util.Date;

import org.ifolks.generator.model.metadata.DataType;

public class DbObjectToObjectConverter {
	
	public static Object getObjectFromDbObject(Object value, DataType type) {
		
		Object result = value;
		
		switch (type) {
		
		//Oracle patch for numbers mapping
		case LONG:
			if (BigDecimal.class.isAssignableFrom(value.getClass())) {
				result = ((BigDecimal)value).longValue();
			}
			return result;

		case INTEGER:
			if (BigDecimal.class.isAssignableFrom(value.getClass())) {
				result = ((BigDecimal)value).intValue();
			}
			return result;

		case SHORT:
			if (BigDecimal.class.isAssignableFrom(value.getClass())) {
				result = ((BigDecimal)value).shortValue();
			}
			return result;

		case BOOLEAN:
			if (BigDecimal.class.isAssignableFrom(value.getClass())) {
				result = ((BigDecimal)value).shortValue()>0;
			}
			return result;

		//Oracle patch for dates mapping
		case DATE:
			if (Date.class.isAssignableFrom(value.getClass())) {
				result = ((Date)value).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			}
			return result;
	
		case DATETIME:
			if (Date.class.isAssignableFrom(value.getClass())) {
				result = (Date)value;
			}
			return result;
		
		default:
			return result;
		}
	}
}
