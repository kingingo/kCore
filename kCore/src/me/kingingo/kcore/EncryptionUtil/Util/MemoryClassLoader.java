package me.kingingo.kcore.EncryptionUtil.Util;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MemoryClassLoader extends ClassLoader{
	private byte[] current = null;
	private String origin;
	protected final HashMap<String,Class> classes = new HashMap<String,Class>();
	protected final HashMap<String,String> files = new HashMap<String,String>();
	protected List<Class> lastLoaded = new ArrayList<Class>();
	
	public MemoryClassLoader(){
		super(ClassLoader.getSystemClassLoader());
	}

	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		if(name.startsWith("java"))return ClassLoader.getSystemClassLoader().loadClass(name);
		else if(classes.containsKey(name))return classes.get(name);
		Class res = null;
		try {
			res = defineClass(name, current,0,current.length,null);
		}catch(Exception e){
			System.out.println("An error occured while loading the class: " + e.getMessage());
		}
		classes.put(name, res);
		files.put(name, origin);
		return res;
	}

	@Override
	public Class<?> loadClass(String name) throws ClassNotFoundException {
		try{
			return this.getParent().loadClass(name);
		}catch(Exception e){
			return findClass(name);
		}
	}
	
	public final void setContents(final byte[] data,final File origin){
		this.origin = origin.getPath();
		current = new byte[data.length];
		System.arraycopy(data, 0, current, 0, data.length);
	}
	
	public final void setContents(final byte[] data,final String origin){
		this.origin = origin;
		current = new byte[data.length];
		System.arraycopy(data, 0, current, 0, data.length);
	}
	
	public final List<String> getLoadedClassNames(){
		return new ArrayList<String>(Arrays.asList(classes.keySet().toArray(new String[classes.size()])));
	}
	
	public final List<Class> getLoadedClasses(){
		return new ArrayList<Class>(Arrays.asList(classes.values().toArray(new Class[classes.size()])));
	}
	
	public final String getOriginFile(final Class c){
		return files.get(c.getName());
	}
	
	public final void destroy(){
		current = null;classes.clear();
	}
}
