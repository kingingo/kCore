package eu.epicpvp.kcore.Kit.Perks;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import eu.epicpvp.kcore.Kit.Perk;
import eu.epicpvp.kcore.Kit.Perks.Event.PerkHasPlayerEvent;
import eu.epicpvp.kcore.Translation.TranslationManager;
import eu.epicpvp.kcore.Util.TimeSpan;

public class PerkStopPerk extends Perk{

	int time=20;
	HashMap<Player,Long> list = new HashMap<>();
	
	public PerkStopPerk(int time) {
		super("StopPerk");
		this.time=time;
	}

	long t;
	@EventHandler(priority=EventPriority.LOWEST)
	public void PerkAllow(PerkHasPlayerEvent ev){
		if(list.containsKey(ev.getPlayer())){
			t=list.get(ev.getPlayer());
			if(t>=System.currentTimeMillis()){
				ev.setCancelled(true);
			}else{
				list.remove(ev.getPlayer());
			}
		}
	}
	
	Player d;
	@EventHandler(priority=EventPriority.LOWEST)
	public void Death(EntityDamageByEntityEvent ev){
		if(ev.getDamager() instanceof Player){
			if(this.getPerkData().hasPlayer(this,((Player) ev.getDamager()))){
				if(ev.getEntity() instanceof Player){
					d=(Player)ev.getEntity();
					if(!list.containsKey(d)){
						d.sendMessage(TranslationManager.getText(d, "PREFIX")+"§eDeine Perks wurden f§r §b"+time+" sec§e aufgehoben.");
						list.put(d, (TimeSpan.SECOND*time)+System.currentTimeMillis());	
					}
				}else if(ev.getEntity() instanceof Projectile){
					d=(Player) ((Projectile)ev.getEntity()).getShooter();
					if(!list.containsKey(d)){
						d.sendMessage(TranslationManager.getText(d, "PREFIX")+"§eDeine Perks wurden f§r §b"+time+" sec§e aufgehoben.");
						list.put(d, (TimeSpan.SECOND*time)+System.currentTimeMillis());	
					}
				}
			}
			
		}
	}

}
