package org.ss.generators.table.elems.abstraction;

import java.io.IOException;
import java.io.Writer;

import org.ss.generators.table.common.ChainRenderer_I;
import org.ss.generators.table.common.Renderable_I;

public abstract class XMLElement_A implements Renderable_I {

	protected String m_TagName;
	
	public abstract Object getContent();

	//Example: Anti-design here - Not his responsibility
	public abstract void render(Writer out) throws IOException;
	
	//Example: Anti-design here - Knowing and doing too much
	public abstract void setRenderer(ChainRenderer_I renderer);
	
	public String getTagName() {
		return this.m_TagName;
	}
	
	public void setTagName(String tagName) {
		this.m_TagName = tagName;
	}

}
