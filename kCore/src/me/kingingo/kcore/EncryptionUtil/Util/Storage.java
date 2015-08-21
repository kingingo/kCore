package me.kingingo.kcore.EncryptionUtil.Util;

import java.util.ArrayList;
import java.util.List;
/**
 * @author Ch4t4r | Daniel Wolf
 * @license Free for commercial use. Do not distribute.
 */
public class Storage<V,K> {

	protected List<V> vList = new ArrayList<V>();
	protected List<K> kList = new ArrayList<K>();
	
	public final void add(V v,K k){
		vList.add(v);
		kList.add(k);
	}
	
	public final void remove(int i){
		vList.remove(i);
		kList.remove(i);
	}
	
	public final V getFirst(int i){
		return vList.get(i);
	}
	
	public final K getSecond(int i){
		return kList.get(i);
	}
	
	public final void clear(){
		kList.clear();
		vList.clear();
	}
}
