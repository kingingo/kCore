package me.kingingo.kcore.Gilden.Commands;

import java.util.ArrayList;

import me.kingingo.kcore.Enum.Text;
import me.kingingo.kcore.Gilden.GildenManager;
import me.kingingo.kcore.PlayerStats.Stats;
import me.kingingo.kcore.Util.UtilPlayer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Home {
	
	public static void useSet(Player p,String[] args,GildenManager manager){
		if(args.length>=2){
			if(!manager.isPlayerInGilde(p.getName())){
				p.sendMessage(Text.GILDE_PREFIX.getText()+Text.GILDE_PLAYER_IS_NOT_IN_GILDE.getText());
				return;
			}
			String g = manager.getPlayerGilde(p.getName());
			String owner=manager.getOwner(g);
			if(!owner.equalsIgnoreCase(p.getName())){
				p.sendMessage(Text.GILDE_PREFIX.getText()+Text.GILDE_OWNER_NOT.getText());
				return;
			}
			
			manager.setInt(g, manager.getTyp(), p.getLocation().getBlockX(), Stats.LOC_X);
			manager.setInt(g, manager.getTyp(), p.getLocation().getBlockY(), Stats.LOC_Y);
			manager.setInt(g, manager.getTyp(), p.getLocation().getBlockZ(), Stats.LOC_Z);
			manager.setString(g, manager.getTyp(), p.getLocation().getWorld().getName(), Stats.WORLD);
			p.sendMessage(Text.GILDE_PREFIX.getText()+Text.GILDE_SETHOME.getText());
		}else{
			p.sendMessage(Text.GILDE_PREFIX.getText()+" /gilde sethome");
		}
	}
	
	public static void use(Player p,String[] args,GildenManager manager){
		if(args.length>=2){
			manager.getTeleport_loc().put(p, p.getLocation());
			manager.getTeleport().put(p, System.currentTimeMillis()+(20*5));
			p.sendMessage(Text.GILDE_PREFIX.getText()+Text.GILDE_HOME.getText(5));
		}else{
			p.sendMessage(Text.GILDE_PREFIX.getText()+" /gilde home");
		}
	}
	
}