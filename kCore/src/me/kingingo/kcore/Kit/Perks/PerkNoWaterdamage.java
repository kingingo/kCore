package me.kingingo.kcore.Kit.Perks;

import me.kingingo.kcore.Kit.Perk;
import me.kingingo.kcore.Util.UtilItem;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;

public class PerkNoWaterdamage extends Perk{

	public PerkNoWaterdamage() {
		super("noWaterdamage",UtilItem.RenameItem(new ItemStack(Material.POTION),"§enoWaterdamage"));
	}
	
	@EventHandler
	public void onBreak(EntityDamageEvent e){
		if(e.getEntity() instanceof Player){
			Player p = (Player)e.getEntity();
			if(!this.getPerkData().hasPlayer(this,p))return;
			if(e.getCause()==DamageCause.DROWNING){
				e.setCancelled(true);
			}
		}
	}
	
}
