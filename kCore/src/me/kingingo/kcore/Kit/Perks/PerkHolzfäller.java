package me.kingingo.kcore.Kit.Perks;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import me.kingingo.kcore.Kit.Perk;

public class PerkHolzfäller extends Perk{

	public PerkHolzfäller() {
		super("Holzfäller");
	}

	@EventHandler
	public void onBreak(BlockBreakEvent e){
		if(!this.getKit().hasPlayer(this,e.getPlayer()))return;
		if(e.getPlayer().getWorld().getName().equalsIgnoreCase("world"))return;
		Block b = e.getBlock();
		
		if(b.getTypeId()==17 || b.getTypeId()==162){
			ItemStack i = getWood(b);
			b.getWorld().dropItem(b.getLocation(),i);
		}
	}

	public static ItemStack getWood(Block b){
		int i = 0;
		Block bl;
		int id = b.getTypeId();
		byte data = b.getData();
		for(int y = b.getY()-30; y < b.getY()+30; y++){
			bl = new Location(b.getWorld(),b.getX(),y,b.getZ()).getBlock();
			if(bl.getTypeId()==17 || bl.getTypeId()==162){
				bl.setType(Material.AIR);
				i++;
			}
		}
		
		return new ItemStack(id,i,data);
	}
	
}
