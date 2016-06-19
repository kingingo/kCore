package eu.epicpvp.kcore.Kit.Perks;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;

import eu.epicpvp.kcore.Kit.Perk;
import eu.epicpvp.kcore.Util.UtilMath;

public class PerkSpawnByDeath extends Perk{

	EntityType e;
	int chance;
	
	public PerkSpawnByDeath(EntityType e,int chance){
		super("SpawnByDeath");
		this.e=e;
		this.chance=chance;
	}
	
	@EventHandler
	public void Death(PlayerDeathEvent ev){
		if(ev.getEntity() instanceof Player && ev.getEntity().getKiller() instanceof Player){
			if(!this.getPerkData().hasPlayer(this,((Player)ev.getEntity())))return;
			if(UtilMath.RandomInt(100, 1)<=chance){
				ev.getEntity().getWorld().spawnEntity(ev.getEntity().getLocation().add(0,0.2,0), e);
			}
		}
	}
	

}
