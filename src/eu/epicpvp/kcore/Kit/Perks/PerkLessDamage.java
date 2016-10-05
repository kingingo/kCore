package eu.epicpvp.kcore.Kit.Perks;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import eu.epicpvp.kcore.Kit.Perk;

public class PerkLessDamage extends Perk{

	private int prozent;
	private EntityType type;
	
	public PerkLessDamage(int prozent,EntityType type) {
		super("LessDamage");
		this.prozent=prozent;
		this.type=type;
	}
	
	@EventHandler
	public void Less(EntityDamageByEntityEvent ev){
		if(ev.getDamager().getType() == this.type && ev.getEntity() instanceof Player){
			if(getPerkData().hasPlayer(this, ((Player)ev.getEntity()))){
				ev.setDamage( (((float)ev.getDamage()/100)*prozent) );
			}
		}
	}

}
