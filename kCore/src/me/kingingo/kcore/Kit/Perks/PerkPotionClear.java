package me.kingingo.kcore.Kit.Perks;

import java.util.HashMap;

import me.kingingo.kcore.Kit.Perk;
import me.kingingo.kcore.Language.Language;
import me.kingingo.kcore.Util.TimeSpan;
import me.kingingo.kcore.Util.UtilParticle;
import me.kingingo.kcore.Util.UtilTime;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PerkPotionClear extends Perk{

	private HashMap<String,Long> timer = new HashMap<>();
	
	public PerkPotionClear() {
		super("PotionClear");
	}
	
	public boolean is(Player player){
		if(timer.containsKey(player.getName())){
			if(timer.get(player.getName()) > System.currentTimeMillis()){
				return true;
			}
			timer.remove(player.getName());
			return false;
		}
		return false;
	}
	
	@EventHandler
	public void Click(PlayerInteractEntityEvent ev){
			if(getPerkData().hasPlayer(this, ev.getPlayer())){
				if(ev.getPlayer().isSneaking()){
						if(ev.getRightClicked() instanceof Player){
							if(!is(ev.getPlayer())){
							timer.put(ev.getPlayer().getName(), System.currentTimeMillis()+(TimeSpan.MINUTE*15));
							UtilParticle.FLAME.display(1, 20, ((Player)ev.getRightClicked()).getLocation(), 15);
							for(PotionEffect e : ((Player)ev.getRightClicked()).getActivePotionEffects()){
								if(e.getType()!=PotionEffectType.JUMP)((Player)ev.getRightClicked()).removePotionEffect(e.getType());
							}
							}else{
								ev.getPlayer().sendMessage(Language.getText(ev.getPlayer(), "PREFIX")+"§cDu musst noch §4"+UtilTime.formatMili( (timer.get(ev.getPlayer().getName())-System.currentTimeMillis()) )+"§c warten");
							}
						}
				}
			}
	}


}
