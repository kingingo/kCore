package me.kingingo.kcore.Kit.Perks;

import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;

import me.kingingo.kcore.Kit.Perk;

public class PerkGetXP extends Perk{
	
	public PerkGetXP() {
		super("GetXP");
	}
	
	@EventHandler
	public void Death(PlayerDeathEvent ev){
		if(getPerkData().hasPlayer(this, ev.getEntity().getKiller())){
			ev.getEntity().getKiller().setExp( ev.getNewExp() );
			ev.setDroppedExp(0);
		}
	}

}
