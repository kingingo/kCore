package me.kingingo.kcore.Listener.Enderpearl;

import me.kingingo.kcore.Language.Language;
import me.kingingo.kcore.Listener.kListener;
import me.kingingo.kcore.Permission.kPermission;
import me.kingingo.kcore.Util.UtilTime;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.plugin.java.JavaPlugin;

public class EnderpearlListener extends kListener{

	private String s;
	private Long l;
	
	public EnderpearlListener(JavaPlugin instance) {
		super(instance, "EnderpearlListener");
	}

	@EventHandler
	public void Enderpearl(PlayerTeleportEvent ev){
		if(ev.getCause()==TeleportCause.ENDER_PEARL && (!ev.getPlayer().isPermissionSet(kPermission.ENDERPEARL.getPermissionToString()) ) ){
			s=UtilTime.getTimeManager().check("enderpearl",ev.getPlayer());
			if(s!=null){
				ev.getPlayer().sendMessage(Language.getText(ev.getPlayer(), "PREFIX")+Language.getText(ev.getPlayer(), "USE_ENDERPEARL_TIME",s));
				ev.setCancelled(true);
			}else{
				l=UtilTime.getTimeManager().hasPermission(ev.getPlayer(), "enderpearl");
				if( l!=0 ){
					UtilTime.getTimeManager().add("enderpearl", ev.getPlayer(), l);
				}else{
					ev.setCancelled(true);
				}
			}
		}
	}
	
}
