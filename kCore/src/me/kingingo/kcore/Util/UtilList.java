package me.kingingo.kcore.Util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64OutputStream;
import org.bukkit.entity.Player;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class UtilList {
	
  //UtilList.serialize( HASHMAP ) || UtilList.serialize( String )
  public static String serialize(Object object) throws IOException {
	    ByteArrayOutputStream byteaOut = new ByteArrayOutputStream();
	    GZIPOutputStream gzipOut = null;
	    try {
	        gzipOut = new GZIPOutputStream(new Base64OutputStream(byteaOut));
	        gzipOut.write(new Gson().toJson(object).getBytes("UTF-8"));
	    } finally {
	        if (gzipOut != null) try { gzipOut.close(); } catch (IOException logOrIgnore) {}
	    }
	    return new String(byteaOut.toByteArray());
	}

  	//UtilList.deserialize(STRING, new TypeToken<HashMap<Object,Object>(){}); || UtilList.deserialize(STRING, new TypeToken<HashMap<String,Player>(){});
	public static <T> T deserialize(String string, TypeToken type) throws IOException {
	    ByteArrayOutputStream byteaOut = new ByteArrayOutputStream();
	    GZIPInputStream gzipIn = null;
	    try {
	        gzipIn = new GZIPInputStream(new Base64InputStream(new ByteArrayInputStream(string.getBytes("UTF-8"))));
	        for (int data; (data = gzipIn.read()) > -1;) {
	            byteaOut.write(data);
	        }
	    } finally {
	        if (gzipIn != null) try { gzipIn.close(); } catch (IOException logOrIgnore) {}
	    }
	    return new Gson().fromJson(new String(byteaOut.toByteArray()), type.getType());
	}
	
	public static void CleanList(ArrayList<?> list){
		if(list.isEmpty())return;
		if(list.get(0) instanceof UUID){
			boolean b = false;
			for(int i = 0; i<list.size(); i++){
				b=false;
				for(Player player : UtilServer.getPlayers()){
					if(UtilPlayer.getRealUUID(player).equals( ((UUID)list.get(0)) )){
						b=true;
						break;
					}
				}
				
				if(!b){
					list.remove(i);
				}
			}
		}else if(list.get(0) instanceof Player){
			boolean b = false;
			for(int i = 0; i<list.size(); i++){
				b=false;
				for(Player player : UtilServer.getPlayers()){
					if(player.getName().equalsIgnoreCase( ((Player)list.get(0)).getName() )){
						b=true;
						break;
					}
				}
				
				if(!b){
					list.remove(i);
				}
			}
		}else if(list.get(0) instanceof String){
			boolean b = false;
			for(int i = 0; i<list.size(); i++){
				b=false;
				for(Player player : UtilServer.getPlayers()){
					if(player.getName().equalsIgnoreCase( ((String)list.get(0)) )){
						b=true;
						break;
					}
				}
				
				if(!b){
					list.remove(i);
				}
			}
		}
	}
	
	public static void CleanList(HashMap<?,?> list){
		if(list.isEmpty())return;
		if(list.keySet().toArray()[0] instanceof UUID){
			boolean b = false;
			for(int i = 0; i<list.size(); i++){
				b=false;
				for(Player player : UtilServer.getPlayers()){
					if(UtilPlayer.getRealUUID(player).equals( ((UUID)list.keySet().toArray()[i]) )){
						b=true;
						break;
					}
				}
				
				if(!b){
					list.remove(i);
				}
			}
		}else if(list.keySet().toArray()[0] instanceof Player){
			boolean b = false;
			for(int i = 0; i<list.size(); i++){
				b=false;
				for(Player player : UtilServer.getPlayers()){
					if(player.getName().equalsIgnoreCase( ((Player)list.keySet().toArray()[i]).getName() )){
						b=true;
						break;
					}
				}
				
				if(!b){
					list.remove(i);
				}
			}
		}else if(list.keySet().toArray()[0] instanceof String){
			boolean b = false;
			for(int i = 0; i<list.size(); i++){
				b=false;
				for(Player player : UtilServer.getPlayers()){
					if(player.getName().equalsIgnoreCase( ((String)list.keySet().toArray()[i]) )){
						b=true;
						break;
					}
				}
				
				if(!b){
					list.remove(i);
				}
			}
		}
	}
	
}
