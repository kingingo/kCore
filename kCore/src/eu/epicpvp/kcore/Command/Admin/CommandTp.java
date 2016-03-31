package eu.epicpvp.kcore.Command.Admin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Language.Language;
import eu.epicpvp.kcore.Permission.PermissionType;
import eu.epicpvp.kcore.Util.UtilPlayer;

public class CommandTp implements CommandExecutor{
	
	private Player player;
	private Player tpFROM;
	private Player tpTO;

	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "tp", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender cs, Command cmd, String arg2,String[] args) {
		player = (Player)cs;
		if(player.hasPermission(PermissionType.PLAYER_TELEPORT.getPermissionToString())){
			if(args.length==0){
				player.sendMessage(Language.getText(player, "PREFIX")+"§6/tp [Player]");
			}else{
				if(args.length==1){
					if(UtilPlayer.isOnline(args[0])){
						tpTO=Bukkit.getPlayer(args[0]);
						player.teleport(tpTO,TeleportCause.PLUGIN);
						player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "TELEPORT"));
					}else{
						player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "PLAYER_IS_OFFLINE",args[0]));
					}
				}else{
					if(UtilPlayer.isOnline(args[0])){
						tpTO=Bukkit.getPlayer(args[0]);
						if(UtilPlayer.isOnline(args[1])){
							tpFROM=Bukkit.getPlayer(args[1]);
							tpTO.teleport(tpFROM,TeleportCause.PLUGIN);
							player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "TELEPORT"));
						}else{
							player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "PLAYER_IS_OFFLINE",args[1]));
						}
					}else{
						player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "PLAYER_IS_OFFLINE",args[0]));
					}
				}
			}
		}
		return false;
	}
	
}