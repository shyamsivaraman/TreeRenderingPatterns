package org.ss.generators.table;

import java.util.HashMap;

public class HTMLProcessingContext {
	
	public enum PROVIDER {LINK_CONFIG_PROVIDER, CSS_CONFIG_PROVIDER};
	
	private HashMap<String, Object> m_EnvMap = new HashMap<String, Object>();
	private HashMap<PROVIDER, Provider_I> m_Providers = new HashMap<PROVIDER, Provider_I>();
	
	public <T> T getProvider(PROVIDER providerType, T clazz) {
		return (T)m_Providers.get(providerType);
	}
	
	public void addProvider(PROVIDER providerType, Provider_I provider) {
		m_Providers.put(providerType, provider);
	}
	
	public void addEnvObject(String envId, Object envObj) {
		this.m_EnvMap.put(envId, envObj);
	}
	
	public Object getEnvObject(String envId) {
		return this.m_EnvMap.get(envId);
	}
	
	public static interface Provider_I { }
	
	/**
	 * Dictates which columns to link. The 'how to link' is managed by the LinkProcessor
	 */
	public static class HTMLLinkConfigProvider implements Provider_I {
		private short m_linkedCols = -1;
		
		public HTMLLinkConfigProvider() { }
		
		public void setColsToLink(short linkedCols) {
			this.m_linkedCols = linkedCols;
		}
		
		public short getColsToLink() {
			return this.m_linkedCols;
		}
	}
	
	public static class HTMLCSSConfigProvider implements Provider_I {
		private String m_Value;
		public HTMLCSSConfigProvider() {}
		public HTMLCSSConfigProvider(String val) {
			this.m_Value = val;
		}
		public void print() {
			System.out.println("CSS config provider => " + this.m_Value);
		}
	}
	
	public static void main(String[] args) {
		HTMLProcessingContext hpc = new HTMLProcessingContext();
		hpc.addProvider(PROVIDER.LINK_CONFIG_PROVIDER, null);
		hpc.addProvider(PROVIDER.CSS_CONFIG_PROVIDER, null);
		
		hpc.getProvider(PROVIDER.LINK_CONFIG_PROVIDER, new HTMLLinkConfigProvider());
	}

}
