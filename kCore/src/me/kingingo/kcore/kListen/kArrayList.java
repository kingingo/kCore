package me.kingingo.kcore.kListen;

import java.util.AbstractList;
import java.util.List;
import java.util.RandomAccess;
   
public class kArrayList<E> extends AbstractList<E> implements List<E>, RandomAccess, Cloneable, java.io.Serializable {

	private static final long serialVersionUID = 9068208470331591592L;

	@Override
	public E get(int arg0) {
		return this.get(arg0);
	}

	@Override
	public int size() {
		return this.size();
	}
	   
}