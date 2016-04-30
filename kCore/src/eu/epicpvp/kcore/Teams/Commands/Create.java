package eu.epicpvp.kcore.Teams.Commands;

import eu.epicpvp.kcore.Teams.Team;
import eu.epicpvp.kcore.Teams.TeamManager;
import eu.epicpvp.kcore.Teams.Exceptions.TeamNameAlreadyExistsException;
import eu.epicpvp.kcore.Translation.TranslationHandler;
import eu.epicpvp.kcore.Util.UtilPlayer;

import org.bukkit.entity.Player;

import dev.wolveringer.dataserver.gamestats.GameType;
import dev.wolveringer.dataserver.gamestats.StatsKey;

public class Create {
	
	public static boolean on(Player player, String[] args, TeamManager teamManager){
		if(args.length==2){
			if(teamManager.getPlayerTeam(player)!=null){
				player.sendMessage(TranslationHandler.getText(player, "GILDE_PREFIX")+TranslationHandler.getText(player, "GILDE_PLAYER_IS_IN_GILDE"));
				return false;
			}
			String teamName = args[1];
			if(teamName.length()<2){
				player.sendMessage(TranslationHandler.getText(player, "GILDE_PREFIX")+TranslationHandler.getText(player, "GILDE_NAME_LENGTH_MIN",2));
				return false;
			}
			if(teamName.length()>5){
				player.sendMessage(TranslationHandler.getText(player, "GILDE_PREFIX")+TranslationHandler.getText(player, "GILDE_NAME_LENGTH_MAX",5));
				return false;
			}

			if(teamName.contains("'")) return false;

			if(!teamName.matches("[a-zA-Z0-9]*")){
				player.sendMessage(TranslationHandler.getText(player, "GILDE_PREFIX")+" §c§lDu hast ein Ung§ltiges Zeichen in deinen Clannamen!");
				return false;
			}
			
			try {
				Team team = teamManager.createTeam(UtilPlayer.getPlayerId(player), teamName, "§7"+teamName+"§b*§f");
				team.add(UtilPlayer.getPlayerId(player));
				if(teamManager.getServerType() == GameType.PVP){
					team.set(StatsKey.LOC_X, player.getLocation().getBlockX());
					team.set(StatsKey.LOC_Y, player.getLocation().getBlockY());
					team.set(StatsKey.LOC_Z, player.getLocation().getBlockZ());
					team.set(StatsKey.WORLD, player.getLocation().getWorld().getName());
				}
			} catch (TeamNameAlreadyExistsException e) {
				player.sendMessage(TranslationHandler.getText(player, "GILDE_PREFIX")+TranslationHandler.getText(player, "GILDE_EXIST"));
				return false;
			}

//			if(manager instanceof SkyBlockGildenManager){
//				SkyBlockGildenManager sky = (SkyBlockGildenManager)manager;
//				if(sky.getStats().getDouble(player, StatsKey.MONEY)>=500.0){
//					sky.getStats().add(player, StatsKey.MONEY,-500.0);
//				}else{
//					player.sendMessage(TranslationHandler.getText(player, "PREFIX")+"Du brauchst 500 Epics um eine Gilde zu erstellen.");
//					return;
//				}
//			}
			
			player.sendMessage(TranslationHandler.getText(player, "GILDE_PREFIX")+TranslationHandler.getText(player, "GILDE_CREATE",teamManager));
		}else{
			player.sendMessage(TranslationHandler.getText(player, "GILDE_PREFIX")+" /gilde erstellen [Gilde]");
		}
		
		return false;
	}
	
}
