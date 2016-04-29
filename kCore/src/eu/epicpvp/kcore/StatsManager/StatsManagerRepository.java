package eu.epicpvp.kcore.StatsManager;

import java.util.EnumMap;
import java.util.Map;

import dev.wolveringer.dataserver.gamestats.GameType;
import eu.epicpvp.kcore.Util.UtilServer;
import org.bukkit.plugin.java.JavaPlugin;

public final class StatsManagerRepository {
	
	private StatsManagerRepository() {
		throw new UnsupportedOperationException();
	}
	
	private static final Map<GameType, StatsManager> statsManagers = new EnumMap<>(GameType.class);
	
	static boolean addStatsManager(StatsManager statsManager){
		if(!statsManagers.containsKey(statsManager.getType())){
			statsManagers.put(statsManager.getType(),statsManager);
			return true;
		}
		return false;
	}
	
	public static StatsManager getStatsManager(JavaPlugin plugin, GameType gameType) {
		StatsManager manager = statsManagers.get(gameType);
		if (manager != null) {
			return manager;
		} else {
			return new StatsManager(plugin, UtilServer.getClient(), gameType);
		}
	}
}
