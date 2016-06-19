package eu.epicpvp.kcore.Command.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Permission.PermissionType;
import eu.epicpvp.kcore.Translation.TranslationHandler;
import eu.epicpvp.kcore.Util.UtilServer;

public class CommandExt implements CommandExecutor{

	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "ext", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		Player player = (Player) sender;
		if(player.hasPermission(PermissionType.EXT.getPermissionToString())){
			if(args.length==0){
				player.setFireTicks(0);
				player.sendMessage(TranslationHandler.getText(player, "PREFIX")+TranslationHandler.getText(player, "EXT"));
			}else{
				if(args[0].equalsIgnoreCase("all")){
					if(player.hasPermission(PermissionType.EXT_ALL.getPermissionToString())){
						for(Player p : UtilServer.getPlayers()){
							p.setFireTicks(0);
							p.sendMessage(TranslationHandler.getText(player, "PREFIX")+TranslationHandler.getText(player, "EXT_ALL", player.getName()));
						}
					}
				}else{
					if(player.hasPermission(PermissionType.EXT_OTHER.getPermissionToString())){
						if(Bukkit.getPlayer(args[0])!=null){
							Bukkit.getPlayer(args[0]).setFireTicks(20);
							Bukkit.getPlayer(args[0]).sendMessage(TranslationHandler.getText(player, "PREFIX")+TranslationHandler.getText(player, "EXT", player.getName()));
							player.sendMessage(TranslationHandler.getText(player, "PREFIX")+TranslationHandler.getText(player, "EXT",args[0]));
						}else{
							player.sendMessage(TranslationHandler.getText(player, "PREFIX")+TranslationHandler.getText(player, "PLAYER_IS_OFFLINE",args[0]));
						}
					}
				}
			}
		}
		return false;
	}

}
