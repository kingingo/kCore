package eu.epicpvp.kcore.Teams;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import dev.wolveringer.dataserver.gamestats.StatsKey;
import eu.epicpvp.kcore.Translation.TranslationHandler;
import eu.epicpvp.kcore.Util.UtilPlayer;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.entity.Player;

@Getter
public class Team {
	private final TeamManager teamManager;
	private final int teamId;
	private final String name;
	private final String prefix;
	private final int ownerId;
	@Getter(AccessLevel.NONE)
	protected final List<Integer> players = new ArrayList<>();
	@Getter(AccessLevel.NONE)
	protected final Multimap<Integer, String> playerPermissions = HashMultimap.create();

	public Team(TeamManager teamManager, int teamId, String name, String prefix, int ownerId) {
		this.teamManager = teamManager;
		this.teamId = teamId;
		this.name = name;
		this.prefix = prefix;
		this.ownerId = ownerId;
		loadStatistics();
	}

	public void loadStatistics() {

	}

	public void add(StatsKey key, Object value) {
//		StatsManagerRepository.getStatsManager(teamManager.getTeamType()).add(teamId, key, value);
	}

	public void set(StatsKey key, Object value) {
//		StatsManagerRepository.getStatsManager(teamManager.getTeamType()).set(teamId, key, value);
	}

	public Object get(StatsKey key) {
//		StatsManagerRepository.getStatsManager(teamManager.getTeamType()).set(teamId, key, value);
		return null;
	}

	public void broadcast(String translationKey, Object... values) {
		Player player;
		for (int playerId : players) {
			player = UtilPlayer.searchExact(playerId);

			if (player != null) {
				player.sendMessage(TranslationHandler.getText(player, "GILDEN_PREFIX") + TranslationHandler.getText(player, translationKey, values));
			}
		}
	}

	public void add(int playerId) {
		players.add(playerId);
	}

	public void remove(int playerId) {
		players.remove(playerId);
	}
}
