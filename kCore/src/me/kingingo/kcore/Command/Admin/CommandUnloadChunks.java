package me.kingingo.kcore.Command.Admin;

import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Language.Language;
import me.kingingo.kcore.Permission.kPermission;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandUnloadChunks implements CommandExecutor{
	
	@me.kingingo.kcore.Command.CommandHandler.Command(command = "unloadchunks", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		Player player = (Player)sender;
		
		if(player.hasPermission(kPermission.MONITOR.getPermissionToString())){
			if(args.length==0){
				player.sendMessage(Language.getText(player,"PREFIX")+"/unloadchunks [World/ALL]");
			}else{
				if(args[0].equalsIgnoreCase("all")){
					int a = 0;
	                for(World world : Bukkit.getWorlds()){
	                	for(Chunk ch : world.getLoadedChunks()){
	                		if(ch.unload(true, true)){
	                			a++;
	                		}
	                	}
	                }
	                
	                player.sendMessage(Language.getText(player, "PREFIX")+" unloaded Chunks:§e "+a);
				}else{
					if(Bukkit.getWorld(args[0])!=null){
						int a = 0;
		                
		                for(Chunk ch : Bukkit.getWorld(args[0]).getLoadedChunks()){
		                	if(ch.unload(true, true)){
		                		a++;
		                	}
		                }
		                player.sendMessage(Language.getText(player, "PREFIX")+" unloaded Chunks from "+args[0]+":§e "+a);
					}else{
		                player.sendMessage(Language.getText(player, "PREFIX")+"§cThe world §e"+args[0]+"§c was not found!");
					}
				}
			}
		}
		return false;
	}

}
