package eu.epicpvp.kcore.Command.Admin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Permission.PermissionType;
import eu.epicpvp.kcore.Translation.TranslationHandler;
import eu.epicpvp.kcore.Util.UtilPlayer;

public class CommandTpHere implements CommandExecutor{

	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "tphere", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender cs, Command cmd, String arg2,String[] args) {
		Player player = (Player) cs;
		if(player.hasPermission(PermissionType.PLAYER_TELEPORT_HERE.getPermissionToString())){
			if(args.length==0){
				player.sendMessage(TranslationHandler.getText(player, "PREFIX")+"§6/tphere [Player]");
			}else{
					if(UtilPlayer.isOnline(args[0])){
						Player tp = Bukkit.getPlayer(args[0]);
						tp.teleport(player,TeleportCause.PLUGIN);
						player.sendMessage(TranslationHandler.getText(player, "PREFIX")+TranslationHandler.getText(player, "TELEPORT_HERE", tp.getName()));
					}else{
						player.sendMessage(TranslationHandler.getText(player, "PREFIX")+TranslationHandler.getText(player, "PLAYER_IS_OFFLINE",args[0]));
					}
			}
		}
		return false;
	}
	
}