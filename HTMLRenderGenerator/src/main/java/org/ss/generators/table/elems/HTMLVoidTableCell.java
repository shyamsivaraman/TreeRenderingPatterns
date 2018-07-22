package org.ss.generators.table.elems;

import java.io.IOException;
import java.io.Writer;

public class HTMLVoidTableCell extends HTMLTableCell {

	public HTMLVoidTableCell() {
		super("");
	}
	
	@Override
	public void render(Writer out) throws IOException {
		return;
	}
}
