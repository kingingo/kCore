package me.kingingo.kcore.Kit.Perks;

import java.util.ArrayList;

import me.kingingo.kcore.Kit.Perk;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerFishEvent.State;

public class PerkEnterhacken extends Perk{

	private ArrayList<Player> damage = new ArrayList<>();
	
	public PerkEnterhacken() {
		super("Enterhacken");
	}
	
	@EventHandler
	public void Enterhaken(PlayerFishEvent ev){
		if(ev.getState()==State.IN_GROUND){
			if(!getKit().getPlayers().contains(ev.getPlayer()))return;
			damage.add(ev.getPlayer());
			ev.getPlayer().setVelocity( ev.getHook().getLocation().add(0,2,0).toVector().subtract(ev.getPlayer().getLocation().toVector()).normalize().multiply(3.5) );
		}
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void Damage(EntityDamageEvent ev){
		if(ev.getCause() == DamageCause.FALL){
			if(ev.getEntity() instanceof Player&&damage.contains( ((Player)ev.getEntity()) )){
				ev.setDamage(0);
				damage.remove(((Player)ev.getEntity()));
			}
		}
	}

}
