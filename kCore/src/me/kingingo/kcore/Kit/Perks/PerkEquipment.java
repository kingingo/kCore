package me.kingingo.kcore.Kit.Perks;

import me.kingingo.kcore.Kit.Perk;
import me.kingingo.kcore.Kit.Perks.Event.PerkEquipmentEvent;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.inventory.ItemStack;

public class PerkEquipment extends Perk{

	private ItemStack[] item;
	
	public PerkEquipment(ItemStack[] item) {
		super("Equipment");
		this.item=item;
	}

	@EventHandler(priority=EventPriority.HIGHEST)
	public void Start(PerkEquipmentEvent ev){
		for(Player p : getKit().getPlayers()){
			p.getInventory().addItem(item);
		}
	}
	
}
