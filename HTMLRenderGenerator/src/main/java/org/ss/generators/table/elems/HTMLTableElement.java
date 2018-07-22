package org.ss.generators.table.elems;

import org.ss.generators.table.elems.abstraction.HTMLElement_A;

public class HTMLTableElement extends HTMLElement_A {
	
	public HTMLTableElement() {
		this.m_TagName = "TABLE";
		this.addHeader(new HTMLTableHeaderElement());
		this.addBody(new HTMLTableBodyElement());
	}
	
	public HTMLTableHeaderElement getTableHeader() {
		return (HTMLTableHeaderElement)this.getChildElement(0);
	}
	
	public HTMLTableBodyElement getTableBody() {
		return (HTMLTableBodyElement)this.getChildElement(1);
	}
	
	public void addHeader(HTMLTableHeaderElement hdr) {
		this.addChildElement(hdr);
	}
	
	public void addBody(HTMLTableBodyElement body) {
		this.addChildElement(body);
	}
}
