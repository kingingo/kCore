package me.kingingo.kcore.Listener.Chat;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

import lombok.Getter;
import me.kingingo.kcore.Listener.kListener;
import me.kingingo.kcore.Permission.Permission;
import me.kingingo.kcore.Permission.PermissionManager;
import me.kingingo.kcore.Util.UtilString;

public class ChatListener extends kListener{

	@Getter
	private PermissionManager manager;
	
	public ChatListener(JavaPlugin instance,PermissionManager manager){
		super(instance,"ChatListener");
		this.manager=manager;
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		if (!event.isCancelled()) {
			Player p = event.getPlayer();
			String msg = event.getMessage();
			if((!manager.hasPermission(p, Permission.CHAT_LINK))&&
					(msg.toLowerCase().contains("minioncraft")||
							msg.toLowerCase().contains("mastercraft")||
							UtilString.checkForIP(msg))){
				event.setCancelled(true);
				return;
			}
			msg=msg.replaceAll("%","");
			if(manager.hasPermission(p, Permission.ALL_PERMISSION))msg=msg.replaceAll("&", "§");
			event.setFormat(manager.getPrefix(p) + p.getName() + "§7: "+ msg);
		}
	}
	
}
