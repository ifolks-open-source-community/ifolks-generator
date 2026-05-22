package org.ifolks.generator.components.population.files.readers.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.sql.DataSource;
import javax.xml.XMLConstants;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.ifolks.generator.components.population.datasources.InputDataSourceProvider;
import org.ifolks.generator.components.population.files.readers.interfaces.DataReader;
import org.ifolks.generator.model.domain.business.Bean;
import org.ifolks.generator.model.exception.DataSourceNotFoundException;
import org.ifolks.generator.model.exception.InvalidXmlSourceAndScriptFileException;
import org.ifolks.generator.model.exception.PopulationFileNotFoundException;
import org.ifolks.generator.model.exception.ReadBackupFailureException;
import org.ifolks.generator.model.population.SourceAndScript;
/**
 * Implementation of a {@link DataReader} that uses a path to a Xml backup file, a {@link InputDataSourceProvider} and a {@link Table} for meta-data
 * the processing will use a {@link SourceAndScriptDataReader} by converting the xml backup file to a {@link SourceAndScript} to extract a {@link DataSource} and a script
 * @author Nicolas Thibault
 *
 */
import org.springframework.core.io.ResourceLoader;
import org.xml.sax.SAXException;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

public class XmlFileSourceAndScriptDataReader implements DataReader {
	
	private static final String SCHEMA_LOCATION = "backup-1.0.xsd";
	
	/*
	 * properties
	 */
	private InputDataSourceProvider inputSourceProvider;
	private SourceAndScriptDataReader sourceAndScriptDataReader;
	private ResourceLoader resourceLoader;
	
	/*
	 * constructor
	 */
	public XmlFileSourceAndScriptDataReader(InputDataSourceProvider inputSourceProvider, ResourceLoader resourceLoader) {
		this.inputSourceProvider = inputSourceProvider;
		this.resourceLoader = resourceLoader;
	}

	

	@Override
	public List<Object[]> readData(String backupFilePath, Bean bean) {
		
		DataSource inputSource;
		SourceAndScript sourceAndScript;
		try {
			sourceAndScript = parse(backupFilePath);
		} catch (IOException e) {
			throw new ReadBackupFailureException("Failed to read source and script",e);
		}
	
		try {
			inputSource = inputSourceProvider.getDataSource(sourceAndScript.getSource());
		} catch (DataSourceNotFoundException e) {
			throw new ReadBackupFailureException("Invalid backup source", e);			
		}
		
		sourceAndScriptDataReader = new SourceAndScriptDataReader(inputSource);
		
		return sourceAndScriptDataReader.readData(sourceAndScript.getScript(), bean);
	}
	
	private SourceAndScript parse(String scriptFilePath) throws IOException {

		try {
			
			JAXBContext jaxbContext = JAXBContext.newInstance(SourceAndScript.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			
			SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI); 
	        Schema schema = schemaFactory.newSchema(getClass().getClassLoader().getResource(SCHEMA_LOCATION));
	        jaxbUnmarshaller.setSchema(schema);
			
	        SourceAndScript sourceAndScript;
	        if (scriptFilePath.startsWith("classpath:")) {
				try (InputStream is = resourceLoader.getResource(scriptFilePath).getInputStream()) {
					sourceAndScript = (SourceAndScript) jaxbUnmarshaller.unmarshal(is);
				}
	        } else {
	        	Path path = Paths.get(scriptFilePath);
	    		if (!Files.exists(path)) {
	    			throw new PopulationFileNotFoundException("Unable to find backup file : " + scriptFilePath);
	    		}
	    		File file = path.toFile();
	        	sourceAndScript = (SourceAndScript) jaxbUnmarshaller.unmarshal(file);
	        }
			
			return sourceAndScript;
			
		} catch (JAXBException | SAXException e) {
			throw new InvalidXmlSourceAndScriptFileException("Unable to parse backup file : " + scriptFilePath, e);
		}
	}
}
