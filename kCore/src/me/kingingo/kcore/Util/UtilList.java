package me.kingingo.kcore.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;

public class UtilList {
	
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
