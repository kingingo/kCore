package me.kingingo.kcore.ItemFake;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import lombok.Getter;
import me.kingingo.kcore.ItemFake.Events.ItemFakePickupEvent;

public class ItemFake {
	
	@Getter
	Item item;
	@Getter
	Location location;
	
	public ItemFake(Location loc,ItemStack item){
		this.location=loc;
		getLocation().getWorld().loadChunk(getLocation().getWorld().getChunkAt(getLocation()));
		this.item=getLocation().getWorld().dropItemNaturally(getLocation().add(0,0.3,0), item);
	}
	
	public ItemFake(Location loc,int id){
		this.location=loc;
		getLocation().getWorld().loadChunk(getLocation().getWorld().getChunkAt(getLocation()));
		this.item=getLocation().getWorld().dropItemNaturally(getLocation().add(0,0.3,0), new ItemStack(id));
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void PickUp(PlayerPickupItemEvent ev){
		if(ev.getItem().getEntityId()==getEntityID()){
			ev.setCancelled(true);
			Bukkit.getPluginManager().callEvent(new ItemFakePickupEvent(ev.getItem(),ev.getPlayer(),this));
		}
	}
	
	public void remove(){
		item.remove();
		item=null;
	}
	
	public int getEntityID(){
		return item.getEntityId();
	}
	
	public ItemStack getItemStack(){
		return item.getItemStack();
	}

}
