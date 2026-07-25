package org.ifolks.generator.components.metadata.factory.impl;

import java.util.ArrayList;
import java.util.List;

import org.ifolks.generator.components.metadata.factory.interfaces.BasicViewBeanFactory;
import org.ifolks.generator.components.metadata.factory.interfaces.ViewPropertiesFactory;
import org.ifolks.generator.model.domain.business.Bean;
import org.ifolks.generator.model.domain.business.OneToMany;
import org.ifolks.generator.model.domain.ui.BasicViewBean;
import org.ifolks.generator.model.domain.ui.FilterProperty;
import org.ifolks.generator.model.domain.ui.ViewProperty;
import org.ifolks.generator.model.metadata.FilterRangeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component("javaBasicViewBeanFactory")
public class JavaBasicViewBeanFactory implements BasicViewBeanFactory {
	
	@Autowired
	@Qualifier("javaViewPropertiesFactory")
	private ViewPropertiesFactory viewPropertiesFactory;

	@Override
	public BasicViewBean getBasicViewBean(Bean bean) {
		
		BasicViewBean basicViewBean = new BasicViewBean();
		
		basicViewBean.recordName = bean.className + "BasicView";
		basicViewBean.objectName = bean.objectName + "BasicView";
		
		basicViewBean.baseMapperClassName = basicViewBean.recordName + "BaseMapper";
		basicViewBean.mapperClassName = basicViewBean.recordName + "Mapper";
		basicViewBean.mapperObjectName = basicViewBean.objectName + "Mapper";
		
		basicViewBean.filter.className = bean.className + "Filter";
		basicViewBean.filter.objectName = bean.objectName + "Filter";

		basicViewBean.sortingClassName = bean.className + "Sorting";

		
		basicViewBean.properties = viewPropertiesFactory.getBasicViewProperties(bean);
		
		for (ViewProperty property:basicViewBean.properties) {
			if (property.filterable) {
				basicViewBean.filter.properties.addAll(buildFilterProperties(property));
			}
		}
		
		return basicViewBean;
	}

	@Override
	public BasicViewBean getBasicViewBean(OneToMany oneToMany) {
		
		BasicViewBean basicViewBean = new BasicViewBean();
		
		Bean bean = oneToMany.referenceBean;
		
		basicViewBean.recordName = bean.className + "BasicView";
		basicViewBean.objectName = bean.objectName + "BasicView";
		
		basicViewBean.baseMapperClassName = basicViewBean.recordName + "BaseMapper";
		basicViewBean.mapperClassName = basicViewBean.recordName + "Mapper";
		basicViewBean.mapperObjectName = basicViewBean.objectName + "Mapper";
		
		basicViewBean.filter.className = bean.className + "Filter";
		basicViewBean.filter.objectName = bean.objectName + "Filter";

		basicViewBean.sortingClassName = bean.className + "Sorting";

		
		basicViewBean.properties = viewPropertiesFactory.getBasicViewProperties(oneToMany);
		
		for (ViewProperty property:basicViewBean.properties) {
			if (property.filterable) {
				basicViewBean.filter.properties.addAll(buildFilterProperties(property));
			}
		}
		
		return basicViewBean;
	}

	private List<FilterProperty> buildFilterProperties(ViewProperty property) {
		List<FilterProperty> result = new ArrayList<>();
		if (property.dataType.isLimitable()) {
			FilterProperty minProperty = new FilterProperty();
			minProperty.name = property.name + "MinValue";
			minProperty.baseName = property.name;
			minProperty.rangeType = FilterRangeType.MIN;
			minProperty.dataType = property.dataType;
			minProperty.tsType = property.tsType;
			result.add(minProperty);
			
			FilterProperty maxProperty = new FilterProperty();
			maxProperty.name = property.name + "MaxValue";
			maxProperty.baseName = property.name;
			maxProperty.rangeType = FilterRangeType.MAX;
			maxProperty.dataType = property.dataType;
			maxProperty.tsType = property.tsType;
			result.add(maxProperty);
		} else {
			FilterProperty filterProperty = new FilterProperty();
			filterProperty.name = property.name;
			filterProperty.baseName = property.name;
			filterProperty.rangeType = FilterRangeType.NONE;
			filterProperty.dataType = property.dataType;
			filterProperty.tsType = property.tsType;
			result.add(filterProperty);
		}
		return result;
	}

	
}
