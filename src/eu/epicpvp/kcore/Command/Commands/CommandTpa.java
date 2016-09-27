package eu.epicpvp.kcore.Command.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Permission.PermissionType;
import eu.epicpvp.kcore.TeleportManager.TeleportCheck;
import eu.epicpvp.kcore.TeleportManager.TeleportManager;
import eu.epicpvp.kcore.TeleportManager.Teleporter;
import eu.epicpvp.kcore.Translation.TranslationHandler;
import eu.epicpvp.kcore.Util.UtilPlayer;
import eu.epicpvp.kcore.Util.UtilServer;
import eu.epicpvp.kcore.Util.UtilTime;
import lombok.Getter;

public class CommandTpa implements CommandExecutor {

	@Getter
	private TeleportManager manager;

	public CommandTpa(TeleportManager manager) {
		this.manager = manager;
	}

	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "tpa", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender cs, Command cmd, String arg2, String[] args) {
		Player player = (Player) cs;
		if (getManager().getPermManager().hasPermission(player, PermissionType.PLAYER_TELEPORT_A)) {
			if (args.length == 0) {
				player.sendMessage(TranslationHandler.getText(player, "PREFIX") + "§6/tpa [Player]");
			} else {
				String s = UtilTime.getTimeManager().check(cmd.getName(), player);
				if (s != null) {
					player.sendMessage(TranslationHandler.getText(player, "PREFIX") + TranslationHandler.getText(player, "USE_BEFEHL_TIME", s));
				} else {
					Player tp = UtilServer.getNickedPlayer(args[0]);
					if (tp != null) {
						if (getManager().getTeleport_anfrage().containsKey(tp))getManager().getTeleport_anfrage().remove(tp);
						
						if (player.hasPermission(PermissionType.PLAYER_TELEPORT_A_BYPASS.getPermissionToString())) {
							getManager().getTeleport_anfrage().put(tp, new Teleporter(player, tp));
						} else {
							if(getManager().getCheck() == TeleportCheck.NEAR){
								if(!manager.near(player)){
									getManager().getTeleport_anfrage().put(tp, new Teleporter(player, tp, 3,true));
								}else{
									player.sendMessage( TranslationHandler.getPrefixAndText( player, "WARZ_CMD_SPAWN_NEAR_TO_PLAYER" ) );
									return false;
								}
							}else{
								getManager().getTeleport_anfrage().put(tp, new Teleporter(player, tp, 3));
							}
						}
						player.sendMessage(TranslationHandler.getText(player, "PREFIX") + TranslationHandler.getText(player, "TELEPORT_ANFRAGE_SENDER", tp.getName()));
						tp.sendMessage(TranslationHandler.getText(player, "PREFIX") + TranslationHandler.getText(player, "TELEPORT_ANFRAGE_EMPFÄNGER", player.getName()));
						long l = UtilTime.getTimeManager().hasPermission(player, cmd.getName());
						if (l != 0) {
							UtilTime.getTimeManager().add(cmd.getName(), player, l);
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