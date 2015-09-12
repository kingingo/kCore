package me.kingingo.kcore.Command.Commands;

import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Language.Language;
import me.kingingo.kcore.Permission.kPermission;
import me.kingingo.kcore.Util.UtilServer;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandExt implements CommandExecutor{
	
	private Player player;
	
	@me.kingingo.kcore.Command.CommandHandler.Command(command = "ext", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		player = (Player)sender;
		if(player.hasPermission(kPermission.EXT.getPermissionToString())){
			if(args.length==0){
				player.setFireTicks(0);
				player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "EXT"));
			}else{
				if(args[0].equalsIgnoreCase("all")){
					if(player.hasPermission(kPermission.EXT_ALL.getPermissionToString())){
						for(Player p : UtilServer.getPlayers()){
							p.setFireTicks(0);
							p.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "EXT_ALL",player.getName()));
						}
					}
				}else{
					if(player.hasPermission(kPermission.EXT_OTHER.getPermissionToString())){
						if(Bukkit.getPlayer(args[0])!=null){
							Bukkit.getPlayer(args[0]).setFireTicks(20);
							Bukkit.getPlayer(args[0]).sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "EXT",player.getName()));
							player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "EXT",args[0]));
						}else{
							player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "PLAYER_IS_OFFLINE",args[0]));
						}
					}
				}
			}
		}
		return false;
	}

}
