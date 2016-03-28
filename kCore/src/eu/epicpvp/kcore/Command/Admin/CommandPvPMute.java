package eu.epicpvp.kcore.Command.Admin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.java.JavaPlugin;

import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Language.Language;
import eu.epicpvp.kcore.Listener.kListener;
import eu.epicpvp.kcore.Permission.PermissionType;

public class CommandPvPMute extends kListener implements CommandExecutor{
	
	boolean chat=true;
	
	public CommandPvPMute(JavaPlugin instance){
		super(instance,"PvPMute");
	}
	
	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "pvpmute", sender = Sender.EVERYONE)
	public boolean onCommand(CommandSender cs, Command cmd, String arg2,String[] args) {
		if(cs instanceof Player){
			Player p = (Player)cs;
			if(p.hasPermission(PermissionType.PVP_MUTE_ALL.getPermissionToString())){
				if(chat){
					chat=false;
					p.sendMessage(Language.getText(p, "PREFIX")+Language.getText(p, "PVP_MUTE"));
				}else{
					chat=true;
					p.sendMessage(Language.getText(p, "PREFIX")+Language.getText(p, "PVP_UNMUTE"));
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

