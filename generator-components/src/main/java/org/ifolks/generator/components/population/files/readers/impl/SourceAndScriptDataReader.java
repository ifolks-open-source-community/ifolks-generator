package org.ifolks.generator.components.population.files.readers.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.ifolks.generator.components.population.converters.DbObjectToObjectConverter;
import org.ifolks.generator.components.population.files.readers.interfaces.DataReader;
import org.ifolks.generator.model.domain.business.Bean;
import org.ifolks.generator.model.domain.database.Table;
import org.ifolks.generator.model.exception.ReadBackupFailureException;
import org.ifolks.generator.model.metadata.DataType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

/**
 * Implementation of a {@link DataReader} that uses a script, a {@link JdbcTemplate} and a {@link Table} for meta-data
 * the {@link JdbcTemplate} is instantiated with a {@link DataSource} when instantiating the class
 * @author Nicolas Thibault
 *
 */
public class SourceAndScriptDataReader {

	/*
	 * properties
	 */
	private JdbcTemplate jdbcTemplate;
	
	/*
	 * constructor
	 */
	public SourceAndScriptDataReader(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	
	public List<Object[]> readData(String script, Bean bean) throws ReadBackupFailureException {
		
		List<DataType> types = bean.getPopulationTypes();
		
		return jdbcTemplate.query(script, new BasicRowMapper(types));

	}
	


	private class BasicRowMapper implements RowMapper<Object[]> {
		
		private List<DataType> types;
		
		public BasicRowMapper(List<DataType> types) {
			this.types = types;
		}

		@Override
	    public Object[] mapRow(ResultSet resultSet, int rowNum) throws SQLException {

	        List<Object> objectList = new ArrayList<>();
	        for (int i=1;i<=resultSet.getMetaData().getColumnCount();i++) {
	        	objectList.add(DbObjectToObjectConverter.getObjectFromDbObject(resultSet.getObject(i), types.get(i-1)));
	        }
	        
	        return objectList.toArray();
	    }

	}

}
