package eu.epicpvp.iterator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import lombok.Getter;

public class InfinityIterator<E> implements Iterator<E>{
	@Getter
	private int index;
	@Getter
	private List<E> elements;
	
	public InfinityIterator(List<E> elements) {
		this.elements = new ArrayList<>(elements); //Copy
	}
	
	@Override
	public boolean hasNext() {
		return elements.size() > 0;
	}

	@Override
	public E next() {
		if(!hasNext())
			throw new UnsupportedOperationException("End reached");
		return elements.get(index++%elements.size());
	}
}
