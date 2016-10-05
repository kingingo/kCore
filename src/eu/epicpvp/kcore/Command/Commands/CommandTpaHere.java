package eu.epicpvp.kcore.Command.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Permission.PermissionType;
import eu.epicpvp.kcore.TeleportManager.TeleportManager;
import eu.epicpvp.kcore.TeleportManager.Teleporter;
import eu.epicpvp.kcore.Translation.TranslationHandler;
import eu.epicpvp.kcore.Util.UtilServer;
import eu.epicpvp.kcore.Util.UtilTime;
import lombok.Getter;

public class CommandTpaHere implements CommandExecutor {

	@Getter
	private TeleportManager manager;

	public CommandTpaHere(TeleportManager manager) {
		this.manager = manager;
	}

	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "tpahere", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender cs, Command cmd, String arg2, String[] args) {
		Player player = (Player) cs;
		if (getManager().getPermManager().hasPermission(player, PermissionType.PLAYER_TELEPORT_AHERE)) {
			if (args.length == 0) {
				if(getManager().getPermManager().hasPermission(player, PermissionType.PLAYER_TELEPORT_AHERE_PATTERN)){
					player.sendMessage(TranslationHandler.getText(player, "PREFIX") + "§6/tpahere [Player] <mode:[player:pattern]>");
				}
				else
				{
					player.sendMessage(TranslationHandler.getText(player, "PREFIX") + "§6/tpahere [Player]");
				}
			} else {
				String s = UtilTime.getTimeManager().check(cmd.getName(), player);
				if (s != null) {
					player.sendMessage(TranslationHandler.getText(player, "PREFIX") + TranslationHandler.getText(player, "USE_BEFEHL_TIME", s));
				} else {
					if(args.length == 1 || (args.length == 2 && args[1].equalsIgnoreCase("player"))){
						Player tp = UtilServer.getNickedPlayer(args[0]);
						if (tp != null) {
							sendTPA(tp, player);
							player.sendMessage(TranslationHandler.getText(player, "PREFIX") + TranslationHandler.getText(player, "TELEPORT_ANFRAGE_SENDER", tp.getName()));
							tp.sendMessage(TranslationHandler.getText(player, "PREFIX") + TranslationHandler.getText(player, "TELEPORT_ANFRAGE_HERE_EMPFÄNGER", player.getName()));
							Long l = UtilTime.getTimeManager().hasPermission(player, cmd.getName());
							if (l != 0) {
								UtilTime.getTimeManager().add(cmd.getName(), player, l);
							}
						} else {
							player.sendMessage(TranslationHandler.getText(player, "PREFIX") + TranslationHandler.getText(player, "PLAYER_IS_OFFLINE", args[0]));
						}
					}
					else
					{
						if(args.length == 2 && args[1].equalsIgnoreCase("pattern") && getManager().getPermManager().hasPermission(player, PermissionType.PLAYER_TELEPORT_AHERE_PATTERN, true)){
							int requests = 0;
							
							for(Player p : Bukkit.getOnlinePlayers())
								if(p.getName().matches(args[0])){
									sendTPA(p, player);
									p.sendMessage(TranslationHandler.getText(player, "PREFIX") + TranslationHandler.getText(player, "TELEPORT_ANFRAGE_HERE_EMPFÄNGER", player.getName()));
									requests++;
								}
							player.sendMessage("§aYou have requested "+requests+" Players.");
						}
					}
				}
			}
		}
		return true;
	}

	
	public void sendTPA(Player target,Player sender){
		if (getManager().getTeleport_anfrage().containsKey(target))
			getManager().getTeleport_anfrage().remove(target);
		if (target.hasPermission(PermissionType.PLAYER_TELEPORT_A_BYPASS.getPermissionToString())) {
			//Teleporter(tp, player)
			getManager().getTeleport_anfrage().put(target, new Teleporter(target, sender));
		} else {
			//Teleporter(tp, player,3)
			getManager().getTeleport_anfrage().put(target, new Teleporter(target, sender, 3));
		}
	}
}