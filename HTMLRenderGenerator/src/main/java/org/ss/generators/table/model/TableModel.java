package org.ss.generators.table.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections4.comparators.ComparatorChain;
import org.ss.generators.table.model.TableColumnModel.SORT_ORDER;

public class TableModel {

	private TableColumnModel m_ColModel;
	private boolean m_IsSorted;
	//columnIndex - rowIndex - span
	private int[][][][] m_MergedIndex;
	private volatile int m_RowCounter = 0;
	private short[] m_RowKeyIndices;
	private List<TableRow> m_Rows = new ArrayList<TableRow>();
	private Object[] m_TrailingRow = new Object[0];
	
	public TableModel(TableColumnModel colModel) {
		this.m_ColModel = colModel;
	}
	
	/**************************** Data Input calls *************************/
	
	/**
	 * Creates a table row with a unique id assigned to it. The cell values passed
	 * into the row should be in the same sequence as the columns were sequenced in 
	 * the TableColumnModel
	 * Note: Column re-arrangement is not supported
	 * 
	 * @param cellValues
	 * @return
	 */
	
	public TableModel addRow(Object... cellValues) {
		TableRow aRow = new TableRow(this.m_RowCounter++, cellValues);
		this.m_Rows.add(aRow);
		
		return this;
	}
	
	public void setRowKeyIndices(short[] rowKeyIndices) {
		this.m_RowKeyIndices = rowKeyIndices;
	}
	
	public short[] getRowKeyIndices() {
		return this.m_RowKeyIndices;
	}
	
	public int getRowCount() {
		return this.m_RowCounter;
	}
	
	public TableColumnModel getColumnModel() {
		return this.m_ColModel;
	}

	/**
	 * Returns the row as per its new order after sort and merge
	 */
	
	public Object[] getRowCells(int rowIndex) {
		TableRow tr = m_Rows.get(rowIndex);
		Object[] rowCells = tr.m_Cells;
		
		return rowCells;
	}
	
	public RowIterator getRowIterator() {
		return new RowIterator();
	}
	
	public int getInternalRowId(int rowIndex) {
		return m_Rows.get(rowIndex).m_Index;
	}
	
	/**************************** Data Preparation calls *************************/
	
	/**
	 * Sort Process:
	 * 1. Get columnIDs for which sort has to be done => 2. Add the columnIDs for the
	 * columns which are to be merged into the sortColumn list => 3. Pass on to the
	 * comparator chain which compares on a row basis, with each comparator comparing
	 * adjacent column cells
	 * 
	 */
	
	public void sort() {
		short[] sortColIndexes = this.m_ColModel.getSortColumnIndices();
		short[] mergeColIndexes = this.m_ColModel.getMergeColumnIndices();

		List<Short> tmpList = new ArrayList<Short>();
		for(int i=0; i<sortColIndexes.length; i++)
			tmpList.add(sortColIndexes[i]);
		
		for(int i=0; i<mergeColIndexes.length; i++) {
			if(!tmpList.contains(mergeColIndexes[i]))
				tmpList.add(mergeColIndexes[i]);
		}
		
		ComparatorChain<TableRow> cChain = new ComparatorChain<TableRow>();
		
		for(int i=0; i<tmpList.size(); i++) {
			RowComparator rowComp = new RowComparator(tmpList.get(i));
			cChain.addComparator(rowComp);
		}
		
		Collections.sort(this.m_Rows, cChain);
		m_IsSorted = true;
	}
	
	public void merge() {
		if(!this.m_IsSorted && this.m_ColModel.getShouldSortBeforeMerge())
			this.sort();
		
		short[] mergeColIdx = this.m_ColModel.getMergeColumnIndices();
		
		for(int i=0; i<mergeColIdx.length; i++) {
			TableRow prevRow = null;
			boolean groupFound = false;
			short colId = mergeColIdx[i];
			
			TableRow grpStartRow = null;
			
			int mergeCount = 1;
			
			for(int j=0; j<this.m_Rows.size(); j++) {
				TableRow curRow = this.m_Rows.get(j);
				
				if(prevRow == null) {
					prevRow = curRow;
					continue;
				}
				
				if(prevRow.m_Cells[(int)colId].equals(curRow.m_Cells[(int)colId])) {
					if(!groupFound)
						grpStartRow = prevRow;
					
					mergeCount++;
					groupFound = true;
				} else {
					if(groupFound) {
						grpStartRow.addMergeSpan(colId, mergeCount);
						mergeCount = 1;
						groupFound = false;
					}
					
					prevRow = curRow;
				}
			}
			
			if(groupFound)
				grpStartRow.addMergeSpan(colId, mergeCount);
		}
	}
	
