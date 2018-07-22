package org.ss.generators.table.render;

import java.io.IOException;
import java.io.Writer;

import org.ss.generators.table.HTMLDefaultCSSProvider;
import org.ss.generators.table.HTMLTableCSSConfig;
import org.ss.generators.table.common.PipelineProcess_I;
import org.ss.generators.table.elems.HTMLContinuationCell;
import org.ss.generators.table.elems.HTMLElementAttr;
import org.ss.generators.table.elems.HTMLTableCell;
import org.ss.generators.table.elems.HTMLTableElement;
import org.ss.generators.table.elems.HTMLTableHeaderCell;
import org.ss.generators.table.elems.HTMLTableRow;
import org.ss.generators.table.elems.abstraction.HTMLElement_A;

public class HTMLTableCSSProcessor implements PipelineProcess_I {

	private HTMLTableElement m_TableElm;
	private HTMLTableCSSConfig m_Config;
	private boolean m_IsProcessed;
	
	public HTMLTableCSSProcessor(HTMLTableCSSConfig config) {
		this.m_Config = config;
	}
	
	@Override
	public void generate(Writer out) throws IOException {
		if(!this.m_IsProcessed)
			return;
		
		m_TableElm.render(out);
	}

	@Override
	public void process(PipelineProcess_I prevProcess) {
		this.m_TableElm = (HTMLTableElement) prevProcess.getProcessedElement();
		
		this._applyTableCSS();
		this._applyHeaderCSS();
		this._applyRowCSS();
		this._applyDataCellCSS();
		
		this.m_IsProcessed = true;
	}

	@Override
	public HTMLElement_A getProcessedElement() {
		return this.m_TableElm;
	}
	
	/***************************** Private methods ************************/

	private void _applyTableCSS() {
		HTMLElementAttr attr = new HTMLElementAttr("style", this.m_Config.getTableStyleString());
		this.m_TableElm.addAttribute(attr);

		if(this.m_Config.getTableClass() != null) {
			HTMLElementAttr clsAttr = new HTMLElementAttr("class", this.m_Config.getTableClass());
			this.m_TableElm.addAttribute(clsAttr);
		}
	}
	
	private void _applyHeaderCSS() {
		this.m_TableElm.getTableHeader().addAttribute(new HTMLElementAttr("style", this.m_Config.getHeaderStyleString()));
		
		if(this.m_Config.getHeaderClass() != null) {
			HTMLElementAttr clsAttr = new HTMLElementAttr("class", this.m_Config.getHeaderClass());
			this.m_TableElm.getTableHeader().addAttribute(clsAttr);
		}
	}
	
	private void _applyRowCSS() {
		HTMLElementAttr attr = new HTMLElementAttr("style", this.m_Config.getRowStyleString());
		HTMLDefaultCSSProvider.getInstance().setDefaultStyle(HTMLTableRow.class, attr);
		
		if(this.m_Config.getRowClass() != null) {
			HTMLElementAttr clsAttr = new HTMLElementAttr("style", this.m_Config.getRowClass());
			HTMLDefaultCSSProvider.getInstance().setDefaultClass(HTMLTableRow.class, clsAttr);
		}
	}
	
	private void _applyDataCellCSS() {
		HTMLElementAttr attr = new HTMLElementAttr("style", this.m_Config.getCellStyle());
		HTMLDefaultCSSProvider.getInstance().setDefaultStyle(HTMLTableCell.class, attr);
		HTMLDefaultCSSProvider.getInstance().setDefaultStyle(HTMLTableHeaderCell.class, attr);
		HTMLElementAttr attrContn = new HTMLElementAttr("style", this.m_Config.getContinuationCellStyle());
		HTMLDefaultCSSProvider.getInstance().setDefaultStyle(HTMLContinuationCell.class, attrContn);
		
		if(this.m_Config.getCellClass() != null) {
			HTMLElementAttr clsAttr = new HTMLElementAttr("style", this.m_Config.getCellClass());
			HTMLDefaultCSSProvider.getInstance().setDefaultClass(HTMLTableCell.class, clsAttr);
		}
	}
	
}
