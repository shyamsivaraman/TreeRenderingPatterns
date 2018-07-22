package org.ss.generators.table.render.renderers;

import java.util.Date;

import org.ss.generators.table.elems.abstraction.XMLElement_A;
import org.ss.generators.table.render.abstraction.ChainRenderer_A;

/**
 * Default text rendering for primitive wrappers
 */

public class DefaultTextRenderer extends ChainRenderer_A {

	@Override
	public String renderContent(XMLElement_A enclosingElm, Object content) {
		if(content == null || content.toString().trim().equals(""))
			return "";
		
		if(content instanceof Number) {
			return content.toString();
		} else if(content instanceof Date) {
			return "<Add a data formatter instance here>";
		}
		
		return content.toString();
	}

}
