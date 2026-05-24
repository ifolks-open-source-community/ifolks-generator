package org.ifolks.generator.skeletons.core.commands.persistence;

import java.io.File;
import java.io.IOException;

import org.ifolks.generator.model.domain.business.Bean;
import org.ifolks.generator.model.domain.business.Property;
import org.ifolks.generator.model.domain.ui.ViewProperty;
import org.ifolks.generator.model.metadata.RelationType;
import org.ifolks.generator.model.metadata.SelectionMode;
import org.ifolks.generator.skeletons.commands.impl.typed.JavaFileWriteCommand;

public class BaseRepositoryInterfaceFileWriteCommand extends JavaFileWriteCommand {

	private Bean bean;

	public BaseRepositoryInterfaceFileWriteCommand(Bean bean) {
		super(bean.myPackage.model.project.workspaceFolder + File.separator + bean.myPackage.model.persistenceArtefactName + File.separator + bean.myPackage.model.javaSourcesFolder + File.separator
				+ bean.myPackage.baseDAOInterfacePackageName.replace(".", File.separator), bean.baseDaoInterfaceName);

		this.bean = bean;
	}

	@Override
	protected void fetchSpecificImports() {
		javaImports.add("import java.util.List;");
		javaImports.add("import java.util.ArrayList;");
		javaImports.add("import java.time.OffsetDateTime;");
		javaImports.add("import java.time.LocalDate;");
		javaImports.add("import java.math.BigDecimal;");
		javaImports.add("import java.util.Optional;");
		
		javaImports.add("import org.springframework.data.jpa.repository.JpaRepository;");
		javaImports.add("import org.springframework.data.jpa.repository.JpaSpecificationExecutor;");
		javaImports.add("import org.springframework.data.jpa.domain.Specification;");
		javaImports.add("import org.springframework.data.domain.Pageable;");
		javaImports.add("import org.springframework.data.domain.PageRequest;");
		javaImports.add("import org.springframework.data.domain.Sort;");
		
		javaImports.add("import org.ifolks.commons.api.exception.repository.ObjectNotFoundException;");
		javaImports.add("import org.ifolks.commons.api.model.OrderType;");
		
		javaImports.add("import " + bean.myPackage.omPackageName + "." + bean.className + ";");
		javaImports.add("import " + bean.myPackage.filtersPackageName + "." + bean.basicViewBean.filter.className + ";");
		javaImports.add("import " + bean.myPackage.sortingsPackageName + "." + bean.basicViewBean.sortingClassName + ";");
		javaImports.add("import " + bean.myPackage.baseDAOInterfacePackageName.replace(".base", ".specifications") + "." + bean.className + "Specification;");
	}

	@Override
	protected void writeContent() throws IOException {
		writeLine("package " + bean.myPackage.baseDAOInterfacePackageName + ";");
		skipLine();
		writeImports();
		skipLine();

		writeLine("/**");
		writeLine(" * auto generated base repository interface file");
		writeLine(" * <br/>no modification should be done to this file");
		writeLine(" * <br/>processed by ifolks-generator");
		writeLine(" */");
		writeLine("public interface " + this.bean.baseDaoInterfaceName + " extends JpaRepository<" + this.bean.className + ", " + bean.idType + ">, JpaSpecificationExecutor<" + this.bean.className + "> {");
		skipLine();

		createBaseCrudOverrides();
		createLoadObjectList();
		createCount();
		createScroll();

		if (bean.cardinality > 0) {
			createExistsObject();
			createFindObject();
		}
		if (bean.selectable) {
			if (bean.selectionBehavior.selectionMode.equals(SelectionMode.AUTO_COMPLETE)) {
				createSearch();
			}
		}

		writeLine("}");
	}

	private void createBaseCrudOverrides() {
		writeLine("\tdefault List<" + bean.className + "> loadAll() {");
		writeLine("\t\treturn findAll(" + bean.className + "Specification.filterBy(new " + bean.basicViewBean.filter.className + "()));");
		writeLine("\t}");
		skipLine();
	}

	private void createLoadObjectList() {
		for (Property property : bean.properties) {
			if (property.referenceBean != null && property.relation.equals(RelationType.MANY_TO_ONE)) {
				writeLine("\tdefault List<" + this.bean.className + "> loadListFrom" + property.capName + "(" + property.referenceBean.idType + " " + property.name + "Id) {");
				writeLine("\t\treturn findAll(" + bean.className + "Specification.filterBy" + property.capName + "Spec(" + property.name + "Id));");
				writeLine("\t}");
				skipLine();
			}
		}
	}

