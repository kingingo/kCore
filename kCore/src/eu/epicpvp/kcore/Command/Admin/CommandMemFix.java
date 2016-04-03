package eu.epicpvp.kcore.Command.Admin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Language.Language;
import eu.epicpvp.kcore.Permission.PermissionType;

public class CommandMemFix implements CommandExecutor{
	
	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "memfix", sender = Sender.EVERYONE)
	public boolean onCommand(CommandSender cs, Command cmd, String arg2,String[] args) {
		if(cs instanceof Player){
			Player p = (Player)cs;
			
			if(p.hasPermission(PermissionType.COMMAND_MEM.getPermissionToString())){
				System.gc();
				p.sendMessage(Language.getText(p,"PREFIX")+"§aMemory Fix wurde durchgef§hrt!");
			}
		}else{
			System.gc();
			System.out.println("[EpicPvP]: Memory Fix wurde durchgef§hrt!");
		}
		return false;
	}
	
}

