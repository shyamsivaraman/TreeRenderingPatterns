package org.ss.generators.table;

import java.util.HashMap;
import java.util.Map;

import org.ss.generators.table.elems.HTMLElementAttr;
import org.ss.generators.table.elems.abstraction.HTMLElement_A;

public class HTMLDefaultCSSProvider {

	private static final HTMLDefaultCSSProvider m_Instance = new HTMLDefaultCSSProvider();
	private Map<Class<? extends HTMLElement_A>, HTMLElementAttr> m_DefStyleMap = new HashMap<Class<? extends HTMLElement_A>, HTMLElementAttr>();
	private Map<Class<? extends HTMLElement_A>, HTMLElementAttr> m_DefClassMap = new HashMap<Class<? extends HTMLElement_A>, HTMLElementAttr>();
	
	private HTMLDefaultCSSProvider() { }
	
	public static HTMLDefaultCSSProvider getInstance() {
		return m_Instance;
	}
	
	public HTMLElementAttr getDefaultStyle(HTMLElement_A elem) {
		return this.m_DefStyleMap.get(elem.getClass());
	}
	
	public void setDefaultStyle(Class<? extends HTMLElement_A> clazz, HTMLElementAttr attr) {
		this.m_DefStyleMap.put(clazz, attr);
	}
	
	public void setDefaultClass(Class<? extends HTMLElement_A> clazz, HTMLElementAttr attr) {
		this.m_DefClassMap.put(clazz, attr);
	}
	
	public HTMLElementAttr getDefaultClass(HTMLElement_A elem) {
		return this.m_DefClassMap.get(elem);
	}
}
