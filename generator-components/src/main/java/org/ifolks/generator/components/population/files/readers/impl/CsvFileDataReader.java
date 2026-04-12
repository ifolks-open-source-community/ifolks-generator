package org.ifolks.generator.components.population.files.readers.impl;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.ifolks.generator.components.population.converters.StringToObjectConverter;
import org.ifolks.generator.components.population.files.readers.interfaces.DataReader;
import org.ifolks.generator.model.domain.business.Bean;
import org.ifolks.generator.model.exception.ReadBackupFailureException;
import org.ifolks.generator.model.metadata.DataType;


public class CsvFileDataReader implements DataReader {
	
	public List<Object[]> readData(String filePath, Bean bean) {
		
		List<DataType> types = bean.getPopulationTypes();

		List<Object[]> result = new ArrayList<>();
				
		try (Reader reader = new FileReader(filePath);) {
			
			Iterable<CSVRecord> records = CSVFormat.newFormat(';').withQuote('"').withFirstRecordAsHeader().withAllowMissingColumnNames().parse(reader);
			for (CSVRecord record : records) {
				Object[] tokens = new Object[record.size()];
				for (int i = 0;i<record.size();i++) {
					tokens[i] = StringToObjectConverter.getObjectFromString(record.get(i), types.get(i));
				}
				result.add(tokens);
			}
		} catch (IOException e) {
			throw new ReadBackupFailureException("failed to read backup at " + filePath + " : " + e.getClass().getName() + " - " + e.getMessage(), e);
		}
		
		
		return result;
	}
}
