package eu.epicpvp.kcore.Teams;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import dev.wolveringer.dataserver.gamestats.GameType;
import eu.epicpvp.kcore.StatsManager.StatsManager;
import lombok.AccessLevel;
import lombok.Getter;

@Getter
public class Team {

	private final TeamManager manager;
	private final int id;
	private final GameType gameType;
	private final String name;
	private final String prefix;
	@Getter(AccessLevel.NONE)
	protected final List<Integer> players = new ArrayList<>();
	@Getter(AccessLevel.NONE)
	protected final Multimap<Integer, String> playerPermissions = HashMultimap.create();
	private final StatsManager statsManager;

	public Team(TeamManager manager, int id, GameType gameType, String name, String prefix) {
		this.manager = manager;
		this.id = id;
		this.gameType = gameType;
		this.name = name;
		this.prefix = prefix;
		statsManager = StatsManagerRepository.getStatsManager(manager.getPlugin(), gameType);
	}

	public void add(int playerId) {
		players.add(playerId);
	}

	public void remove(int playerId) {
		players.remove(playerId);
	}
}
