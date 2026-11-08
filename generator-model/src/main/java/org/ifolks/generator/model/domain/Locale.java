package org.ifolks.generator.model.domain;

/**
 * Representation of a supported locale in a project
 *
 */
public class Locale {

	public String code;
	public String label;

	public Locale() {
	}

	public Locale(String code, String label) {
		this.code = code;
		this.label = label;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
}
