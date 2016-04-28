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
import eu.epicpvp.kcore.Util.UtilNumber;
import eu.epicpvp.kcore.kCore;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class TeamManager {
	@Getter
	private final kCore instance;
	@Getter
	private final StatsManager statsManager;
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
		this.teamType = getTeamType(serverType);

		if (teamType != null) {
			this.statsManager = StatsManagerRepository.getStatsManager(teamType);

			mysql.Update("CREATE TABLE IF NOT EXISTS `teams` (\n" +
					"  `teamId` int(11) NOT NULL,\n" +
					"  `gameType` varchar(24) COLLATE utf8_unicode_ci NOT NULL,\n" +
					"  `name` varchar(16) COLLATE utf8_unicode_ci NOT NULL,\n" +
					"  `prefix` varchar(16) COLLATE utf8_unicode_ci NOT NULL,\n" +
					"  `owner` int(11) NOT NULL,\n" +
					"  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP\n" +
					") ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;");
			mysql.Update("ALTER TABLE `teams`\n" +
					"  ADD PRIMARY KEY (`teamId`),\n" +
					"  ADD UNIQUE KEY `name` (`name`);");
			mysql.Update("CREATE TABLE IF NOT EXISTS teams_permissions (playerId int, gameType varchar(16), permission varchar(30)) DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci");

			new TeamListener(this);
		} else {
			statsManager = null;
			new NullPointerException("teamType konnte nicht gefunden werden (" + serverType.getShortName() + ")").printStackTrace();
		}
	}

	private static GameType getTeamType(GameType type) {
		switch (type) {
		case PVP:
			return GameType.PVP_TEAMS;
		case SKYBLOCK:
			return GameType.SKYBLOCK_TEAMS;
//		case GAME:
//			return GameType.GAME_TEAMS;
		default:
			return null;
		}
	}

	public Team getPlayerTeam(Player player) {
		StatsManager serverStatsManager = StatsManagerRepository.getStatsManager(serverType);
		int teamId = UtilNumber.toInt(serverStatsManager.get(player, StatsKey.TEAM_ID));
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

	public Team createTeam(int playerId, String name, String prefix) {
		try {
			int teamId = mysql.InsertGetId("INSERT INTO teams (gameType, name, prefix, owner) VALUES (" + serverType + ", " + name + ", " + prefix + ", " + playerId + ")");
			Team team = new Team(this, teamId, name, prefix, playerId);
			this.teams.put(teamId, team);
			return team;
		} catch (SQLException e) {
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
}
