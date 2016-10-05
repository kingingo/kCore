package eu.epicpvp.kcore.Command.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Permission.PermissionType;
import eu.epicpvp.kcore.TeleportManager.TeleportManager;
import eu.epicpvp.kcore.TeleportManager.Teleporter;
import eu.epicpvp.kcore.Translation.TranslationHandler;
import lombok.Getter;

public class CommandTpaccept implements CommandExecutor {

	@Getter
	private TeleportManager manager;

	public CommandTpaccept(TeleportManager manager) {
		this.manager = manager;
	}

	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "tpaccept", alias = {"tpyes"}, sender = Sender.PLAYER)
	public boolean onCommand(CommandSender cs, Command cmd, String arg2, String[] args) {
		Player player = (Player) cs;
		if (getManager().getPermManager().hasPermission(player, PermissionType.PLAYER_TELEPORT_ACCEPT)) {
			if (getManager().getTeleport_anfrage().containsKey(player)) {
				Teleporter teleporter = getManager().getTeleport_anfrage().get(player);

				getManager().getTeleport().add(teleporter);
				Player to = teleporter.getTo();
				if (to != null && !to.getName().equalsIgnoreCase(player.getName())) {
					to.sendMessage(TranslationHandler.getText(player, "PREFIX") + TranslationHandler.getText(player, "ACCEPT_FROM", player.getName()));
				}
				if (teleporter.getFrom() != null && !teleporter.getFrom().getName().equalsIgnoreCase(player.getName())) {
					teleporter.getFrom().sendMessage(TranslationHandler.getText(player, "PREFIX") + TranslationHandler.getText(player, "ACCEPT_FROM", player.getName()));
				}
				getManager().getTeleport_anfrage().remove(player);
				player.sendMessage(TranslationHandler.getText(player, "PREFIX") + TranslationHandler.getText(player, "ACCEPT"));
			} else {
				player.sendMessage(TranslationHandler.getText(player, "PREFIX") + TranslationHandler.getText(player, "NO_ANFRAGE"));
			}
		}
		return false;
	}
}