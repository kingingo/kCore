package me.kingingo.kcore.Listener.Chat;

import lombok.Getter;
import me.kingingo.kcore.Gilden.GildenManager;
import me.kingingo.kcore.Listener.kListener;
import me.kingingo.kcore.Permission.kPermission;
import me.kingingo.kcore.Permission.PermissionManager;
import me.kingingo.kcore.Util.UtilString;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class ChatListener extends kListener{

	@Getter
	private PermissionManager manager;
	private GildenManager gildenmanager;
	
	public ChatListener(JavaPlugin instance,GildenManager gildenmanager,PermissionManager manager){
		super(instance,"ChatListener");
		this.manager=manager;
		this.gildenmanager=gildenmanager;
	}
	
	String g;
	String tag;
	Player p;
	String msg;
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		if (!event.isCancelled()) {
			p = event.getPlayer();
			 msg = event.getMessage();
			if((!manager.hasPermission(p, kPermission.CHAT_LINK))&&
					(msg.toLowerCase().contains("minioncraft")||
							msg.toLowerCase().contains("mastercraft")||
							UtilString.checkForIP(msg))){
				event.setCancelled(true);
				return;
			}
			msg=msg.replaceAll("%","");
			if(manager.hasPermission(p, kPermission.CHAT_FARBIG))msg=msg.replaceAll("&", "§");
			
			if(gildenmanager!=null&&gildenmanager.isPlayerInGilde(p)){
				 g = gildenmanager.getPlayerGilde(p);
				 tag = gildenmanager.getTag(gildenmanager.getPlayerGilde(p));
				g=g.toLowerCase();
				if(gildenmanager.getExtra_prefix().containsKey(g)){
					if(gildenmanager.getExtra_prefix().get(g)==1){
						tag=tag.replaceAll("§7", "§4§l");
					}else if(gildenmanager.getExtra_prefix().get(g)==2){
						tag=tag.replaceAll("§7", "§2§l");
					}else if(gildenmanager.getExtra_prefix().get(g)==3){
						tag=tag.replaceAll("§7", "§e§l");
					}else if(gildenmanager.getExtra_prefix().get(g)>=4 && gildenmanager.getExtra_prefix().get(g)<=6){
						tag=tag.replaceAll("§7", "§3");
					}else if(gildenmanager.getExtra_prefix().get(g)>=7 && gildenmanager.getExtra_prefix().get(g)<=9){
						tag=tag.replaceAll("§7", "§d");
					}else if(gildenmanager.getExtra_prefix().get(g)>=10 && gildenmanager.getExtra_prefix().get(g)<=12){
						tag=tag.replaceAll("§7", "§a");
					}else if(gildenmanager.getExtra_prefix().get(g)>=13 && gildenmanager.getExtra_prefix().get(g)<=15){
						tag=tag.replaceAll("§7", "§b");
					}
				}
			
				event.setFormat(manager.getPrefix(p)+ tag + manager.getPrefix(p).subSequence(0, 2) + p.getName() + "§7: "+ msg);
			}else{
				event.setFormat(manager.getPrefix(p) + p.getName() + "§7: "+ msg);				
			}
		}
	}
	
}
