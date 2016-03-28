package eu.epicpvp.kcore.ItemFake;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

import eu.epicpvp.kcore.ItemFake.Events.ItemFakeCreateEvent;
import eu.epicpvp.kcore.ItemFake.Events.ItemFakeDestroyEvent;
import lombok.Getter;

public class ItemFake {
	
	@Getter
	Item item;
	
	public ItemFake(Item item){
		this.item=item;
		Bukkit.getPluginManager().callEvent(new ItemFakeCreateEvent(this));
	}
	
	public ItemFake(Location loc,ItemStack item){
		loc.getWorld().loadChunk(loc.getWorld().getChunkAt(loc));
		this.item=loc.getWorld().dropItem(loc.clone().add(0,0.3,0), item);
		Bukkit.getPluginManager().callEvent(new ItemFakeCreateEvent(this));
	}
	
	public ItemFake(Location loc,int id){
		loc.getWorld().loadChunk(loc.getWorld().getChunkAt(loc));
		this.item=loc.getWorld().dropItem(loc.clone().add(0,0.3,0), new ItemStack(id));
		Bukkit.getPluginManager().callEvent(new ItemFakeCreateEvent(this));
	}
	
	public Location getLocation(){
		return item.getLocation();
	}
	
	public void remove(){
		ItemFakeDestroyEvent e = new ItemFakeDestroyEvent(this);
		Bukkit.getPluginManager().callEvent(e);
		if(!e.isCancelled()){
			item.remove();
			item=null;
		}
	}
	
	public int getEntityID(){
		return item.getEntityId();
	}
	
	public ItemStack getItemStack(){
		return item.getItemStack();
	}

}
