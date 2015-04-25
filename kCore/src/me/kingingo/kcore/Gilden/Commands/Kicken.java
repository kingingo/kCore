package me.kingingo.kcore.Gilden.Commands;

import java.util.UUID;

import me.kingingo.kcore.Enum.Text;
import me.kingingo.kcore.Gilden.GildenManager;
import me.kingingo.kcore.Gilden.GildenType;
import me.kingingo.kcore.Gilden.SkyBlockGildenManager;
import me.kingingo.kcore.Util.UtilPlayer;
import me.kingingo.kcore.kConfig.kConfig;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Kicken {

	public static void use(Player p,String[] args,GildenManager manager){
		if(args.length==2){
			if(!manager.isPlayerInGilde(p)){
				p.sendMessage(Text.GILDE_PREFIX.getText()+Text.GILDE_PLAYER_IS_NOT_IN_GILDE.getText());
				return;
			}
			String g = manager.getPlayerGilde(p);
			UUID owner=manager.getOwner(g);
			if(!owner.equals(UtilPlayer.getRealUUID(p))){
				p.sendMessage(Text.GILDE_PREFIX.getText()+Text.GILDE_OWNER_NOT.getText());
				return;
			}
			String kick_o = args[1];
			UUID uuid = UtilPlayer.getUUID(kick_o, manager.getMysql());
			if(!manager.isPlayerInGilde(uuid)){
				p.sendMessage(Text.GILDE_PREFIX.getText()+Text.GILDE_IS_NOT_IN_THE_GILD.getText(kick_o));
				return;
			}
			if(!manager.getPlayerGilde(uuid).equalsIgnoreCase(g)){
				p.sendMessage(Text.GILDE_PREFIX.getText()+Text.GILDE_IS_NOT_IN_THE_GILD.getText(kick_o));
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
			
			manager.sendGildenChat(g, Text.GILDE_PREFIX.getText()+Text.GILDE_KICK_PLAYER.getText(kick_o));
			manager.removePlayerEintrag(uuid,kick_o);
		}else{
			p.sendMessage(Text.GILDE_PREFIX.getText()+" /gilde kicken [Player]");
		}
	}
	
}
