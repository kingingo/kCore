package me.kingingo.kcore.Kit.Perks;

import me.kingingo.kcore.Kit.Perk;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.PlayerDeathEvent;

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
