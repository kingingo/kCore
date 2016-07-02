package eu.epicpvp.kcore.Gilden.Commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import dev.wolveringer.dataserver.gamestats.StatsKey;
import eu.epicpvp.kcore.Gilden.GildenManager;
import eu.epicpvp.kcore.Gilden.GildenType;
import eu.epicpvp.kcore.Gilden.SkyBlockGildenManager;
import eu.epicpvp.kcore.Gilden.Events.GildenPlayerTeleportEvent;
import eu.epicpvp.kcore.Permission.PermissionType;
import eu.epicpvp.kcore.Translation.TranslationHandler;
import eu.epicpvp.kcore.Util.TimeSpan;
import eu.epicpvp.kcore.Util.UtilDebug;
import eu.epicpvp.kcore.Util.UtilPlayer;

public class Home {
	
	public static void useSet(Player p,String[] args,GildenManager manager){
		useSet(p, p.getLocation(), args, manager);
	}
	
	public static void useSet(Player p,Location loc,String[] args,GildenManager manager){
		if(args.length==1){
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
			
			if(manager.getTyp()==GildenType.SKY){
				if(manager instanceof SkyBlockGildenManager){
					SkyBlockGildenManager skymanager = (SkyBlockGildenManager)manager;
					
					if(skymanager.getSky().getGilden_world().getIslands().containsKey(g.toLowerCase())){
						return;
					}
					
					if(p.hasPermission(PermissionType.SKYBLOCK_GILDEN_ISLAND.getPermissionToString())){
						skymanager.getSky().addGildenIsland(p, g);
						p.sendMessage(TranslationHandler.getText(p, "GILDE_PREFIX")+TranslationHandler.getText(p, "GILDE_SETISLAND"));
						return;
					}else{
						p.sendMessage(TranslationHandler.getText(p, "GILDE_PREFIX")+TranslationHandler.getText(p, "NO_RANG"));
						return;
					}
				}
			}

			if(UtilDebug.isDebug())UtilDebug.debug("CMD:Home", new String[]{"Gilde:"+g,"PLAYER: "+p.getName()});
			manager.setInt(g, manager.getTyp(), loc.getBlockX(), StatsKey.LOC_X);
			manager.setInt(g, manager.getTyp(), loc.getBlockY(), StatsKey.LOC_Y);
			manager.setInt(g, manager.getTyp(), loc.getBlockZ(), StatsKey.LOC_Z);
			manager.setString(g, manager.getTyp(), loc.getWorld().getName(), StatsKey.WORLD);
			manager.UpdateGilde(g, manager.getTyp());
			if(manager.getTyp()==GildenType.PVP){
				p.sendMessage(TranslationHandler.getText(p, "GILDE_PREFIX")+TranslationHandler.getText(p, "GILDE_SETHOME"));
			}
		}else{
			p.sendMessage(TranslationHandler.getText(p, "GILDE_PREFIX")+" /gilde sethome");
		}
	}
	
	public static void use(Player p,String[] args,GildenManager manager){
		if(args.length==1){
			if(manager.getTeleport().containsKey(p)){
				return;
			}
			if(!manager.isPlayerInGilde(p)){
				p.sendMessage(TranslationHandler.getText(p, "GILDE_PREFIX")+TranslationHandler.getText(p, "GILDE_PLAYER_IS_NOT_IN_GILDE"));
				return;
			}
			GildenPlayerTeleportEvent ev = new GildenPlayerTeleportEvent(p,manager);
			Bukkit.getPluginManager().callEvent(ev);
			if(ev.isCancelled()){
				if(ev.getReason()!=null){
					p.sendMessage(TranslationHandler.getText(p, "GILDE_PREFIX")+ev.getReason());
				}
				return;
			}
			manager.getTeleport_loc().put(p, p.getLocation());
			manager.getTeleport().put(p, (System.currentTimeMillis()+(TimeSpan.SECOND*5)) );
			p.sendMessage(TranslationHandler.getText(p, "GILDE_PREFIX")+TranslationHandler.getText(p, "GILDE_HOME",5+" sekunden"));
		}else if(args.length==2&&manager instanceof SkyBlockGildenManager&&p.isOp()){
			SkyBlockGildenManager sky = (SkyBlockGildenManager)manager;
			
			if(sky.getSky().getGilden_world().getIslands().containsKey(args[1].toLowerCase())){
				p.teleport(sky.getSky().getGilden_world().getIslandHome(args[1].toLowerCase()));
				p.sendMessage(TranslationHandler.getText(p, "PREFIX")+"§aDu wurdest Teleporiert.");
			}else{
				p.sendMessage(TranslationHandler.getText(p, "PREFIX")+"§cGilde nicht gefunden");
			}
		}else if(args.length==2&&p.isOp()){
			if(manager.ExistGilde(args[1])){
				manager.TeleportToHome(p, args[1]);
				p.sendMessage(TranslationHandler.getText(p, "PREFIX")+"§aDu wurdest Teleporiert.");
			}else{
				p.sendMessage(TranslationHandler.getText(p, "PREFIX")+"§cGilde nicht gefunden");
			}
		}else{
			p.sendMessage(TranslationHandler.getText(p, "GILDE_PREFIX")+" /gilde "+args[0]);
		}
	}
	
}