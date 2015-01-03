package me.kingingo.kcore.Util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;

public class UtilReflection {
	
	public static void setValue(String name, Object instance, Object value){
		try{
			Field field = instance.getClass().getDeclaredField(name);
			field.setAccessible(true);
			field.set(instance, value);
		}catch(Exception e){
			System.err.println(e);
		}
	}
	
	public static Object getHandle(World world) {
	    Object nms_entity = null;
	    Method entity_getHandle = getMethod(world.getClass(), "getHandle");
	    try {
	      nms_entity = entity_getHandle.invoke(world, new Object[0]);
	    } catch (IllegalArgumentException e) {
	      e.printStackTrace();
	    } catch (IllegalAccessException e) {
	      e.printStackTrace();
	    } catch (InvocationTargetException e) {
	      e.printStackTrace();
	    }
	    return nms_entity;
	  }

	  public static Object getHandle(Entity entity) {
	    Object nms_entity = null;
	    Method entity_getHandle = getMethod(entity.getClass(), "getHandle");
	    try {
	      nms_entity = entity_getHandle.invoke(entity, new Object[0]);
	    } catch (IllegalArgumentException e) {
	      e.printStackTrace();
	    } catch (IllegalAccessException e) {
	      e.printStackTrace();
	    } catch (InvocationTargetException e) {
	      e.printStackTrace();
	    }
	    return nms_entity;
	  }
	
	public static Object getValue(String name, Object instance){
		try{
			Field field = instance.getClass().getDeclaredField(name);
			field.setAccessible(true);
			return field.get(instance);
		}catch(Exception e){
			System.err.println(e);
			return null;
		}
	}
	
	public static String getVersion() {
		String name = Bukkit.getServer().getClass().getPackage().getName();
		String version = name.substring(name.lastIndexOf('.') + 1) + ".";
		return version;
	}

	public static Class<?> getNMSClass(String className) {
		String fullName = "net.minecraft.server." + getVersion() + className;
		Class<?> clazz = null;
		try {
			clazz = Class.forName(fullName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return clazz;
	}

	public static Class<?> getOBCClass(String className) {
		String fullName = "org.bukkit.craftbukkit." + getVersion() + className;
		Class<?> clazz = null;
		try {
			clazz = Class.forName(fullName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return clazz;
	}

	public static Object getHandle(Object obj) {
		try {
			return getMethod(obj.getClass(), "getHandle", new Class[0]).invoke(obj, new Object[0]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

//	public static Field getField(Class<?> clazz, String name) {
//		try {
//			Field field = clazz.getDeclaredField(name);
//			field.setAccessible(true);
//			return field;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
//
//	public static Method getMethod(Class<?> clazz, String name, Class<?>... args) {
//		for (Method m : clazz.getMethods()) {
//			if ((m.getName().equals(name)) && ((args.length == 0) || (ClassListEqual(args, m.getParameterTypes())))) {
//				m.setAccessible(true);
//				return m;
//			}
//		}
//		return null;
//	}
	
	public static Field getField(Class<?> cl, String field_name) {
	    try {
	      return cl.getDeclaredField(field_name);
	    }
	    catch (SecurityException e) {
	      e.printStackTrace();
	    } catch (NoSuchFieldException e) {
	      e.printStackTrace();
	    }
	    return null;
	  }

	  public static Method getMethod(Class<?> cl, String method, Class<?>[] args) {
	    for (Method m : cl.getMethods()) {
	      if ((m.getName().equals(method)) && (ClassListEqual(args, m.getParameterTypes()))) {
	        return m;
	      }
	    }
	    return null;
	  }

	  public static Method getMethod(Class<?> cl, String method, Integer args) {
	    for (Method m : cl.getMethods()) {
	      if ((m.getName().equals(method)) && (args.equals(new Integer(m.getParameterTypes().length)))) {
	        return m;
	      }
	    }
	    return null;
	  }

	  public static Method getMethod(Class<?> cl, String method) {
	    for (Method m : cl.getMethods()) {
	      if (m.getName().equals(method)) {
	        return m;
	      }
	    }
	    return null;
	  }

	public static boolean ClassListEqual(Class<?>[] l1, Class<?>[] l2) {
		boolean equal = true;
		if (l1.length != l2.length) {
			return false;
		}
		for (int i = 0; i < l1.length; i++) {
			if (l1[i] != l2[i]) {
				equal = false;
				break;
			}
		}
		return equal;
	}
	
}
