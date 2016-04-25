package eu.epicpvp.kcore.Teams;

import java.util.EnumMap;
import java.util.Map;

import dev.wolveringer.dataserver.gamestats.GameType;
import eu.epicpvp.kcore.StatsManager.StatsManager;
import eu.epicpvp.kcore.Util.UtilServer;
import org.bukkit.plugin.java.JavaPlugin;

public final class StatsManagerRepository {
	
	private StatsManagerRepository() {
		throw new UnsupportedOperationException();
	}
	
	private static final Map<GameType, StatsManager> statsManagers = new EnumMap<>(GameType.class);
	
	public static StatsManager getStatsManager(JavaPlugin plugin, GameType gameType) {
		StatsManager manager = statsManagers.get(gameType);
		if (manager != null) {
			return manager;
		} else {
			StatsManager statsManager = new StatsManager(plugin, UtilServer.getClient(), gameType);
			statsManagers.put(gameType, statsManager);
			return statsManager;
		}
	}
}
