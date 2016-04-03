package eu.epicpvp.kcore.TimeManager;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;

import eu.epicpvp.kcore.Listener.kListener;
import eu.epicpvp.kcore.Permission.Permission;
import eu.epicpvp.kcore.Permission.PermissionManager;
import eu.epicpvp.kcore.Util.UtilPlayer;
import eu.epicpvp.kcore.Util.UtilTime;
import lombok.Getter;

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
				for(Permission perm : manager.getPermissionPlayer(player).getPermissions()){
					if(!perm.getPermission().equalsIgnoreCase("epicpvp.timer."+typ)&&perm.getPermission().contains("epicpvp.timer."+typ+".")){
						perms.get(typ).put(UtilPlayer.getRealUUID(player), Long.valueOf(perm.getPermission().substring(("epicpvp.timer."+typ+".").length(), perm.getPermission().length() )));
						break;
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
