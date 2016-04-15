package eu.epicpvp.kcore.Gilden.Commands;

import org.bukkit.entity.Player;

import dev.wolveringer.dataserver.gamestats.StatsKey;
import eu.epicpvp.kcore.Gilden.GildenManager;
import eu.epicpvp.kcore.Gilden.GildenType;
import eu.epicpvp.kcore.Gilden.SkyBlockGildenManager;
import eu.epicpvp.kcore.Translation.TranslationHandler;
import eu.epicpvp.kcore.Util.UtilPlayer;

public class Erstellen {

	public static void use(Player p,String[] args,GildenManager manager){
		if(args.length==2){
			if(manager.isPlayerInGilde(p)){
				p.sendMessage(TranslationHandler.getText(p, "GILDE_PREFIX")+TranslationHandler.getText(p, "GILDE_PLAYER_IS_IN_GILDE"));
				return;
			}
			String g = args[1];
			if(g.length()<2){
				p.sendMessage(TranslationHandler.getText(p, "GILDE_PREFIX")+TranslationHandler.getText(p, "GILDE_NAME_LENGTH_MIN",2));
				return;
			}
			if(g.length()>5){
				p.sendMessage(TranslationHandler.getText(p, "GILDE_PREFIX")+TranslationHandler.getText(p, "GILDE_NAME_LENGTH_MAX",5));
				return;
			}
			
			if(g.contains("'"))return;
			
			if(manager.ExistGilde(g)){
				p.sendMessage(TranslationHandler.getText(p, "GILDE_PREFIX")+TranslationHandler.getText(p, "GILDE_EXIST"));
				return;
			}
			
			if(!g.matches("[a-zA-Z0-9]*")){
				p.sendMessage(TranslationHandler.getText(p, "GILDE_PREFIX")+" §c§lDu hast ein Ung§ltiges Zeichen in deinen Clannamen!");
				return;
			}
			
			if(manager.getTyp()==GildenType.SKY&&manager instanceof SkyBlockGildenManager){
				SkyBlockGildenManager sky = (SkyBlockGildenManager)manager;
				if(sky.getStats().getDouble(p, StatsKey.MONEY)>=500.0){
					sky.getStats().add(p, StatsKey.MONEY,-500.0);
				}else{
					p.sendMessage(TranslationHandler.getText(p, "PREFIX")+"Du brauchst 500 Epics um eine Gilde zu erstellen.");
					return;
				}
			}
			
			manager.createGildenEintrag(g, "§7"+g+"§b*§f", 10, UtilPlayer.getPlayerId(p));
			manager.createPlayerEintrag(p, g);
			if(manager.getTyp()==GildenType.PVP){
				manager.setInt(g, p.getLocation().getBlockX(), StatsKey.LOC_X);
				manager.setInt(g, p.getLocation().getBlockY(), StatsKey.LOC_Y);
				manager.setInt(g, p.getLocation().getBlockZ(), StatsKey.LOC_Z);
				manager.setString(g, p.getLocation().getWorld().getName(), StatsKey.WORLD);
			}
			p.sendMessage(TranslationHandler.getText(p, "GILDE_PREFIX")+TranslationHandler.getText(p, "GILDE_CREATE",g));
		}else{
			p.sendMessage(TranslationHandler.getText(p, "GILDE_PREFIX")+" /gilde erstellen [Gilde]");
		}
	}
	
}
