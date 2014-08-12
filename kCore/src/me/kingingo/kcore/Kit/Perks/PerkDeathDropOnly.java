package me.kingingo.kcore.Kit.Perks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.kingingo.kcore.Enum.Text;
import me.kingingo.kcore.Kit.Perk;
import me.kingingo.kcore.Util.TimeSpan;

import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

public class PerkDeathDropOnly extends Perk{

	int time;
	HashMap<Player,List<Integer>> Times = new HashMap<>();
	HashMap<Integer,Long> Drops = new HashMap<>();
	
	public PerkDeathDropOnly(int time){
		super("DeathDropOnly", Text.PERK_NODROPSBYDEATH.getTexts(time));
		this.time=time;
	}
	
//	@EventHandler
//	public void Despawn(ItemDespawnEvent ev){
//		if(Drops.containsKey(ev.getEntity().getEntityId())){
//			Drops.remove(ev.getEntity().getEntityId());
//			for(int i = 0; i<Times.size(); i++){
//				if(Times.get(i).contains(ev.getEntity().getEntityId())){
//					Times.get(i).remove(ev.getEntity().getEntityId());
//					if(Times.get(i).isEmpty())Times.remove(i);
//				}
//			}
//		}
//	}
	
	@EventHandler
	public void Pickup(PlayerPickupItemEvent ev){
		if(Drops.containsKey(ev.getItem().getEntityId())&&Drops.get(ev.getItem().getEntityId()) >= System.currentTimeMillis()){
			if(Times.containsKey(ev.getPlayer()) && Times.get(ev.getPlayer()).contains(ev.getItem().getEntityId())){
				Drops.remove(ev.getItem().getEntityId());
				Times.get(ev.getPlayer()).remove(ev.getItem().getEntityId());
				if(Times.get(ev.getPlayer()).isEmpty())Times.remove(ev.getPlayer());
				return;
			}
			ev.setCancelled(true);
		}
	}
	
	List<Integer> l;
	Item item;
	@EventHandler(priority=EventPriority.HIGHEST)
	public void Death(PlayerDeathEvent ev){
		if(ev.getEntity() instanceof Player && ev.getEntity().getKiller() instanceof Player){
			if(!this.getKit().hasPlayer(this,((Player)ev.getEntity())))return;
			l= new ArrayList<>();
			for(ItemStack i : ev.getDrops()){
				item=ev.getEntity().getLocation().getWorld().dropItem(ev.getEntity().getLocation().add(0,0.5,0), i);
				l.add(item.getEntityId());
				Drops.put(item.getEntityId(), (TimeSpan.SECOND*time)+System.currentTimeMillis());
			}
			System.err.println("SPAWNED ITEMS FOR "+ev.getEntity().getCustomName());
			Times.put( ((Player)ev.getEntity()) , l);
			ev.getDrops().clear();
		}
	}
	

}
