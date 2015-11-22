package me.kingingo.kcore.Kit.Perks;

import me.kingingo.kcore.Kit.Perk;
import me.kingingo.kcore.Util.UtilPlayer;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;

public class PerkHealPotion extends Perk{

	int more;
	
	public PerkHealPotion(int more) {
		super("HealPotion");
		this.more=more;
	}

	@EventHandler
	public void PotionSplash(PotionSplashEvent ev){
		if(ev.getPotion().getItem().getDurability()==16421||ev.getPotion().getItem().getDurability()==16389){
			for(LivingEntity e : ev.getAffectedEntities()){
				if(e instanceof Player){
					if(getPerkData().hasPlayer(this,  ((Player)e) )){
						UtilPlayer.health( ((Player)e) , more);
					}
				}
			}
		}
	}
	
	@EventHandler
	public void Heal(PlayerItemConsumeEvent ev){
		if(ev.getItem().getType()==Material.POTION&&getPerkData().hasPlayer(this, ev.getPlayer())){
			if(ev.getItem().getDurability()==8229|ev.getItem().getDurability()==8197){
				UtilPlayer.health(ev.getPlayer(), more);
			}
		}
	}
	
}
