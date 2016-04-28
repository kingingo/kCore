package eu.epicpvp.kcore.Teams.Commands;

import eu.epicpvp.kcore.Teams.TeamManager;
import eu.epicpvp.kcore.Translation.TranslationHandler;
import org.bukkit.entity.Player;

public class Create {
	
	public static boolean on(Player player, String[] args, TeamManager teamManager){
		if(args.length==2){
//			if(manager.isPlayerInGilde(player)){
//				player.sendMessage(TranslationHandler.getText(player, "GILDE_PREFIX")+TranslationHandler.getText(player, "GILDE_PLAYER_IS_IN_GILDE"));
//				return;
//			}
//			String g = args[1];
//			if(g.length()<2){
//				player.sendMessage(TranslationHandler.getText(player, "GILDE_PREFIX")+TranslationHandler.getText(player, "GILDE_NAME_LENGTH_MIN",2));
//				return;
//			}
//			if(g.length()>5){
//				player.sendMessage(TranslationHandler.getText(player, "GILDE_PREFIX")+TranslationHandler.getText(player, "GILDE_NAME_LENGTH_MAX",5));
//				return;
//			}
//
//			if(g.contains("'"))return;
//
//			if(manager.ExistGilde(g)){
//				player.sendMessage(TranslationHandler.getText(player, "GILDE_PREFIX")+TranslationHandler.getText(player, "GILDE_EXIST"));
//				return;
//			}
//
//			if(!g.matches("[a-zA-Z0-9]*")){
//				player.sendMessage(TranslationHandler.getText(player, "GILDE_PREFIX")+" §c§lDu hast ein Ung§ltiges Zeichen in deinen Clannamen!");
//				return;
//			}
//
//			if(manager.getTyp()== GildenType.SKY&&manager instanceof SkyBlockGildenManager){
//				SkyBlockGildenManager sky = (SkyBlockGildenManager)manager;
//				if(sky.getStats().getDouble(player, StatsKey.MONEY)>=500.0){
//					sky.getStats().add(player, StatsKey.MONEY,-500.0);
//				}else{
//					player.sendMessage(TranslationHandler.getText(player, "PREFIX")+"Du brauchst 500 Epics um eine Gilde zu erstellen.");
//					return;
//				}
//			}
//
//			manager.createGildenEintrag(g, "§7"+g+"§b*§f", 10, UtilPlayer.getPlayerId(player));
//			manager.createPlayerEintrag(player, g);
//			if(manager.getTyp()==GildenType.PVP){
//				manager.setInt(g, player.getLocation().getBlockX(), StatsKey.LOC_X);
//				manager.setInt(g, player.getLocation().getBlockY(), StatsKey.LOC_Y);
//				manager.setInt(g, player.getLocation().getBlockZ(), StatsKey.LOC_Z);
//				manager.setString(g, player.getLocation().getWorld().getName(), StatsKey.WORLD);
//			}
//			player.sendMessage(TranslationHandler.getText(player, "GILDE_PREFIX")+TranslationHandler.getText(player, "GILDE_CREATE",g));
		}else{
			player.sendMessage(TranslationHandler.getText(player, "GILDE_PREFIX")+" /gilde erstellen [Gilde]");
		}
		
		return false;
	}
	
}
