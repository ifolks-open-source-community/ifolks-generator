package org.ifolks.generator.skeletons.core.commands.persistence;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.ifolks.generator.model.domain.business.Bean;
import org.ifolks.generator.model.domain.business.Property;
import org.ifolks.generator.model.domain.ui.ViewProperty;
import org.ifolks.generator.model.metadata.RelationType;
import org.ifolks.generator.model.metadata.SelectionMode;
import org.ifolks.generator.model.util.naming.JavaClassNaming;
import org.ifolks.generator.skeletons.commands.impl.typed.JavaFileWriteCommand;

public class SpecificationFileWriteCommand extends JavaFileWriteCommand {

	private Bean bean;

	public SpecificationFileWriteCommand(Bean bean) {
		super(bean.myPackage.model.project.workspaceFolder + File.separator + bean.myPackage.model.persistenceArtefactName + File.separator + bean.myPackage.model.javaSourcesFolder + File.separator
				+ bean.myPackage.baseDAOInterfacePackageName.replace(".base", ".specifications").replace(".", File.separator), bean.className + "Specification");

		this.bean = bean;
	}

	@Override
	protected void fetchSpecificImports() {
		javaImports.add("import java.time.OffsetDateTime;");
		javaImports.add("import java.time.LocalDate;");
		javaImports.add("import java.math.BigDecimal;");
		javaImports.add("import java.util.List;");
		javaImports.add("import java.util.ArrayList;");
		
		javaImports.add("import jakarta.persistence.criteria.CriteriaBuilder;");
		javaImports.add("import jakarta.persistence.criteria.CriteriaQuery;");
		javaImports.add("import jakarta.persistence.criteria.Fetch;");
		javaImports.add("import jakarta.persistence.criteria.Join;");
		javaImports.add("import jakarta.persistence.criteria.JoinType;");
		javaImports.add("import jakarta.persistence.criteria.Predicate;");
		javaImports.add("import jakarta.persistence.criteria.Root;");
		
		javaImports.add("import org.springframework.data.jpa.domain.Specification;");
		javaImports.add("import org.springframework.data.domain.Sort;");
		javaImports.add("import org.ifolks.commons.api.model.OrderType;");
		javaImports.add("import static org.ifolks.commons.model.patterns.JpaCriteriaUtils.*;");

		javaImports.add("import " + this.bean.myPackage.omPackageName + "." + this.bean.className + ";");
		javaImports.add("import " + this.bean.myPackage.omPackageName + "." + this.bean.className + "_;");
		javaImports.add("import " + bean.myPackage.sortingsPackageName + "." + bean.basicViewBean.sortingClassName + ";");
		for (org.ifolks.generator.model.domain.business.OneToManyComponent oneToManyComponent : this.bean.oneToManyComponentList) {
			Bean currentBean = oneToManyComponent.referenceBean;
			javaImports.add("import " + currentBean.myPackage.sortingsPackageName + "." + currentBean.basicViewBean.sortingClassName + ";");
		}
		for (ViewProperty viewProperty : this.bean.basicViewBean.properties) {
			if (viewProperty.referenceBean != null) {
				Bean currentBean = viewProperty.referenceBean;
				javaImports.add("import " + currentBean.myPackage.omPackageName + "." + currentBean.className + ";");
				javaImports.add("import " + currentBean.myPackage.omPackageName + "." + currentBean.className + "_;");
			}
		}
		for (Property property : this.bean.properties) {
			if (property.referenceBean != null) {
				Bean currentBean = property.referenceBean;
				javaImports.add("import " + currentBean.myPackage.omPackageName + "." + currentBean.className + ";");
				javaImports.add("import " + currentBean.myPackage.omPackageName + "." + currentBean.className + "_;");
			}
		}
		javaImports.add("import " + bean.myPackage.filtersPackageName + "." + bean.basicViewBean.filter.className + ";");
	}

	@Override
	protected void writeContent() throws IOException {
		writeLine("package " + bean.myPackage.baseDAOInterfacePackageName.replace(".base", ".specifications") + ";");
		skipLine();

		writeImports();
		skipLine();

		writeLine("/**");
		writeLine(" * auto generated specifications class file");
		writeLine(" * <br/>write modifications between specific code marks");
		writeLine(" * <br/>processed by ifolks-generator");
		writeLine(" */");
		writeLine("public class " + this.bean.className + "Specification {");
		skipLine();

		createFilterBySpecification();
		createFilterByParentSpecification();
		
		if (bean.cardinality > 0) {
			createFilterByKeySpecification();
		}
		
		if (bean.selectable) {
			if (bean.selectionBehavior.selectionMode.equals(SelectionMode.AUTO_COMPLETE)) {
				createSearchSpecification();
			}
		}
		
		createGetSortSpecification();
		
		this.writeNotOverridableContent();

		writeLine("}");
	}

