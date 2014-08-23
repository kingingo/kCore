package me.kingingo.kcore.ItemFake;

import lombok.Getter;
import me.kingingo.kcore.ItemFake.Events.ItemFakeCreateEvent;
import me.kingingo.kcore.ItemFake.Events.ItemFakeDestroyEvent;
import me.kingingo.kcore.ItemFake.Events.ItemFakePickupEvent;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class ItemFake implements Listener {
	
	@Getter
	Item item;
	@Getter
	Location location;
	@Getter
	JavaPlugin instance;
	
	public ItemFake(Item item,JavaPlugin plugin){
		this.item=item;
		this.location=item.getLocation();
		this.instance=plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
		Bukkit.getPluginManager().callEvent(new ItemFakeCreateEvent(this));
	}
	
	public ItemFake(Location loc,ItemStack item,JavaPlugin plugin){
		this.location=loc;
		getLocation().getWorld().loadChunk(getLocation().getWorld().getChunkAt(getLocation()));
		this.item=getLocation().getWorld().dropItemNaturally(getLocation().add(0,0.3,0), item);
		this.instance=plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
		Bukkit.getPluginManager().callEvent(new ItemFakeCreateEvent(this));
	}
	
	public ItemFake(Location loc,int id,JavaPlugin plugin){
		this.location=loc;
		getLocation().getWorld().loadChunk(getLocation().getWorld().getChunkAt(getLocation()));
		this.item=getLocation().getWorld().dropItemNaturally(getLocation().add(0,0.3,0), new ItemStack(id));
		this.instance=plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
		Bukkit.getPluginManager().callEvent(new ItemFakeCreateEvent(this));
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void PickUp(PlayerPickupItemEvent ev){
		if(this.item==null)return;
		if(ev.getItem().getEntityId()==getEntityID()){
			ev.setCancelled(true);
			Bukkit.getPluginManager().callEvent(new ItemFakePickupEvent(ev.getItem(),ev.getPlayer(),this));
		}
	}
	
	public void remove(){
		Bukkit.getPluginManager().callEvent(new ItemFakeDestroyEvent(this));
		item.remove();
		item=null;
		PlayerPickupItemEvent.getHandlerList().unregister(this);
	}
	
	public int getEntityID(){
		return item.getEntityId();
	}
	
	public ItemStack getItemStack(){
		return item.getItemStack();
	}

}
