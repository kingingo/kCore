package eu.epicpvp.kcore.Kit.Perks;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.inventory.ItemStack;

import eu.epicpvp.kcore.Kit.Perk;
import eu.epicpvp.kcore.Kit.Perks.Event.PerkStartEvent;

public class PerkEquipment extends Perk{

	private ItemStack[] item;
	
	public PerkEquipment(ItemStack[] item) {
		super("Equipment");
		this.item=item;
	}

	@EventHandler(priority=EventPriority.HIGHEST)
	public void Start(PerkStartEvent ev){
		if(!getPerkData().getPlayers().containsKey(this))return;
		for(Player p : getPerkData().getPlayers().get(this)){
			if(ev.getPlayers().contains(p)){
				p.getInventory().addItem(item);
			}
		}
	}
	
}
