package eu.epicpvp.kcore.Kit.Perks;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;

import eu.epicpvp.kcore.Kit.Perk;
import eu.epicpvp.kcore.Util.UtilItem;

public class PerkNoFiredamage extends Perk{

	public PerkNoFiredamage() {
		super("noFiredamage",UtilItem.RenameItem(new ItemStack(Material.POTION,1,(byte)16451),"§enoFiredamage"));
	}
	
	@EventHandler
	public void onBreak(EntityDamageEvent e){
		if(e.getEntity() instanceof Player){
			Player p = (Player)e.getEntity();
			if(!this.getPerkData().hasPlayer(this,p))return;
			if(e.getCause()==DamageCause.FIRE||e.getCause() == DamageCause.FIRE_TICK||e.getCause()==DamageCause.LAVA){
				e.setCancelled(true);
			}
		}
	}
	
}