	private void createCount() {
		writeLine("\tdefault Long count(" + bean.basicViewBean.filter.className + " filter) {");
		writeLine("\t\treturn count(" + bean.className + "Specification.filterBy(filter));");
		writeLine("\t}");
		skipLine();

		for (Property property : bean.properties) {
			if (property.referenceBean != null && property.relation.equals(RelationType.MANY_TO_ONE)) {
				writeLine("\tdefault Long countFrom" + property.capName + "(" + property.referenceBean.idType + " " + property.name + "Id) {");
				writeLine("\t\treturn count(" + bean.className + "Specification.filterBy" + property.capName + "Spec(" + property.name + "Id));");
				writeLine("\t}");
				skipLine();

				writeLine("\tdefault Long countFrom" + property.capName + "(" + property.referenceBean.idType + " " + property.name + "Id, " + bean.basicViewBean.filter.className + " filter) {");
				writeLine("\t\tSpecification<" + bean.className + "> spec = Specification.where(" + bean.className + "Specification.filterBy" + property.capName + "Spec(" + property.name + "Id))");
				writeLine("\t\t\t.and(" + bean.className + "Specification.filterBy(filter));");
				writeLine("\t\treturn count(spec);");
				writeLine("\t}");
				skipLine();
			}
		}
	}

	private void createScroll() {
		writeLine("\tdefault List<" + this.bean.className + "> scroll(" + bean.basicViewBean.filter.className + " filter, " + bean.basicViewBean.sortingClassName + " sorting, Long firstResult, Long maxResults) {");
		writeLine("\t\tint page = (firstResult == null || maxResults == null || maxResults == 0) ? 0 : (int)(firstResult / maxResults);");
		writeLine("\t\tint size = (maxResults == null || maxResults == 0) ? Integer.MAX_VALUE : maxResults.intValue();");
		writeLine("\t\tPageable pageable = PageRequest.of(page, size, " + bean.className + "Specification.getSort(sorting));");
		writeLine("\t\treturn findAll(" + bean.className + "Specification.filterBy(filter), pageable).getContent();");
		writeLine("\t}");
		skipLine();

		for (Property property : bean.properties) {
			if (property.referenceBean != null && property.relation.equals(RelationType.MANY_TO_ONE)) {
				writeLine("\tdefault List<" + this.bean.className + "> scrollFrom" + property.capName + "(" + property.referenceBean.idType + " " + property.name + "Id, " + bean.basicViewBean.filter.className + " filter, " + bean.basicViewBean.sortingClassName + " sorting, Long firstResult, Long maxResults) {");
				writeLine("\t\tint page = (firstResult == null || maxResults == null || maxResults == 0) ? 0 : (int)(firstResult / maxResults);");
				writeLine("\t\tint size = (maxResults == null || maxResults == 0) ? Integer.MAX_VALUE : maxResults.intValue();");
				writeLine("\t\tPageable pageable = PageRequest.of(page, size, " + bean.className + "Specification.getSort(sorting));");
				writeLine("\t\tSpecification<" + bean.className + "> spec = Specification.where(" + bean.className + "Specification.filterBy" + property.capName + "Spec(" + property.name + "Id))");
				writeLine("\t\t\t.and(" + bean.className + "Specification.filterBy(filter));");
				writeLine("\t\treturn findAll(spec, pageable).getContent();");
				writeLine("\t}");
				skipLine();
			}
		}
	}

	private void createExistsObject() {
		boolean start = true;
		write("\tdefault boolean exists(");
		for (ViewProperty property : bean.referenceViewProperties) {
			if (start) start = false; else write(", ");
			write(property.javaType + " " + property.name);
		}
		writeLine(") {");
		write("\t\treturn findOne(" + bean.className + "Specification.filterByKeySpec(");
		start = true;
		for (ViewProperty property : bean.referenceViewProperties) {
			if (start) start = false; else write(", ");
			write(property.name);
		}
		writeLine(")).isPresent();");
		writeLine("\t}");
		skipLine();
	}

	private void createFindObject() {
		boolean start = true;
		write("\tdefault Optional<" + this.bean.className + "> find(");
		for (ViewProperty property : bean.referenceViewProperties) {
			if (start) start = false; else write(", ");
			write(property.javaType + " " + property.name);
		}
		writeLine(") {");
		write("\t\treturn findOne(" + bean.className + "Specification.filterByKeySpec(");
		start = true;
		for (ViewProperty property : bean.referenceViewProperties) {
			if (start) start = false; else write(", ");
			write(property.name);
		}
		writeLine("));");
		writeLine("\t}");
		skipLine();
	}

	private void createSearch() {
		writeLine("\tdefault List<" + this.bean.className + "> search(String arg) {");
		writeLine("\t\treturn findAll(" + bean.className + "Specification.searchSpec(arg), PageRequest.of(0, 20)).getContent();");
		writeLine("\t}");
		skipLine();
	}
}
