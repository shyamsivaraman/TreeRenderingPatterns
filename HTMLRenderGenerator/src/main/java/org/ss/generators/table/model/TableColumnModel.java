package org.ss.generators.table.model;

/**
 * Note: Column re-arrangement is not supported
 *
 */

public class TableColumnModel {

	public enum COL_DATA_TYPE { INTEGER, LONG, FLOAT, DOUBLE, STRING, DATE, TIMESTAMP, LOCK };
	public enum SORT_ORDER {ASC, DESC };
	
	private TableColumn[] m_Columns;
	
	private short m_ColCounter = 0;
	private SORT_ORDER m_SortOrder;
	private short m_SortCols[] = new short[0];
	private short m_MergeCols[] = new short[0];
	private short m_ColDisplayOrder[];
	private boolean m_DoSortBeforeMerge;
	
	/**
	 * Since columns need to have an internal ID associated with them,
	 * the TableColumn object can't be created externally and has to be
	 * requested from the TableColumnModel class
	 * 
	 * @return
	 */
	
	public TableColumn newColumn() {
		return new TableColumn(m_ColCounter++);
	}
	
	/**
	 * Add a column type (TableColumn) to the model.
	 * 
	 * @param col
	 * @return
	 */
	public TableColumnModel addColumn(TableColumn col) {
		TableColumn[] tmpCols = null;
		
		if(this.m_Columns == null) {
			tmpCols = new TableColumn[1];
			tmpCols[0] = col;
		} else {
			tmpCols = new TableColumn[this.m_Columns.length+1];
			System.arraycopy(this.m_Columns, 0, tmpCols, 0, this.m_Columns.length);
			tmpCols[this.m_Columns.length] = col;
		}
		
		this.m_Columns = tmpCols;
		
		return this;
	}
	
	/**
	 * Build method to be called mandatorily after adding all the columns
	 * are added
	 */
	
	public void build() {
		this._setDisplayOrder(this.m_Columns);
	}
	
	/**
	 * Get the count of columns hidden or not from the main array
	 * 
	 * @return
	 */
	
	public short getColumnCount() {
		return (short)this.m_Columns.length;
	}
	
	public TableColumn getColumn(int index) {
		return this.m_Columns[index];
	}
	
	/**
	 * Set the sortCols array to the IDs of the columns, which were passed
	 * in as the argument. The columns will be sorted in the same order as they
	 * were passed in. The sorting is done using a comparator chain which does
	 * full sort on the primary column, and sorts the remaining columns if the
	 * values in two cells of the primary column are the same
	 * 
	 * @param cols
	 */
	
	public void setSortColumns(TableColumn... cols) {
		if(cols == null)
			return;
		
		this.m_SortCols = new short[cols.length];
		
		for(int i=0; i<cols.length; i++)
			this.m_SortCols[i] = cols[i].getColId();
	}
	
	/**
	 * Set the mergeCols array to the IDs of the columns, which were passed in as 
	 * the argument. The columns will be merged in the same order as they were passed
	 * 
	 * @param cols
	 */
	
	public void addMergeColumns(boolean doSortBeforeMerge, TableColumn... cols) {
		if(cols == null)
			return;
		
		this.m_MergeCols = new short[cols.length];
		
		for(int i=0; i<cols.length; i++)
			this.m_MergeCols[i] = cols[i].getColId();
		
		this.m_DoSortBeforeMerge = doSortBeforeMerge;
	}
	
	public boolean getShouldSortBeforeMerge() {
		return this.m_DoSortBeforeMerge;
	}
	
	public short[] getSortColumnIndices() {
		return this.m_SortCols;
	}
	
	public short[] getMergeColumnIndices() {
		return this.m_MergeCols;
	}
	
	public short[] getColumnDisplayOrder() {
		return this.m_ColDisplayOrder;
	}
	
	/**
	 * Sets the overall sorting order for all the columns
	 * specified via the setSortColumns method
	 * 
	 * @param sortOrder
	 */
	
	public void setSortOrder(SORT_ORDER sortOrder) {
		this.m_SortOrder = sortOrder;
	}
	
