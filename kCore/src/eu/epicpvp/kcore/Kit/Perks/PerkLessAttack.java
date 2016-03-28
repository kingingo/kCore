package eu.epicpvp.kcore.Kit.Perks;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import eu.epicpvp.kcore.Kit.Perk;

public class PerkLessAttack extends Perk{

	private int prozent;
	
	public PerkLessAttack(int prozent) {
		super("LessAttack");
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
