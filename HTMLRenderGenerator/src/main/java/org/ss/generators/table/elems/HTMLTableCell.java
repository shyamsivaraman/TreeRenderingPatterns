package org.ss.generators.table.elems;

import org.ss.generators.table.HTMLRendererRegistry;
import org.ss.generators.table.common.ChainRenderer_I;
import org.ss.generators.table.elems.abstraction.HTMLElement_A;
import org.ss.generators.table.render.renderers.DefaultTextRenderer;

public class HTMLTableCell extends HTMLElement_A {
	private short m_ColIndex;
	
	public HTMLTableCell(Object text) {
		this.m_TagName = "TD";
		this.m_TagContent = text;
	}
	
	public HTMLTableCell(Object text, int rowSpan) {
		this.m_TagName = "TD";
		this.m_TagContent = text;
		
		if(rowSpan > 1)
			this.addAttribute(new HTMLElementAttr("rowspan", Integer.toString(rowSpan)));
	}

	public short getColIndex() {
		return m_ColIndex;
	}

	public void setColIndex(short m_ColIndex) {
		this.m_ColIndex = m_ColIndex;
	}
	
	protected ChainRenderer_I getRenderer() {
		if(m_Renderer != null)
			return this.m_Renderer;
		
		ChainRenderer_I renderer = HTMLRendererRegistry.getInstance().getRenderer(this.m_TagContent.getClass(), this.m_ColIndex);
		
		if(renderer == null)
			return new DefaultTextRenderer();
		else
			return renderer;
	}
}