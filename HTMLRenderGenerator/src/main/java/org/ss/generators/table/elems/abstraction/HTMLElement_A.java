package org.ss.generators.table.elems.abstraction;

import java.io.IOException;
import java.io.Writer;

import org.ss.generators.table.HTMLDefaultCSSProvider;
import org.ss.generators.table.HTMLRendererRegistry;
import org.ss.generators.table.common.ChainRenderer_I;
import org.ss.generators.table.elems.HTMLElementAttr;

public class HTMLElement_A extends XMLElement_A {
	
	protected String m_TagName;
	protected Object m_TagContent = null;
	
	protected ChainRenderer_I m_Renderer;
	protected HTMLDefaultCSSProvider m_DefaultCSS = HTMLDefaultCSSProvider.getInstance();
	
	private HTMLElementAttr[] m_Attrs = new HTMLElementAttr[0];
	private HTMLElement_A[] m_ChildElements = new HTMLElement_A[0];
	
	@Override
	public void render(Writer out) throws IOException {
		this._writeOpeningTag(out);
		
		if(this.m_TagContent != null) {
			ChainRenderer_I renderer = getRenderer();
			
			if(renderer != null)
				out.write(renderer.renderContent(this, this.m_TagContent).toString());
		}
		
		this._generateChildTags(out, this.m_ChildElements);
		
		this._writeClosingTag(out);
	}
	
	@Override
	public void setRenderer(ChainRenderer_I renderer) {
		this.m_Renderer = renderer;
	}
	
	protected ChainRenderer_I getRenderer() {
		if(this.m_Renderer != null)
			return this.m_Renderer;
		
		return HTMLRendererRegistry.getInstance().getRenderer(this.m_TagContent.getClass());
	}
	
	public void addAttribute(HTMLElementAttr attr) {
		this.m_Attrs = this._addToArray(m_Attrs, attr, new HTMLElementAttr[m_Attrs.length+1]);
	}
	
	protected void addChildElement(HTMLElement_A e) {
		this.m_ChildElements = this._addToArray(m_ChildElements, e, new HTMLElement_A[m_ChildElements.length+1]);
	}
	
	public void wrapTextElementWith(HTMLElement_A e) {
		e.m_TagContent = this.m_TagContent;
		this.addChildElement(e);
		this.m_TagContent = null;
	}
	
	public HTMLElement_A getChildElement(int index) {
		return this.m_ChildElements[index];
	}
	
	public int getChildCount() {
		return this.m_ChildElements.length;
	}
	
	@Override
	public Object getContent() {
		return this.m_TagContent;
	}
	
	/*-------------------------------- Private methods ---------------------------------*/
	
	private void _generateChildTags(Writer out, HTMLElement_A[] elems) throws IOException {
		for(int i=0; i<elems.length; i++)
			elems[i].render(out);
	}
	
	private void _writeOpeningTag(Writer out) throws IOException {
		out.write("<");
		out.write(this.m_TagName);
		
		HTMLElementAttr defAttr = this.m_DefaultCSS.getDefaultStyle(this);
		if(defAttr != null) {
			out.write(" ");
			out.write(defAttr.toString());
		}
		
		if(m_Attrs.length>0) {
			out.write(" ");
			out.write(m_Attrs[0].toString());
		}
		
		for(int i=1; i<m_Attrs.length; i++) {
			out.write(" ");
			out.write(m_Attrs[i].toString());
		}
		
		out.write(">");
	}
	
	private void _writeClosingTag(Writer out) throws IOException {
		out.write("</");
		out.write(this.m_TagName);
		out.write(">");
	}
	
	private <T> T[] _addToArray(T[] arr, T theVal, T[] destArr) {
		T[] tmpArr = null;
		
		if(arr == null || arr.length == 0) {
			tmpArr = destArr;
			tmpArr[0] = theVal;
		} else {
			tmpArr = destArr;
			System.arraycopy(arr, 0, tmpArr, 0, arr.length);
			tmpArr[arr.length] = theVal;
		}
		
		return tmpArr;
	}

}
