package eu.epicpvp.kcore.Teams.Commands;

import dev.wolveringer.dataserver.gamestats.GameType;
import eu.epicpvp.kcore.Teams.Team;
import eu.epicpvp.kcore.Teams.TeamManager;
import eu.epicpvp.kcore.Translation.TranslationHandler;
import eu.epicpvp.kcore.Util.UtilPlayer;
import org.bukkit.entity.Player;

public class Kick {
	public static boolean on(Player player, String[] args, TeamManager teamManager) {
		if (args.length != 2) {
			player.sendMessage(TranslationHandler.getText(player, "GILDE_PREFIX") + " /gilde kicken [Player]");
			return true;
		}
		int playerId = UtilPlayer.getPlayerId(player);
		Team team = teamManager.getTeamIfLoaded(playerId);
		if (team == null) {
			player.sendMessage(TranslationHandler.getText(player, "GILDE_PREFIX") + TranslationHandler.getText(player, "GILDE_PLAYER_IS_NOT_IN_GILDE"));
			return true;
		}
		String kick_o = args[1];
		int kick_id = UtilPlayer.getPlayerId(kick_o);
		if (team.getRank(playerId).ordinal() < team.getRank(kick_id).ordinal()) {
			player.sendMessage(TranslationHandler.getText(player, "GILDE_PREFIX") + TranslationHandler.getText(player, "GILDE_OWNER_NOT")); //TODO neue nachricht - keine rechte
			return true;
		}
		if (!team.getPlayers().contains(kick_id)) {
			player.sendMessage(TranslationHandler.getText(player, "GILDE_PREFIX") + TranslationHandler.getText(player, "GILDE_IS_NOT_IN_THE_GILD", kick_o));
			return true;
		}

		if (teamManager.getTeamType() == GameType.TEAMS_SKYBLOCK) {
//			SkyBlockGildenManager sky = (SkyBlockGildenManager)teamManager;
//			kConfig config;
//			
//			if(sky.getSky().getInstance().getUserData().getConfigs().containsKey(kick_id)&&UtilPlayer.isOnline(kick_o)){
//				config=sky.getSky().getInstance().getUserData().getConfig(Bukkit.getPlayer(kick_o));
//				Bukkit.getPlayer(kick_o).teleport(Bukkit.getWorld("world").getSpawnLocation());
//			}else{
//				config=sky.getSky().getInstance().getUserData().loadConfig(kick_id);
//			}
//			
//			for(String path : config.getPathList("homes").keySet()){
//				if(config.getLocation("homes."+path).getWorld().getName().equalsIgnoreCase(sky.getSky().getGilden_world().getWorld().getName())){
//					config.set("homes."+path, null);
//				}
//			}
//			config.save();
		}

		team.broadcast("GILDE_KICK_PLAYER", kick_o);
		teamManager.delete(team);
		return true;
	}
}
