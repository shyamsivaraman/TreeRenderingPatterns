package org.ss.generators.table.render.renderers;

import org.ss.generators.table.elems.abstraction.XMLElement_A;
import org.ss.generators.table.render.abstraction.ChainRenderer_A;

public class ImageRenderer extends ChainRenderer_A {

	@Override
	public Object renderContent(XMLElement_A enclosingElm, Object element) {
		if(element == null)
			return "";
		
		return (element.toString().equals("Y")) ? "<img src='../../../mdi/images/lock.png'/>" : "";
	}
	
}
