package eu.epicpvp.kcore.Teams.Commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import eu.epicpvp.kcore.Teams.Team;
import eu.epicpvp.kcore.Teams.TeamManager;
import eu.epicpvp.kcore.Teams.TeamRank;
import eu.epicpvp.kcore.Translation.TranslationHandler;
import eu.epicpvp.kcore.Util.UtilPlayer;

public class Invite {
	public static boolean on(Player player, String[] args, TeamManager teamManager) {
		if (args.length != 2) {
			player.sendMessage(TranslationHandler.getText(player, "GILDE_PREFIX") + "/gilde einladen [Player]");
			return true;
		}
		String einladen_o = args[1];
		if (!UtilPlayer.isOnline(einladen_o)) {
			player.sendMessage(TranslationHandler.getText(player, "GILDE_PREFIX") + TranslationHandler.getText(player, "GILDE_PLAYER_OFFLINE", einladen_o));
			return true;
		}

		Player einladen = Bukkit.getPlayer(einladen_o);
		if (teamManager.getTeamIfLoaded(einladen) != null) {
			player.sendMessage(TranslationHandler.getText(player, "GILDE_PREFIX") + TranslationHandler.getText(player, "GILDE_PLAYER_IS_IN_GILDE1", einladen_o));
			return true;
		}

		Team playerTeam = teamManager.getTeamIfLoaded(player);
		if (playerTeam == null) {
			player.sendMessage(TranslationHandler.getText(player, "GILDE_PREFIX") + TranslationHandler.getText(player, "GILDE_PLAYER_IS_NOT_IN_GILDE", einladen_o));
			return true;
		}

		TeamRank rank = playerTeam.getRank(player);
		if (rank == TeamRank.USER) {
			player.sendMessage(TranslationHandler.getText(player, "GILDE_PREFIX") + TranslationHandler.getText(player, "GILDE_OWNER_NOT"));
			return true;
		}

		if (playerTeam.getPlayers().size() >= 10) {
			player.sendMessage(TranslationHandler.getText(player, "GILDE_PREFIX") + TranslationHandler.getText(player, "GILDE_COUNT"));
			return true;
		}

		playerTeam.getInvited().add(UtilPlayer.getPlayerId(einladen));
		player.sendMessage(TranslationHandler.getText(player, "GILDE_PREFIX") + TranslationHandler.getText(player, "GILDE_EINLADEN", einladen_o));
		einladen.sendMessage(TranslationHandler.getText(player, "GILDE_PREFIX") + TranslationHandler.getText(player, "GILDE_EILADUNG", playerTeam.getName()));
		return true;
	}
}
