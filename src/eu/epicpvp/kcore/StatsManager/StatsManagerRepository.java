package eu.epicpvp.kcore.StatsManager;

import java.util.EnumMap;
import java.util.Map;

import dev.wolveringer.dataserver.gamestats.GameType;
import eu.epicpvp.kcore.Util.UtilServer;

public final class StatsManagerRepository {

	private StatsManagerRepository() {
		throw new UnsupportedOperationException();
	}

	private static final Map<GameType, StatsManager> statsManagers = new EnumMap<>(GameType.class);

	public static boolean registerStatsManager(StatsManager statsManager) {
		if (!statsManagers.containsKey(statsManager.getType())) {
			statsManagers.put(statsManager.getType(), statsManager);
			return true;
		}
		return false;
	}

	public static StatsManager createStatsManager(GameType type) {
		StatsManager manager = null;
		if (!statsManagers.containsKey(type)) {
			statsManagers.put(type, manager = new StatsManager(UtilServer.getPermissionManager().getInstance(), UtilServer.getClient(), type));
		}
		if(manager == null)
			manager = statsManagers.get(type);
		return manager;
	}

	
	public static StatsManager getStatsManager(GameType gameType) {
		StatsManager manager = statsManagers.get(gameType);
		if (manager != null) {
			return manager;
		} else {
			manager = new StatsManager(UtilServer.getPermissionManager().getInstance(), UtilServer.getClient(), gameType);
			statsManagers.put(gameType, manager);
			return manager;
		}
	}
}
