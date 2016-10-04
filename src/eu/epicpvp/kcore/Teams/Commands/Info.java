package eu.epicpvp.kcore.Teams.Commands;

import java.util.List;

import org.bukkit.entity.Player;

import eu.epicpvp.datenclient.client.LoadedPlayer;
import dev.wolveringer.dataserver.gamestats.StatsKey;
import eu.epicpvp.kcore.Teams.Team;
import eu.epicpvp.kcore.Teams.TeamManager;
import eu.epicpvp.kcore.Translation.TranslationHandler;
import eu.epicpvp.kcore.Util.UtilPlayer;
import eu.epicpvp.kcore.Util.UtilServer;

public class Info {
	public static boolean on(Player player, String[] args, TeamManager teamManager) {
		if (args.length == 1) {
			int playerId = UtilPlayer.getPlayerId(player);
			Team team = teamManager.getTeamIfLoaded(playerId);
			if (team == null) {
				player.sendMessage(TranslationHandler.getText(player, "GILDE_PREFIX") + TranslationHandler.getText(player, "GILDE_PLAYER_IS_NOT_IN_GILDE"));
				return true;
			}
			player.sendMessage(TranslationHandler.getText(player, "GILDE_STATS_PREFIX"));

			String msg = buildTeamInfo(team);
			player.sendMessage(msg);
		} else if (args.length == 2) {
			String teamName = args[1];
			Team team = teamManager.getTeamIfLoaded(teamName);
			if (team == null) {
				player.sendMessage(TranslationHandler.getText(player, "GILDE_PREFIX") + TranslationHandler.getText(player, "GILDE_EXIST_NOT"));
				return true;
			}
			teamName = team.getName();

			player.sendMessage(TranslationHandler.getText(player, "GILDE_STATS_PREFIXBY", teamName));

			String msg = buildTeamInfo(team);
			player.sendMessage(msg);
		} else {
			player.sendMessage(TranslationHandler.getText(player, "GILDE_PREFIX") + " /gilde info oder /gilde info [Gilde]");
		}
		return true;
	}

	private static String buildTeamInfo(Team team) {
		List<Integer> players = team.getPlayers();
		StringBuilder builder = new StringBuilder(9 + 13 + 18 + 16 + players.size() * 18);//+16 for no resize hopefully but still have buffer close to expected size
		for (StatsKey s : team.getTeamManager().getTeamType().getStats()) {
			if (s == StatsKey.LOC_X || s == StatsKey.LOC_Y || s == StatsKey.LOC_Z || s == StatsKey.WORLD) {
				continue;
			}

			if (s == StatsKey.ELO) {
				builder.append("§6FAME: §b").append(team.getStatistic(s)).append('\n');
			} else if (s.getType() == int.class || s.getType() == double.class) {
				builder.append("§6").append(s.getMySQLName()).append(": §b").append(team.getStatistic(s)).append('\n');
			}
		}
		builder.append("§6Anzahl: §b").append(players.size()).append("/10").append('\n');
		builder.append("§6List:");
		for (int plrId : players) {
			LoadedPlayer teamMember = UtilServer.getClient().getPlayerAndLoad(plrId);
			if (UtilPlayer.isOnline(plrId)) {
				builder.append(" §a").append(teamMember.getName());
			} else {
				builder.append(" §c").append(teamMember.getName());
			}
			builder.append(',');
		}
		return builder.substring(0, builder.length() - 1);
	}
}
