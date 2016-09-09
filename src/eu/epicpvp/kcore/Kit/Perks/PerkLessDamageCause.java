package eu.epicpvp.kcore.Kit.Perks;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;

import eu.epicpvp.kcore.Kit.Perk;
import eu.epicpvp.kcore.Util.UtilItem;

public class PerkLessDamageCause extends Perk{

	private int prozent;
	private DamageCause type;
	
	public PerkLessDamageCause(int prozent,DamageCause type) {
		super("Half-Falldamage",UtilItem.RenameItem(new ItemStack(Material.IRON_BOOTS),"§eHalf-Falldamage"));
		this.prozent=prozent;
		this.type=type;
	}
	
	@EventHandler
	public void Less(EntityDamageEvent ev){
		if(ev.getCause() == this.type && ev.getEntity() instanceof Player){
			if(getPerkData().hasPlayer(this, ((Player)ev.getEntity()))){
				ev.setDamage( (((float)ev.getDamage()/100)*prozent) );
			}
		}
	}

}
