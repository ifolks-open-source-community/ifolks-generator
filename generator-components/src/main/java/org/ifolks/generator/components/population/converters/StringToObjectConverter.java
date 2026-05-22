package org.ifolks.generator.components.population.converters;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

import org.ifolks.generator.model.metadata.DataType;

public class StringToObjectConverter {
	
	public static Object getObjectFromString(String value, DataType type) {
		
		if (value.equals("")) {
			return null;
		}
		
		switch(type) {

			case DATETIME:
				return OffsetDateTime.parse(value);

		
			case DATE:
				return LocalDate.parse(value, DateTimeFormatter.ISO_DATE);
	
	
			case DOUBLE:
				return Double.valueOf(value);
	
			
			case BIG_DECIMAL:
				return new BigDecimal(value);
	
	
			case LONG:
				return Long.valueOf(value);
	
			
			case INTEGER:
				return Integer.valueOf(value);
	
			
			case SHORT:
				return Short.valueOf(value);
	
	
			case BOOLEAN:
				return Boolean.valueOf(value);
	
			default:
				return value;
		}
	}
}
