package me.kingingo.kcore.Util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;

public class UtilReflection {
	
	public static void setValue(String name, Object instance, Object value){
		setValue(instance.getClass(),name, instance, value);
	}
	
	public static void setValue(Class c,String name, Object instance, Object value){
		try{
			Field field = c.getDeclaredField(name);
			field.setAccessible(true);
			field.set(instance, value);
		}catch(Exception e){
			System.err.println(e);
		}
	}
	
	public static void setStaticValue(Field field, Object value){
	    try
	    {
	      Field modifier = Field.class.getDeclaredField("modifiers");

	      modifier.setAccessible(true);
	      modifier.setInt(field, field.getModifiers() & 0xFFFFFFEF);
	      field.set(null, value); } catch (Exception ex) {
	    }
	  }

	  public static void setPrivateValue(Object obj, String name, Object value) {
	    try {
	      Field field = obj.getClass().getDeclaredField(name);
	      field.setAccessible(true);
	      field.set(obj, value);
	    }
	    catch (IllegalArgumentException ex) {
	    }
	    catch (IllegalAccessException ex) {
	    }
	    catch (Exception ex) {
	    }
	  }

	  public static Object getPrivateValue(Object obj, String name) {
	    Field field = null;
	    Class clazz = obj.getClass();
	    try
	    {
	      do {
	        field = clazz.getDeclaredField(name);
	        clazz = clazz.getSuperclass();
	      }while ((field == null) && (clazz != null));
	      field.setAccessible(true);
	      return field.get(obj); } catch (Exception ex) {
	    }
	    return null;
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

	public static Object getValue( String name, Object instance){
		return getValue(instance.getClass(), name, instance);
	}
	  
	public static Object getValue(Class c, String name, Object instance){
		try{
			Field field = c.getDeclaredField(name);
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

	  public static Object createNMSTextComponent(String text) {
			if (text == null || text.isEmpty()) {
				return null;
			}

			Class c = getClassByName(getNMSPackageName() + ".ChatComponentText");
			try {
				Constructor constructor = c.getDeclaredConstructor(String.class);
				return constructor.newInstance(text);
			} catch (Exception ex) {
				ex.printStackTrace();
				return null;
			}
		}
	  
	  public static Class getClassByName(String name) {
			try {
				return Class.forName(name);
			} catch (Exception e) {
				// Class not found
				return null;
			}
		}
	  
	  public static String getNMSPackageName() {
			return "net.minecraft.server." + Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
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
