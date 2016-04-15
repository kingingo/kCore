package eu.epicpvp.kcore.Command.Admin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Permission.PermissionType;
import eu.epicpvp.kcore.Translation.TranslationHandler;
import eu.epicpvp.kcore.Util.UtilServer;

public class CommandLagg implements CommandExecutor{
	
	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "lagg", alias={"lag","mem","memory"}, sender = Sender.EVERYONE)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		if(sender instanceof Player){
			Player player = (Player)sender;
			
			if(player.hasPermission(PermissionType.MONITOR.getPermissionToString())){
				if(args.length==0){
					UtilServer.getLagMeter().sendUpdate(player);
				}else{
					if(args[0].equalsIgnoreCase("gc")){
						System.gc();
						player.sendMessage(TranslationHandler.getText(player, "PREFIX")+"§aSystem GC ausgef§hrt!");
					}
				}	
			}
		}else{
			if(args.length==0){
				UtilServer.getLagMeter().sendUpdate();
			}else{
				if(args[0].equalsIgnoreCase("gc")){
					System.gc();
					System.out.println("[LagMeter] System GC ausgef§hrt!");
				}
			}	
		}
		return false;
	}

}
