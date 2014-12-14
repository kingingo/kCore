package me.kingingo.kcore.Kit.Perks;

import java.util.HashMap;

import me.kingingo.kcore.Kit.Perk;
import me.kingingo.kcore.Util.TimeSpan;
import me.kingingo.kcore.Util.UtilParticle;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.potion.PotionEffect;

public class PerkPotionClear extends Perk{

	private HashMap<Player,Long> timer = new HashMap<>();
	
	public PerkPotionClear() {
		super("PotionClear");
	}
	
	public boolean is(Player player){
		if(timer.containsKey(player)||timer.get(player) > System.currentTimeMillis()){
			timer.remove(player);
			return false;
		}
		return true;
	}
	
	@EventHandler
	public void Click(PlayerInteractEntityEvent ev){
		if(ev.getPlayer().isSneaking()){
			if(getPerkData().hasPlayer(this, ev.getPlayer())){
				if(!is(ev.getPlayer())){
					if(ev.getRightClicked() instanceof Player){
						for(PotionEffect e : ((Player)ev.getRightClicked()).getActivePotionEffects())((Player)ev.getRightClicked()).removePotionEffect(e.getType());
						timer.put(ev.getPlayer(), System.currentTimeMillis()+(TimeSpan.HOUR*2));
						UtilParticle.FLAME.display(10F, 4F, 10F, 0, 60, ev.getPlayer().getLocation(), 10);
					}
				}
			}
		}
	}


}
