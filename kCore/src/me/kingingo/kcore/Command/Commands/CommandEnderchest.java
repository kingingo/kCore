package me.kingingo.kcore.Command.Commands;

import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Enum.Text;
import me.kingingo.kcore.Permission.kPermission;
import me.kingingo.kcore.Util.UtilPlayer;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandEnderchest implements CommandExecutor{
	
	private Player player;
	private Player target;
	
	@me.kingingo.kcore.Command.CommandHandler.Command(command = "enderchest", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		player = (Player)sender;
		
		if(args.length==0){
			if(player.hasPermission(kPermission.ENDERCHEST.getPermissionToString())){
				player.openInventory(player.getEnderChest());
			}
		}else{
			if(player.hasPermission(kPermission.ENDERCHEST_OTHER.getPermissionToString())){
				if(UtilPlayer.isOnline(args[0])){
					target=Bukkit.getPlayer(args[0]);
					player.openInventory(target.getEnderChest());
				}else{
					player.sendMessage(Text.PREFIX.getText()+Text.PLAYER_IS_OFFLINE.getText(args[0]));
				}
			}
		}
		return false;
	}

}
