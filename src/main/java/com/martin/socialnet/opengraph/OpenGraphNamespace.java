package com.martin.socialnet.opengraph;

/**
 * Represents an OpenGraph namespace
 *
 * @author Callum Jones
 */
public record OpenGraphNamespace(String prefix, String schemaURI) {
	/**
	 * Construct a namespace
	 *
	 * @param prefix    The OpenGraph assigned namespace prefix such as og or og_appname
	 * @param schemaURI The URL for the OpenGraph schema
	 */
	public OpenGraphNamespace {
	}
}
