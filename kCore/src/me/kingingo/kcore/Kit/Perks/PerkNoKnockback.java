package me.kingingo.kcore.Kit.Perks;

import me.kingingo.kcore.Kit.Perk;
import me.konsolas.aac.api.HackType;
import me.konsolas.aac.api.PlayerViolationKickEvent;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class PerkNoKnockback extends Perk{

	JavaPlugin instance;
	
	public PerkNoKnockback(JavaPlugin instance) {
		super("noKnockback");
		this.instance=instance;
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void kick(PlayerViolationKickEvent ev){
		if(ev.getHackType()==HackType.KNOCKBACK){
			if(this.getPerkData().hasPlayer(this, ev.getPlayer())){
				ev.setCancelled(true);
			}
		}
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	  public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
	   if(e.getEntity() instanceof Player&&!e.isCancelled()){
		   final Player op = (Player)e.getEntity();
		   if(this.getPerkData().hasPlayer(this, op)){
			   op.setVelocity(new Vector());
			   Bukkit.getScheduler().scheduleSyncDelayedTask(instance, new Runnable(){

				@Override
				public void run() {
					op.setVelocity(new Vector());
				}
				   
			   },1L);
		   }
	   }
	  }
	
	
}
