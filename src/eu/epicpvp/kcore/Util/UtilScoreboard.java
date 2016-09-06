package eu.epicpvp.kcore.Util;

import java.util.ArrayList;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import eu.epicpvp.kcore.Scoreboard.Events.PlayerSetScoreboardEvent;
import net.minecraft.server.v1_8_R3.ScoreboardScore;

public class UtilScoreboard {

	public static String getScoreboardDisplayname() {
		return "§c§lClashMC.eu";
	}

	public static void setTeams(Scoreboard board, Set<Team> teams) {
		Team tt;
		for (Team team : teams) {
			if (!UtilScoreboard.existTeam(board, team.getName())) {
				tt = board.registerNewTeam(team.getName());
				tt.setPrefix(team.getPrefix());
			}

			tt = board.getTeam(team.getName());
			for (String player : team.getEntries()) {
				if (!tt.hasEntry(player)) {
					tt.addEntry(player);
				}
			}
		}
	}

	public static Set<Team> cloneTeams(Scoreboard board) {
		return board.getTeams();
	}

	public static void setScoreboard(Scoreboard board, Player player) {
		player.setScoreboard(board);
		Bukkit.getPluginManager().callEvent(new PlayerSetScoreboardEvent(player));
	}

	public static void removePlayer(Player player) {
		player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
	}

	public static boolean existTeam(Scoreboard board, String Team) {
		return (board.getTeam(Team) != null);
	}

	public static void removePlayerFromTeam(Scoreboard board, String Team, Player p) {
		if (board.getTeam(Team) == null)
			new NullPointerException("team == NULL");
		if (!board.getTeam(Team).getEntries().contains(p))
			return;
		Team r = board.getTeam(Team);
		r.removeEntry(p.getName());
	}

	public static void addPlayerToTeam(Scoreboard board, String Team, Player p) {
		Team t = board.getTeam(Team);
		if (t == null)
			new NullPointerException("team == NULL");
		addPlayerToTeam(board, t, p);
	}

	public static void addPlayerToTeam(Scoreboard board, Team t, Player p) {
		if (t.getEntries().contains(p.getName()))
			return;
		t.addEntry(p.getName());
	}

	public static Team addTeam(Scoreboard board, String Team) {
		return addTeam(board, Team, null, null, null);
	}

	public static Team addTeam(Scoreboard board, String Team, String prefix) {
		return addTeam(board, Team, null, prefix, null);
	}

	public static Team addTeam(Scoreboard board, String Team, String displayName, String prefix) {
		return addTeam(board, Team, displayName, prefix, null);
	}

	public static Team addTeam(Scoreboard board, String Team, String displayName, String prefix, String suffix) {
		Team t = board.getTeam(Team);
		if (t != null)
			return t;
		Team r = board.registerNewTeam(UtilString.cut(Team));

		if (displayName != null)
			r.setDisplayName(UtilString.cut(displayName, 32));
		if (prefix != null)
			r.setPrefix(UtilString.cut(prefix));
		if (suffix != null)
			r.setSuffix(UtilString.cut(suffix));
		return r;
	}

	public static Team setTeamPrefix(Scoreboard board, String team, String prefix) {
		Team t = addTeam(board, team, prefix);
		if (!t.getPrefix().equals(prefix))
			t.setPrefix(prefix);
		return t;
	}

	public static Score searchScore(Scoreboard b, String s) {
		net.minecraft.server.v1_8_R3.Scoreboard board = (net.minecraft.server.v1_8_R3.Scoreboard) UtilReflection.getValue("board", b);
		for (ScoreboardScore sc : board.getScores())
			if (sc.getPlayerName().toLowerCase().contains(s.toLowerCase()))
				return b.getObjective(DisplaySlot.valueOf(sc.getObjective().getName())).getScore(sc.getPlayerName());
		return null;
	}

	public static ArrayList<Score> getScores(Scoreboard b) {
		ArrayList<Score> scores = new ArrayList<>();
		net.minecraft.server.v1_8_R3.Scoreboard board = (net.minecraft.server.v1_8_R3.Scoreboard) UtilReflection.getValue("board", b);
		for (ScoreboardScore sc : board.getScores()) {
			scores.add(b.getObjective(DisplaySlot.valueOf(sc.getObjective().getName())).getScore(sc.getPlayerName()));
		}
		return scores;
	}

	public static void resetScore(Scoreboard board, String p1, DisplaySlot typ) {
		board.resetScores(p1);
	}

	public static void resetScore(Scoreboard b, int id, DisplaySlot typ) {
		net.minecraft.server.v1_8_R3.Scoreboard board = (net.minecraft.server.v1_8_R3.Scoreboard) UtilReflection.getValue("board", b);
		for (ScoreboardScore sc : board.getScores()) {
			if (!sc.getObjective().getName().equalsIgnoreCase(typ.name()))
				continue;
			if (sc.getScore() == id) {
				resetScore(b, b.getObjective(DisplaySlot.valueOf(sc.getObjective().getName())).getScore(sc.getPlayerName()).getEntry(), typ);
			}
		}

	}

	public static void clearScores(Scoreboard b, DisplaySlot typ) {
		if (b == null)
			return;
		if (b.getObjective(typ) == null)
			return;
		net.minecraft.server.v1_8_R3.Scoreboard board = (net.minecraft.server.v1_8_R3.Scoreboard) UtilReflection.getValue("board", b);

		for (ScoreboardScore sc : board.getScores()) {
			if (!sc.getObjective().getName().equalsIgnoreCase(typ.name()))
				continue;
			resetScore(b, b.getObjective(DisplaySlot.valueOf(sc.getObjective().getName())).getScore(sc.getPlayerName()).getEntry(), typ);
		}
	}

	public static void setScore(Scoreboard board, String scorename, DisplaySlot typ, int i) {
		if (board == null)
			return;
		if (board.getObjective(typ) == null)
			return;
		board.getObjective(typ).getScore(scorename).setScore(i);
	}

	public static void addLiveBoard(Scoreboard board, String DisplayName) {
		if (board.getObjective(DisplaySlot.BELOW_NAME.name()) == null) {
			Objective o = board.registerNewObjective("showhealth", "health");
			o.setDisplaySlot(DisplaySlot.BELOW_NAME);
			o.setDisplayName(DisplayName);
		}
	}
	
	public static void resendScoreboard(Player player){
		Scoreboard board = player.getScoreboard();
		player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
		player.setScoreboard(board);
	}

	public static void resendTeamsFromPlayer(Player player){
		for(Player p : Bukkit.getOnlinePlayers()){
			Scoreboard board = p.getScoreboard();
			for(Team team : board.getTeams()){
				for(String s : team.getEntries()){
					if(s.equalsIgnoreCase(player.getName())){
						team.removeEntry(player.getName());
						team.addEntry(player.getName());
					}
				}
			}
		}
	}
	
	public static void addBoard(Scoreboard board, DisplaySlot typ, String DisplayName) {
		if (typ == DisplaySlot.SIDEBAR && DisplayName.length() >= 32) {
			DisplayName = DisplayName.substring(0, 31);
		}
		if (board.getObjective(typ.name()) == null) {
			Objective o = board.registerNewObjective(typ.name(), "dummy");
			o.setDisplaySlot(typ);
			o.setDisplayName(DisplayName);
		}
	}

}
