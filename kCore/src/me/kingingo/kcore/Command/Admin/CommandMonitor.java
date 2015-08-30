package me.kingingo.kcore.Command.Admin;

import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Monitor.LagMeter;
import me.kingingo.kcore.Permission.kPermission;
import me.kingingo.kcore.Util.UtilServer;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandMonitor implements CommandExecutor{
	
	@me.kingingo.kcore.Command.CommandHandler.Command(command = "monitor", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		Player player = (Player)sender;
		
		if(player.hasPermission(kPermission.MONITOR.getPermissionToString())){
			if (UtilServer.getLagMeter().get_monitoring().contains(player)){
				UtilServer.getLagMeter().get_monitoring().remove(player);
			}else {
		    	 UtilServer.getLagMeter().get_monitoring().add(player);
		    }
		}
		return false;
	}

}
