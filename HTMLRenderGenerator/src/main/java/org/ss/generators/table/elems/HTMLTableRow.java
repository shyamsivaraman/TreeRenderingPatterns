package org.ss.generators.table.elems;

import org.ss.generators.table.elems.abstraction.HTMLElement_A;

public class HTMLTableRow extends HTMLElement_A {
	private boolean m_IsHeader = false;
	
	public HTMLTableRow(boolean isHeader) {
		this.m_TagName = "TR";
		this.m_IsHeader = isHeader;
	}
	
	public HTMLTableRow(boolean isHeader, Object data) {
		this(isHeader);
		this.m_TagContent = data;
	}
	
	public void addCell(HTMLTableCell hdrCell) {
		hdrCell.setTagName(((this.m_IsHeader) ? "TH" : "TD"));
		this.addChildElement(hdrCell);
	}
}
