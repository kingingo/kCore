package eu.epicpvp.kcore.kListen;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Set;

  public class kHashMap<K,V> extends AbstractMap<K,V> implements Map<K,V>, Cloneable, Serializable{

	private static final long serialVersionUID = -5979173328012264825L;

	@Override
	public Set<java.util.Map.Entry<K, V>> entrySet() {
		return this.entrySet();
	}
	  
  }