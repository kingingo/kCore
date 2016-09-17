package eu.epicpvp.kcore.Command.Admin;

import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Permission.PermissionType;
import eu.epicpvp.kcore.Translation.TranslationHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

public class CommandTp implements CommandExecutor {

	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "tp", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender cs, Command cmd, String arg2, String[] args) {
		Player player = (Player) cs;
		if (player.hasPermission(PermissionType.PLAYER_TELEPORT.getPermissionToString())) {
			if (args.length == 0) {
				player.sendMessage(TranslationHandler.getText(player, "PREFIX") + "§6/tp [Player]");
			} else {
				Player tpTO = Bukkit.getPlayer(args[0]);
				if (args.length == 1) {
					if (tpTO != null) {
						player.teleport(tpTO, TeleportCause.PLUGIN);
						player.sendMessage(TranslationHandler.getText(player, "PREFIX") + TranslationHandler.getText(player, "TELEPORT"));
					} else {
						player.sendMessage(TranslationHandler.getText(player, "PREFIX") + TranslationHandler.getText(player, "PLAYER_IS_OFFLINE", args[0]));
					}
				} else {
					if (tpTO != null) {
						Player tpFROM = Bukkit.getPlayer(args[1]);
						if (tpFROM != null) {
							tpTO.teleport(tpFROM, TeleportCause.PLUGIN);
							player.sendMessage(TranslationHandler.getText(player, "PREFIX") + TranslationHandler.getText(player, "TELEPORT"));
						} else {
							player.sendMessage(TranslationHandler.getText(player, "PREFIX") + TranslationHandler.getText(player, "PLAYER_IS_OFFLINE", args[1]));
						}
					} else {
						player.sendMessage(TranslationHandler.getText(player, "PREFIX") + TranslationHandler.getText(player, "PLAYER_IS_OFFLINE", args[0]));
					}
				}
			}
		}
		return false;
	}
}
