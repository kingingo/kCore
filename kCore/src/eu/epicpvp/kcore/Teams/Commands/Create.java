package eu.epicpvp.kcore.Teams.Commands;

import dev.wolveringer.dataserver.gamestats.GameType;
import dev.wolveringer.dataserver.gamestats.StatsKey;
import eu.epicpvp.kcore.Gilden.SkyBlockGildenManager;
import eu.epicpvp.kcore.Teams.TeamManager;
import eu.epicpvp.kcore.Translation.TranslationHandler;
import eu.epicpvp.kcore.Util.UtilPlayer;
import org.bukkit.entity.Player;

public class Create {
	public static boolean on(Player player, String[] args, TeamManager teamManager) {
		if (args.length == 2) {
			if (teamManager.getTeamIfLoaded(player) != null) {
				player.sendMessage(TranslationHandler.getText(player, "GILDE_PREFIX") + TranslationHandler.getText(player, "GILDE_PLAYER_IS_IN_GILDE"));
				return false;
			}
			String teamName = args[1];
			if (teamName.length() < 2) {
				player.sendMessage(TranslationHandler.getText(player, "GILDE_PREFIX") + TranslationHandler.getText(player, "GILDE_NAME_LENGTH_MIN", 2));
				return false;
			}
			if (teamName.length() > 5) {
				player.sendMessage(TranslationHandler.getText(player, "GILDE_PREFIX") + TranslationHandler.getText(player, "GILDE_NAME_LENGTH_MAX", 5));
				return false;
			}

			if (teamName.contains("'")) {
				return false;
			}

			if (!teamName.matches("[a-zA-Z0-9]*")) {
				player.sendMessage(TranslationHandler.getText(player, "GILDE_PREFIX") + " §c§lDu hast ein Ung§ltiges Zeichen in deinen Clannamen!");
				return false;
			}

			teamManager.createTeam(UtilPlayer.getPlayerId(player), teamName, "§7" + teamName + "§b*§f", team -> {
				//success
				if (teamManager.getTeamType() == GameType.PLAYER_TEAMS_PVP) {
					team.setStatistic(StatsKey.LOC_X, player.getLocation().getBlockX());
					team.setStatistic(StatsKey.LOC_Y, player.getLocation().getBlockY());
					team.setStatistic(StatsKey.LOC_Z, player.getLocation().getBlockZ());
					team.setStatistic(StatsKey.WORLD, player.getLocation().getWorld().getName());
				} else if (teamManager.getTeamType() == GameType.PLAYER_TEAMS_SKYBLOCK) {
//					SkyBlockGildenManager sky = (SkyBlockGildenManager)manager;
//					if(sky.getStats().getDouble(player, StatsKey.MONEY)>=500.0){
//						sky.getStats().add(player, StatsKey.MONEY,-500.0);
//					}else{
//						player.sendMessage(TranslationHandler.getText(player, "PREFIX")+"Du brauchst 500 Epics um eine Gilde zu erstellen.");
//						return;
//					}
				}
				player.sendMessage(TranslationHandler.getText(player, "GILDE_PREFIX") + TranslationHandler.getText(player, "GILDE_CREATE", teamManager));
			}, obj -> {
				//team already exists
				player.sendMessage(TranslationHandler.getText(player, "GILDE_PREFIX") + TranslationHandler.getText(player, "GILDE_EXIST"));
			});
		} else {
			player.sendMessage(TranslationHandler.getText(player, "GILDE_PREFIX") + " /gilde erstellen [Gilde]");
		}

		return false;
	}
}
