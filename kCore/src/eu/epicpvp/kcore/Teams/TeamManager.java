package eu.epicpvp.kcore.Teams;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import dev.wolveringer.dataserver.gamestats.GameType;
import dev.wolveringer.dataserver.gamestats.StatsKey;
import eu.epicpvp.kcore.MySQL.MySQL;
import eu.epicpvp.kcore.StatsManager.StatsManager;
import eu.epicpvp.kcore.StatsManager.StatsManagerRepository;
import eu.epicpvp.kcore.Teams.Events.TeamLoadedEvent;
import eu.epicpvp.kcore.kCore;
import lombok.Getter;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class TeamManager {
	@Getter
	private final kCore instance;
	@Getter
	private final StatsManager teamStatsManager;
	@Getter
	private final GameType serverType;
	@Getter
	private final GameType teamType;
	private final MySQL mysql;
	private final Map<Integer, Team> teams = new HashMap<>();
	private final Map<String, Team> teamsByName = new HashMap<>();

	public TeamManager(kCore instance, MySQL mysql, GameType serverType) {
		this.instance = instance;
		this.mysql = mysql;
		this.serverType = serverType;
		this.teamType = serverType.getTeamType();

		if (teamType != null) {
			this.teamStatsManager = StatsManagerRepository.getStatsManager(instance, teamType);

			mysql.Update("CREATE TABLE IF NOT EXISTS `teams` (\n" +
					"  `teamId` int(11) NOT NULL,\n" +
					"  `gameType` varchar(32) COLLATE utf8_unicode_ci NOT NULL,\n" +
					"  `name` varchar(16) COLLATE utf8_unicode_ci NOT NULL,\n" +
					"  `prefix` varchar(16) COLLATE utf8_unicode_ci NOT NULL,\n" +
					"  `owner` int(11) NOT NULL,\n" +
					"  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP\n" +
					") ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;");
			mysql.Update("ALTER TABLE `teams`\n" +
					"  ADD PRIMARY KEY (`teamId`),\n" +
					"  ADD UNIQUE KEY `name` (`name`);");
			mysql.Update("CREATE TABLE IF NOT EXISTS teams_permissions (playerId int, gameType varchar(32), permission varchar(32)) DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci");

			new TeamListener(this);
		} else {
			teamStatsManager = null;
			new NullPointerException("teamType konnte nicht gefunden werden (" + serverType.getShortName() + ")").printStackTrace();
		}
	}

	public Team getPlayerTeam(Player player) {
		int teamId = teamStatsManager.getInt(player, StatsKey.TEAM_ID);
		if (teamId < 0) {
			return null;
		}
		return getTeam(teamId);
	}

	public Team getTeam(int teamId) {
		Team team = teams.get(teamId);
		if (team == null) {
			team = loadTeam(teamId);
			if (team != null) {
				teams.put(teamId, team);
				teamsByName.put(team.getName(), team);
				Bukkit.getPluginManager().callEvent(new TeamLoadedEvent(team));
			}
		}
		return team;
	}

	public Team getTeam(String name) {
		String lowercaseName = name.toLowerCase();
		Team team = teamsByName.get(lowercaseName);
		if (team == null) {
			team = loadTeam(name);
			if (team != null) {
				teams.put(team.getTeamId(), team);
				teamsByName.put(team.getName(), team);
				Bukkit.getPluginManager().callEvent(new TeamLoadedEvent(team));
			}
		}
		return team;
	}

	public Team createTeam(int playerId, String name, String prefix) throws TeamNameAlreadyExistsException {
		try {
			int teamId = mysql.InsertGetId("INSERT INTO teams (gameType, name, prefix, owner) VALUES (" + serverType + ", " + name + ", " + prefix + ", " + playerId + ")");
			Team team = new Team(this, teamId, name, prefix, playerId);
			this.teams.put(teamId, team);
			return team;
		} catch (SQLException e) {
			if (e.getErrorCode() == 1062) {
				throw new TeamNameAlreadyExistsException();
			}
			e.printStackTrace();
		}
		return null;
	}

	private Team loadTeam(int teamId) {
		try (ResultSet rs = mysql.Query("SELECT * FROM teams WHERE teamId='" + teamId + "'")) {
			if (rs.next()) {
				//GameType gameType = GameType.get(rs.getString("name"));
				String name = rs.getString("name");
				String prefix = rs.getString("prefix");
				int ownerId = rs.getInt("owner");
				return new Team(this, teamId, name, prefix, ownerId);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	private Team loadTeam(String name) {
		try (ResultSet rs = mysql.Query("SELECT * FROM teams WHERE name='" + name + "'")) {
			if (rs.next()) {
				int teamId = rs.getInt("teamId");
				//GameType gameType = GameType.get(rs.getString("name"));
				name = rs.getString("name");
				String prefix = rs.getString("prefix");
				int ownerId = rs.getInt("owner");
				return new Team(this, teamId, name, prefix,ownerId);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public static int getTeamStatsId(int teamId) {
		Validate.isTrue(teamId >= 0, "invalide teamId versucht zu teamStatsId zu konverten");
		int teamStatsId = -teamId;
		teamStatsId -= 1000;
		return teamStatsId;
	}

	public static int getTeamId(int teamStatsId) {
		Validate.isTrue(teamStatsId >= 0, "invalide teamStatsId versucht zu teamId zu konverten");
		int teamId = teamStatsId + 1000;
		teamId = -teamId;
		return teamId;
	}
}
