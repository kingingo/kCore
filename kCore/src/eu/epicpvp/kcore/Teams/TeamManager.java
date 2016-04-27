package eu.epicpvp.kcore.Teams;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import dev.wolveringer.dataserver.gamestats.GameType;
import eu.epicpvp.kcore.kCore;
import eu.epicpvp.kcore.MySQL.MySQL;
import lombok.Getter;

public class TeamManager {

	@Getter
	private final kCore plugin;
	private final MySQL mysql;
	private final Map<Integer, Team> teams = new HashMap<>();
	private final Map<String, Team> teamsByName = new HashMap<>();

	public TeamManager(kCore plugin, MySQL mysql) {
		this.plugin = plugin;
		this.mysql = mysql;

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
		mysql.Update("CREATE TABLE IF NOT EXISTS teams_member (playerId int, gameType varchar(16), teamId int) DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci");
		mysql.Update("CREATE TABLE IF NOT EXISTS teams_permissions (playerId int, gameType varchar(16), permission varchar(30)) DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci");
	}

	public Team getTeam(int teamId) {
		Team team = teams.get(teamId);
		if (team == null) {
			team = loadTeam(teamId);
			if (team != null) {
				teams.put(teamId, team);
				teamsByName.put(team.getName(), team);
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
				teams.put(team.getId(), team);
				teamsByName.put(team.getName(), team);
			}
		}
		return team;
	}

	private Team loadTeam(int teamId) {
		try (ResultSet rs = mysql.Query("SELECT * FROM teams WHERE teamId='" + teamId + "'")) {
			if(rs.next()){
				GameType gameType = GameType.get(rs.getString("name"));
				String name = rs.getString("name");
				String prefix = rs.getString("prefix");
				return new Team(this, teamId, gameType, name, prefix);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	private Team loadTeam(String name) {
		try (ResultSet rs = mysql.Query("SELECT * FROM teams WHERE name='" + name + "'")) {
			if(rs.next()){
				int teamId = rs.getInt("teamId");
				GameType gameType = GameType.get(rs.getString("name"));
				name = rs.getString("name");
				String prefix = rs.getString("prefix");
				return new Team(this, teamId, gameType, name, prefix);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return null;
	}
}
