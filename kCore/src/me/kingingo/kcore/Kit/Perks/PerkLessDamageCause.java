package me.kingingo.kcore.Kit.Perks;

import me.kingingo.kcore.Kit.Perk;

import org.bukkit.entity.Damageable;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class PerkLessDamageCause extends Perk{

	private int prozent;
	private DamageCause type;
	
	public PerkLessDamageCause(int prozent,DamageCause type) {
		super("LessDamage");
		this.prozent=prozent;
		this.type=type;
	}
	
	@EventHandler
	public void Less(EntityDamageEvent ev){
		if(ev.getCause() == this.type && ev.getEntity() instanceof Player){
			if(getPerkData().hasPlayer(this, ((Player)ev.getEntity()))){
				ev.setDamage( (((float)ev.getDamage()/100)*prozent) );
			}
		}
	}

}
