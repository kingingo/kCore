package eu.epicpvp.kcore.Kit.Perks;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.PlayerDeathEvent;

import eu.epicpvp.kcore.Kit.Perk;

public class PerkKeepXP extends Perk{
	
	public PerkKeepXP() {
		super("KeepXP");
	}
	
	@EventHandler(priority=EventPriority.LOW)
	public void Death(PlayerDeathEvent ev){
		if(ev.getEntity() instanceof Player && getPerkData().hasPlayer(this, ev.getEntity())){
			ev.setKeepLevel(true);
			ev.setDroppedExp(0);
		}
	}

}
