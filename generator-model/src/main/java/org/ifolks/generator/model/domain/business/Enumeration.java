package org.ifolks.generator.model.domain.business;

import java.util.ArrayList;
import java.util.List;

import org.ifolks.generator.model.domain.Package;

public class Enumeration {

	public Package myPackage;
	public String name;
	public String className;
	public String description;
	public List<EnumItem> items = new ArrayList<>();

}
