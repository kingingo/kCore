package me.kingingo.kcore.Command.Admin;

import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Enum.Text;
import me.kingingo.kcore.Listener.kListener;
import me.kingingo.kcore.Permission.kPermission;
import me.kingingo.kcore.Permission.PermissionManager;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandCMDMute extends kListener implements CommandExecutor{
	
	boolean chat=true;
	
	public CommandCMDMute(JavaPlugin instance){
		super(instance,"CMDMute");
	}
	
	@me.kingingo.kcore.Command.CommandHandler.Command(command = "cmdmute", sender = Sender.EVERYONE)
	public boolean onCommand(CommandSender cs, Command cmd, String arg2,String[] args) {
		if(cs instanceof Player){
			Player p = (Player)cs;
			if(p.hasPermission(kPermission.COMMAND_COMMAND_MUTE_ALL.getPermissionToString())){
				if(chat){
					chat=false;
					p.sendMessage(Text.PREFIX.getText()+Text.CMD_MUTE.getText());
				}else{
					chat=true;
					p.sendMessage(Text.PREFIX.getText()+Text.CMD_UNMUTE.getText());
				}
			}
		}else{
			if(chat){
				chat=false;
				System.out.println("[CmdMute] Cmd mutet");
			}else{
				chat=true;
				System.out.println("[CmdMute] Cmd unmutet");
			}
		}
		return false;
	}

	@EventHandler(priority=EventPriority.LOWEST)
	public void CMD(PlayerCommandPreprocessEvent ev){
		if(!chat&&!ev.getPlayer().hasPermission(kPermission.COMMAND_COMMAND_MUTE_ALL_ALLOW.getPermissionToString())){
			ev.setCancelled(true);
		}
	}
	
}

