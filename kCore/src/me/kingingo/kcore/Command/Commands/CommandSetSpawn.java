package me.kingingo.kcore.Command.Commands;

import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Language.Language;
import me.kingingo.kcore.Permission.kPermission;
import me.kingingo.kcore.kConfig.kConfig;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandSetSpawn implements CommandExecutor{
	
	private kConfig config;
	private CommandSpawn spawn;
	
	public CommandSetSpawn(kConfig config,CommandSpawn spawn){
		this.config=config;
		this.spawn=spawn;
	}
	
	@me.kingingo.kcore.Command.CommandHandler.Command(command = "setspawn", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		Player player = (Player)sender;
		if(player.hasPermission(kPermission.SPAWN_SET.getPermissionToString())||player.isOp()){
			player.getWorld().setSpawnLocation(player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ());
			config.setLocation("spawn", player.getWorld().getSpawnLocation());
			config.save();
			spawn.setSpawn(player.getWorld().getSpawnLocation());
			player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "SPAWN_SET"));
		}
		return false;
	}
	
}
