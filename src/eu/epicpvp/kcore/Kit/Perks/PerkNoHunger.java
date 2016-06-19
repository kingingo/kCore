package eu.epicpvp.kcore.Kit.Perks;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.inventory.ItemStack;

import eu.epicpvp.kcore.Kit.Perk;
import eu.epicpvp.kcore.Util.UtilItem;

public class PerkNoHunger extends Perk{

	public PerkNoHunger(){
		super("NoHunger",UtilItem.RenameItem(new ItemStack(Material.BREAD),"§eNoHunger"));
	}
	
	@EventHandler
	public void Food(FoodLevelChangeEvent ev){
		if(ev.getEntity()instanceof Player&&this.getPerkData().hasPlayer( this,((Player)ev.getEntity()) ))ev.setFoodLevel(20);
	}
	

}
