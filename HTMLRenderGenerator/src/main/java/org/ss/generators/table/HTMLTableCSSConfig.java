package org.ss.generators.table;

public class HTMLTableCSSConfig {

	private String m_CTableStyle;
	private String m_TableClass;
	
	private String m_CHeaderStyle;
	private String m_HeaderClass;
	
	private String m_CRowStyle;
	private String m_RowClass;
	
	private String m_CCellStyle;
	private String m_CellClass;
	private String m_ContinuationCellStyle;
	
	{
		this.m_TableClass = "datagrid";
		this.m_CTableStyle = "width:100%;border-collapse:collapse;";
		
		this.m_CHeaderStyle = "height:30px;text-align:left;";
		this.m_HeaderClass = "datagrid_header";
		
		this.m_CRowStyle = "height:25px;border-right:1pt solid #bbb;";
		
		this.m_CCellStyle = "border-right:1pt solid #bbb;border-bottom:1pt solid #bbb;vertical-align:middle;"
				+ "border-top:1pt solid #bbb;border-left:1pt solid #bbb;"
				+ "padding:6px 5px;";
		this.m_ContinuationCellStyle = "border:0;text-decoration:none;padding-top:10px;";
	}
	
	public String getTableStyleString() {
		return this.m_CTableStyle;
	}
	
	public String getHeaderStyleString() {
		return this.m_CHeaderStyle;
	}
	
	public String getRowStyleString() {
		return this.m_CRowStyle;
	}
	
	public void addTableStyle(String styleName, String value) {
		this.m_CTableStyle = this.m_CTableStyle + styleName + ": " + value + ";";
	}
	
	public void resetTableStyle() {
		this.m_CTableStyle = "";
	}
	
	public void setTableClass(String c) {
		this.m_TableClass = c;
	}
	
	public String getTableClass() {
		return this.m_TableClass;
	}
	
	public void setHeaderClass(String c) {
		this.m_HeaderClass = c;
	}
	
	public String getHeaderClass() {
		return this.m_HeaderClass;
	}
	
	public void setRowClass(String c) {
		this.m_RowClass = c;
	}
	
	public String getRowClass() {
		return this.m_RowClass;
	}
	
	public String getCellStyle() {
		return this.m_CCellStyle;
	}
	
	public void getCellStyle(String cellStyle) {
		this.m_CCellStyle = cellStyle;
	}
	
	public String getCellClass() {
		return this.m_CellClass;
	}
	
	public void setCellClass(String cellClass) {
		this.m_CellClass = cellClass;
	}
	
	public String getContinuationCellStyle() {
		return this.m_ContinuationCellStyle;
	}
	
}
