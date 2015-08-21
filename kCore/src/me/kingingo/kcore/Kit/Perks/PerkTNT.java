package me.kingingo.kcore.Kit.Perks;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;

import me.kingingo.kcore.Kit.Perk;

public class PerkTNT extends Perk{

	public PerkTNT() {
		super("TNT");
	}
	
	@EventHandler
	public void Place(BlockPlaceEvent ev){
		if(!this.getPerkData().hasPlayer(this,ev.getPlayer()))return;
		if(ev.getBlock().getType()==Material.TNT){
			ev.getBlock().setType(Material.AIR);
			ev.getBlock().getLocation().getWorld().spawnEntity(ev.getBlock().getLocation(),EntityType.PRIMED_TNT);
		}
	}

}