	private void createFilterBySpecification() {
		writeLine("\tpublic static Specification<" + bean.className + "> filterBy(" + bean.basicViewBean.filter.className + " filter) {");
		writeLine("\t\treturn (root, query, cb) -> {");
		writeLine("\t\t\tboolean isCountQuery = Long.class.equals(query.getResultType()) || long.class.equals(query.getResultType());");
		writeLine("\t\t\tList<Predicate> predicates = new ArrayList<>();");
		skipLine();

		for (Alias alias : getAllAliases(bean, null)) {
			writeLine("\t\t\tJoin<" + alias.parentBeanDataType + ", " + alias.beanDataType + "> " + alias.name + ";");
			writeLine("\t\t\tif (isCountQuery) {");
			writeLine("\t\t\t\t" + alias.name + " = " + alias.parentName + ".join(" + alias.parentBeanDataType + "_." + alias.propertyName + ", JoinType.LEFT);");
			writeLine("\t\t\t} else {");
			writeLine("\t\t\t\t" + alias.name + " = (Join<" + alias.parentBeanDataType + ", " + alias.beanDataType + ">)" + alias.parentName + ".fetch(" + alias.parentBeanDataType + "_." + alias.propertyName + ", JoinType.LEFT);");
			writeLine("\t\t\t}");
			skipLine();
		}

		for (ViewProperty property : this.bean.basicViewBean.properties) {
			if (property.filterable) {
				writeRestriction(property);
			}
		}

		writeLine("\t\t\treturn cb.and(predicates.toArray(new Predicate[0]));");
		writeLine("\t\t};");
		writeLine("\t}");
		skipLine();
	}

	private void createFilterByParentSpecification() {
		for (Property property : this.bean.properties) {
			if (property.referenceBean != null && (property.relation.equals(RelationType.MANY_TO_ONE) || property.relation.equals(RelationType.MANY_TO_ONE_COMPONENT))) {
				writeLine("\tpublic static Specification<" + bean.className + "> filterBy" + property.capName + "Spec(" + property.referenceBean.idType + " parentId) {");
				writeLine("\t\treturn (root, query, cb) -> {");
				writeLine("\t\t\tboolean isCountQuery = Long.class.equals(query.getResultType()) || long.class.equals(query.getResultType());");
				writeLine("\t\t\tJoin<" + bean.className + ", " + property.javaType + "> " + property.name + ";");
				writeLine("\t\t\tif (isCountQuery) {");
				writeLine("\t\t\t\t" + property.name + " = root.join(" + bean.className + "_." + property.name + ", JoinType.LEFT);");
				writeLine("\t\t\t} else {");
				writeLine("\t\t\t\t" + property.name + " = (Join<" + bean.className + ", " + property.javaType + ">)root.fetch(" + bean.className + "_." + property.name + ", JoinType.LEFT);");
				writeLine("\t\t\t}");
				writeLine("\t\t\tif (parentId == null) {");
				writeLine("\t\t\t\treturn cb.isNull(" + property.name + ".get(" + property.javaType + "_.id));");
				writeLine("\t\t\t} else {");
				writeLine("\t\t\t\treturn cb.equal(" + property.name + ".get(" + property.javaType + "_.id), parentId);");
				writeLine("\t\t\t}");
				writeLine("\t\t};");
				writeLine("\t}");
				skipLine();
			}
		}
	}

	private void createFilterByKeySpecification() {
		List<ViewProperty> findPropertyList = this.bean.referenceViewProperties;

		write("\tpublic static Specification<" + bean.className + "> filterByKeySpec(");
		boolean start = true;
		for (ViewProperty property : findPropertyList) {
			if (start) start = false; else write(", ");
			write(property.javaType + " " + property.name);
		}
		writeLine(") {");
		writeLine("\t\treturn (root, query, cb) -> {");
		writeLine("\t\t\tList<Predicate> predicates = new ArrayList<>();");

		for (Alias alias : getReferenceAliases(bean, "")) {
			writeLine("\t\t\tJoin<" + alias.parentBeanDataType + ", " + alias.beanDataType + "> " + alias.name + " = " + alias.parentName + ".join(" + alias.parentBeanDataType + "_." + alias.propertyName + ", JoinType.LEFT);");
		}
		skipLine();

		for (ViewProperty property : this.bean.referenceViewProperties) {
			writeEqualsRestriction(property);
		}
		skipLine();

		writeLine("\t\t\treturn cb.and(predicates.toArray(new Predicate[0]));");
		writeLine("\t\t};");
		writeLine("\t}");
		skipLine();
	}

