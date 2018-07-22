package org.ss.generators.table.render;

import org.ss.generators.table.common.PipelineProcess_I;

public class ProcessorPipeline {

	private PipelineProcess_I m_Processors[] = new PipelineProcess_I[0];
	
	public void process() {
		for(int i=0; i<this.m_Processors.length; i++) {
			this.m_Processors[i].process((i==0) ? null : this.m_Processors[i-1]);
		}
	}
	
	public PipelineProcess_I getTailProcessor() {
		if(this.m_Processors.length == 0)
			return null;
		
		return this.m_Processors[this.m_Processors.length-1];
	}
	
	public void addProcessor(PipelineProcess_I processor) {
		this.m_Processors = this._addToArray(this.m_Processors, processor, new PipelineProcess_I[this.m_Processors.length+1]);
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
