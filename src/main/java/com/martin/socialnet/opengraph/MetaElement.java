package com.martin.socialnet.opengraph;

import java.net.URL;

/**
 * Represents OpenGraph enabled meta data for a specific document
 *
 * @param namespace either "og" an NS specific
 * @author Callum Jones
 */
public record MetaElement(OpenGraphNamespace namespace, String property, String content) {
	/**
	 * Construct the representation of an element
	 *
	 * @param namespace The namespace the element belongs to
	 * @param property  The property key
	 * @param content   The content or value of this element
	 */
	public MetaElement {
	}

	/**
	 * Fetch the content string of the element
	 */
	@Override
	public String content() {
		return content;
	}

	/**
	 * Fetch the OpenGraph namespace
	 */
	@Override
	public OpenGraphNamespace namespace() {
		return namespace;
	}

	/**
	 * Fetch the property of the element
	 */
	@Override
	public String property() {
		return property;
	}

	/**
	 * Fetch the OpenGraph data from the object
	 *
	 * @return If the content is a URL, then an attempted will be made to build OpenGraph data from the object
	 */
	public OpenGraph getExtendedData() {
		//The Java language should know the best form of a URL
		try {
			URL url = new URL(content());

			//success
			return new OpenGraph(url.toString(), true);
		} catch (Exception e) {
			return null; //not a valid URL
		}
	}
}