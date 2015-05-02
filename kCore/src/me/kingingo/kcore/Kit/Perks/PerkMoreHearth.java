package me.kingingo.kcore.Kit.Perks;

import me.kingingo.kcore.Kit.Perk;
import me.kingingo.kcore.Util.UtilPlayer;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PerkMoreHearth extends Perk{

	int prozent;
	int herz;
	
	public PerkMoreHearth(int herz,int prozent) {
		super("MoreHearth");
		this.prozent=prozent;
		this.herz=herz;
	}
	
	Player a;
	Player d;
	@EventHandler
	  public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
	   if(e.getDamager() instanceof Player && e.getEntity() instanceof Player){
		   d=(Player)e.getEntity();
		   a=(Player)e.getDamager();
		   
		   if(this.getPerkData().hasPlayer(this,a)){
			   if(UtilPlayer.getHealth(a)<=herz){
				   e.setDamage( e.getDamage()+((e.getDamage()/100)*prozent) );  
			   }
		   }
	   }
	  }
	
}
