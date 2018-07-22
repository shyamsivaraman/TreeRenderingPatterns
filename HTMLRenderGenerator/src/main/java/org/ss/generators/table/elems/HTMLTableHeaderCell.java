package org.ss.generators.table.elems;

import org.ss.generators.table.common.ChainRenderer_I;
import org.ss.generators.table.render.renderers.DefaultTextRenderer;

public class HTMLTableHeaderCell extends HTMLTableCell {

	private short m_ColIndex;
	
	public HTMLTableHeaderCell(String text) {
		super(text);
		this.m_TagName = "TD";
		this.m_TagContent = text;
	}
	
	public HTMLTableHeaderCell(String text, int rowSpan) {
		super(text, rowSpan);
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
		
		return new DefaultTextRenderer();
	}
}
