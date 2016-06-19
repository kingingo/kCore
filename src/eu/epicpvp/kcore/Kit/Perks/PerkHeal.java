package eu.epicpvp.kcore.Kit.Perks;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import eu.epicpvp.kcore.Kit.Perk;
import eu.epicpvp.kcore.Update.UpdateType;
import eu.epicpvp.kcore.Update.Event.UpdateEvent;
import eu.epicpvp.kcore.Util.UtilPlayer;

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
