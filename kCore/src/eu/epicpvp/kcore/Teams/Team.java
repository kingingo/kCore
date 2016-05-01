package eu.epicpvp.kcore.Teams;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import dev.wolveringer.dataserver.gamestats.StatsKey;
import eu.epicpvp.kcore.StatsManager.StatsManager;
import eu.epicpvp.kcore.StatsManager.StatsManagerRepository;
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
	protected final List<Integer> players = new ArrayList<>();
	protected final List<Integer> invited = new ArrayList<>();
	@Getter(AccessLevel.NONE)
	protected final Multimap<Integer, String> playerPermissions = HashMultimap.create();

	public Team(TeamManager teamManager, int teamId, String name, String prefix) {
		this.teamManager = teamManager;
		this.teamId = teamId;
		this.name = name;
		this.prefix = prefix;
	}
	
	public TeamRank getRank(Player player){
		return getRank(UtilPlayer.getPlayerId(player));
	}

	public TeamRank getRank(int playerId){
		return TeamRank.values()[teamManager.getServerStatsManager().getInt(playerId, StatsKey.TEAM_RANK)];
	}

	public void setRank(int playerId,TeamRank rank){
		teamManager.getServerStatsManager().set(playerId,StatsKey.TEAM_RANK,rank.ordinal());
	}
	
	public void addStatistic(StatsKey key, Object value) {
		teamManager.getTeamStatsManager().add(teamId, key, value);
	}

	public void setStatistic(StatsKey key, Object value) {
		teamManager.getTeamStatsManager().set(teamId, key, value);
	}

	public Object getStatistic(StatsKey key) {
		return teamManager.getTeamStatsManager().get(teamId, key);
	}

	public int getStatisticInt(StatsKey key) {
		Object stat = teamManager.getTeamStatsManager().get(teamId, key);
		return stat == null ? 0 : (Integer) stat;
	}

	public double getStatisticDouble(StatsKey key) {
		Object stat = teamManager.getTeamStatsManager().get(teamId, key);
		return stat == null ? 0 : (Double) stat;
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

	public void addPlayer(int playerId, TeamRank rank) {
		players.add(playerId);
		teamManager.getServerStatsManager().set(playerId, StatsKey.TEAM_ID, teamId);
		teamManager.getServerStatsManager().set(playerId, StatsKey.TEAM_RANK, rank.ordinal());
	}

	public void removePlayer(int playerId) {
		players.remove(playerId);
		teamManager.getServerStatsManager().set(playerId, StatsKey.TEAM_ID, -1);
		teamManager.getServerStatsManager().set(playerId, StatsKey.TEAM_RANK, -1);
		teamManager.getServerStatsManager().save(playerId);
	}
}