	/**
	 * @deprecated
	 * 
	 * Generates a 4D array with nesting as follows:
	 * 0 : ColumnIndex, 1: Array of 2D arrays storing the grouping ranges,
	 * 2: Array of 1D arrays storing start position and count ahead value
	 */
	
	private void merge_old() {
		if(!this.m_IsSorted)
			this.sort();
		
		short[] mergeColIdx = this.m_ColModel.getMergeColumnIndices();
		m_MergedIndex = new int[mergeColIdx.length][][][];
		
		for(int i=0; i<mergeColIdx.length; i++) {
			TableRow prevRow = null;
			boolean groupFound = false;
			short colIdx = mergeColIdx[i];
			int[][] cgCounts = new int[1][2];
			ArrayList<int[][]> colGrps = new ArrayList<int[][]>();
			
			int mergeCount = 1;
			
			for(int j=0; j<this.m_Rows.size(); j++) {
				TableRow curRow = this.m_Rows.get(j);
				
				if(prevRow == null) {
					prevRow = curRow;
					continue;
				}
				
				if(prevRow.m_Cells[(int)colIdx].equals(curRow.m_Cells[(int)colIdx])) {
					if(!groupFound)
						cgCounts[0][0] = prevRow.m_Index;
					
					cgCounts[0][1] = mergeCount++;
					groupFound = true;
				} else {
					if(groupFound) {
						colGrps.add(cgCounts);
						cgCounts = new int[1][2];
						mergeCount = 1;
						groupFound = false;
					}
					
					prevRow = curRow;
				}
			}
			
			if(groupFound)
				colGrps.add(cgCounts);
			
			int[][][] tmpArr = colGrps.toArray(new int[colGrps.size()][][]);
			this.m_MergedIndex[i] = tmpArr;
		}
	}
	
	/**************************** Internal classes & methods *************************/
	
	/**
	 * Method to test the correctness of mergeIndex array 
	 */
	
	private void _printMergeIndex() {
		for(int i=0; i<this.m_MergedIndex.length; i++) {
			int[][][] groupCounts = this.m_MergedIndex[i];
			
			for(int j=0; j<groupCounts.length; j++) {
				int[][] cntArr = groupCounts[j];
				
				for(int k=0; k<cntArr.length; k++) {
					System.out.print("Row Index: " + cntArr[k][0] + " => ");
					System.out.print("Count: " + cntArr[k][1] + "\n");
				}
			}
		}
	}
	
	/**
	 * Comparator which compares two cells of a given column index. The input to the
	 * compare are two table rows, from which the cells are picked based on the column
	 * index provided. The return value indicates the position of the rows when looked
	 * at from the order of the cells
	 */
	
	private class RowComparator implements Comparator<TableRow>{
		
		private short m_ColIdx;
		
		RowComparator(short colIdx) {
			this.m_ColIdx = colIdx;
		}

