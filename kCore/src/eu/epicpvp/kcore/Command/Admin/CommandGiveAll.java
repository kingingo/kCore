package eu.epicpvp.kcore.Command.Admin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Permission.PermissionType;
import eu.epicpvp.kcore.Util.UtilServer;

public class CommandGiveAll implements CommandExecutor{
	
	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "giveall", sender = Sender.EVERYONE)
	public boolean onCommand(CommandSender cs, Command cmd, String arg2,String[] args) {
		if(cs instanceof Player){
			Player p = (Player)cs;
			if(p.hasPermission(PermissionType.COMMAND_GIVE_ALL.getPermissionToString())){
				for(Player player : UtilServer.getPlayers()){
					if(player==p)continue;
					player.getInventory().addItem(p.getItemInHand());
				}
				
				UtilServer.broadcastLanguage("GIVEALL",new String[]{p.getName(),String.valueOf(p.getItemInHand().getAmount()), String.valueOf(p.getItemInHand().getTypeId())});
			}
		}
		return false;
	}
	
}

