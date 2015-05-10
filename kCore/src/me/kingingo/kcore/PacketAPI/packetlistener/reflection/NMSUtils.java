/*
 * Copyright 2015 Marvin Schäfer (inventivetalent). All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 *
 *    1. Redistributions of source code must retain the above copyright notice, this list of
 *       conditions and the following disclaimer.
 *
 *    2. Redistributions in binary form must reproduce the above copyright notice, this list
 *       of conditions and the following disclaimer in the documentation and/or other materials
 *       provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ''AS IS'' AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * The views and conclusions contained in the software and documentation are those of the
 * authors and contributors and should not be interpreted as representing official policies,
 * either expressed or implied, of anybody else.
 */

package me.kingingo.kcore.PacketAPI.packetlistener.reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.bukkit.Bukkit;

public class NMSUtils {

	private static String	version	= getVersion();

	public static String getVersion() {
		if (version != null) return version;
		String name = Bukkit.getServer().getClass().getPackage().getName();
		String version = name.substring(name.lastIndexOf('.') + 1) + ".";
		return version;
	}

	public static Class<?> getNMSClassWithException(String className) throws Exception {
		String fullName = "net.minecraft.server." + getVersion() + className;
		return Class.forName(fullName);
	}

	public static Class<?> getNMSClass(String className) {
		Class<?> clazz = null;
		try {
			clazz = getNMSClassWithException(className);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return clazz;
	}

	public static Class<?> getOBCClassWithException(String className) throws Exception {
		String fullName = "org.bukkit.craftbukkit." + getVersion() + className;
		return Class.forName(fullName);
	}

	public static Class<?> getOBCClass(String className) {
		Class<?> clazz = null;
		try {
			clazz = getOBCClassWithException(className);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return clazz;
	}

	public static Object getHandle(Object obj) {
		try {
			return getMethod(obj.getClass(), "getHandle").invoke(obj);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private static Class<?>	c	= getOBCClass("block.CraftBlock");
	private static Method	m	= getMethod(c, "getNMSBlock");

	public static Object getBlockHandleWithException(Object obj) throws Exception {
		return m.invoke(obj);
	}

	public static Object getBlockHandle(Object obj) {
		try {
			return m.invoke(obj);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Field getFieldWithException(Class<?> clazz, String name) throws Exception {
		Field field = clazz.getDeclaredField(name);
		field.setAccessible(true);
		Field modifiersField = Field.class.getDeclaredField("modifiers");
		modifiersField.setAccessible(true);
		int modifiers = modifiersField.getInt(field);
		modifiers &= ~Modifier.FINAL;
		modifiersField.setInt(field, modifiers);
		return field;
	}

	public static Field getField(Class<?> clazz, String name) {
		try {
			return getFieldWithException(clazz, name);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Field getFieldOfTypeWithException(Class<?> clazz, Class<?> type, String name) throws Exception {
		Field field = clazz.getDeclaredField(name);
		if (!field.getType().equals(type)) throw new Exception("Field Not Found");
		field.setAccessible(true);
		Field modifiersField = Field.class.getDeclaredField("modifiers");
		modifiersField.setAccessible(true);
		int modifiers = modifiersField.getInt(field);
		modifiers &= ~Modifier.FINAL;
		modifiersField.setInt(field, modifiers);
		return field;
	}

	public static Field getFieldOfType(Class<?> clazz, Class<?> type, String name) {
		try {
			return getFieldOfTypeWithException(clazz, type, name);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Field getFirstFieldOfTypeWithException(Class<?> clazz, Class<?> type) throws Exception {
		for (Field field : clazz.getDeclaredFields()) {
			if (field.getType().equals(type)) {
				field.setAccessible(true);
				Field modifiersField = Field.class.getDeclaredField("modifiers");
				modifiersField.setAccessible(true);
				int modifiers = modifiersField.getInt(field);
				modifiers &= ~Modifier.FINAL;
				modifiersField.setInt(field, modifiers);
				return field;
			}
		}
		throw new Exception("Field Not Found");
	}

	public static Field getFirstFieldOfType(Class<?> clazz, Class<?> type) {
		try {
			return getFirstFieldOfTypeWithException(clazz, type);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Field getLastFieldOfTypeWithException(Class<?> clazz, Class<?> type) throws Exception {
		Field field = null;
		for (Field f : clazz.getDeclaredFields()) {
			if (f.getType().equals(type)) {
				field = f;
			}
		}
		if (field == null) throw new Exception("Field Not Found");
		field.setAccessible(true);
		Field modifiersField = Field.class.getDeclaredField("modifiers");
		modifiersField.setAccessible(true);
		int modifiers = modifiersField.getInt(field);
		modifiers &= ~Modifier.FINAL;
		modifiersField.setInt(field, modifiers);
		return field;
	}

	public static Field getLastFieldOfType(Class<?> clazz, Class<?> type) {
		try {
			return getLastFieldOfTypeWithException(clazz, type);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Method getMethod(Class<?> clazz, String name, Class<?>... args) {
		for (Method m : clazz.getMethods())
			if (m.getName().equals(name) && (args.length == 0 || ClassListEqual(args, m.getParameterTypes()))) {
				m.setAccessible(true);
				return m;
			}
		for (Method m : clazz.getDeclaredMethods())
			if (m.getName().equals(name) && (args.length == 0 || ClassListEqual(args, m.getParameterTypes()))) {
				m.setAccessible(true);
				return m;
			}
		return null;
	}

	public static boolean ClassListEqual(Class<?>[] l1, Class<?>[] l2) {
		boolean equal = true;
		if (l1.length != l2.length) return false;
		for (int i = 0; i < l1.length; i++)
			if (l1[i] != l2[i]) {
				equal = false;
				break;
			}
		return equal;
	}

	public static Class<?> getInnerClass(Class<?> c, String className) {
		Class<?> clazz = null;
		try {
			for (Class<?> cl : c.getDeclaredClasses()) {
				if (cl.getSimpleName().equals(className)) {
					clazz = cl;
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return clazz;
	}

	public static Constructor<?> getConstructor(Class<?> clazz, Class<?>... args) {
		for (Constructor<?> c : clazz.getConstructors()) {
			if (args.length == 0 || ClassListEqual(args, c.getParameterTypes())) {
				c.setAccessible(true);
				return c;
			}
		}
		for (Constructor<?> c : clazz.getDeclaredConstructors()) {
			if (args.length == 0 || ClassListEqual(args, c.getParameterTypes())) {
				c.setAccessible(true);
				return c;
			}
		}
		return null;
	}

}
