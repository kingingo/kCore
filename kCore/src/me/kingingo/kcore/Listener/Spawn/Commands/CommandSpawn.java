package me.kingingo.kcore.Listener.Spawn.Commands;

import lombok.Getter;
import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Listener.Spawn.SpawnListener;
import me.kingingo.kcore.Permission.Permission;
import me.kingingo.kcore.Util.TimeSpan;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandSpawn implements CommandExecutor{
	
	@Getter
	private SpawnListener listener;
	
	public CommandSpawn(SpawnListener listener){
		this.listener=listener;
	}
	
	@me.kingingo.kcore.Command.CommandHandler.Command(command = "spawn", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		Player player = (Player)sender;
		if(listener.getPermManager().hasPermission(player, Permission.SPAWN_IGNORE_DELAY)){
			listener.TeleportToSpawn(player);
		}else{
			listener.getTeleport_loc().put(player, player.getLocation());
			listener.getTeleport().put(player, (System.currentTimeMillis()+(TimeSpan.SECOND*5)) );
		}
		return false;
	}
	
}
