package me.kingingo.kcore.Kit.Perks;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import me.kingingo.kcore.Kit.Perk;

public class PerkLessDamage extends Perk{

	private int prozent;
	
	public PerkLessDamage(int prozent) {
		super("LessDamage");
		this.prozent=prozent;
	}
	
	@EventHandler
	public void Less(EntityDamageByEntityEvent ev){
		if(ev.getDamager() instanceof Player){
			if(getPerkData().hasPlayer(this, ((Player)ev.getDamager()))){
				ev.setDamage( ((ev.getDamage()*prozent)/100) );
			}
		}
	}

}
