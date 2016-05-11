package eu.epicpvp.kcore.Command.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Permission.PermissionType;

public class CommandWorkbench implements CommandExecutor{

	private Player player;
	
	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "workbench",alias={"wb","craft","craftingtable"}, sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		player=(Player)sender;
		if(player.hasPermission(PermissionType.WORKBENCH.getPermissionToString())){
			player.openWorkbench(player.getLocation(), true);
			return true;
		}
		return false;
	}
}
