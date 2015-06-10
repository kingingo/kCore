package me.kingingo.kcore.ItemFake;

import java.util.ArrayList;
import java.util.HashMap;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.Hologram.Hologram;
import me.kingingo.kcore.Hologram.nametags.NameTagMessage;
import me.kingingo.kcore.ItemFake.Events.ItemFakeCreateEvent;
import me.kingingo.kcore.ItemFake.Events.ItemFakeDestroyEvent;
import me.kingingo.kcore.ItemFake.Events.ItemFakePickupEvent;
import me.kingingo.kcore.Update.UpdateType;
import me.kingingo.kcore.Update.Event.UpdateEvent;
import me.kingingo.kcore.Util.UtilServer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class ItemFakeManager implements Listener {

	@Getter
	private ArrayList<ItemFake> list = new ArrayList<>();
	@Getter
	private JavaPlugin instance;
	@Getter
	private Hologram hm;
	@Getter
	@Setter
	private double distance=4.0;
	@Getter
	private HashMap<ItemFake,HashMap<Player,NameTagMessage>> itemfake = new HashMap<>();
	private NameTagMessage ntm;
	private ArrayList<Player> ntm_remove = new ArrayList<Player>();
	
	public ItemFakeManager(JavaPlugin instance,Hologram hm){
		this.instance=instance;
		this.hm=hm;
		Bukkit.getPluginManager().registerEvents(this, instance);
	}
	
	@EventHandler
	public void RemoveHoloGram(UpdateEvent ev){
		if(ev.getType()!=UpdateType.SEC_2)return;
		for(ItemFake i : itemfake.keySet()){
			ntm_remove.clear();
			for(Player p : itemfake.get(i).keySet()){
				ntm=itemfake.get(i).get(p);
				if(ntm==null||i==null||p==null)continue;
				if(!i.getLocation().getWorld().getName().equalsIgnoreCase(p.getWorld().getName()))continue;
				if(i.getLocation().distance(p.getLocation()) > getDistance()){
					ntm.clear(p);
					ntm_remove.add(p);
				}
			}
			for(Player p : ntm_remove){
				itemfake.get(i).remove(p);
			}
		}
	}
	
	@EventHandler
	public void ItemDespawn(ItemDespawnEvent ev){
		for(ItemFake f : list){
			if(f.getItem().getEntityId()==ev.getEntity().getEntityId()){
				ev.setCancelled(true);
				break;
			}
		}
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void PickUp(PlayerPickupItemEvent ev){
		for(ItemFake f : list){
			if(f.getItem().getEntityId()==ev.getItem().getEntityId()){
				ev.setCancelled(true);
				Bukkit.getPluginManager().callEvent(new ItemFakePickupEvent(ev.getItem(),ev.getPlayer(),f));
				break;
			}
		}
	}
	
	@EventHandler
	public void Update(UpdateEvent ev){
		if(ev.getType()!=UpdateType.FAST)return;
		for(ItemFake i : list ){
			if(!i.getItemStack().hasItemMeta())continue;
			if(!i.getItemStack().getItemMeta().hasDisplayName())continue;
			for(Player p : UtilServer.getPlayers()){
				if(itemfake.containsKey(i)&&itemfake.get(i).containsKey(p))continue;
				if(!i.getLocation().getWorld().getName().equalsIgnoreCase(p.getWorld().getName()))continue;
				if(i.getLocation().distance(p.getLocation()) <= getDistance()){
					ntm = hm.sendText(p, i.getLocation().clone().add(0,0.2,0), i.getItemStack().getItemMeta().getDisplayName());
					if(!itemfake.containsKey(i))itemfake.put(i, new HashMap<Player,NameTagMessage>());
					itemfake.get(i).put(p, ntm);
				}
			}
		}
	}
	
	@EventHandler
	public void Create(ItemFakeCreateEvent ev){
		list.add(ev.getItemfake());
	}
	
	@EventHandler
	public void Remove(ItemFakeDestroyEvent ev){
		list.remove(ev.getItemfake());
		if(itemfake.containsKey(ev.getItemfake())){
			for(Player player : itemfake.get(ev.getItemfake()).keySet())itemfake.get(ev.getItemfake()).get(player).clear(player);
			itemfake.remove(ev.getItemfake());
		}
	}
	
}
