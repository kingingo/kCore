package eu.epicpvp.kcore.Villager;

import java.lang.reflect.Field;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftCreature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import eu.epicpvp.kcore.Inventory.InventoryPageBase;
import eu.epicpvp.kcore.Inventory.Inventory.InventoryMerchant;
import eu.epicpvp.kcore.Inventory.Item.Click;
import eu.epicpvp.kcore.Inventory.Item.Buttons.ButtonBase;
import eu.epicpvp.kcore.Inventory.Item.Buttons.ButtonOpenInventory;
import eu.epicpvp.kcore.Inventory.Item.Buttons.ButtonOpenMerchant;
import eu.epicpvp.kcore.Listener.kListener;
import eu.epicpvp.kcore.Listener.EntityClick.EntityClickListener;
import eu.epicpvp.kcore.Merchant.Merchant;
import eu.epicpvp.kcore.Merchant.MerchantOffer;
import eu.epicpvp.kcore.Update.UpdateType;
import eu.epicpvp.kcore.Update.Event.UpdateEvent;
import eu.epicpvp.kcore.Util.InventorySize;
import eu.epicpvp.kcore.Util.UtilEvent.ActionType;
import eu.epicpvp.kcore.Util.UtilInv;
import eu.epicpvp.kcore.Util.UtilItem;
import eu.epicpvp.kcore.Villager.Event.VillagerAddShopEvent;
import eu.epicpvp.kcore.Villager.Event.VillagerShopEvent;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.v1_8_R3.EntityCreature;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.EntityInsentient;
import net.minecraft.server.v1_8_R3.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_8_R3.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_8_R3.PathfinderGoalSelector;

public class VillagerShop extends kListener{

	@Getter
	private String name;
	@Getter
	private Location spawn;
	@Getter
	private Entity villager;
	@Getter
	private InventoryPageBase inventoryMerchant;
	@Getter
	private InventoryPageBase inventory;
	private Field _goalSelector;
	private Field _targetSelector;
	@Getter
	@Setter
	private boolean damage=false;
	@Getter
	@Setter
	private boolean move=false;
	private EntityType type;
	
	public VillagerShop(JavaPlugin instance,EntityType type,String name,Location spawn,InventorySize size){
		super(instance,"VillagerShop:"+name);
		this.name=name;
		this.type=type;
		this.inventoryMerchant=new InventoryPageBase(InventorySize._27, getName()+" | Villager");
		this.inventory=new InventoryPageBase(InventorySize._27, getName()+" | Inventory");
		this.inventoryMerchant.addButton(0, new ButtonBase(new Click(){

			@Override
			public void onClick(Player player, ActionType type, Object object) {
				UtilVillagerShop.getVillager().remove(player);
				player.openInventory(getInventory());
			}
			
		}, UtilItem.Item(new ItemStack(Material.REDSTONE), new String[]{}, "§aInventory Shop")));
		this.inventory.addButton(0, new ButtonBase(new Click(){

			@Override
			public void onClick(Player player, ActionType type, Object object) {
				UtilVillagerShop.getVillager().add(player);
				player.openInventory(getInventoryMerchant());
			}
			
		}, UtilItem.Item(new ItemStack(Material.GLOWSTONE_DUST), new String[]{}, "§aInventory Shop")));
		UtilInv.getBase(instance).addPage(inventoryMerchant);
		UtilInv.getBase(instance).addPage(inventory);
		spawn(spawn);
	}
	
	@EventHandler
	public void quit(PlayerQuitEvent ev){
		if(UtilVillagerShop.getVillager().contains(ev.getPlayer()))UtilVillagerShop.getVillager().remove(ev.getPlayer());
	}
	
	public void spawn(){
		spawn(this.spawn);
	}
	
	public void spawn(Location loc){
		if(villager!=null){
			this.villager.remove();
			this.villager=null;
		}
		this.spawn=loc;
		this.spawn=spawn.add(0.5,0.1,0.5);
		this.spawn.getWorld().loadChunk(this.spawn.getWorld().getChunkAt(this.spawn));
		this.villager=this.spawn.getWorld().spawnEntity(getSpawn(), this.type);
		
		EntityClickListener.getEntities().add(villager);
		VillagerClearPath();
	}
	
	public void addShop(ItemStack itemStack,Merchant merchant,int slot){
		VillagerAddShopEvent ev = new VillagerAddShopEvent(this, merchant, itemStack, slot);
		Bukkit.getPluginManager().callEvent(ev);
		if(ev.isCancelled())return;
		
		for(MerchantOffer offer : ev.getMerchant().getOffers()){
			offer.setMaxUses(Integer.MAX_VALUE);
		}
		InventoryMerchant inv = new InventoryMerchant(ev.getMerchant(),this.inventory);
		UtilInv.getBase().addPage(inv);
		inventory.addButton(ev.getSlot(), new ButtonOpenInventory(inv, ev.getItemStack()));
		inventoryMerchant.addButton(ev.getSlot(), new ButtonOpenMerchant(ev.getMerchant(), ev.getItemStack()));
	}
	
	public void finish(){
		this.inventoryMerchant.fill(Material.STAINED_GLASS_PANE, (byte) 7);
		this.inventory.fill(Material.STAINED_GLASS_PANE, (byte) 7);
	}
	
	@EventHandler
	public void chunk(ChunkUnloadEvent ev){
		if(ev.getChunk() == spawn.getChunk()){
			ev.setCancelled(true);
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
			if(ev.getRightClicked().getEntityId()==villager.getEntityId()){
				this.villager=ev.getRightClicked();
				ev.setCancelled(true);
				VillagerShopEvent event = new VillagerShopEvent(ev.getPlayer(), this);
				Bukkit.getPluginManager().callEvent(event);

				if(event.isCancelled())return;
				
				if(UtilVillagerShop.getVillager().contains(ev.getPlayer())){
					ev.getPlayer().openInventory(getInventoryMerchant());
				}else{
					ev.getPlayer().openInventory(getInventory());
				}
			}
		}
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void Click(PlayerInteractEntityEvent ev){
		if(ev.getRightClicked().getType()==type){
			if(ev.getRightClicked().getEntityId()==villager.getEntityId()){
				VillagerShopEvent event = new VillagerShopEvent(ev.getPlayer(), this);
				Bukkit.getPluginManager().callEvent(event);
				
				if(event.isCancelled())return;
				if(UtilVillagerShop.getVillager().contains(ev.getPlayer())){
					ev.getPlayer().openInventory(getInventoryMerchant());
				}else{
					ev.getPlayer().openInventory(getInventory());
				}
			}
		}
	}
}
