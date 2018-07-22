package org.ss.generators.table.render.abstraction;

import org.ss.generators.table.common.ChainRenderer_I;

public abstract class ChainRenderer_A implements ChainRenderer_I {
	
	private ChainRenderer_I nextRenderer;
	
	public ChainRenderer_I getNextRenderer() {
		return this.nextRenderer;
	}
	
	public void setNextRenderer(ChainRenderer_I renderer) {
		this.nextRenderer = renderer;
	}
	
}
