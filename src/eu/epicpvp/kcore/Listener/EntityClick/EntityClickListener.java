package eu.epicpvp.kcore.Listener.EntityClick;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;

import eu.epicpvp.kcore.Inventory.Item.Click;
import eu.epicpvp.kcore.Listener.kListener;
import eu.epicpvp.kcore.Update.UpdateType;
import eu.epicpvp.kcore.Update.Event.UpdateEvent;
import eu.epicpvp.kcore.Util.UtilEvent.ActionType;
import lombok.Getter;
import lombok.Setter;

public class EntityClickListener extends kListener{
	
	@Getter
	private static ArrayList<Entity> entities=new ArrayList<>();
	
	private Click click;
	@Getter
	@Setter
	private Entity entity;
	private Location location;
	private EntityType type;
	
	public EntityClickListener(JavaPlugin instance,Click click, Entity entity) {
		super(instance, (entity.getCustomName()!=null?entity.getCustomName():"CreatureInv")+"Listener");
		this.click=click;
		this.entity=entity;
		this.location=entity.getLocation();
		this.type=entity.getType();
		entities.add(entity);
	}
	
	@EventHandler
	public void inter(PlayerInteractEntityEvent ev){
		if(this.entity!=null&&ev.getRightClicked().getEntityId()==this.entity.getEntityId()){
			ev.setCancelled(true);
			click.onClick(ev.getPlayer(), ActionType.BLOCK, entity);
		}
	}
	
	@EventHandler
	public void udpat(UpdateEvent ev){
		if(ev.getType()==UpdateType.MIN_08){
			if(entity.isDead()){
				this.entity=this.entity.getWorld().spawnEntity(this.location, this.type);
			}
		}
	}
	
	@EventHandler
	public void explode(EntityExplodeEvent ev){
		if(this.entity!=null&&ev.getEntity().getEntityId()==this.entity.getEntityId())ev.setCancelled(true);
	}
	
	@EventHandler
	public void teleport(EntityTeleportEvent ev){
		if(this.entity!=null&&ev.getEntity().getEntityId()==this.entity.getEntityId())ev.setCancelled(true);
	}
	
	@EventHandler
	public void flame(EntityCombustEvent ev){
		if(this.entity!=null&&ev.getEntity().getEntityId()==this.entity.getEntityId())ev.setCancelled(true);
	}
	
	@EventHandler
	public void Damage(EntityDamageByEntityEvent ev){
		if(this.entity!=null&&ev.getEntity().getEntityId()==this.entity.getEntityId())ev.setCancelled(true);
		if(this.entity!=null&&ev.getDamager().getEntityId()==this.entity.getEntityId())ev.setCancelled(true);
	}
	
	@EventHandler
	public void Damage(EntityDamageEvent ev){
		if(this.entity!=null&&ev.getEntity().getEntityId()==this.entity.getEntityId())ev.setCancelled(true);
	}
	
	@EventHandler
	public void target(EntityTargetEvent ev){
		if(this.entity!=null&&ev.getEntity().getEntityId()==this.entity.getEntityId())ev.setCancelled(true);
	}
}
