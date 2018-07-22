package org.ss.generators.table.elems;

import org.ss.generators.table.elems.abstraction.HTMLElement_A;

/**
 * Sample table body element
 * <tbody>
 * 	<tr>
 * 		<td>V1</td>
 * 		<td rowspan=2">V2</td>
 * 	</tr>
 * </tbody>
 */

public class HTMLTableBodyElement extends HTMLElement_A {
	public HTMLTableBodyElement() {
		this.m_TagName = "TBODY";
	}
	
	public void addRow(HTMLTableRow row) {
		this.addChildElement(row);
	}
}
