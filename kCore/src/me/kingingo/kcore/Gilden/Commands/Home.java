package me.kingingo.kcore.Gilden.Commands;

import java.util.UUID;

import me.kingingo.kcore.Gilden.GildenManager;
import me.kingingo.kcore.Gilden.GildenType;
import me.kingingo.kcore.Gilden.SkyBlockGildenManager;
import me.kingingo.kcore.Gilden.Events.GildenPlayerTeleportEvent;
import me.kingingo.kcore.Language.Language;
import me.kingingo.kcore.Permission.kPermission;
import me.kingingo.kcore.StatsManager.Stats;
import me.kingingo.kcore.Util.TimeSpan;
import me.kingingo.kcore.Util.UtilPlayer;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Home {
	
	public static void useSet(Player p,String[] args,GildenManager manager){
		useSet(p, p.getLocation(), args, manager);
	}
	
	public static void useSet(Player p,Location loc,String[] args,GildenManager manager){
		if(args.length==1){
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
			
			if(manager.getTyp()==GildenType.SKY){
				if(manager instanceof SkyBlockGildenManager){
					SkyBlockGildenManager skymanager = (SkyBlockGildenManager)manager;
					
					if(skymanager.getSky().getGilden_world().getIslands().containsKey(g.toLowerCase())){
						return;
					}
					
					if(p.hasPermission(kPermission.SKYBLOCK_GILDEN_ISLAND.getPermissionToString())){
						skymanager.getSky().addGildenIsland(p, g);
						p.sendMessage(Language.getText(p, "GILDE_PREFIX")+Language.getText(p, "GILDE_SETISLAND"));
						return;
					}else{
						p.sendMessage(Language.getText(p, "GILDE_PREFIX")+Language.getText(p, "NO_RANG"));
						return;
					}
				}
			}
			
			manager.setInt(g, manager.getTyp(), loc.getBlockX(), Stats.LOC_X);
			manager.setInt(g, manager.getTyp(), loc.getBlockY(), Stats.LOC_Y);
			manager.setInt(g, manager.getTyp(), loc.getBlockZ(), Stats.LOC_Z);
			manager.setString(g, manager.getTyp(), loc.getWorld().getName(), Stats.WORLD);
			manager.UpdateGilde(g, manager.getTyp());
			if(manager.getTyp()==GildenType.PVP){
				p.sendMessage(Language.getText(p, "GILDE_PREFIX")+Language.getText(p, "GILDE_SETHOME"));
			}
		}else{
			p.sendMessage(Language.getText(p, "GILDE_PREFIX")+" /gilde sethome");
		}
	}
	
	public static void use(Player p,String[] args,GildenManager manager){
		if(args.length==1){
			if(manager.getTeleport().containsKey(p)){
				return;
			}
			if(!manager.isPlayerInGilde(p)){
				p.sendMessage(Language.getText(p, "GILDE_PREFIX")+Language.getText(p, "GILDE_PLAYER_IS_NOT_IN_GILDE"));
				return;
			}
			GildenPlayerTeleportEvent ev = new GildenPlayerTeleportEvent(p,manager);
			Bukkit.getPluginManager().callEvent(ev);
			if(ev.isCancelled()){
				if(ev.getReason()!=null){
					p.sendMessage(Language.getText(p, "GILDE_PREFIX")+ev.getReason());
				}
				return;
			}
			manager.getTeleport_loc().put(p, p.getLocation());
			manager.getTeleport().put(p, (System.currentTimeMillis()+(TimeSpan.SECOND*5)) );
			p.sendMessage(Language.getText(p, "GILDE_PREFIX")+Language.getText(p, "GILDE_HOME",5+" sekunden"));
		}else if(args.length==2&&manager instanceof SkyBlockGildenManager&&p.isOp()){
			SkyBlockGildenManager sky = (SkyBlockGildenManager)manager;
			
			if(sky.getSky().getGilden_world().getIslands().containsKey(args[1].toLowerCase())){
				p.teleport(sky.getSky().getGilden_world().getIslandHome(args[1].toLowerCase()));
				p.sendMessage(Language.getText(p, "PREFIX")+"§aDu wurdest Teleporiert.");
			}else{
				p.sendMessage(Language.getText(p, "PREFIX")+"§cGilde nicht gefunden");
			}
		}else{
			p.sendMessage(Language.getText(p, "GILDE_PREFIX")+" /gilde "+args[0]);
		}
	}
	
}
