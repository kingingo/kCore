package me.kingingo.kcore.Util;

import java.lang.reflect.Field;

import org.bukkit.block.Skull;
import org.bukkit.craftbukkit.v1_7_R4.block.CraftSkull;

import net.minecraft.util.com.mojang.authlib.GameProfile;

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
	
}
