package me.kingingo.kcore.PacketAPI.Packets;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.Util.SkinData;
import me.kingingo.kcore.Util.UtilReflection;
import me.kingingo.kcore.Util.UtilSkin;

import org.bukkit.plugin.java.JavaPlugin;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

public class kGameProfile extends GameProfile{
    @Getter
    @Setter
    private String skinName;
    @Getter
    @Setter
    private String skinValue;
    @Getter
    @Setter
    private String skinSignature;
    
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
	
	public void loadSkin(SkinData data){
		setSkinName(data.getSkinName());
		setSkinValue(data.getSkinValue());
		setSkinSignature(getSkinSignature());
		updateTextures();
	}
	
	public void loadSkin(JavaPlugin instance){
		loadSkin(instance,getId());
	}
	
	public void loadSkin(JavaPlugin instance,UUID uuid){
		UtilSkin.loadSkin(instance,uuid,this);
	}
	
	
	public void updateTextures(){
	    getProperties().removeAll("textures");
	    getProperties().put(getSkinName(), new Property(getSkinName(), getSkinValue(), getSkinSignature()));
	}
}
