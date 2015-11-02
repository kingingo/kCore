package me.kingingo.kcore.Command.Admin;

import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Language.Language;
import me.kingingo.kcore.Permission.kPermission;
import me.kingingo.kcore.Util.UtilServer;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandLagg implements CommandExecutor{
	
	@me.kingingo.kcore.Command.CommandHandler.Command(command = "lagg", alias={"lag","mem","memory"}, sender = Sender.EVERYONE)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		if(sender instanceof Player){
			Player player = (Player)sender;
			
			if(player.hasPermission(kPermission.MONITOR.getPermissionToString())){
				if(args.length==0){
					UtilServer.getLagMeter().sendUpdate(player);
				}else{
					if(args[0].equalsIgnoreCase("gc")){
						System.gc();
						player.sendMessage(Language.getText(player, "PREFIX")+"§aSystem GC ausgeführt!");
					}
				}	
			}
		}else{
			if(args.length==0){
				UtilServer.getLagMeter().sendUpdate();
			}else{
				if(args[0].equalsIgnoreCase("gc")){
					System.gc();
					System.out.println("[LagMeter] System GC ausgeführt!");
				}
			}	
		}
		return false;
	}

}
