package eu.epicpvp.kcore.Command.Admin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Permission.PermissionType;
import eu.epicpvp.kcore.Translation.TranslationHandler;
import eu.epicpvp.kcore.Util.UtilServer;

public class CommandUnloadChunks implements CommandExecutor{
	
	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "unloadchunks", sender = Sender.EVERYONE)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		if(sender instanceof Player){
			Player player = (Player)sender;
			
			if(player.hasPermission(PermissionType.MONITOR.getPermissionToString())){
				if(args.length==0){
					player.sendMessage(TranslationHandler.getText(player,"PREFIX")+"/unloadchunks [World/ALL]");
				}else{
					if(args[0].equalsIgnoreCase("all")){
						if(UtilServer.getLagMeter()!=null)UtilServer.getLagMeter().unloadChunks(null, player);
					}else{
						if(UtilServer.getLagMeter()!=null)UtilServer.getLagMeter().unloadChunks(args[0], player);
					}
				}
			}
		}else{
			if(args.length==0){
				System.out.println("/unloadchunks [World/ALL]");
			}else{
				if(args[0].equalsIgnoreCase("all")){
					if(UtilServer.getLagMeter()!=null)UtilServer.getLagMeter().unloadChunks(null, null);
				}else{
					if(UtilServer.getLagMeter()!=null)UtilServer.getLagMeter().unloadChunks(args[0], null);
				}
			}
		}
		return false;
	}
	

}
