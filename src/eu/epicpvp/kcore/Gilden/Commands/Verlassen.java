package eu.epicpvp.kcore.Gilden.Commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import eu.epicpvp.kcore.Gilden.GildenManager;
import eu.epicpvp.kcore.Gilden.GildenType;
import eu.epicpvp.kcore.Gilden.SkyBlockGildenManager;
import eu.epicpvp.kcore.Translation.TranslationHandler;
import eu.epicpvp.kcore.Util.UtilPlayer;
import eu.epicpvp.kcore.kConfig.kConfig;

public class Verlassen {

	public static void use(Player p,String[] args,GildenManager manager){
		if(args.length==1){
			if(manager.isPlayerInGilde(p)){
				String g = manager.getPlayerGilde(p);
				int owner = manager.getOwner(manager.getPlayerGilde(p));
				
				if(owner==UtilPlayer.getPlayerId(p)){
					if(owner!=UtilPlayer.getPlayerId(p)){
						System.err.println("[GildenManager] Command Verlassen: Owner == NULL");
						System.err.println("[GildenManager] Gilde: "+g);
						System.err.println("[GildenManager] Owner-ID: "+owner);
						System.err.println("[GildenManager] Player-ID: "+UtilPlayer.getPlayerId(p));
						return;
					}
					
					if(manager.getTyp()==GildenType.SKY&&manager instanceof SkyBlockGildenManager){
						SkyBlockGildenManager sky = (SkyBlockGildenManager)manager;
						manager.getMember(g);
						kConfig config;
						for(int n : manager.getGilden_player().keySet()){
							if(sky.getSky().getInstance().getUserData().getConfigs().containsKey(n)&&UtilPlayer.isOnline(n)){
								config=sky.getSky().getInstance().getUserData().getConfig(n);
								if(UtilPlayer.searchExact(n).getWorld().getName().equalsIgnoreCase(sky.getSky().getGilden_world().getWorld().getName()))
									UtilPlayer.searchExact(n).teleport(Bukkit.getWorld("world").getSpawnLocation());
							}else{
								config=sky.getSky().getInstance().getUserData().loadConfig(n);
							}
							
							for(String path : config.getPathList("homes").keySet()){
								if(config.getLocation("homes."+path).getWorld().getName().equalsIgnoreCase(sky.getSky().getGilden_world().getWorld().getName())){
									config.set("homes."+path, null);
								}
							}
							config.save();
						}
					}
					
					manager.sendGildenChat(g, "GILDE_CLOSED");
					manager.removeGildenEintrag(p,g);
				}else{
					
					if(manager.getTyp()==GildenType.SKY&&manager instanceof SkyBlockGildenManager){
						SkyBlockGildenManager sky = (SkyBlockGildenManager)manager;
						kConfig config=sky.getSky().getInstance().getUserData().getConfig(p);
						for(String path : config.getPathList("homes").keySet()){
							if(config.getLocation("homes."+path).getWorld().getName().equalsIgnoreCase(sky.getSky().getGilden_world().getWorld().getName())){
								config.set("homes."+path, null);
							}
						}
						config.save();
						if(p.getWorld().getName().equalsIgnoreCase(sky.getSky().getGilden_world().getWorld().getName()))
							p.teleport(Bukkit.getWorld("world").getSpawnLocation());
					}
					
					manager.sendGildenChat(g, "GILDE_PLAYER_GO_OUT",p.getName());
					manager.removePlayerEintrag(p);
					p.setDisplayName(p.getName());
				}
			}else{
				p.sendMessage(TranslationHandler.getText(p, "GILDE_PREFIX")+TranslationHandler.getText(p, "GILDE_PLAYER_IS_IN_GILDE"));
			}
		}else{
			p.sendMessage(TranslationHandler.getText(p, "GILDE_PREFIX")+" /gilde verlassen");
		}
	}
	
}
