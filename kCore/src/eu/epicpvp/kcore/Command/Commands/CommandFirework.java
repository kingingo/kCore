package eu.epicpvp.kcore.Command.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Permission.PermissionType;
import eu.epicpvp.kcore.Util.UtilFirework;

public class CommandFirework implements CommandExecutor{
	
	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "firework",alias={"fw"}, sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		Player player=(Player)sender;
		if(player.hasPermission(PermissionType.COMMAND_FIREWORK.getPermissionToString())){
			UtilFirework.start(-1, player.getLocation(), UtilFirework.RandomColor(), UtilFirework.RandomType());
			return true;
		}
		return false;
	}
}
