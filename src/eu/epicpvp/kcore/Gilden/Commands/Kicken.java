package eu.epicpvp.kcore.Gilden.Commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import eu.epicpvp.kcore.Gilden.GildenManager;
import eu.epicpvp.kcore.Gilden.GildenType;
import eu.epicpvp.kcore.Gilden.SkyBlockGildenManager;
import eu.epicpvp.kcore.Translation.TranslationHandler;
import eu.epicpvp.kcore.Util.UtilPlayer;
import eu.epicpvp.kcore.kConfig.kConfig;

public class Kicken {

	public static void use(Player p,String[] args,GildenManager manager){
		if(args.length==2){
			if(!manager.isPlayerInGilde(p)){
				p.sendMessage(TranslationHandler.getText(p, "GILDE_PREFIX")+TranslationHandler.getText(p, "GILDE_PLAYER_IS_NOT_IN_GILDE"));
				return;
			}
			String g = manager.getPlayerGilde(p);
			int owner = manager.getOwner(manager.getPlayerGilde(p));
			if(owner!=UtilPlayer.getPlayerId(p)){
				p.sendMessage(TranslationHandler.getText(p, "GILDE_PREFIX")+TranslationHandler.getText(p, "GILDE_OWNER_NOT"));
				return;
			}
			String kick_o = args[1];
			int kick_id = UtilPlayer.getPlayerId(kick_o);
			if(!manager.isPlayerInGilde(kick_id)){
				p.sendMessage(TranslationHandler.getText(p, "GILDE_PREFIX")+TranslationHandler.getText(p, "GILDE_IS_NOT_IN_THE_GILD",kick_o));
				return;
			}
			if(!manager.getPlayerGilde(kick_id).equalsIgnoreCase(g)){
				p.sendMessage(TranslationHandler.getText(p, "GILDE_PREFIX")+TranslationHandler.getText(p, "GILDE_IS_NOT_IN_THE_GILD",kick_o));
				return;
			}
			
			if(manager.getTyp()==GildenType.SKY&&manager instanceof SkyBlockGildenManager){
				SkyBlockGildenManager sky = (SkyBlockGildenManager)manager;
				kConfig config;
				
				if(sky.getSky().getInstance().getUserData().getConfigs().containsKey(kick_id)&&UtilPlayer.isOnline(kick_o)){
					config=sky.getSky().getInstance().getUserData().getConfig(Bukkit.getPlayer(kick_o));
					if(Bukkit.getPlayer(kick_o).getWorld().getUID()!=Bukkit.getWorld("world").getUID())Bukkit.getPlayer(kick_o).teleport(Bukkit.getWorld("world").getSpawnLocation());
				}else{
					config=sky.getSky().getInstance().getUserData().loadConfig(kick_id);
				}
				
				for(String path : config.getPathList("homes").keySet()){
					if(config.getLocation("homes."+path).getWorld().getName().equalsIgnoreCase(sky.getSky().getGilden_world().getWorld().getName())){
						config.set("homes."+path, null);
					}
				}
				config.save();
			}
			
			manager.sendGildenChat(g, "GILDE_KICK_PLAYER",kick_o);
			manager.removePlayerEintrag(kick_id,kick_o);
		}else{
			p.sendMessage(TranslationHandler.getText(p, "GILDE_PREFIX")+" /gilde kicken [Player]");
		}
	}
	
}
