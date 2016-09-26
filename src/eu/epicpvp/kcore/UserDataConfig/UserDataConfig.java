package eu.epicpvp.kcore.UserDataConfig;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;
import com.google.common.base.Charsets;
import eu.epicpvp.kcore.Listener.kListener;
import eu.epicpvp.kcore.UserDataConfig.Events.UserDataConfigLoadEvent;
import eu.epicpvp.kcore.UserDataConfig.Events.UserDataConfigRemoveEvent;
import eu.epicpvp.kcore.Util.UtilFile;
import eu.epicpvp.kcore.Util.UtilPlayer;
import eu.epicpvp.kcore.Util.UtilServer;
import eu.epicpvp.kcore.kConfig.kConfig;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class UserDataConfig extends kListener {

	@Getter
	private HashMap<Integer, kConfig> configs;
	@Getter
	private JavaPlugin instance;
	@Getter
	private String dataFolder;

	public UserDataConfig(JavaPlugin instance) {
		super(instance, "UserDataConfig");
		UtilServer.setUserData(this);
		this.configs = new HashMap<>();
		this.instance = instance;
		this.dataFolder = "plugins" + File.separator + instance.getPlugin(instance.getClass()).getName() + File.separator + "userdata";
		new File(getDataFolder()).mkdirs();
	}

	public static UUID getOfflineUUID(String player) {
		return UUID.nameUUIDFromBytes(new StringBuilder().append("OfflinePlayer:").append(player.toLowerCase()).toString().getBytes(Charsets.UTF_8));
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void Login(AsyncPlayerPreLoginEvent ev) {
		if (UtilServer.getClient() == null) {
			ev.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "Server still starting up...");
		}
		int playerId = UtilPlayer.getPlayerId(ev.getName());

		File file = new File(getDataFolder(), playerId + ".yml");
		kConfig config = new kConfig(file);
		if (configs != null) {
			logMessage("Die Config von " + ev.getName() + "(" + playerId + "/" + ev.getUniqueId() + ") wurde geladen");
			configs.put(playerId, config);
		} else {
			logMessage("AsyncPlayerPreLoginEvent configs HASHMAP == NULL");
		}
	}

	@EventHandler
	public void Join(PlayerJoinEvent ev) {
		int playerId = UtilPlayer.getPlayerId(ev.getPlayer());
		if (configs != null && configs.containsKey(playerId)) {
			Bukkit.getPluginManager().callEvent(new UserDataConfigLoadEvent(configs.get(playerId), ev.getPlayer(), (!configs.get(playerId).getFile().exists())));
		}
	}

	public kConfig loadConfig(int playerId) {
		return new kConfig(new File(getDataFolder(), playerId + ".yml"));
	}

	public void saveConfig(Player player) {
		saveConfig(UtilPlayer.getPlayerId(player));
	}

	public void saveConfig(int playerId) {
		if(!configs.containsKey(playerId)){
			logMessage("Die Config konnte nicht gespeichert werden, weil sie nicht in der Liste gefunden wurde!");
			return;
		}
		
		try {
			configs.get(playerId).save(new File(getDataFolder(), playerId + ".yml"));
		} catch (IOException e) {
			logMessage("Die Config konnte nicht gespeichert werden Error: " + e.getMessage());
		} catch (NullPointerException e) {
			logMessage("Die Config konnte nicht gespeichert werden NullPointerException: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public boolean contains(Player player) {
		return contains(UtilPlayer.getPlayerId(player));
	}

	public boolean contains(int playerId) {
		return configs.containsKey(playerId);
	}

	public kConfig getConfig(Player player) {
		return getConfig(UtilPlayer.getPlayerId(player));
	}

	public kConfig getConfig(int playerId) {
		if (configs != null && !configs.containsKey(playerId)) {
			configs.put(playerId, loadConfig(playerId));
		}
		return configs.get(playerId);
	}

	public void saveAllConfigs() {
		for (Integer playerId : configs.keySet()) {
			saveConfig(playerId);
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void Disable(PluginDisableEvent ev) {
		if (ev.getPlugin().getName().equalsIgnoreCase("kCore")) {
			saveAllConfigs();
			instance = null;
			dataFolder = null;
			configs.clear();
			configs = null;
		}
	}

	@EventHandler(priority = EventPriority.LOW)
	public void Quit(PlayerQuitEvent ev) {
		UtilServer.loopbackUntilValidDataserverConnection(() -> {
			int playerId = UtilPlayer.getPlayerId(ev.getPlayer());
			if (configs != null && configs.containsKey(playerId)) {
				Bukkit.getPluginManager().callEvent(new UserDataConfigRemoveEvent(configs.get(playerId), ev.getPlayer()));
				saveConfig(playerId);
				configs.remove(playerId);
			}
		}, "save player " + ev.getPlayer().getName() + " config", true);
	}

}
