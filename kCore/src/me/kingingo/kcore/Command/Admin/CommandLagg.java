package me.kingingo.kcore.Command.Admin;

import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Permission.kPermission;
import me.kingingo.kcore.Util.UtilServer;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandLagg implements CommandExecutor{
	
	@me.kingingo.kcore.Command.CommandHandler.Command(command = "lagg", alias={"lag","mem","memory"}, sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		Player player = (Player)sender;
		
		if(player.hasPermission(kPermission.MONITOR.getPermissionToString())){
			UtilServer.getLagMeter().sendUpdate(player);
		}
		return false;
	}

}
