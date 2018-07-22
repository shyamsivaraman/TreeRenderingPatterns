package org.ss.generators.table;

import org.ss.generators.table.elems.abstraction.HTMLElement_A;

public class HTMLActionConfig {

	private ActionConfig[] m_ActionConfigs = new ActionConfig[0];
	
	public void addActionConfig(int colIndex, String linkPath, Class<? extends HTMLElement_A>  widgetType) {
		ActionConfig actionConfig = new ActionConfig();
		actionConfig.m_ColIdxToLink = colIndex;
		actionConfig.m_LinkPath = linkPath;
		actionConfig.m_WidgetType = widgetType;
		
		ActionConfig[] tmpCfgs = null;
		
		if(this.m_ActionConfigs == null) {
			this.m_ActionConfigs = new ActionConfig[1];
			this.m_ActionConfigs[0] = actionConfig;
			return;
		} else {
			tmpCfgs = new ActionConfig[this.m_ActionConfigs.length+1];
			System.arraycopy(this.m_ActionConfigs, 0, tmpCfgs, 0, this.m_ActionConfigs.length);
			tmpCfgs[this.m_ActionConfigs.length] = actionConfig;
		}
		
		this.m_ActionConfigs = tmpCfgs;
	}
	
	public ActionConfig[] getActionConfigs() {
		return this.m_ActionConfigs;
	}
	
	public class ActionConfig {
		int m_ColIdxToLink;
		String m_LinkPath;
		Class<? extends HTMLElement_A> m_WidgetType;
		
		public int getColIndexToLink() {
			return this.m_ColIdxToLink;
		}
		
		public String getLinkPath() {
			return this.m_LinkPath;
		}
		
		public Class<? extends HTMLElement_A> getWidgetType() {
			return this.m_WidgetType;
		}
	}
}