		@Override
		public int compare(TableRow o1, TableRow o2) {
			Object c1 = o1.getCellValue(m_ColIdx);
			Object c2 = o2.getCellValue(m_ColIdx);
			
			int negV = m_ColModel.getSortOrder().equals(SORT_ORDER.ASC) ? 1 : -1;
			
			if(c1 == null && c2 == null)
				return 0;
			else if (c1 == null)
				return 1 * negV;
			else if(c2 == null)
				return -1 * negV;
			
			if(c1 instanceof String) {
				return ((String)c1).compareTo((String)c2) * negV;
			} else if(c1 instanceof Integer) {
				int i1 = ((Integer)c1).intValue();
				int i2 = ((Integer)c2).intValue();
				return ((i1 == i2) ? 0 : ((i1 > i2) ? 1 : -1)) * negV;
			} else if(c1 instanceof Float) {
				float f1 = ((Float)c1).floatValue();
				float f2 = ((Float)c2).floatValue();
				return ((f1 == f2) ? 0 : ((f1 > f2) ? 1 : -1)) * negV;
			} else if(c1 instanceof Double) {
				double f1 = ((Double)c1).doubleValue();
				double f2 = ((Double)c2).doubleValue();
				return ((f1 == f2) ? 0 : ((f1 > f2) ? 1 : -1)) * negV;
			} else if(c1 instanceof Long) {
				long f1 = ((Long)c1).longValue();
				long f2 = ((Long)c2).longValue();
				return ((f1 == f2) ? 0 : ((f1 > f2) ? 1 : -1)) * negV;
			} else if(c1 instanceof java.util.Date) {
				long f1 = ((java.util.Date)c1).getTime();
				long f2 = ((java.util.Date)c2).getTime();
				return ((f1 == f2) ? 0 : ((f1 > f2) ? 1 : -1)) * negV;
			} else if(c1 instanceof java.sql.Date) {
				long f1 = ((java.sql.Date)c1).getTime();
				long f2 = ((java.sql.Date)c2).getTime();
				return ((f1 == f2) ? 0 : ((f1 > f2) ? 1 : -1)) * negV;
			} else if(c1 instanceof java.sql.Timestamp) {
				long f1 = ((java.sql.Timestamp)c1).getTime();
				long f2 = ((java.sql.Timestamp)c2).getTime();
				return ((f1 == f2) ? 0 : ((f1 > f2) ? 1 : -1)) * negV;
			}
			
			return 0;
		}
	}
	
	/**
	 * Represents a Row in the table with a assigned index value, value of which
	 * will be used for storing in m_SortIndex
	 */

	private class TableRow {
		//Immutable index within integer limits
		private final int m_Index;
		private Object[] m_Cells;
		private int[] m_MergeSpan;
		
		TableRow(int index, Object[] cells) {
			//Truncate the data to column count, if size differs from header column count
			if(cells.length != m_ColModel.getColumnCount()) {
				this.m_Cells = new Object[m_ColModel.getColumnCount()-1];
				System.arraycopy(cells, 0, this.m_Cells, 0, m_ColModel.getColumnCount()-1);
			} else {
				this.m_Cells = cells;
			}
			
			this.m_Index = index;
		}
		
		Object getCellValue(int index) {
			if(index > m_ColModel.getColumnCount())
				return null;
			
			return this.m_Cells[index];
		}
		
		int getRowIndex() {
			return this.m_Index;
		}
		
		void addMergeSpan(short colId, int spanLength) {
			if(m_MergeSpan == null)
				m_MergeSpan = new int[m_ColModel.getColumnCount()];
			
			for(int i=0; i<m_ColModel.getColumnCount(); i++) {
				if(m_ColModel.getColumn(i).getColId() == colId) {
					m_MergeSpan[i] = spanLength;
				}
			}
		}
		
		int[] getMergeSpans() {
			return this.m_MergeSpan;
		}
	}
	
	public class RowIterator implements Iterator<Object[]> {

		private int curIndex = -1;
		
		@Override
		public boolean hasNext() {
			return curIndex < m_Rows.size()-1;
		}

		@Override
		public Object[] next() {
			curIndex++;
			return m_Rows.get(curIndex).m_Cells;
		}
		
		public int[] getMergeSpanInfo() {
			return m_Rows.get(curIndex).getMergeSpans();
		}
		
		public TableRowId getRowId() {
			TableRowId rowId = new TableRowId();
			for(int i=0; i<m_RowKeyIndices.length; i++) {
				rowId.addKeyValue(m_ColModel.getColumn(m_RowKeyIndices[i]).getColumnName(),
				m_Rows.get(curIndex).m_Cells[m_RowKeyIndices[i]]);
			}
			
			return rowId;
		}
	}

	/**
	 * Trailing row supplied in to determine if a 'Continued...' indicator needs to be provided
	 * when a table's column is in merged mode
	 * 
	 * @param tCells
	 */
	
	public void setTrailingRow(Object[] tCells) {
		this.m_TrailingRow = tCells;
	}
	
	public Object[] getTrailingRow() {
		return this.m_TrailingRow;
	}
}
