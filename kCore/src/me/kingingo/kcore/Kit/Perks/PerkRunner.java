package me.kingingo.kcore.Kit.Perks;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.kingingo.kcore.Kit.Perk;
import me.kingingo.kcore.Kit.Perks.Event.PerkPlayerAddEvent;
import me.kingingo.kcore.Kit.Perks.Event.PerkPlayerRemoveEvent;
import me.kingingo.kcore.Kit.Perks.Event.PerkStartEvent;
import me.konsolas.aac.api.HackType;
import me.konsolas.aac.api.PlayerViolationEvent;
import me.konsolas.aac.api.PlayerViolationKickEvent;

public class PerkRunner extends Perk{

	private float speed;
	
	public PerkRunner(float speed) {
		super("Runner");
		this.speed=speed;
	}
	
	@EventHandler
	public void Start(PerkStartEvent ev){
		if(getPerkData().getPlayers().containsKey(this)){
			for(Player p : getPerkData().getPlayers().get(this))p.setWalkSpeed(speed);
		}
	}
	
	@EventHandler
	public void Remove(PerkPlayerRemoveEvent ev){
		if(getPerkData().hasPlayer(this, ev.getPlayer()) && (ev.getPerkString()==null||ev.getPerkString().equalsIgnoreCase(getName())) ){
			ev.getPlayer().setWalkSpeed(0.2F);
		}
	}
	
	@EventHandler
	public void PotionSplash(PotionSplashEvent ev){
		for(LivingEntity living : ev.getAffectedEntities()){
			if(living instanceof Player){
				if(getPerkData().hasPlayer(this, ((Player)living) )){
					for(PotionEffect pe : ev.getPotion().getEffects()){
						if(pe.getType().getName().equalsIgnoreCase("SPEED")){
							ev.setCancelled(true);
							break;
						}
					}	
				}
			}
		}
	}
	
	@EventHandler
	public void PlayerItemConsum(PlayerItemConsumeEvent ev){
		if(getPerkData().hasPlayer(this, ev.getPlayer())&&ev.getItem().getType()==Material.POTION){
			if(ev.getItem().getDurability()==8194||ev.getItem().getDurability()==8258||ev.getItem().getDurability()==8290||ev.getItem().getDurability()==8226){
				ev.setItem(null);
				ev.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void Add(PerkPlayerAddEvent ev){
		if(getPerkData().hasPlayer(this, ev.getPlayer()) && (ev.getPerkString()==null||ev.getPerkString().equalsIgnoreCase(getName())) ){
			ev.getPlayer().removePotionEffect(PotionEffectType.SPEED);
			ev.getPlayer().setWalkSpeed(speed);
		}
	}

}
