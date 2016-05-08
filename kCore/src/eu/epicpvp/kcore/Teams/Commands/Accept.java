package eu.epicpvp.kcore.Teams.Commands;

import org.bukkit.entity.Player;

import eu.epicpvp.kcore.Teams.Team;
import eu.epicpvp.kcore.Teams.TeamManager;
import eu.epicpvp.kcore.Teams.TeamRank;
import eu.epicpvp.kcore.Translation.TranslationHandler;
import eu.epicpvp.kcore.Util.UtilPlayer;

public class Accept {
	public static boolean on(Player player, String[] args, TeamManager teamManager) {
		if (args.length == 2) {
			String teamName = args[1];
			Team teamInvited = teamManager.getTeamIfLoaded(teamName);
			int playerId = UtilPlayer.getPlayerId(player);
			if (teamInvited != null && teamInvited.getInvited().contains(playerId)) {
				if (teamManager.getTeamIfLoaded(player) != null) {
					player.sendMessage(TranslationHandler.getText(player, "GILDE_PREFIX") + TranslationHandler.getText(player, "GILDE_PLAYER_IS_IN_GILDE"));
					teamInvited.getInvited().remove(playerId);
					return true;
				}
				if (teamInvited.getPlayers().size() >= 10) {
					player.sendMessage(TranslationHandler.getText(player, "GILDE_PREFIX") + TranslationHandler.getText(player, "GILDE_COUNT"));
					teamInvited.getInvited().remove(playerId);
					return true;
				}
				teamInvited.getInvited().remove(playerId);
				teamInvited.addPlayer(playerId, TeamRank.USER);
				teamInvited.broadcast("GILDE_PLAYER_ENTRE", player.getName());
			} else {
				player.sendMessage(TranslationHandler.getText(player, "GILDE_PREFIX") + TranslationHandler.getText(player, "GILDE_PLAYER_NICHT_EINGELADEN"));
			}
		} else {
			player.sendMessage(TranslationHandler.getText(player, "GILDE_PREFIX") + " /gilde annehmen <Name>");
		}
		return true;
	}
}