	private void createGetSortSpecification() {
		writeLine("\tpublic static Sort getSort(" + bean.basicViewBean.sortingClassName + " sorting) {");
		writeLine("\t\tList<Sort.Order> orders = new ArrayList<>();");
		for (ViewProperty property : bean.basicViewBean.properties) {
			writeLine("\t\tif (sorting.get" + property.capName + "OrderType() != null) {");
			writeLine("\t\t\torders.add(new Sort.Order(sorting.get" + property.capName + "OrderType() == OrderType.ASC ? Sort.Direction.ASC : Sort.Direction.DESC, \"" + property.name + "\"));");
			writeLine("\t\t}");
		}
		writeLine("\t\torders.add(new Sort.Order(Sort.Direction.DESC, \"id\"));");
		writeLine("\t\treturn Sort.by(orders);");
		writeLine("\t}");
		skipLine();

		for (org.ifolks.generator.model.domain.business.OneToManyComponent oneToManyComponent : bean.oneToManyComponentList) {
			Bean currentBean = oneToManyComponent.referenceBean;
			writeLine("\tpublic static Sort getSort(" + currentBean.basicViewBean.sortingClassName + " sorting) {");
			writeLine("\t\tList<Sort.Order> orders = new ArrayList<>();");
			for (ViewProperty property : currentBean.basicViewBean.properties) {
				writeLine("\t\tif (sorting.get" + property.capName + "OrderType() != null) {");
				writeLine("\t\t\torders.add(new Sort.Order(sorting.get" + property.capName + "OrderType() == OrderType.ASC ? Sort.Direction.ASC : Sort.Direction.DESC, \"" + property.name + "\"));");
				writeLine("\t\t}");
			}
			writeLine("\t\torders.add(new Sort.Order(Sort.Direction.DESC, \"id\"));");
			writeLine("\t\treturn Sort.by(orders);");
			writeLine("\t}");
			skipLine();
		}
	}

	private void createSearchSpecification() {
		writeLine("\tpublic static Specification<" + bean.className + "> searchSpec(String arg) {");
		writeLine("\t\treturn (root, query, cb) -> {");
		writeLine("\t\t\tList<Predicate> predicates = new ArrayList<>();");

		if (bean.selectionBehavior != null && bean.selectionBehavior.labelProperty != null) {
			Property searchProperty = bean.selectionBehavior.labelProperty;
			switch (searchProperty.textFilterType) {
				case CONTAINS:
					writeLine("\t\t\tPredicate predicate = getStringContainsRestriction(cb, root.get(" + bean.className + "_." + searchProperty.name + "), arg);");
					break;
				case CONTAINS_IGNORE_ACCENTS:
					writeLine("\t\t\tPredicate predicate = getUnaccentuatedStringContainsRestriction(cb, root.get(" + bean.className + "_." + searchProperty.name + "), arg);");
					break;
				case STARTS_WITH:
					writeLine("\t\t\tPredicate predicate = getStringStartsWithRestriction(cb, root.get(" + bean.className + "_." + searchProperty.name + "), arg);");
					break;
				default:
					writeLine("\t\t\tPredicate predicate = null;");
					break;	
			}
			writeLine("\t\t\tif (predicate != null) {");
			writeLine("\t\t\t\tpredicates.add(predicate);");
			writeLine("\t\t\t}");
		}
		writeLine("\t\t\treturn cb.and(predicates.toArray(new Predicate[0]));");
		writeLine("\t\t};");
		writeLine("\t}");
		skipLine();
	}

	private class Alias {
		public String propertyName;
	    public String name;
	    public String parentName;
	    public String beanDataType;
	    public String parentBeanDataType;
	}	
	
	private List<Alias> getAliases(Bean bean, int size, String parentName, boolean checkVisibility) {
		List<Alias> aliasList = new ArrayList<Alias>();

		for (int i = 0; i < size; i++) {
			Property currentProperty = bean.properties.get(i);
			if ((!checkVisibility || currentProperty.visibility.isListVisible()) && currentProperty.referenceBean != null && (!checkVisibility || !currentProperty.relation.isComponentLink())) {
				Alias alias = new Alias();
				alias.propertyName = currentProperty.name;
				alias.parentName = StringUtils.isEmpty(parentName)?"root":parentName;
				alias.name = StringUtils.isEmpty(parentName)?currentProperty.name:parentName+JavaClassNaming.getClassNameFromObjectName(currentProperty.name);
				alias.parentBeanDataType = bean.className;
				alias.beanDataType = currentProperty.javaType;
				aliasList.add(alias);
				aliasList.addAll(getAliases(currentProperty, alias.name, checkVisibility));
			}
		}

		return aliasList;
	}
	
