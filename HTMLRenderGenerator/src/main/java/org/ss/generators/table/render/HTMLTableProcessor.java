package org.ss.generators.table.render;

import java.io.IOException;
import java.io.Writer;

import org.ss.generators.table.common.PipelineProcess_I;
import org.ss.generators.table.elems.HTMLContinuationCell;
import org.ss.generators.table.elems.HTMLElementAttr;
import org.ss.generators.table.elems.HTMLTableBodyElement;
import org.ss.generators.table.elems.HTMLTableCell;
import org.ss.generators.table.elems.HTMLTableElement;
import org.ss.generators.table.elems.HTMLTableHeaderCell;
import org.ss.generators.table.elems.HTMLTableRow;
import org.ss.generators.table.elems.HTMLVoidTableCell;
import org.ss.generators.table.elems.abstraction.HTMLElement_A;
import org.ss.generators.table.model.TableColumnModel;
import org.ss.generators.table.model.TableColumnModel.TableColumn;
import org.ss.generators.table.model.TableModel;
import org.ss.generators.table.model.TableModel.RowIterator;
import org.ss.generators.table.model.TableRowId;
import org.ss.generators.table.render.renderers.ImageRenderer;

/**
 * TODO: Implement queue based rendering pipeline to reduce the amount of objects
 * held in memory. Choose the queue based rendering when number of rows are too
 * huge (as configured), else choose the default recursive rendering strategy
 *
 */

public class HTMLTableProcessor implements PipelineProcess_I {
	
	private TableModel m_TableModel;
	
	private HTMLTableElement m_TableElm;
	private boolean m_IsProcessed = false;
	
	public HTMLTableProcessor(TableModel tModel) {
		this.m_TableModel = tModel;
	}
	
	/**
	 * The previous process is expected to be null as this process will be always added
	 * as the first item in the rendering pipeline
	 */
	
	@Override
	public void process(PipelineProcess_I prevProcess) {
		m_TableElm = this._createTableElement();
		this._process();
	}
	
	@Override
	public HTMLElement_A getProcessedElement() {
		return this.m_TableElm;
	}
	
	public void setProcessed(boolean isProcessed) {
		this.m_IsProcessed = isProcessed;
	}
	
	@Override
	public void generate(Writer out) throws IOException {
		if(out == null)
			return;
		
		if(!this.m_IsProcessed)
			this._process();
		
		m_TableElm.render(out);
	}
	
	/***************************** Private methods ******************************/
	
	private void _process() {
		this._createTableHeader();
		this._createTableBody();
		this.m_IsProcessed = true;
	}
	
	private HTMLTableElement _createTableElement() {
		HTMLTableElement tElm = new HTMLTableElement();
		//Add table attributes from rConfig
		
		return tElm;
	}
	
	private void _createTableHeader() {
		TableColumnModel colModel = this.m_TableModel.getColumnModel();
		short[] colKeys = colModel.getColumnDisplayOrder();
		
		HTMLTableRow hdrRow = m_TableElm.getTableHeader().getHeaderRow();
		for(int i=0; i<colKeys.length; i++) {
			TableColumn tc1 = colModel.getColumn(colKeys[i]);
			
			if(tc1.getIsHidden())
				continue;
			
			HTMLTableHeaderCell c1 = new HTMLTableHeaderCell(tc1.getDisplayName());

			if(tc1.getColumnDataType() == TableColumnModel.COL_DATA_TYPE.LOCK)
				c1.setRenderer(new ImageRenderer());
				
			hdrRow.addCell(c1);
		}
	}
	
	private void _createTableBody() {
		HTMLTableBodyElement tBody = m_TableElm.getTableBody();
		
		TableColumnModel colModel = this.m_TableModel.getColumnModel();
		short[] colKeys = colModel.getColumnDisplayOrder();
		
		int rowCounter = 0;
		Object[] theCells = null;
		int[] spanSkipCount = null;
		RowIterator rIter = this.m_TableModel.getRowIterator();
		
		while(rIter.hasNext()) {
			theCells = rIter.next();
			TableRowId tRowId = rIter.getRowId();
			HTMLTableRow row = new HTMLTableRow(false, tRowId);
			
			HTMLElementAttr rowIDAttr = new HTMLElementAttr("rowId", this.m_TableModel.getInternalRowId(rowCounter)+"");
			row.addAttribute(rowIDAttr);
			
			int[] spanInfo = rIter.getMergeSpanInfo();
			spanSkipCount = _mergeSpanCounts(spanSkipCount, spanInfo);
			
			for(int j=0; j<colKeys.length; j++) {
				if(colModel.getColumn(colKeys[j]).getIsHidden())
					continue;
				
				int colId = colModel.getColumn(colKeys[j]).getColId();
				
				HTMLTableCell cell = new HTMLTableCell(theCells[colId]);
				cell.setColIndex(colKeys[j]);
				
				if(spanInfo != null && spanInfo[colId] != 0 && spanInfo[colId]==spanSkipCount[colId]) {
					cell.addAttribute(new HTMLElementAttr("rowspan", spanInfo[colId]+""));
					spanSkipCount[colId] = spanSkipCount[colId] - 1;
				} else if(spanSkipCount != null && spanSkipCount[colId] > 0) {
					cell = new HTMLVoidTableCell();
					spanSkipCount[colId] = spanSkipCount[colId] - 1;
				}

				row.addCell(cell);
			}
			
			tBody.addRow(row);
			rowCounter++;
		}
		
		_addPageContinuation(tBody, theCells);
	}
	
	private void _addPageContinuation(HTMLTableBodyElement tBody, Object[] theLastRow) {
		if(theLastRow == null || theLastRow.length == 0)
			return;
		
		Object[] theTrRow = this.m_TableModel.getTrailingRow();
		if(theTrRow == null || theTrRow.length == 0)
			return;
		
		HTMLTableRow row = new HTMLTableRow(false);
		short[] mergeColIndices = this.m_TableModel.getColumnModel().getMergeColumnIndices();
		
		for(int i=0; i<mergeColIndices.length; i++) {
			if(((String)theTrRow[mergeColIndices[i]]).equals((String)theLastRow[mergeColIndices[i]])) {
				row.addCell(new HTMLContinuationCell("Continued.."));
				break;
			}
		}
		
		for(int i=1; i<theLastRow.length; i++) {
			row.addCell(new HTMLVoidTableCell());
		}
		
		tBody.addRow(row);
	}
	
	private int[] _mergeSpanCounts(int[] oldCount, int[] newCount) {
		if(oldCount != null && newCount != null) {
			int[] mergedCount = new int[newCount.length];
			for(int i=0; i<newCount.length; i++) {
				mergedCount[i] = (oldCount[i] > 0) ? oldCount[i] : newCount[i];
			}
			
			return mergedCount;
		} else if(oldCount != null)
			return oldCount;
		else
			return newCount;
	}
	
}
