package me.kingingo.kcore.Command.Commands;

import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Enum.Text;
import me.kingingo.kcore.Permission.kPermission;
import me.kingingo.kcore.Util.UtilServer;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandFeed implements CommandExecutor{
	
	@me.kingingo.kcore.Command.CommandHandler.Command(command = "feed",alias={"eat","essen","füttern"}, sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		Player player = (Player)sender;
		if(player.hasPermission(kPermission.FEED.getPermissionToString())){
			if(args.length==0){
				player.setFoodLevel(20);
				player.sendMessage(Text.PREFIX.getText()+Text.FEED.getText());
			}else{
				if(args[0].equalsIgnoreCase("all")){
					if(player.hasPermission(kPermission.FEED_ALL.getPermissionToString())){
						for(Player p : UtilServer.getPlayers()){
							p.setFoodLevel(20);
							p.sendMessage(Text.PREFIX.getText()+Text.FEED_ALL.getText(player.getName()));
						}
					}
				}else{
					if(player.hasPermission(kPermission.FEED_OTHER.getPermissionToString())){
						if(Bukkit.getPlayer(args[0])!=null){
							Bukkit.getPlayer(args[0]).setFoodLevel(20);
							Bukkit.getPlayer(args[0]).sendMessage(Text.PREFIX.getText()+Text.FEED_ALL.getText(player.getName()));
							player.sendMessage(Text.PREFIX.getText()+Text.FEED_OTHER.getText(args[0]));
						}else{
							player.sendMessage(Text.PREFIX.getText()+Text.PLAYER_IS_OFFLINE.getText(args[0]));
						}
					}
				}
			}
		}
		return false;
	}

}