	private List<Alias> getReferenceAliases(Bean bean, String parentName) {
		return getAliases(bean, bean.cardinality, parentName, false);
	}
	
	private List<Alias> getAllAliases(Bean bean, String parentName) {
		return getAliases(bean, bean.properties.size(), parentName, true);
	}
	
	private List<Alias> getAliases(Property property, String parentName, boolean checkVisibility) {
		if (property.embedded) {
			return getAliases(property.referenceBean, property.referenceBean.properties.size(), parentName, checkVisibility);
		} else {
			return getAliases(property.referenceBean, property.referenceBean.cardinality, parentName, checkVisibility);
		}
	}
	
	private void writeRestriction(ViewProperty property) {
		switch (property.dataType) {
			case BOOLEAN :
				writeBooleanRestriction(property);
				return;
			case SHORT :
			case INTEGER :
			case LONG :
				writeLongRestriction(property);
				return;
			case DATE :
				writeDateRestriction(property);
				return;
			case DATETIME :
				writeDateTimeRestriction(property);
				return;
			case DOUBLE :
				writeDoubleRestriction(property);
				return;
			case BIG_DECIMAL :
				writeBigDecimalRestriction(property);
				return;
			case STRING :
			case TEXT :
				writeTextRestriction(property);
				return;
		}
	}
	
	private void writeTextRestriction(ViewProperty property) {
		String joinedAliasName = StringUtils.isEmpty(property.joinedAliasName)?"root":property.joinedAliasName;
		
		switch (property.textFilterType) {
			case CONTAINS:
				writeLine("\t\t\taddStringContainsRestriction(cb, predicates, " + joinedAliasName + ".get(" + property.lastParentBeanClassName + "_." + property.lastPropertyName + "), filter.get" + property.capName + "());");
				break;
			case CONTAINS_IGNORE_ACCENTS:
				writeLine("\t\t\taddUnaccentuatedStringContainsRestriction(cb, predicates, " + joinedAliasName + ".get(" + property.lastParentBeanClassName + "_." + property.lastPropertyName + "), filter.get" + property.capName + "());");
				break;
			case STARTS_WITH:
				writeLine("\t\t\taddStringStartsWithRestriction(cb, predicates, " + joinedAliasName + ".get(" + property.lastParentBeanClassName + "_." + property.lastPropertyName + "), filter.get" + property.capName + "());");
				break;
			default:
				break;	
		}
	}
	
	private void writeBooleanRestriction(ViewProperty property) {
		String joinedAliasName = StringUtils.isEmpty(property.joinedAliasName)?"root":property.joinedAliasName;
		writeLine("\t\t\taddBooleanRestriction(cb, predicates, " + joinedAliasName + ".get(" + property.lastParentBeanClassName + "_." + property.lastPropertyName + "), filter.get" + property.capName + "());");
	}
	
	private void writeDoubleRestriction(ViewProperty property) {
		writeComparableRestriction(property);
	}
	
	private void writeBigDecimalRestriction(ViewProperty property) {
		writeComparableRestriction(property);
	}
	
	private void writeDateRestriction(ViewProperty property) {
		writeComparableRestriction(property);
	}
	
	private void writeDateTimeRestriction(ViewProperty property) {
		writeComparableRestriction(property);
	}
	
	private void writeLongRestriction(ViewProperty property) {
		writeComparableRestriction(property);
	}
	
	private void writeComparableRestriction(ViewProperty property) {
		String joinedAliasName = StringUtils.isEmpty(property.joinedAliasName)?"root":property.joinedAliasName;
		writeLine("\t\t\taddBetweenRestriction(cb, predicates, " + joinedAliasName + ".get(" + property.lastParentBeanClassName + "_." + property.lastPropertyName + "), filter.get" + property.capName + "MinValue(), filter.get" + property.capName + "MaxValue());");
	}
	
	private void writeEqualsRestriction(ViewProperty property) {
		String joinedAliasName = StringUtils.isEmpty(property.joinedAliasName)?"root":property.joinedAliasName;
		writeLine("\t\t\taddEqualsRestriction(cb, predicates, " + joinedAliasName + ".get(" + property.lastParentBeanClassName + "_." + property.lastPropertyName + "), " + property.name + ");");
	}
}
