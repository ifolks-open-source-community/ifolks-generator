package org.ifolks.generator.model.metadata;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name="enumeration")
public class EnumerationMetaData {

	@XmlAttribute(required=true)
	private String name;
	
	@XmlAttribute
	private String description;
	
	@XmlElement(name="item", required=true)
	private List<EnumItemMetaData> items = new ArrayList<>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<EnumItemMetaData> getItems() {
		return items;
	}

	public void setItems(List<EnumItemMetaData> items) {
		this.items = items;
	}
}
