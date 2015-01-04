package me.kingingo.kcore.Command.Admin;

import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Enum.Text;
import me.kingingo.kcore.Permission.Permission;
import me.kingingo.kcore.Permission.PermissionManager;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class CommandChatMute implements CommandExecutor, Listener{
	
	PermissionManager permManager;
	boolean chat=true;
	
	public CommandChatMute(PermissionManager permManager){
		this.permManager=permManager;
		Bukkit.getPluginManager().registerEvents(this, permManager.getInstance());
	}
	
	@me.kingingo.kcore.Command.CommandHandler.Command(command = "chatmute", sender = Sender.EVERYONE)
	public boolean onCommand(CommandSender cs, Command cmd, String arg2,String[] args) {
		if(cs instanceof Player){
			Player p = (Player)cs;
			if(permManager.hasPermission(p, Permission.COMMAND_MUTE_ALL)){
				if(chat){
					chat=false;
					p.sendMessage(Text.PREFIX.getText()+Text.CHAT_MUTE.getText());
				}else{
					chat=true;
					p.sendMessage(Text.PREFIX.getText()+Text.CHAT_UNMUTE.getText());
				}
			}
		}else{
			if(chat){
				chat=false;
				System.out.println("[ChatMute] Chat mutet");
			}else{
				chat=true;
				System.out.println("[ChatMute] Chat unmutet");
			}
		}
		return false;
	}

	@EventHandler(priority=EventPriority.LOWEST)
	public void Chat(AsyncPlayerChatEvent ev){
		if(!chat&&!permManager.hasPermission(ev.getPlayer(), Permission.COMMAND_MUTE_ALL_ALLOW)){
			ev.setCancelled(true);
		}
	}
	
}

