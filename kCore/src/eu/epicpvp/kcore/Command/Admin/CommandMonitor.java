package eu.epicpvp.kcore.Command.Admin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Permission.PermissionType;
import eu.epicpvp.kcore.Util.UtilServer;

public class CommandMonitor implements CommandExecutor{
	
	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "monitor", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		Player player = (Player)sender;
		
		if(player.hasPermission(PermissionType.MONITOR.getPermissionToString())){
			if (UtilServer.getLagMeter().get_monitoring().contains(player)){
				UtilServer.getLagMeter().get_monitoring().remove(player);
			}else {
		    	 UtilServer.getLagMeter().get_monitoring().add(player);
		    }
		}
		return false;
	}

}
