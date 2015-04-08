package me.kingingo.kcore.UserDataConfig;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import lombok.Getter;
import me.kingingo.kcore.Listener.kListener;
import me.kingingo.kcore.UserDataConfig.Events.UserDataConfigAddDefaultEvent;
import me.kingingo.kcore.UserDataConfig.Events.UserDataConfigRemoveEvent;
import me.kingingo.kcore.Util.UtilPlayer;
import me.kingingo.kcore.kConfig.kConfig;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class UserDataConfig extends kListener{

	@Getter
	private HashMap<UUID,kConfig> configs = new HashMap<>();
	@Getter
	private JavaPlugin instance;
	@Getter
	private String dataFolder;
	@Getter
	private kConfig config;
	private UUID uuid;
	private File file;
	
	public UserDataConfig(JavaPlugin instance){
		super(instance,"UserDataConfig");
		this.instance=instance;
		this.dataFolder="plugins"+File.separator+instance.getPlugin(instance.getClass()).getName()+File.separator+"userdata";
		new File(getDataFolder()).mkdirs();
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void Login(AsyncPlayerPreLoginEvent ev){
		uuid = UtilPlayer.getRealUUID(ev.getName(), ev.getUniqueId());
		file = new File(getDataFolder(),uuid+".yml");
		config=new kConfig(file);
		Bukkit.getPluginManager().callEvent(new UserDataConfigAddDefaultEvent(getConfig(),uuid));
		getConfig().options().copyDefaults(true);
		try {
			getConfig().save(file);
		} catch (IOException e) {
			Log("Die Config konnte nicht gespeichert werden Error: "+e.getMessage());
		}
		configs.put(uuid, config);
	}
	
	public void saveConfig(Player player){
		saveConfig(UtilPlayer.getRealUUID(player));
	}
	
	public void saveConfig(UUID uuid){
		try {
			configs.get(uuid).save( new File(getDataFolder(),uuid+".yml") );
		} catch (IOException e) {
			Log("Die Config konnte nicht gespeichert werden Error: "+e.getMessage());
		}
	}
	
	public kConfig getConfig(Player player){
		return getConfig(UtilPlayer.getRealUUID(player));
	}
	
	public kConfig getConfig(UUID uuid){
		return configs.get(uuid);
	}
	
	public void saveAllConfigs(){
		for(UUID uuid : configs.keySet()){
			saveConfig(uuid);
		}
	}
	
	@EventHandler
	public void addDefault(UserDataConfigAddDefaultEvent ev){
		ev.getConfig().addDefault("uuid", ev.getUuid().toString());
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void Disable(PluginDisableEvent ev){
		saveAllConfigs();
		configs.clear();
		instance=null;
		dataFolder=null;
		uuid=null;
		file=null;
		config=null;
		configs=null;
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void Quit(PlayerQuitEvent ev){
		uuid=UtilPlayer.getRealUUID(ev.getPlayer());
		if(configs.containsKey(uuid)){
			Bukkit.getPluginManager().callEvent(new UserDataConfigRemoveEvent(configs.get(uuid), ev.getPlayer()));
			saveConfig(uuid);
			configs.remove(uuid);
		}
	}
	
}
