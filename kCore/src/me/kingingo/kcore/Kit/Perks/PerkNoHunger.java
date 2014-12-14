package me.kingingo.kcore.Kit.Perks;

import me.kingingo.kcore.Enum.Text;
import me.kingingo.kcore.Kit.Perk;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class PerkNoHunger extends Perk{

	public PerkNoHunger(){
		super("NoHunger");
	}
	
	@EventHandler
	public void Food(FoodLevelChangeEvent ev){
		if(ev.getEntity()instanceof Player&&this.getPerkData().hasPlayer( this,((Player)ev.getEntity()) ))ev.setFoodLevel(20);
	}
	

}
