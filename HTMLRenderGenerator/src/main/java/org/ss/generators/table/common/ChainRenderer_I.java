package org.ss.generators.table.common;

import org.ss.generators.table.elems.abstraction.XMLElement_A;

public interface ChainRenderer_I {

	public Object renderContent(XMLElement_A enclosingElm, Object element);
	
	public ChainRenderer_I getNextRenderer();
	
	public void setNextRenderer(ChainRenderer_I renderer);

}
