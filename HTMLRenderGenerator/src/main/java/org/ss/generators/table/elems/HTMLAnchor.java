package org.ss.generators.table.elems;

import org.ss.generators.table.elems.abstraction.HTMLElement_A;

public class HTMLAnchor extends HTMLElement_A {
	
	public HTMLAnchor(String theText) {
		this.m_TagName = "A";
		this.m_TagContent = theText;
	}

}
