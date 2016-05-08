package eu.epicpvp.kcore.Teams.Commands;

import org.bukkit.entity.Player;

import dev.wolveringer.dataserver.protocoll.packets.PacketOutTopTen;
import eu.epicpvp.kcore.Teams.TeamManager;

public class Ranking {
	public static boolean on(Player player, String[] args, TeamManager teamManager) {
		eu.epicpvp.kcore.StatsManager.Ranking ranking = teamManager.getRanking();

		player.sendMessage("§b– – – – – – – – §6§lRanking | Top 15 §b– – – – – – – –");
		player.sendMessage("§b Place | " + ranking.getStats().getMySQLName() + " | Team");
		PacketOutTopTen.RankInformation[] rankInfos = ranking.getRanking();
		for (int i = 0; i < rankInfos.length; i++) {
			PacketOutTopTen.RankInformation rankInfo = rankInfos[i];
			String teamName = TeamManager.getRealTeamName(rankInfo.getPlayer());
			player.sendMessage("§b#§6" + (i + 1) + "§b | §6" + rankInfo.getTopValue() + " §b|§d " + teamName);
		}

		return true;
	}
}
