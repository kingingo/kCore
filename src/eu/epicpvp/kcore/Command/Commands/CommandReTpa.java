package eu.epicpvp.kcore.Command.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.TeleportManager.TeleportManager;
import eu.epicpvp.kcore.Translation.TranslationHandler;
import eu.epicpvp.kcore.Util.UtilServer;
import lombok.Getter;

public class CommandReTpa implements CommandExecutor {

	@Getter
	private TeleportManager manager;

	public CommandReTpa(TeleportManager manager) {
		this.manager = manager;
	}

	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "retpa", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender cs, Command cmd, String arg2, String[] args) {
		Player player = (Player) cs;
		if (args.length == 0) {
			cs.sendMessage(TranslationHandler.getText(player, "PREFIX") + "/retpa <name>");
			return false;
		}
		String targetName = args[0];
		Player target = UtilServer.getNickedPlayer(targetName);
		if (target == null) {
			cs.sendMessage(TranslationHandler.getText(player, "PREFIX") + TranslationHandler.getText(player, "PLAYER_IS_OFFLINE", targetName));
			return false;
		}
		if (getManager().getTeleport_anfrage().containsKey(target)) {
			getManager().getTeleport_anfrage().remove(target);
			player.sendMessage(TranslationHandler.getText(player, "PREFIX") + TranslationHandler.getText(player, "CLEAR"));
		} else {
			player.sendMessage(TranslationHandler.getText(player, "PREFIX") + TranslationHandler.getText(player, "NO_ANFRAGE"));
		}
		return false;
	}

}