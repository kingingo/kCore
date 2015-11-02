package me.kingingo.kcore.Util;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;
import java.util.logging.Level;

import me.kingingo.kcore.PacketAPI.Packets.kGameProfile;
import me.kingingo.kcore.kConfig.kConfig;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class UtilSkin {

//	private static kConfig config;
	
	public static void loadSkin(JavaPlugin instance,UUID uuid,kGameProfile profile){
//		if(config==null)config=new kConfig(UtilFile.getYMLFile(instance, "skins"));
//		
//		if(config.contains(uuid.toString())){
//			profile.setSkinName(config.getString(uuid.toString()+".name"));
//			profile.setSkinValue(config.getString(uuid.toString()+".value"));
//   		 	profile.setSkinSignature(config.contains(uuid.toString()+".signature") ? config.getString(uuid.toString()+".signature") : null);
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
					        }
					        catch (Exception e){
					          Bukkit.getLogger().log(Level.WARNING, "Failed to apply auth property", e);
					        } 
				     }
				     
//				     config.set(uuid.toString()+".name", profile.getSkinName());
//				     config.set(uuid.toString()+".value", profile.getSkinValue());
//				     config.set(uuid.toString()+".signature", profile.getSkinSignature());
//				     config.save();
				     
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
