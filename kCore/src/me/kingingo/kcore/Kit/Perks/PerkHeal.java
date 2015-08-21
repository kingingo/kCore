package me.kingingo.kcore.Kit.Perks;

import me.kingingo.kcore.Kit.Perk;
import me.kingingo.kcore.Update.UpdateType;
import me.kingingo.kcore.Update.Event.UpdateEvent;
import me.kingingo.kcore.Util.UtilPlayer;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class PerkHeal extends Perk{

	double heal;
	public PerkHeal(double heal){
		super("Heal");
		this.heal=heal;
	}
	
	@EventHandler
	public void Updater(UpdateEvent ev){
		if(ev.getType()!=UpdateType.SLOW)return;
		if(!this.getPerkData().getPlayers().containsKey(this))return;
		for(Player p : this.getPerkData().getPlayers().get(this)){
			if(p.getFoodLevel()==20){
				UtilPlayer.health(p,heal);
			}
		}
	}
	

}
