package eu.epicpvp.kcore.Command.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Permission.PermissionType;
import eu.epicpvp.kcore.Translation.TranslationHandler;
import eu.epicpvp.kcore.kConfig.kConfig;

public class CommandSetSpawn implements CommandExecutor{
	
	private kConfig config;
	private CommandSpawn spawn;
	
	public CommandSetSpawn(kConfig config,CommandSpawn spawn){
		this.config=config;
		this.spawn=spawn;
	}
	
	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "setspawn", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		Player player = (Player)sender;
		if(player.hasPermission(PermissionType.SPAWN_SET.getPermissionToString())||player.isOp()){
			player.getWorld().setSpawnLocation(player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ());
			config.setLocation("spawn", player.getWorld().getSpawnLocation());
			config.save();
			spawn.setSpawn(player.getWorld().getSpawnLocation());
			player.sendMessage(TranslationHandler.getText(player, "PREFIX")+TranslationHandler.getText(player, "SPAWN_SET"));
		}
		return false;
	}
	
}
