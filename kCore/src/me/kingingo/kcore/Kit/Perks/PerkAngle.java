package me.kingingo.kcore.Kit.Perks;

import java.util.ArrayList;

import me.kingingo.kcore.Kit.Perk;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerFishEvent.State;

public class PerkAngle extends Perk{

	private ArrayList<Entity> damage = new ArrayList<>();
	
	public PerkAngle() {
		super("Angle");
	}
	
	@EventHandler
	public void Enterhaken(PlayerFishEvent ev){
		if(ev.getState()==State.CAUGHT_ENTITY){
			if(!getPerkData().hasPlayer(this,ev.getPlayer()))return;
			damage.add(ev.getCaught());
			ev.getCaught().setVelocity( ev.getPlayer().getLocation().add(0,1,0).toVector().subtract(ev.getCaught().getLocation().toVector()).normalize().multiply(2.5) );
		}
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void Damage(EntityDamageEvent ev){
		if(ev.getCause() == DamageCause.FALL){
			if(ev.getEntity() instanceof Player&&damage.contains(ev.getEntity())){
				ev.setDamage(0);
				damage.remove(ev.getEntity());
			}
		}
	}


}
