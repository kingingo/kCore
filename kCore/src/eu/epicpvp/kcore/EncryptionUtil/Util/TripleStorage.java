package eu.epicpvp.kcore.EncryptionUtil.Util;

import java.util.ArrayList;
import java.util.List;
/**
 * @author Ch4t4r | Daniel Wolf
 * @license Free for commercial use. Do not distribute.
 */
public class TripleStorage<V,K,B> {

	protected List<V> vList = new ArrayList<V>();
	protected List<K> kList = new ArrayList<K>();
	protected List<B> bList = new ArrayList<B>();
	
	public final void add(V v,K k,B b){
		vList.add(v);
		kList.add(k);
		bList.add(b);
	}
	
	public final void remove(int i){
		vList.remove(i);
		kList.remove(i);
		bList.remove(i);
	}
	
	public final V getFirst(int i){
		return vList.get(i);
	}
	
	public final K getSecond(int i){
		return kList.get(i);
	}
	
	public final B getThird(int i){
		return bList.get(i);
	}
	
	public final void clear(){
		kList.clear();
		vList.clear();
		bList.clear();
	}
}
