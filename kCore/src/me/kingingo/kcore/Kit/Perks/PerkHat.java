package me.kingingo.kcore.Kit.Perks;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;

import me.kingingo.kcore.Kit.Perk;
import me.kingingo.kcore.Util.UtilItem;

public class PerkHat extends Perk{

	public PerkHat() {
		super("Hat");
	}
	
	@EventHandler
	public void HatDeath(PlayerDeathEvent ev){
		if(ev.getEntity() instanceof Player){
			if(ev.getEntity().getKiller() instanceof Player){
				if(!this.getPerkData().hasPlayer(this, ((Player)ev.getEntity()).getKiller() ))return;
				((Player)ev.getEntity()).getKiller().getInventory().addItem( UtilItem.Head( ((Player)ev.getEntity()).getName() ) );
			}
		}
	}

}
