package me.kingingo.kcore.Villager;

import java.lang.reflect.Field;
import java.util.HashMap;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.Merchant.Merchant;
import me.kingingo.kcore.Update.UpdateType;
import me.kingingo.kcore.Update.Event.UpdateEvent;
import me.kingingo.kcore.Util.InventorySize;
import me.kingingo.kcore.Util.TimeSpan;
import me.kingingo.kcore.Util.UtilItem;
import net.minecraft.server.v1_7_R4.EntityCreature;
import net.minecraft.server.v1_7_R4.EntityHuman;
import net.minecraft.server.v1_7_R4.EntityInsentient;
import net.minecraft.server.v1_7_R4.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_7_R4.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_7_R4.PathfinderGoalSelector;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftCreature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class VillagerShop implements Listener {

	@Getter
	String name;
	@Getter
	Location spawn;
	@Getter
	Entity villager;
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
	long spawn_time = 0;
	EntityType type;
	
	public VillagerShop(JavaPlugin instance,EntityType type,String name,Location spawn,InventorySize size){
		this.name=name;
		this.type=type;
		spawn(spawn);
		this.inventory=Bukkit.createInventory(null, size.getSize(), getName());
		Bukkit.getPluginManager().registerEvents(this, instance);
	}
	
	public void spawn(){
		if(villager!=null){
			this.villager.remove();
			this.villager=null;
		}
		this.spawn.getWorld().loadChunk(this.spawn.getWorld().getChunkAt(this.spawn));
		this.spawn_time=System.currentTimeMillis();
		this.villager=this.spawn.getWorld().spawnEntity(getSpawn(), this.type);
		VillagerClearPath();
	}
	
	public void spawn(Location loc){
		if(villager!=null){
			this.villager.remove();
			this.villager=null;
		}
		this.spawn=loc;
		this.spawn=spawn.add(0,0.5,0);
		this.spawn.getWorld().loadChunk(this.spawn.getWorld().getChunkAt(this.spawn));
		this.spawn_time=System.currentTimeMillis();
		this.villager=this.spawn.getWorld().spawnEntity(getSpawn(), this.type);
		VillagerClearPath();
	}
	
	public void addShop(ItemStack item,Merchant merchant,int slot){
		shops.put(item, merchant);
		inventory.setItem(slot, item);
	}
	
	public void finish(){
		ItemMeta im;
		for(int i = 0; i<getInventory().getSize();i++){
			if(getInventory().getItem(i)==null||getInventory().getItem(i).getType()==Material.AIR){
				getInventory().setItem(i, new ItemStack(Material.FENCE));
				getInventory().getItem(i).setType(Material.STAINED_GLASS_PANE);
				getInventory().getItem(i).setDurability((byte) 7);
				im = getInventory().getItem(i).getItemMeta();
				im.setDisplayName(" ");
				getInventory().getItem(i).setItemMeta(im);
			}
		}
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
	
//	@EventHandler
//	public void Exsit(UpdateEvent ev){
//		if(ev.getType()!=UpdateType.SLOWEST)return;
//		for(Villager e : getSpawn().getWorld().getEntitiesByClass(Villager.class)){
//			if(e.getLocation().distance(getSpawn()) <= 2){
//				this.villager=e;
//				break;
//			}
//		}
//	}
	
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
		if(ev.getEntity().getType()==this.type){
			if(ev.getEntity().getEntityId()==getVillager().getEntityId()){
				if(!move){
					ev.setTarget(null);
					ev.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler
	public void Target(EntityTargetEvent ev){
		if(ev.getEntity().getType()==type){
			if(ev.getEntity().getEntityId()==getVillager().getEntityId()){
				if(!move){
					ev.setTarget(null);
					ev.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler
	public void DamageByEntity(EntityDamageByEntityEvent ev){
		if(ev.getEntity().getType()==type){
			if(ev.getEntity().getEntityId()==getVillager().getEntityId()){
				if(!damage){
					ev.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler
	public void Death(EntityDeathEvent ev){
		if(ev.getEntity().getEntityId()==villager.getEntityId()){
			spawn.getWorld().loadChunk(spawn.getWorld().getChunkAt(spawn));
			this.villager=(Villager)spawn.getWorld().spawnEntity(getSpawn(), EntityType.VILLAGER);
			VillagerClearPath();
		}
	}
	
	@EventHandler
	public void Damage(EntityDamageEvent ev){
		if(ev.getEntity().getType()==type){
			if(ev.getEntity().getEntityId()==getVillager().getEntityId()){
				if(!damage){
					ev.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void ClickFail(PlayerInteractEntityEvent ev){
		if(!ev.isCancelled()&&ev.getRightClicked().getType()==type){
			if(ev.getRightClicked().getLocation().distance(getSpawn())<=3){
				this.villager=ev.getRightClicked();
				ev.setCancelled(true);
				ev.getPlayer().openInventory(getInventory());
			}
		}
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void Click(PlayerInteractEntityEvent ev){
		if(ev.getRightClicked().getType()==type){
			if(ev.getRightClicked().getEntityId()==villager.getEntityId()){
				ev.setCancelled(true);
				ev.getPlayer().openInventory(getInventory());
			}
		}
	}
	
	Merchant m;
	@EventHandler
	public void ClickInventory(InventoryClickEvent ev){
	if (!(ev.getWhoClicked() instanceof Player)|| ev.getInventory() == null || ev.getCursor() == null || ev.getCurrentItem() == null)return;
		if(ev.getInventory().getName().equalsIgnoreCase(getName())){
			Player p = (Player)ev.getWhoClicked();
			ev.setCancelled(true);
			p.closeInventory();
			for(ItemStack s : getShops().keySet()){
				if(UtilItem.ItemNameEquals(ev.getCurrentItem(), s)){
					m = getShops().get(s).clone();
					m.setCustomer(p);
					m.setTitle(getName());
					m.openTrading(p);
					break;
				}
			}
		}
	}
	
}
