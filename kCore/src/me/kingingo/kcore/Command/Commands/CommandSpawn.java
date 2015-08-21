package me.kingingo.kcore.Command.Commands;

import java.io.File;

import lombok.Setter;
import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Listener.kListener;
import me.kingingo.kcore.Permission.kPermission;
import me.kingingo.kcore.TeleportManager.TeleportManager;
import me.kingingo.kcore.TeleportManager.Teleporter;
import me.kingingo.kcore.kConfig.kConfig;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerRespawnEvent;

public class CommandSpawn extends kListener implements CommandExecutor{
	
	private TeleportManager teleport;
	private Player player;
	@Setter
	private Location spawn;
	private kConfig config;
	
	public CommandSpawn(TeleportManager teleport){
		super(teleport.getPermManager().getInstance(),"CommandSpawn");
		this.teleport=teleport;
		this.config=new kConfig(new File("plugins"+File.separator+teleport.getPermManager().getInstance().getPlugin(teleport.getPermManager().getInstance().getClass()).getName()+File.separator+"spawn.yml"));
		if(config.isSet("spawn")){
			spawn=config.getLocation("spawn");
			spawn.getWorld().setSpawnLocation(spawn.getBlockX(), spawn.getBlockY(), spawn.getBlockZ());
		}else{
			spawn=Bukkit.getWorld("world").getSpawnLocation();
		}
		teleport.getCmd().register(CommandSetSpawn.class, new CommandSetSpawn(config,this));
	}
	
	@EventHandler
	public void Respawn(PlayerRespawnEvent ev){
		if(spawn!=null)ev.setRespawnLocation(spawn);
	}
	
	@me.kingingo.kcore.Command.CommandHandler.Command(command = "spawn", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		player = (Player)sender;
		if(player.hasPermission(kPermission.SPAWN_IGNORE_DELAY.getPermissionToString())){
			player.teleport(spawn);
		}else{
			teleport.getTeleport().add(new Teleporter(player,spawn,7));
		}
		return false;
	}
	
}
