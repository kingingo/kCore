package eu.epicpvp.kcore.Teams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import dev.wolveringer.client.Callback;
import dev.wolveringer.client.LoadedPlayer;
import dev.wolveringer.dataserver.gamestats.GameType;
import dev.wolveringer.dataserver.gamestats.StatsKey;
import eu.epicpvp.kcore.StatsManager.Ranking;
import eu.epicpvp.kcore.StatsManager.StatsManager;
import eu.epicpvp.kcore.StatsManager.StatsManagerRepository;
import eu.epicpvp.kcore.Teams.Events.TeamLoadedEvent;
import eu.epicpvp.kcore.Util.UtilServer;
import eu.epicpvp.kcore.kCore;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class TeamManager {
	private static final String TEAM_FAKEPLAYER_PREFIX = "_team_team_team_";
	@Getter
	private final kCore instance;
	@Getter
	private final GameType serverType;
	@Getter
	private final GameType teamType;
	@Getter
	private final StatsManager serverStatsManager;
	@Getter
	private final StatsManager teamStatsManager;
	private final TIntObjectMap<Team> teams = new TIntObjectHashMap<>();
	private final Map<String, Team> teamsByName = new HashMap<>();
	@Getter
	private Ranking ranking;

	public TeamManager(@Nonnull kCore instance, @Nonnull GameType serverType, @Nonnull StatsKey rankingKey) {
		this.instance = instance;
		this.serverType = serverType;
		this.teamType = serverType.getTeamType();
		this.serverStatsManager = StatsManagerRepository.getStatsManager(serverType);
		this.teamStatsManager = StatsManagerRepository.getStatsManager(teamType);
		this.ranking = new Ranking(teamType, rankingKey);
		this.teamStatsManager.addRanking(this.ranking);
		new TeamListener(this);
	}

	public void getTeam(@Nonnull Player player, @Nullable Callback<Team> callback) {
		int teamId = teamStatsManager.getInt(player, StatsKey.TEAM_ID);
		if (teamId <= 0) {
			if (callback != null) {
				callback.call(null);
			}
		}
		getTeam(teamId, callback);
	}

	@Nullable
	public Team getTeamIfLoaded(@Nonnegative int teamId) {
		return teams.get(teamId);
	}

	@Nullable
	public Team getTeamIfLoaded(@Nonnull String teamName) {
		return teamsByName.get(teamName.toLowerCase());
	}

	@Nullable
	public Team getTeamIfLoaded(@Nonnull Player player) {
		int teamId = teamStatsManager.getInt(player, StatsKey.TEAM_ID);
		return getTeamIfLoaded(teamId);
	}

	public void getTeam(int teamId, @Nullable Callback<Team> callback) {
		if (teamId <= 0) {
			if (callback != null) {
				callback.call(null);
			}
			return;
		} 
		Team team = teams.get(teamId);
		if (team != null) {
			if (callback != null) {
				callback.call(team);
			}
			return;
		}
		loadTeam(teamId, loadedTeam -> {
			if (loadedTeam != null) {
				teams.put(teamId, loadedTeam);
				teamsByName.put(loadedTeam.getName().toLowerCase(), loadedTeam);
				if (callback != null) {
					callback.call(loadedTeam);
				}
				Bukkit.getPluginManager().callEvent(new TeamLoadedEvent(loadedTeam));
			}
		});
	}

	public void getTeam(String name, Callback<Team> callback) {
		Team team = teamsByName.get(name.toLowerCase());
		if (team != null) {
			if (callback != null) {
				callback.call(team);
			}
			return;
		}
		int teamId = UtilServer.getClient().getPlayerAndLoad(name).getPlayerId();
		loadTeam(teamId, loadedTeam -> {
			if (loadedTeam != null) {
				teams.put(loadedTeam.getTeamId(), loadedTeam);
				teamsByName.put(loadedTeam.getName(), loadedTeam);
				if (callback != null) {
					callback.call(loadedTeam);
				}
				Bukkit.getPluginManager().callEvent(new TeamLoadedEvent(loadedTeam));
			}
		});
	}

	public void createTeam(int ownerId, String name, String prefix, @Nullable Callback<Team> successCallback, @Nullable Callback<Void> alreadyExistsCallback) {
		LoadedPlayer teamPlayer = UtilServer.getClient().getPlayerAndLoad(getTeamStatsName(name));
		teamStatsManager.loadPlayer(teamPlayer, teamId -> {
			if (teamStatsManager.get(teamId, StatsKey.TEAM_PREFIX) != null) {
				if (alreadyExistsCallback != null) {
					alreadyExistsCallback.call(null);
				}
				return;
			}
			teamStatsManager.set(teamId, StatsKey.TEAM_PREFIX, prefix);
			Team team = new Team(TeamManager.this, teamId, name, prefix);
			team.addPlayer(ownerId, TeamRank.OWNER);
			if(successCallback!=null){
				successCallback.call(team);
			}
		});
	}

	public void delete(Team playerTeam) {
		new ArrayList<>(playerTeam.getPlayers())
				.forEach(playerTeam::removePlayer);
		//TODO delete team data
	}

	private void loadTeam(int teamId, @Nullable Callback<Team> callback) {
		teamStatsManager.loadPlayer(teamId, loadedTeamId -> {
			Team team = null;
			String prefix = teamStatsManager.getString(teamId, StatsKey.TEAM_PREFIX);
			if (prefix != null) {
				LoadedPlayer teamPlayer = UtilServer.getClient().getPlayerAndLoad(teamId);
				String name = getRealTeamName(teamPlayer.getName());
				team = new Team(TeamManager.this, teamId, name, prefix);
				loadPlayersInTeam(team);
			}
			if (callback != null) {
				callback.call(team);
			}
		});
	}

	private void loadPlayersInTeam(Team team) {
		//TODO wait on markus, then use getPlayersWithStats(gameType, statsKey, Object value)
	}

	public static String getTeamStatsName(String teamName) {
		return TEAM_FAKEPLAYER_PREFIX + teamName;
	}

	public static String getRealTeamName(String teamStatsName) {
		return teamStatsName.substring(TEAM_FAKEPLAYER_PREFIX.length());
	}
}
