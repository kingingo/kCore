package eu.epicpvp.kcore.Gilden.Commands;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import eu.epicpvp.kcore.Language.Language;
import eu.epicpvp.kcore.Util.UtilPlayer;
import eu.epicpvp.kcore.kConfig.kConfig;
import eu.epicpvp.kcore.Gilden.GildenManager;
import eu.epicpvp.kcore.Gilden.GildenType;
import eu.epicpvp.kcore.Gilden.SkyBlockGildenManager;

public class Kicken {

	public static void use(Player p,String[] args,GildenManager manager){
		if(args.length==2){
			if(!manager.isPlayerInGilde(p)){
				p.sendMessage(Language.getText(p, "GILDE_PREFIX")+Language.getText(p, "GILDE_PLAYER_IS_NOT_IN_GILDE"));
				return;
			}
			String g = manager.getPlayerGilde(p);
			UUID owner=manager.getOwner(g);
			if(!owner.equals(UtilPlayer.getRealUUID(p))){
				p.sendMessage(Language.getText(p, "GILDE_PREFIX")+Language.getText(p, "GILDE_OWNER_NOT"));
				return;
			}
			String kick_o = args[1];
			UUID uuid = UtilPlayer.getUUID(kick_o, manager.getMysql());
			if(!manager.isPlayerInGilde(uuid)){
				p.sendMessage(Language.getText(p, "GILDE_PREFIX")+Language.getText(p, "GILDE_IS_NOT_IN_THE_GILD",kick_o));
				return;
			}
			if(!manager.getPlayerGilde(uuid).equalsIgnoreCase(g)){
				p.sendMessage(Language.getText(p, "GILDE_PREFIX")+Language.getText(p, "GILDE_IS_NOT_IN_THE_GILD",kick_o));
				return;
			}
			
			if(manager.getTyp()==GildenType.SKY&&manager instanceof SkyBlockGildenManager){
				SkyBlockGildenManager sky = (SkyBlockGildenManager)manager;
				kConfig config;
				
				if(sky.getSky().getInstance().getUserData().getConfigs().containsKey(uuid)&&UtilPlayer.isOnline(kick_o)){
					config=sky.getSky().getInstance().getUserData().getConfig(Bukkit.getPlayer(kick_o));
					Bukkit.getPlayer(kick_o).teleport(Bukkit.getWorld("world").getSpawnLocation());
				}else{
					config=sky.getSky().getInstance().getUserData().loadConfig(uuid);
				}
				
				for(String path : config.getPathList("homes").keySet()){
					if(config.getLocation("homes."+path).getWorld().getName().equalsIgnoreCase(sky.getSky().getGilden_world().getWorld().getName())){
						config.set("homes."+path, null);
					}
				}
				config.save();
			}
			
			manager.sendGildenChat(g, "GILDE_KICK_PLAYER",kick_o);
			manager.removePlayerEintrag(uuid,kick_o);
		}else{
			p.sendMessage(Language.getText(p, "GILDE_PREFIX")+" /gilde kicken [Player]");
		}
	}
	
}
