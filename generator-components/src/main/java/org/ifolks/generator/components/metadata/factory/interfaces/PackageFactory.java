package org.ifolks.generator.components.metadata.factory.interfaces;

import java.util.List;

import org.ifolks.generator.model.domain.Model;
import org.ifolks.generator.model.domain.Package;
import org.ifolks.generator.model.metadata.PackageMetaData;


/**
 * 
 * @author Nicolas Thibault
 *
 */
public interface PackageFactory {


	List<Package> scanPackages(PackageMetaData packageMetaData, Model model, Package parent);

	void fillPackage(PackageMetaData packageMetaData, Model model);

}
