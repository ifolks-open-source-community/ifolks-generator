package org.ifolks.generator.model.metadata;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name="supportedLocales")
public class SupportedLocalesMetaData {

	@XmlAttribute
	private String defaultLocale;

	@XmlElement(name="locale")
	private List<LocaleMetaData> locales = new ArrayList<>();

	public String getDefaultLocale() {
		return defaultLocale;
	}

	public void setDefaultLocale(String defaultLocale) {
		this.defaultLocale = defaultLocale;
	}

	public List<LocaleMetaData> getLocales() {
		return locales;
	}

	public void setLocales(List<LocaleMetaData> locales) {
		this.locales = locales;
	}
}
