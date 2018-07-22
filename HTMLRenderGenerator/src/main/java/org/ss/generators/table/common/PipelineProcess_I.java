package org.ss.generators.table.common;

import java.io.IOException;
import java.io.Writer;

import org.ss.generators.table.elems.abstraction.XMLElement_A;

public interface PipelineProcess_I {

	public void process(PipelineProcess_I prevProcess);
	
	public void generate(Writer out) throws IOException;
	
	public XMLElement_A getProcessedElement();
}
