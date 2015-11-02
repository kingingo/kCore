package me.kingingo.kcore.PacketAPI.Packets;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;
import java.util.logging.Level;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.Util.UtilReflection;
import me.kingingo.kcore.Util.UtilSkin;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
