package eu.epicpvp.kcore.Teams.Commands;

import dev.wolveringer.dataserver.gamestats.GameType;
import eu.epicpvp.kcore.Teams.Team;
import eu.epicpvp.kcore.Teams.TeamManager;
import eu.epicpvp.kcore.Teams.TeamRank;
import eu.epicpvp.kcore.Translation.TranslationHandler;
import eu.epicpvp.kcore.Util.UtilPlayer;
import org.bukkit.entity.Player;

public class Leave {
	public static boolean on(Player player, String[] args, TeamManager teamManager) {
		if (args.length != 1) {
			player.sendMessage(TranslationHandler.getText(player, "GILDE_PREFIX") + " /gilde verlassen");
			return true;
		}
		int playerId = UtilPlayer.getPlayerId(player);
		Team team = teamManager.getTeamIfLoaded(playerId);

		if (team == null) {
			player.sendMessage(TranslationHandler.getText(player, "GILDE_PREFIX") + TranslationHandler.getText(player, "GILDE_PLAYER_IS_NOT_IN_GILDE"));
			return true;
		}
		TeamRank rank = team.getRank(player);
		if (rank == TeamRank.OWNER) {

			if (teamManager.getTeamType() == GameType.PLAYER_TEAMS_SKYBLOCK) {
//					SkyBlockGildenManager sky = (SkyBlockGildenManager)teamManager;
//					kConfig config;
//					for(int n : teamManager.getGilden_player().keySet()){
//						if(sky.getSky().getInstance().getUserData().getConfigs().containsKey(n)&&UtilPlayer.isOnline(n)){
//							config=sky.getSky().getInstance().getUserData().getConfig(n);
//							if(UtilPlayer.searchExact(n).getWorld().getName().equalsIgnoreCase(sky.getSky().getGilden_world().getWorld().getName()))UtilPlayer.searchExact(n).teleport(Bukkit.getWorld("world").getSpawnLocation());
//						}else{
//							config=sky.getSky().getInstance().getUserData().loadConfig(n);
//						}
//						
//						for(String path : config.getPathList("homes").keySet()){
//							if(config.getLocation("homes."+path).getWorld().getName().equalsIgnoreCase(sky.getSky().getGilden_world().getWorld().getName())){
//								config.set("homes."+path, null);
//							}
//						}
//						config.save();
//					}
			}

			team.broadcast("GILDE_CLOSED");
			teamManager.delete(team);
		} else {
			if (teamManager.getTeamType() == GameType.PLAYER_TEAMS_SKYBLOCK) {
//					SkyBlockGildenManager sky = (SkyBlockGildenManager)teamManager;
//					kConfig config=sky.getSky().getInstance().getUserData().getConfig(player);
//					for(String path : config.getPathList("homes").keySet()){
//						if(config.getLocation("homes."+path).getWorld().getName().equalsIgnoreCase(sky.getSky().getGilden_world().getWorld().getName())){
//							config.set("homes."+path, null);
//						}
//					}
//					config.save();
//					if(player.getWorld().getName().equalsIgnoreCase(sky.getSky().getGilden_world().getWorld().getName()))player.teleport(Bukkit.getWorld("world").getSpawnLocation());
			}

			team.broadcast("GILDE_PLAYER_GO_OUT", player.getName());
			team.removePlayer(playerId);
			player.setDisplayName(player.getName());
		}
		return true;
	}
}
