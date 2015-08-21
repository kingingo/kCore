package me.kingingo.kcore.Kit.Perks;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerExpChangeEvent;

import me.kingingo.kcore.Kit.Perk;

public class PerkDoubleXP extends Perk{
	
	public PerkDoubleXP() {
		super("DoubleXP");
	}
	
	@EventHandler
	public void XP(PlayerExpChangeEvent ev){
		if(getPerkData().hasPlayer(this, ev.getPlayer())){
			ev.setAmount(ev.getAmount()*2);
		}
	}

}
