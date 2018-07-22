package org.ss.generators.table.render;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URLEncoder;

import org.ss.generators.table.HTMLProcessingContext;
import org.ss.generators.table.HTMLProcessingContext.HTMLLinkConfigProvider;
import org.ss.generators.table.common.PipelineProcess_I;
import org.ss.generators.table.elems.HTMLAnchor;
import org.ss.generators.table.elems.HTMLContinuationCell;
import org.ss.generators.table.elems.HTMLElementAttr;
import org.ss.generators.table.elems.HTMLTableCell;
import org.ss.generators.table.elems.HTMLTableElement;
import org.ss.generators.table.elems.HTMLTableRow;
import org.ss.generators.table.elems.abstraction.HTMLElement_A;

public class HTMLTableLinkProcessor implements PipelineProcess_I {

	public static final String QUEUE_IDENTIFIER = "QUEUE_IDENTIFIER";
	public static final String USER_SESSION_INFO = "USER_SESSION_INFO";
	
	private HTMLProcessingContext m_Context;
	private HTMLTableElement m_TableElm;
	private boolean m_IsProcessed = false;
	
	public HTMLTableLinkProcessor(HTMLProcessingContext aCtx) {
		this.m_Context = aCtx;
	}
	
	@Override
	public void generate(Writer out) throws IOException {
		if(!this.m_IsProcessed)
			return;
		
		this.m_TableElm.render(out);
	}
	
	@Override
	public void process(PipelineProcess_I prevProcess) {
		this.m_TableElm = (HTMLTableElement)prevProcess.getProcessedElement();
		this._applyCellLinks();
		
		this.m_IsProcessed = true;
	}

	@Override
	public HTMLElement_A getProcessedElement() {
		return this.m_TableElm;
	}
	
	private void _applyCellLinks() {
		if(this.m_TableElm == null)
			return;
		
		int childRowCount = this.m_TableElm.getTableBody().getChildCount();
		HTMLLinkConfigProvider aConfigProvider = m_Context.getProvider(
				HTMLProcessingContext.PROVIDER.LINK_CONFIG_PROVIDER, new HTMLLinkConfigProvider());
		short linkedCols = aConfigProvider.getColsToLink();
		if(linkedCols == -1)
			return;
		
		for(int i=0; i<childRowCount; i++) {
			HTMLTableRow aRow = (HTMLTableRow)this.m_TableElm.getTableBody().getChildElement(i);
			
			HTMLTableCell aCell = (HTMLTableCell)aRow.getChildElement(linkedCols);
			
			if(aCell instanceof HTMLContinuationCell)
				continue;
			
			HTMLElement_A aElem;
			aElem = new HTMLAnchor("");
			aCell.wrapTextElementWith(aElem);
			
			String aLink = "javascript:void";
			try {
				aLink = URLEncoder.encode(aLink, "UTF-8").replaceAll("\\%28", "(").replaceAll("\\%29", ")").replaceAll("\\+", "%20").replaceAll("\\%27", "'").replaceAll("\\%21", "!").replaceAll("\\%7E", "~");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			
			HTMLElementAttr hrefAttr = new HTMLElementAttr("href", aLink);
			aElem.addAttribute(hrefAttr);
		}
	}
	
}
