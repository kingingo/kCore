package eu.epicpvp.kcore.Command.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

import eu.epicpvp.kcore.Command.CommandHandler;
import eu.epicpvp.kcore.Permission.PermissionType;
import eu.epicpvp.kcore.Util.UtilServer;

public class CommandAnvil implements CommandExecutor {
	
	@Override
	@CommandHandler.Command(command = "anvil", alias = {"amboss"}, sender = CommandHandler.Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		Player plr = (Player) sender;
		if (UtilServer.getPermissionManager().hasPermission(plr, PermissionType.AMBOSS)) {
			plr.openInventory(Bukkit.createInventory(null, InventoryType.ANVIL));
		}
		return false;
	}
}
