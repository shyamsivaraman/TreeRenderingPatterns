package org.ss.generators.table;

import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

import org.ss.generators.table.common.ChainRenderer_I;

/**
 * Renderer priority - 1 (Low) --- |n| (High)
 * Column Renderer having high priority always
 */

public class HTMLRendererRegistry {
	
	public static enum RENDERER_GRANULARITY {DATA, COLUMN};

	private boolean isBuilt = false;
	private static final HTMLRendererRegistry m_Instance = new HTMLRendererRegistry();
	private HashMap<Class<?>, PriorityQueue<TypedRenderer>> rdrMap = new HashMap<>();
	private HashMap<Class<?>, ChainRenderer_I> rendererChain = new HashMap<>();
	private HashMap<Short, TypedRenderer> colRMap = new HashMap<>();
	
	public ChainRenderer_I getRenderer(Class<?> targetClass) {
		return this.getRenderer(targetClass, (short)-1);
	}
	
	public ChainRenderer_I getRenderer(Class<?> targetClass, short colIndex) {
		if(!isBuilt)
			build();
		
		ChainRenderer_I renderer = null;
		
		if(colIndex > -1 && colRMap.get(colIndex) != null)
			renderer = colRMap.get(colIndex).renderer;
		
		if(renderer == null)
			return rendererChain.get(targetClass);
		
		return renderer;
	}
	
	public void registerDataRenderer(Class<?> clazz, ChainRenderer_I renderer, short priority) {
		if(isBuilt)
			return;
		
		PriorityQueue<TypedRenderer> priorityQ = getRendererQueue(clazz);
		priorityQ.add(new TypedRenderer(priority, renderer, RENDERER_GRANULARITY.DATA));
	}
	
	public void registerColumnRenderer(short colIndex, ChainRenderer_I renderer, short priority) {
		if(isBuilt)
			return;
		
		this.colRMap.put(colIndex, new TypedRenderer(priority, renderer, RENDERER_GRANULARITY.COLUMN));
	}
	
	public static HTMLRendererRegistry getInstance() {
		return m_Instance;
	}
	
	public void build() {
		if(isBuilt)
			return;
		
		for(Class<?> clazz : rdrMap.keySet()) {
			PriorityQueue<TypedRenderer> dataRenderers = rdrMap.get(clazz);
			if(dataRenderers == null)
				continue;
			
			TypedRenderer tRenderer = null;
			ChainRenderer_I headRenderer = null;
			ChainRenderer_I curRenderer = null;
			while((tRenderer = dataRenderers.poll()) != null) {
				if(curRenderer == null) {
					curRenderer = tRenderer.renderer;
					headRenderer = tRenderer.renderer;
					continue;
				}
				
				ChainRenderer_I next = tRenderer.renderer;
				curRenderer.setNextRenderer(next);
				curRenderer = next;
			}
			
			this.rendererChain.put(clazz, headRenderer);
		}
		
		this.isBuilt = true;
	}
	
	private PriorityQueue<TypedRenderer> getRendererQueue(Class<?> clazz) {
		PriorityQueue<TypedRenderer> priorityQ = null;
		if(this.rdrMap.get(clazz) == null) {
			priorityQ = new PriorityQueue<>(new Comparator<TypedRenderer>() {
				@Override
				public int compare(TypedRenderer o1, TypedRenderer o2) {
					return (o1.priority == o2.priority) ? 0 : (o1.priority > o2.priority) ? -1 : 1;
				}
			});
			
			this.rdrMap.put(clazz, priorityQ);
		} else {
			priorityQ = this.rdrMap.get(clazz);
		}
		
		return priorityQ;
	}
	
	private static class TypedRenderer {
		private short priority;
		private ChainRenderer_I renderer;
		private RENDERER_GRANULARITY type;
		private short colIndex;
		
		TypedRenderer(short p, ChainRenderer_I r, RENDERER_GRANULARITY t) {
			this.priority = p;
			this.renderer = r;
			this.type = t;
		}
	}

	public void clearRegistry() {
		this.colRMap.clear();
		this.rdrMap.clear();
		this.rendererChain.clear();
		this.isBuilt = false;
	}

}
