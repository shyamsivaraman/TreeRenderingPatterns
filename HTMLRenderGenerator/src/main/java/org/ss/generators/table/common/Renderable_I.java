package org.ss.generators.table.common;

import java.io.IOException;
import java.io.Writer;

public interface Renderable_I {
	
	public void render(Writer out) throws IOException;
	
	public Object getContent();
	
	public void setRenderer(ChainRenderer_I renderer);

}
