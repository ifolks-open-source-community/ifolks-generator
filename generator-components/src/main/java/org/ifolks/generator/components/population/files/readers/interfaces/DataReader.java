package org.ifolks.generator.components.population.files.readers.interfaces;

import java.util.List;

import org.ifolks.generator.model.domain.business.Bean;
import org.ifolks.generator.model.exception.ReadBackupFailureException;

/**
 * reads data as a List of object arrays<br/>
 * Depending on the implementation, the data will be fetched from a database query, a csv file, ...
 * @author Nicolas Thibault
 *
 */
public interface DataReader {

	List<Object[]> readData(String filePath, Bean bean) throws ReadBackupFailureException;
}
