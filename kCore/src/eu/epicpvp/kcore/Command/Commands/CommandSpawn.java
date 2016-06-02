package eu.epicpvp.kcore.Command.Commands;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.event.world.WorldLoadEvent;

import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Listener.kListener;
import eu.epicpvp.kcore.Permission.PermissionType;
import eu.epicpvp.kcore.TeleportManager.TeleportManager;
import eu.epicpvp.kcore.TeleportManager.Teleporter;
import eu.epicpvp.kcore.kConfig.kConfig;
import lombok.Getter;
import lombok.Setter;

public class CommandSpawn extends kListener implements CommandExecutor{
	
	private TeleportManager teleport;
	@Setter
	@Getter
	private Location spawn;
	private kConfig config;
	
	public CommandSpawn(TeleportManager teleport){
		super(teleport.getPermManager().getInstance(),"CommandSpawn");
		this.teleport=teleport;
		this.config=new kConfig(new File(teleport.getPermManager().getInstance().getDataFolder(), "spawn.yml"));
		if(config.getString("spawn.world")!=null&&Bukkit.getWorld(config.getString("spawn.world"))!=null){
			if(config.isSet("spawn")){
				spawn=config.getLocation("spawn");
				spawn.getWorld().setSpawnLocation(spawn.getBlockX(), spawn.getBlockY(), spawn.getBlockZ());
			}else{
				spawn=Bukkit.getWorld("world").getSpawnLocation();
			}
		}
		teleport.getCmd().register(CommandSetSpawn.class, new CommandSetSpawn(config,this));
	}
	
	@EventHandler
	public void load(WorldLoadEvent ev){
		if(spawn==null){
			if(ev.getWorld().getName().equalsIgnoreCase(config.getString("spawn.world"))){
				if(config.isSet("spawn")){
					spawn=config.getLocation("spawn");
					spawn.getWorld().setSpawnLocation(spawn.getBlockX(), spawn.getBlockY(), spawn.getBlockZ());
				}else{
					spawn=Bukkit.getWorld("world").getSpawnLocation();
				}
			}
		}
	}
	
	@EventHandler
	public void Respawn(PlayerRespawnEvent ev){
		if(spawn!=null)ev.setRespawnLocation(spawn);
	}
	
	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "spawn", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		Player player = (Player) sender;
		if(player.hasPermission(PermissionType.SPAWN_IGNORE_DELAY.getPermissionToString())){
			player.teleport(spawn,TeleportCause.PLUGIN);
		}else{
			teleport.getTeleport().add(new Teleporter(player,spawn,3));
		}
		return false;
	}
	
}
