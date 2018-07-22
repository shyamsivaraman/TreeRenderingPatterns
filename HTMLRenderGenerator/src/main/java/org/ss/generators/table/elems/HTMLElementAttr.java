package org.ss.generators.table.elems;

public class HTMLElementAttr {
	private String m_Name;
	private Object m_Value;
	
	public HTMLElementAttr(String name, Object value) {
		this.m_Name = name;
		this.m_Value = value;
	}
	
	public String toString() {
		return this.m_Name + "=\"" + m_Value.toString() + "\"";
	}
}
