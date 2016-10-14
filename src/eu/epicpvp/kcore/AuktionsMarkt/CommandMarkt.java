package eu.epicpvp.kcore.AuktionsMarkt;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.epicpvp.kcore.Command.CommandHandler.Sender;

public class CommandMarkt implements CommandExecutor {
	
	@Override
	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "markt", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		Player player = (Player)sender;
		
		AuktionsMarkt.getAuktionsMarkt().open(player);
		return false;
	}
}
