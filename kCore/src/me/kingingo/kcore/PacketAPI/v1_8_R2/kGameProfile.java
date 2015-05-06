package me.kingingo.kcore.PacketAPI.v1_8_R2;

import java.util.UUID;

import me.kingingo.kcore.Util.UtilReflection;

import com.mojang.authlib.GameProfile;

public class kGameProfile extends GameProfile{

	public kGameProfile(UUID uuid,String name){
		super(uuid,name);
	}
	
	public void setUUID(UUID uuid){
		UtilReflection.setValue("id", this, uuid);
	}
	
	public void setName(String name){
		UtilReflection.setValue("name", this, name);
	}
	
	public void setLegacy(boolean flag){
		UtilReflection.setValue("legacy", this, flag);
	}
	
}