	public SORT_ORDER getSortOrder() {
		return this.m_SortOrder;
	}

	/**
	 * The TableColumn type is the definition on a table's column. In certain
	 * contexts a table column should have information to represent a table's
	 * header, hence, the need for a nested TableColumn property.
	 * 
	 * There is a TODO option to interface an I18N adapter to the class so that
	 * an appropriate display label, as well as the default cell rendering could 
	 * be determined (each cell can have its own renderer, when not found the,
	 * column renderer should be used, which is the format expression)
	 * 
	 * The colId is an internal immutable index specified based on sequence of
	 * column addition. This index is referenced by the sort orders, merge order
	 * and display ordering
	 */
	
	public class TableColumn {
		
		private TableColumn[] m_SubColumns;
		
		//Immutable index for column used to refer this column uniquely in
		//other structures meant for denoting column display order, column
		//hiding etc
		private final short m_ColId;
		private boolean m_IsHidden;
		private String m_DisplayName;
		private String m_InternalColName;
		private boolean m_IsKeyColumn;
		private COL_DATA_TYPE m_ColumnDataType;
		private String m_FormatExpr;
		
		//private String m_Locale;
		//private I18NProvider_I m_I18NProvider;
		
		TableColumn(short colId) {
			this.m_ColId = colId;
		}
		
		public TableColumn addSubColumn(TableColumn subCol) {
			if(m_SubColumns == null) {
				m_SubColumns = new TableColumn[1];
			} else {
				TableColumn[] tmpCols = new TableColumn[m_SubColumns.length+1];

				System.arraycopy(this.m_SubColumns, 0, tmpCols, 0, m_SubColumns.length);
				tmpCols[m_SubColumns.length] = subCol;
				
				this.m_SubColumns = tmpCols;
			}
			
			return this;
		}
		
		public TableColumn setFormatExpression(String regEx) {
			this.m_FormatExpr = regEx;
			return this;
		}
		
		public String getFormatExpression() {
			return this.m_FormatExpr;
		}
		
		public TableColumn setColumnDataType(COL_DATA_TYPE colDataType) {
			this.m_ColumnDataType = colDataType;
			return this;
		}
		
		public COL_DATA_TYPE getColumnDataType() {
			return this.m_ColumnDataType;
		}
		
		public TableColumn setHidden(boolean isHidden) {
			this.m_IsHidden = isHidden;
			return this;
		}
		
		public boolean getIsHidden() {
			return this.m_IsHidden;
		}
		
		public TableColumn setDisplayName(String displayName) {
			this.m_DisplayName = displayName;
			return this;
		}
		
		public String getDisplayName() {
			return this.m_DisplayName;
		}
		
		public TableColumn setColumnName(String internalName) {
			this.m_InternalColName = internalName;
			return this;
		}
		
		public String getColumnName() {
			return this.m_InternalColName;
		}
		
		public TableColumn setIsKeyColumn(boolean isKeyColumn) {
			this.m_IsKeyColumn = isKeyColumn;
			return this;
		}
		
		public boolean isKeyColumn() {
			return this.m_IsKeyColumn;
		}
		
		public short getColId() {
			return this.m_ColId;
		}
	}
	
	private short[] _addToArray(short[] arr, short theVal) {
		short[] tmpSorts = null;
		
		if(arr == null) {
			tmpSorts = new short[1];
			tmpSorts[0] = theVal;
		} else {
			tmpSorts = new short[arr.length+1];
			System.arraycopy(arr, 0, tmpSorts, 0, arr.length);
			tmpSorts[arr.length] = theVal;
		}
		
		return tmpSorts;
	}
	
	/**
	 * Stores the display order into a separate array as a sequence of
	 * "columnID". The main column storage array is untouched when doing
	 * column re-arrangement operations
	 * 
	 * @param cols
	 */
	private void _setDisplayOrder(TableColumn... cols) {
		if(cols == null)
			return;
		
		this.m_ColDisplayOrder = new short[cols.length];
		
		for(int i=0; i<cols.length; i++)
			this.m_ColDisplayOrder[i] = cols[i].getColId();
	}
	
}
