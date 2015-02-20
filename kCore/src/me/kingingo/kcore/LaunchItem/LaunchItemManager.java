package me.kingingo.kcore.LaunchItem;

import java.util.ArrayList;

import lombok.Getter;
import me.kingingo.kcore.kListener;
import me.kingingo.kcore.Update.UpdateType;
import me.kingingo.kcore.Update.Event.UpdateEvent;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class LaunchItemManager extends kListener{

	@Getter
	private JavaPlugin instance;
	@Getter
	ArrayList<LaunchItem> items = new ArrayList<>();
	
	public LaunchItemManager(JavaPlugin instance){
		super(instance,"[LaunchItemManager]");
		this.instance=instance;
	}
	
	@EventHandler
	public void Effect(UpdateEvent ev){
		if(ev.getType()!=UpdateType.FASTEST)return;
		if(getItems().isEmpty())return;
		for(int i = 0; i<getItems().size(); i++){
			if(getItems().get(i).check())getItems().remove(i);
		}
	}
	
	boolean b = false;
	@EventHandler
	public void PickUp(PlayerPickupItemEvent ev){
		for(LaunchItem item : items){
			for(Entity e : item.getDroppedItem()){
				if(e.getEntityId()==ev.getItem().getEntityId()){
					ev.setCancelled(true);
					b=true;
					break;
				}
			}
			if(b){
				b=false;
				break;
			}
		}
	}
	
	public void removeItem(LaunchItem item){
		item.remove();
		getItems().remove(item);
	}
	
	public void removeItem(Player player){
		LaunchItem i = null;
		for(LaunchItem item : getItems()){
			if(item.getPlayer().getName().equalsIgnoreCase(player.getName())){
				i=item;
				item.remove();
				break;
			}
		}
		if(i!=null)getItems().remove(i);
	}
	
	public LaunchItem LaunchItem(LaunchItem item){
		items.add(item);
		return item;
	}
	
}
