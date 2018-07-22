package org.ss.generators.table.elems;

import org.ss.generators.table.elems.abstraction.HTMLElement_A;

/**
 * Sample table header element
 * <thead>
 *	<tr>
 *		<th>Month</th>
 *		<th>Savings</th>
 *	</tr>
 * </thead>
 */

public class HTMLTableHeaderElement extends HTMLElement_A {
	public HTMLTableHeaderElement() {
		this.m_TagName = "THEAD";
		this.addHeaderRow(new HTMLTableRow(true));
	}
	
	public void addHeaderRow(HTMLTableRow hdrRow) {
		this.addChildElement(hdrRow);
	}
	
	public HTMLTableRow getHeaderRow() {
		return (HTMLTableRow)this.getChildElement(0);
	}
}
