package me.kingingo.kcore.Listener.Spawn.Commands;

import java.io.File;

import lombok.Getter;
import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Enum.Text;
import me.kingingo.kcore.Listener.Spawn.SpawnListener;
import me.kingingo.kcore.Permission.kPermission;
import me.kingingo.kcore.Util.UtilFile;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandSetSpawn implements CommandExecutor{
	
	@Getter
	private SpawnListener listener;
	
	public CommandSetSpawn(SpawnListener listener){
		this.listener=listener;
	}
	
	@me.kingingo.kcore.Command.CommandHandler.Command(command = "setspawn", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		Player player = (Player)sender;
		if(listener.getPermManager().hasPermission(player, kPermission.SPAWN_SET)||player.isOp()){
			player.getWorld().setSpawnLocation(player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ());
			UtilFile.deleteFile(new File(listener.getWorld().getName()+File.separator+"spawn.dat"));
			UtilFile.createFile(new File(listener.getWorld().getName()+File.separator+"spawn.dat"), new String[]{String.valueOf(player.getLocation().getBlockX()),String.valueOf(player.getLocation().getBlockY()),String.valueOf(player.getLocation().getBlockZ())});
			player.sendMessage(Text.PREFIX.getText()+Text.SPAWN_SET.getText());
		}
		return false;
	}
	
}
