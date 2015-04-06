package me.kingingo.kcore.TimeManager;

import java.util.HashMap;
import java.util.UUID;

import lombok.Getter;
import me.kingingo.kcore.Listener.kListener;
import me.kingingo.kcore.Permission.PermissionManager;
import me.kingingo.kcore.Util.UtilPlayer;
import me.kingingo.kcore.Util.UtilTime;

import org.bukkit.entity.Player;

public class TimeManager extends kListener{

	@Getter
	private HashMap<String,HashMap<UUID,Long>> timer = new HashMap<>();
	private HashMap<String,HashMap<UUID,Long>> perms = new HashMap<>();
	private PermissionManager manager;
	
	public TimeManager(PermissionManager manager){
		super(manager.getInstance(),"TimeManager");
		this.manager=manager;
	}

	public Long hasPermission(Player player,String typ){
		typ=typ.toLowerCase();
		if(!perms.containsKey(typ))perms.put(typ, new HashMap<UUID,Long>());
		if(perms.containsKey(typ)&&!perms.get(typ).containsKey(UtilPlayer.getRealUUID(player))){
			if(player.hasPermission("epicpvp.timer."+typ)){
				if(manager.getPlist().containsKey(UtilPlayer.getRealUUID(player))){
					for(String perm : manager.getPlist().get(UtilPlayer.getRealUUID(player)).getPermissions().keySet()){
						if(!perm.equalsIgnoreCase("epicpvp.timer."+typ)&&perm.contains("epicpvp.timer."+typ+".")){
							perms.get(typ).put(UtilPlayer.getRealUUID(player), Long.valueOf(perm.substring(("epicpvp.timer."+typ+".").length(), perm.length() )));
							break;
						}
					}
				}
			}
			
			if(!perms.get(typ).containsKey(UtilPlayer.getRealUUID(player))){
				perms.get(typ).put(UtilPlayer.getRealUUID(player), (long)0);
			}
		}
		
		return perms.get(typ).get(UtilPlayer.getRealUUID(player));
	}
	
	public String check(String typ,Player player){
		return check(typ, UtilPlayer.getRealUUID(player));
	}
	
	public String check(String typ,UUID uuid){
		typ=typ.toLowerCase();
		if(timer.containsKey(typ)){
			if(timer.get(typ).containsKey(uuid)){
				if(timer.get(typ).get(uuid) >= System.currentTimeMillis()){
					return UtilTime.formatMili(timer.get(typ).get(uuid)-System.currentTimeMillis());
				}else{
					timer.get(typ).remove(uuid);
					return null;
				}
			}
			return null;
		}
		return null;
	}
	
	public void remove(String typ,Player player){
		remove(typ, UtilPlayer.getRealUUID(player));
	}
	
	public void remove(String typ,UUID uuid){
		typ=typ.toLowerCase();
		if(timer.containsKey(typ)){
			timer.get(typ).remove(uuid);
		}
	}
	
	public void add(String typ,Player player,long time){
		add(typ,UtilPlayer.getRealUUID(player),time);
	}
	
	public void add(String typ,UUID uuid,long time){
		typ=typ.toLowerCase();
		if(!timer.containsKey(typ))timer.put(typ, new HashMap<UUID,Long>());
		timer.get(typ).put(uuid, System.currentTimeMillis()+time);
	}
	
}
