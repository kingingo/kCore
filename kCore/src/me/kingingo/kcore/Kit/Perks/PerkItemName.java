package me.kingingo.kcore.Kit.Perks;

import me.kingingo.kcore.Kit.Perk;
import me.kingingo.kcore.Util.UtilItem;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class PerkItemName extends Perk{
	
	public PerkItemName() {
		super("ItemName");
	}
	
	@EventHandler
	public void Pickup(PlayerPickupItemEvent ev){
		if(getPerkData().hasPlayer(this, ev.getPlayer())){
			if(ev.getItem().getItemStack().getType()==Material.IRON_AXE||ev.getItem().getItemStack().getType()==Material.DIAMOND_AXE||ev.getItem().getItemStack().getType()==Material.WOOD_AXE||ev.getItem().getItemStack().getType()==Material.GOLD_AXE){
				UtilItem.RenameItem(ev.getItem().getItemStack(), "§3Axt von§6 "+ev.getPlayer().getName());
			}else if(ev.getItem().getItemStack().getType()==Material.IRON_PICKAXE||ev.getItem().getItemStack().getType()==Material.DIAMOND_PICKAXE||ev.getItem().getItemStack().getType()==Material.WOOD_PICKAXE||ev.getItem().getItemStack().getType()==Material.GOLD_PICKAXE){
				UtilItem.RenameItem(ev.getItem().getItemStack(), "§3Spitzhacke von§6 "+ev.getPlayer().getName());
			}else if(ev.getItem().getItemStack().getType()==Material.IRON_SWORD||ev.getItem().getItemStack().getType()==Material.DIAMOND_SWORD||ev.getItem().getItemStack().getType()==Material.WOOD_SWORD||ev.getItem().getItemStack().getType()==Material.GOLD_SWORD){
				UtilItem.RenameItem(ev.getItem().getItemStack(), "§3Schwert von§6 "+ev.getPlayer().getName());
			}else if(ev.getItem().getItemStack().getType()==Material.IRON_SPADE||ev.getItem().getItemStack().getType()==Material.DIAMOND_SPADE||ev.getItem().getItemStack().getType()==Material.WOOD_SPADE||ev.getItem().getItemStack().getType()==Material.GOLD_SPADE){
				UtilItem.RenameItem(ev.getItem().getItemStack(), "§3Schaufel von§6 "+ev.getPlayer().getName());
			}else if(ev.getItem().getItemStack().getType()==Material.IRON_HOE||ev.getItem().getItemStack().getType()==Material.DIAMOND_HOE||ev.getItem().getItemStack().getType()==Material.WOOD_HOE||ev.getItem().getItemStack().getType()==Material.GOLD_HOE){
				UtilItem.RenameItem(ev.getItem().getItemStack(), "§3Harke von§6 "+ev.getPlayer().getName());
			}else if(ev.getItem().getItemStack().getTypeId()>=298&&ev.getItem().getItemStack().getTypeId()<=317){
				UtilItem.RenameItem(ev.getItem().getItemStack(), "§3Ruestungsteil von§6 "+ev.getPlayer().getName());
			}
		}
	}

}
