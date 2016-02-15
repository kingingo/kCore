package me.kingingo.kcore.Util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;
import java.util.logging.Level;

import me.kingingo.kcore.PacketAPI.Packets.kGameProfile;
import me.kingingo.kcore.kConfig.kConfig;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

public class UtilSkin {
	
	private static kConfig config;
	
	public static void loadConfig(JavaPlugin instance){
		if(config==null){
			config=new kConfig(UtilFile.getYMLFile(instance, "skins"));
		}
	}
	
	public static void setSkinPlayer(Player player, SkinData data){
		GameProfile profile=((CraftPlayer)player).getHandle().getProfile();
		
		profile.getProperties().removeAll("textures");
		profile.getProperties().put(data.getSkinName(), new Property(data.getSkinName(), data.getSkinValue(), data.getSkinSignature()));
	}
	
	public static SkinData loadSkin(JavaPlugin instance,UUID uuid){
		loadConfig(instance);
		
		SkinData data = new SkinData(uuid);
		
//		if(config.contains("Skin."+uuid)){
//			data.setSkinName(config.getString("Skin."+uuid+".name"));
//			data.setSkinValue(config.getString("Skin."+uuid+".value"));
//			data.setSkinSignature(config.getString("Skin."+uuid+".signature"));
//		}else{ 
			try{
		      URL url = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid.toString().replaceAll("-", "") + "?unsigned=false");
		      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		      connection.setRequestMethod("GET");
		      connection.setRequestProperty("Content-Type", "application/json");
		      connection.setUseCaches(false);
		      connection.setDoInput(true);
		      connection.setDoOutput(true);
		      
		      InputStreamReader inr = new InputStreamReader(connection.getInputStream());
		      BufferedReader reader = new BufferedReader(inr);
		      String s = null;
		      StringBuilder sb = new StringBuilder();
		      
		      while ((s = reader.readLine()) != null) {
		           sb.append(s);
		        }
		        
			   	JsonElement element = new JsonParser().parse(sb.toString());
			   	JsonArray pro = (JsonArray)((JsonObject)element.getAsJsonObject()).get("properties");
				 for (int i = 0; i < pro.size(); i++) {
				 try {
					 JsonObject property = (JsonObject)pro.get(i);
					 data.setSkinName((String)property.get("name").getAsString());
					 data.setSkinValue((String)property.get("value").getAsString());
				     data.setSkinSignature(property.has("signature") ? (String)property.get("signature").getAsString() : null);
				     
				     config.set("Skin."+uuid+".name", data.getSkinName());
				     config.set("Skin."+uuid+".value", data.getSkinValue());
				     config.set("Skin."+uuid+".signature", data.getSkinSignature());
				     config.save();
					}catch (Exception e){
						Bukkit.getLogger().log(Level.WARNING, "Failed to apply auth property", e);
					} 
				}
				     
			         inr.close();
			         reader.close();
			         connection.disconnect();
				     return data;
		    }
		    catch (Exception ex) {
		    	System.out.println("[UtilSkin] Error: ");
		    	ex.printStackTrace();
		    }
//		}
		
	    return data;
  }
	
	public static void loadSkin(JavaPlugin instance,UUID uuid,kGameProfile profile){
		loadConfig(instance);
		
//		if(config.contains("Skin."+uuid)){
//			profile.setSkinName(config.getString("Skin."+uuid+".name"));
//   		 	profile.setSkinValue(config.getString("Skin."+uuid+".value"));
//   		 	profile.setSkinSignature(config.getString("Skin."+uuid+".signature"));
//		}else{
			try
		    {
		      URL url = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid.toString().replaceAll("-", "") + "?unsigned=false");
		      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		      connection.setRequestMethod("GET");
		      connection.setRequestProperty("Content-Type", "application/json");
		      connection.setUseCaches(false);
		      connection.setDoInput(true);
		      connection.setDoOutput(true);
		      
		      InputStreamReader inr = new InputStreamReader(connection.getInputStream());
		      BufferedReader reader = new BufferedReader(inr);
		      String s = null;
		      StringBuilder sb = new StringBuilder();
		      
		      while ((s = reader.readLine()) != null) {
		           sb.append(s);
		        }
		        
		      try{
			         JsonElement element = new JsonParser().parse(sb.toString());
				     JsonArray pro = (JsonArray)((JsonObject)element.getAsJsonObject()).get("properties");
				     
				     for (int i = 0; i < pro.size(); i++) {
				    	 try {
				    		 JsonObject property = (JsonObject)pro.get(i);
				    		 profile.setSkinName((String)property.get("name").getAsString());
				    		 profile.setSkinValue((String)property.get("value").getAsString());
				    		 profile.setSkinSignature(property.has("signature") ? (String)property.get("signature").getAsString() : null);
				    		 
						     config.set("Skin."+uuid+".name", profile.getSkinName());
						     config.set("Skin."+uuid+".value", profile.getSkinValue());
						     config.set("Skin."+uuid+".signature", profile.getSkinSignature());
						     config.save();
					        }
					        catch (Exception e){
					          Bukkit.getLogger().log(Level.WARNING, "Failed to apply auth property", e);
					        } 
				     }
				     
				     profile.updateTextures();
			         inr.close();
			         reader.close();
			         connection.disconnect();
				     
		        }catch(IllegalStateException e){
		        	e.printStackTrace();
		        }
		    }
		    catch (Exception ex) {
		    	ex.printStackTrace();
		    }
//		}  
	  }
	
}
