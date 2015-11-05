package me.kingingo.kcore.Command.Admin;

import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Language.Language;
import me.kingingo.kcore.Permission.kPermission;
import me.kingingo.kcore.Util.UtilServer;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandUnloadChunks implements CommandExecutor{
	
	@me.kingingo.kcore.Command.CommandHandler.Command(command = "unloadchunks", sender = Sender.EVERYONE)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		if(sender instanceof Player){
			Player player = (Player)sender;
			
			if(player.hasPermission(kPermission.MONITOR.getPermissionToString())){
				if(args.length==0){
					player.sendMessage(Language.getText(player,"PREFIX")+"/unloadchunks [World/ALL]");
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
