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
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class ItemFake implements Listener {
	
	@Getter
	Item item;
	@Getter
	JavaPlugin instance;
	
	public ItemFake(Item item,JavaPlugin plugin){
		this.item=item;
		this.instance=plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
		Bukkit.getPluginManager().callEvent(new ItemFakeCreateEvent(this));
	}
	
	public ItemFake(Location loc,ItemStack item,JavaPlugin plugin){
		loc.getWorld().loadChunk(loc.getWorld().getChunkAt(loc));
		this.item=loc.getWorld().dropItem(loc.clone().add(0,0.3,0), item);
		this.instance=plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
		Bukkit.getPluginManager().callEvent(new ItemFakeCreateEvent(this));
	}
	
	public ItemFake(Location loc,int id,JavaPlugin plugin){
		loc.getWorld().loadChunk(loc.getWorld().getChunkAt(loc));
		this.item=loc.getWorld().dropItem(loc.clone().add(0,0.3,0), new ItemStack(id));
		this.instance=plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
		Bukkit.getPluginManager().callEvent(new ItemFakeCreateEvent(this));
	}
	
	public Location getLocation(){
		return item.getLocation();
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
