package me.kingingo.kcore.Gilden.Commands;

import java.util.UUID;

import me.kingingo.kcore.Enum.Text;
import me.kingingo.kcore.Gilden.GildenManager;
import me.kingingo.kcore.Gilden.GildenType;
import me.kingingo.kcore.Gilden.SkyBlockGildenManager;
import me.kingingo.kcore.Gilden.Events.GildenPlayerTeleportEvent;
import me.kingingo.kcore.Permission.kPermission;
import me.kingingo.kcore.PlayerStats.Stats;
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
				p.sendMessage(Text.GILDE_PREFIX.getText()+Text.GILDE_PLAYER_IS_NOT_IN_GILDE.getText());
				return;
			}
			String g = manager.getPlayerGilde(p);
			UUID owner=manager.getOwner(g);
			if(!owner.equals(UtilPlayer.getRealUUID(p))){
				p.sendMessage(Text.GILDE_PREFIX.getText()+Text.GILDE_OWNER_NOT.getText());
				return;
			}
			
			if(manager.getTyp()==GildenType.SKY){
				if(manager instanceof SkyBlockGildenManager){
					SkyBlockGildenManager skymanager = (SkyBlockGildenManager)manager;
					if(p.hasPermission(kPermission.SKYBLOCK_GILDEN_ISLAND.getPermissionToString())){
						skymanager.getSky().addGildenIsland(p, g);
						p.sendMessage(Text.GILDE_PREFIX.getText()+Text.GILDE_SETISLAND.getText());
						return;
					}else{
						p.sendMessage(Text.GILDE_PREFIX.getText()+Text.NO_RANG.getText());
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
				p.sendMessage(Text.GILDE_PREFIX.getText()+Text.GILDE_SETHOME.getText());
			}
		}else{
			p.sendMessage(Text.GILDE_PREFIX.getText()+" /gilde sethome");
		}
	}
	
	public static void use(Player p,String[] args,GildenManager manager){
		if(args.length==1){
			if(manager.getTeleport().containsKey(p)){
				return;
			}
			if(!manager.isPlayerInGilde(p)){
				p.sendMessage(Text.GILDE_PREFIX.getText()+Text.GILDE_PLAYER_IS_NOT_IN_GILDE.getText());
				return;
			}
			GildenPlayerTeleportEvent ev = new GildenPlayerTeleportEvent(p,manager);
			Bukkit.getPluginManager().callEvent(ev);
			if(ev.isCancelled()){
				if(ev.getReason()!=null){
					p.sendMessage(Text.GILDE_PREFIX.getText()+ev.getReason());
				}
				return;
			}
			manager.getTeleport_loc().put(p, p.getLocation());
			manager.getTeleport().put(p, (System.currentTimeMillis()+(TimeSpan.SECOND*5)) );
			p.sendMessage(Text.GILDE_PREFIX.getText()+Text.GILDE_HOME.getText(5+" sekunden"));
		}else{
			p.sendMessage(Text.GILDE_PREFIX.getText()+" /gilde "+args[0]);
		}
	}
	
}
