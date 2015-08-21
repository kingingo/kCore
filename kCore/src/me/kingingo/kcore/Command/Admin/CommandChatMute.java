package me.kingingo.kcore.Command.Admin;

import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Language.Language;
import me.kingingo.kcore.Listener.kListener;
import me.kingingo.kcore.Permission.kPermission;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandChatMute extends kListener implements CommandExecutor{
	
	boolean chat=true;
	
	public CommandChatMute(JavaPlugin instance){
		super(instance,"ChatMute");
	}
	
	@me.kingingo.kcore.Command.CommandHandler.Command(command = "chatmute", sender = Sender.EVERYONE)
	public boolean onCommand(CommandSender cs, Command cmd, String arg2,String[] args) {
		if(cs instanceof Player){
			Player p = (Player)cs;
			if(p.hasPermission(kPermission.COMMAND_MUTE_ALL.getPermissionToString())){
				if(chat){
					chat=false;
					p.sendMessage(Language.getText(p, "PREFIX")+Language.getText(p, "CHAT_MUTE"));
				}else{
					chat=true;
					p.sendMessage(Language.getText(p, "PREFIX")+Language.getText(p, "CHAT_UNMUTE"));
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
		if(!chat&&!ev.getPlayer().hasPermission(kPermission.COMMAND_MUTE_ALL_ALLOW.getPermissionToString())){
			ev.setCancelled(true);
		}
	}
	
}

