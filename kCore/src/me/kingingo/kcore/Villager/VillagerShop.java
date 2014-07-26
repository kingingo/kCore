package me.kingingo.kcore.Villager;

import java.lang.reflect.Field;
import java.util.HashMap;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.Merchant.Merchant;
import me.kingingo.kcore.Update.UpdateType;
import me.kingingo.kcore.Update.Event.UpdateEvent;
import me.kingingo.kcore.Util.InventorySize;
import me.kingingo.kcore.Util.UtilItem;
import net.minecraft.server.v1_7_R4.EntityCreature;
import net.minecraft.server.v1_7_R4.EntityHuman;
import net.minecraft.server.v1_7_R4.EntityInsentient;
import net.minecraft.server.v1_7_R4.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_7_R4.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_7_R4.PathfinderGoalSelector;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftCreature;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class VillagerShop implements Listener {

	@Getter
	String name;
	@Getter
	Location spawn;
	@Getter
	Villager villager;
	@Getter
	Inventory inventory;
	@Getter
	HashMap<ItemStack,Merchant> shops = new HashMap<>();
	private Field _goalSelector;
	private Field _targetSelector;
	@Getter
	@Setter
	boolean damage=false;
	@Getter
	@Setter
	boolean move=false;
	
	public VillagerShop(JavaPlugin instance,String name,Location spawn,InventorySize size){
		this.name=name;
		this.spawn=spawn.add(0,0.15,0);
		this.inventory=Bukkit.createInventory(null, size.getSize(), getName());
		this.villager=(Villager)spawn.getWorld().spawnEntity(getSpawn(), EntityType.VILLAGER);
		VillagerClearPath();
		Bukkit.getPluginManager().registerEvents(this, instance);
	}
	
	public void addShop(ItemStack item,Merchant merchant,int slot){
		shops.put(item, merchant);
		inventory.setItem(slot, item);
	}
	
	public void VillagerClearPath(){
		try
	    {
	      this._goalSelector = EntityInsentient.class.getDeclaredField("goalSelector");
	      this._goalSelector.setAccessible(true);
	      this._targetSelector = EntityInsentient.class.getDeclaredField("targetSelector");
	      this._targetSelector.setAccessible(true);

	      EntityCreature creature = ((CraftCreature)getVillager()).getHandle();

	      PathfinderGoalSelector goalSelector = new PathfinderGoalSelector(((CraftWorld)getVillager().getWorld()).getHandle().methodProfiler);

	      goalSelector.a(0, new PathfinderGoalLookAtPlayer(creature, EntityHuman.class, 6.0F));
	      goalSelector.a(1, new PathfinderGoalRandomLookaround(creature));

	      this._goalSelector.set(creature, goalSelector);
	      this._targetSelector.set(creature, new PathfinderGoalSelector(((CraftWorld)getVillager().getWorld()).getHandle().methodProfiler));
	    }
	    catch (IllegalArgumentException e)
	    {
	      e.printStackTrace();
	    }
	    catch (IllegalAccessException e)
	    {
	      e.printStackTrace();
	    }
	    catch (NoSuchFieldException e)
	    {
	      e.printStackTrace();
	    }
	    catch (SecurityException e)
	    {
	      e.printStackTrace();
	    }
	}
	
	@EventHandler
	public void Move(UpdateEvent ev){
		if(ev.getType()!=UpdateType.SLOW)return;
		Location l = getVillager().getLocation();
		if(l.getBlockX()!=getSpawn().getBlockX()){
			if(l.getBlockZ()!=getSpawn().getBlockZ()){
				if(l.getBlockY()!=getSpawn().getBlockY()){
					getVillager().teleport(getSpawn());
				}
			}
		}
	}
	
	@EventHandler
	public void TargetLivingEntity(EntityTargetLivingEntityEvent ev){
		if(ev.getEntity() instanceof Villager){
			Villager v = (Villager)ev.getEntity();
			if(v.getEntityId()==getVillager().getEntityId()){
				if(!move){
					ev.setTarget(null);
					ev.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler
	public void Target(EntityTargetEvent ev){
		if(ev.getEntity() instanceof Villager){
			Villager v = (Villager)ev.getEntity();
			if(v.getEntityId()==getVillager().getEntityId()){
				if(!move){
					ev.setTarget(null);
					ev.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler
	public void DamageByEntity(EntityDamageByEntityEvent ev){
		if(ev.getEntity() instanceof Villager){
			Villager v = (Villager) ev.getEntity();
			if(v.getEntityId()==getVillager().getEntityId()){
				if(!damage){
					ev.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler
	public void Damage(EntityDamageEvent ev){
		if(ev.getEntity() instanceof Villager){
			Villager v = (Villager) ev.getEntity();
			if(v.getEntityId()==getVillager().getEntityId()){
				if(!damage){
					ev.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler
	public void Click(PlayerInteractEntityEvent ev){
		if(ev.getRightClicked() instanceof Villager){
			Villager v = (Villager)ev.getRightClicked();
			if(v.getEntityId()==villager.getEntityId()){
				ev.getPlayer().openInventory(getInventory());
			}
		}
	}
	
	@EventHandler
	public void ClickInventory(InventoryClickEvent ev){
	if (!(ev.getWhoClicked() instanceof Player)|| ev.getInventory() == null || ev.getCursor() == null || ev.getCurrentItem() == null)return;
		if(ev.getInventory().getName().equalsIgnoreCase(getName())){
			Player p = (Player)ev.getWhoClicked();
			ev.setCancelled(true);
			p.closeInventory();
			for(ItemStack s : getShops().keySet()){
				if(UtilItem.ItemNameEquals(ev.getCurrentItem(), s)){
					getShops().get(s).openTrading(p,getName());
					break;
				}
			}
		}
	}
	
}
