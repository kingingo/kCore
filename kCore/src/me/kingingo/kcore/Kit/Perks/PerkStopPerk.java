package me.kingingo.kcore.Kit.Perks;

import java.util.HashMap;

import me.kingingo.kcore.Enum.Text;
import me.kingingo.kcore.Kit.Perk;
import me.kingingo.kcore.Kit.Perks.Event.PerkHasPlayerEvent;
import me.kingingo.kcore.Util.TimeSpan;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PerkStopPerk extends Perk{

	int time;
	HashMap<Player,Long> list = new HashMap<>();
	
	public PerkStopPerk(int time) {
		super("StopPerk");
	}

	long t;
	@EventHandler
	public void PerkAllow(PerkHasPlayerEvent ev){
		if(list.containsKey(ev.getPlayer())){
			t=list.get(ev.getPlayer());
			if(t>System.currentTimeMillis()){
				ev.setCancelled(true);
			}else{
				list.remove(ev.getPlayer());
			}
		}
	}
	
	Player d;
	@EventHandler
	public void Death(PlayerDeathEvent ev){
		if(ev.getEntity() instanceof Player && ev.getEntity().getKiller() instanceof Player){
			if(!this.getPerkData().hasPlayer(this, ev.getEntity().getKiller()))return;
			d=(Player)ev.getEntity();
			if(!list.containsKey(d)){
				list.put(d, (TimeSpan.SECOND*time)+System.currentTimeMillis());
			}
		}
	}

}
