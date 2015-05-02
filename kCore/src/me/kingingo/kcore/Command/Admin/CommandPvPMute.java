package me.kingingo.kcore.Command.Admin;

import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Enum.Text;
import me.kingingo.kcore.Listener.kListener;
import me.kingingo.kcore.Permission.kPermission;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandPvPMute extends kListener implements CommandExecutor{
	
	boolean chat=true;
	
	public CommandPvPMute(JavaPlugin instance){
		super(instance,"PvPMute");
	}
	
	@me.kingingo.kcore.Command.CommandHandler.Command(command = "pvpmute", sender = Sender.EVERYONE)
	public boolean onCommand(CommandSender cs, Command cmd, String arg2,String[] args) {
		if(cs instanceof Player){
			Player p = (Player)cs;
			if(p.hasPermission(kPermission.PVP_MUTE_ALL.getPermissionToString())){
				if(chat){
					chat=false;
					p.sendMessage(Text.PREFIX.getText()+Text.PVP_MUTE.getText());
				}else{
					chat=true;
					p.sendMessage(Text.PREFIX.getText()+Text.PVP_UNMUTE.getText());
				}
			}
		}else{
			if(chat){
				chat=false;
				System.out.println("[PvPMute] PvP mutet");
			}else{
				chat=true;
				System.out.println("[PvPMute] PvP unmutet");
			}
		}
		return false;
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void Chat(EntityDamageEvent ev){
		if(!chat){
			ev.setCancelled(true);
		}
	}

	@EventHandler(priority=EventPriority.LOWEST)
	public void Chat(EntityDamageByEntityEvent ev){
		if(!chat){
			ev.setCancelled(true);
		}
	}
	
}
