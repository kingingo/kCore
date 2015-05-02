package me.kingingo.kcore.Command.Admin;

import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Enum.Text;
import me.kingingo.kcore.Permission.kPermission;
import me.kingingo.kcore.Util.UtilPlayer;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandTpHere implements CommandExecutor{
	
	private Player player;
	private Player tp;
	@me.kingingo.kcore.Command.CommandHandler.Command(command = "tphere", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender cs, Command cmd, String arg2,String[] args) {
		player = (Player)cs;
		if(player.hasPermission(kPermission.PLAYER_TELEPORT_HERE.getPermissionToString())){
			if(args.length==0){
				player.sendMessage(Text.PREFIX.getText()+"�6/tphere [Player]");
			}else{
					if(UtilPlayer.isOnline(args[0])){
						tp=Bukkit.getPlayer(args[0]);
						tp.teleport(player);
						player.sendMessage(Text.PREFIX.getText()+Text.TELEPORT_HERE.getText(tp.getName()));
					}else{
						player.sendMessage(Text.PREFIX.getText()+Text.PLAYER_IS_OFFLINE.getText(args[0]));
					}
			}
		}
		return false;
	}
	
}