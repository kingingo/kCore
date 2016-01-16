package me.kingingo.kcore.Util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Level;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.PacketAPI.Packets.kGameProfile;

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
	
	public static void setSkinPlayer(Player player, SkinData data){
		GameProfile profile=((CraftPlayer)player).getHandle().getProfile();
		
		profile.getProperties().removeAll("textures");
		profile.getProperties().put(data.getSkinName(), new Property(data.getSkinName(), data.getSkinValue(), data.getSkinSignature()));
	}
	
	public static SkinData loadSkin(JavaPlugin instance,UUID uuid){
		SkinData data = new SkinData(uuid);
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
	        
		   	JsonElement element = new JsonParser().parse(sb.toString());
		   	JsonArray pro = (JsonArray)((JsonObject)element.getAsJsonObject()).get("properties");
			 for (int i = 0; i < pro.size(); i++) {
			 try {
				 JsonObject property = (JsonObject)pro.get(i);
				 data.setSkinName((String)property.get("name").getAsString());
				 data.setSkinValue((String)property.get("value").getAsString());
			     data.setSkinSignature(property.has("signature") ? (String)property.get("signature").getAsString() : null);
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
	    return data;
  }
	
	public static void loadSkin(JavaPlugin instance,UUID uuid,kGameProfile profile){
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
	  }
	
}
