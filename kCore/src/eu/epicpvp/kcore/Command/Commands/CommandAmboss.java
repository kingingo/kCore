package eu.epicpvp.kcore.Command.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Permission.PermissionType;

public class CommandAmboss implements CommandExecutor{

	private Player player;
	
	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "amboss",alias={"anvil"}, sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		player=(Player)sender;
		if(player.hasPermission(PermissionType.AMBOSS.getPermissionToString())){
			player.openInventory(Bukkit.createInventory(null, InventoryType.ANVIL, "§cAnvil"));
			return true;
		}
		return false;
	}
}
