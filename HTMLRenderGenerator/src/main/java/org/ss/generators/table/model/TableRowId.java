package org.ss.generators.table.model;

import java.util.HashMap;
import java.util.Set;

public class TableRowId {

	private HashMap<String, Object> keyValueMap = new HashMap<>();
	
	public void addKeyValue(String column, Object value) {
		this.keyValueMap.put(column, value);
	}
	
	public Set<String> getKeyColumns() {
		return this.keyValueMap.keySet();
	}
	
	public Object getValue(String key) {
		return this.keyValueMap.get(key);
	}
	
	public java.sql.Timestamp getTimestampValue(String key)
	{
		Object aValue = this.getValue(key);
		if (aValue == null)
			return null;

		if (aValue instanceof java.sql.Timestamp)
			return (java.sql.Timestamp) aValue;
		else if (aValue instanceof java.util.Date)
			return new java.sql.Timestamp(((java.util.Date) aValue).getTime());
		else if (aValue instanceof Number)
			return new java.sql.Timestamp(((Number) aValue).longValue());

		return new java.sql.Timestamp(new java.util.Date(aValue.toString()).getTime());
	}
	
	public java.sql.Date getDateValue(String key)
	{
		Object aValue = this.getValue(key);
		if (aValue == null)
			return null;

		if (aValue instanceof java.sql.Date)
			return (java.sql.Date) aValue;
		else if (aValue instanceof java.util.Date)
			return new java.sql.Date(((java.util.Date) aValue).getTime());
		else if (aValue instanceof Number)
			return new java.sql.Date(((Number) aValue).longValue());

		return new java.sql.Date(new java.util.Date(aValue.toString()).getTime());
	}
}
