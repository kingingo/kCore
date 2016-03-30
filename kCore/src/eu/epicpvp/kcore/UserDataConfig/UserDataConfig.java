package eu.epicpvp.kcore.UserDataConfig;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.java.JavaPlugin;

import eu.epicpvp.kcore.Listener.kListener;
import eu.epicpvp.kcore.UserDataConfig.Events.UserDataConfigLoadEvent;
import eu.epicpvp.kcore.UserDataConfig.Events.UserDataConfigRemoveEvent;
import eu.epicpvp.kcore.Util.UtilPlayer;
import eu.epicpvp.kcore.Util.UtilServer;
import eu.epicpvp.kcore.kConfig.kConfig;
import lombok.Getter;
import lombok.Setter;

public class UserDataConfig extends kListener{

	@Getter
	private HashMap<UUID,kConfig> configs;
	@Getter
	private JavaPlugin instance;
	@Getter
	private String dataFolder;
	@Getter
	private kConfig config;
	private UUID uuid;
	private File file;
	@Getter
	@Setter
	private boolean restart=false;
	
	public UserDataConfig(JavaPlugin instance){
		super(instance,"UserDataConfig");
		UtilServer.setUserData(this);
		this.configs= new HashMap<>();
		this.instance=instance;
		this.dataFolder="plugins"+File.separator+instance.getPlugin(instance.getClass()).getName()+File.separator+"userdata";
		new File(getDataFolder()).mkdirs();
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void Login(AsyncPlayerPreLoginEvent ev){
		uuid = UtilPlayer.getRealUUID(ev.getName(), ev.getUniqueId());
		if(uuid!=null){
			file = new File(getDataFolder(),uuid+".yml");
			if(file!=null){
				config=new kConfig(file);
				if(config!=null){
					if(configs!=null){
						Log("Die Config von "+ev.getName()+"("+uuid+"/"+ev.getUniqueId()+") wurde geladen");
						configs.put(uuid, config);
					}else{
						Log("AsyncPlayerPreLoginEvent configs HASHMAP == NULL");
					}
				}else{
					Log("AsyncPlayerPreLoginEvent config == NULL");
				}
			}else{
				Log("AsyncPlayerPreLoginEvent File == NULL");
			}
		}
	}
	
	@EventHandler
	public void Join(PlayerJoinEvent ev){
		uuid=UtilPlayer.getRealUUID(ev.getPlayer());
		if(uuid!=null&&configs!=null&&configs.containsKey(uuid)){
			Bukkit.getPluginManager().callEvent(new UserDataConfigLoadEvent(configs.get(uuid), ev.getPlayer()));
		}
	}
	
	public kConfig loadConfig(UUID uuid){
		file = new File(getDataFolder(),uuid+".yml");
		config=new kConfig(file);
		return config;
	}
	
	public void saveConfig(Player player){
		saveConfig(UtilPlayer.getRealUUID(player));
	}
	
	public void saveConfig(UUID uuid){
		try {
			configs.get(uuid).save( new File(getDataFolder(),uuid+".yml") );
		} catch (IOException e) {
			Log("Die Config konnte nicht gespeichert werden Error: "+e.getMessage());
		}catch(NullPointerException e){
			Log("Die Config konnte nicht gespeichert werden NullpointerError: "+e.getMessage());
		}
	}
	
	public kConfig getConfig(Player player){
		return getConfig(UtilPlayer.getRealUUID(player));
	}
	
	public kConfig getConfig(UUID uuid){
		if(configs!=null&&!configs.containsKey(uuid)){
			configs.put(uuid, loadConfig(uuid));
		}
		return configs.get(uuid);
	}
	
	public void saveAllConfigs(){
		if(config==null)return;
		for(UUID uuid : configs.keySet()){
			saveConfig(uuid);
		}
	}
	
//	@EventHandler
//	public void PacketReceive(PacketReceiveEvent ev){
//		if(ev.getPacket() instanceof WORLD_CHANGE_DATA){
//			WORLD_CHANGE_DATA packet = (WORLD_CHANGE_DATA)ev.getPacket();
//			UtilPlayer.setWorldChangeUUID(Bukkit.getWorld(packet.getWorldName()), packet.getOld_uuid(), packet.getNew_uuid());
//			file = new File(getDataFolder(),packet.getOld_uuid()+".yml");
//			
//			if(file.exists()){
//				file.renameTo(new File(getDataFolder(),packet.getNew_uuid()+".yml"));
//			}
//		}
//	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void Disable(PluginDisableEvent ev){
		if(ev.getPlugin().getName().equalsIgnoreCase("kCore")){
			saveAllConfigs();
			if(config!=null)configs.clear();
			instance=null;
			dataFolder=null;
			uuid=null;
			file=null;
			config=null;
			configs=null;
		}
	}
	
	@EventHandler(priority=EventPriority.LOW)
	public void Quit(PlayerQuitEvent ev){
		if(isRestart())return;
		uuid=UtilPlayer.getRealUUID(ev.getPlayer());
		if(uuid!=null&&configs!=null&&configs.containsKey(uuid)){
			Bukkit.getPluginManager().callEvent(new UserDataConfigRemoveEvent(configs.get(uuid), ev.getPlayer()));
			saveConfig(uuid);
			configs.remove(uuid);
		}
	}
	
}
