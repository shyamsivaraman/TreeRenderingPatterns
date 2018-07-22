package org.ss.generators.table.elems;

import org.ss.generators.table.elems.abstraction.HTMLElement_A;

public class HTMLButton extends HTMLElement_A {

	public HTMLButton(String theText) {
		this.m_TagName = "BUTTON";
		this.m_TagContent = theText;
	}
}
