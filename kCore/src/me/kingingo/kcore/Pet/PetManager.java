package me.kingingo.kcore.Pet;

import java.lang.reflect.Field;
import java.util.HashMap;
import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.Pet.Setting.PetSetting;
import me.kingingo.kcore.Update.UpdateType;
import me.kingingo.kcore.Update.Event.UpdateEvent;
import me.kingingo.kcore.Pet.Event.PetWithOutOwnerLocationEvent;
import net.minecraft.server.v1_7_R4.EntityCreature;
import net.minecraft.server.v1_7_R4.EntityHuman;
import net.minecraft.server.v1_7_R4.EntityInsentient;
import net.minecraft.server.v1_7_R4.Navigation;
import net.minecraft.server.v1_7_R4.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_7_R4.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_7_R4.PathfinderGoalSelector;
import net.minecraft.server.v1_7_R4.EntityLiving;
import net.minecraft.server.v1_7_R4.PathEntity;
import org.bukkit.entity.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftCreature;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftEntity;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.entity.Creature;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;


public class PetManager implements Listener{

	@Getter
	private JavaPlugin instance;
	@Getter
	private HashMap<String,org.bukkit.entity.Creature> activePetOwners;
	@Getter
	private HashMap<Creature,Location> petToLocation;
	@Getter
	private HashMap<Creature,Integer> failedAttemptsToLocation;
	@Getter
	private HashMap<String, Integer> failedAttempts;
	private Field _goalSelector;
	private Field _targetSelector;
	@Getter
	private boolean setting=false;
	@Getter
	private HashMap<EntityType,PetSetting> setting_list;
	
	public PetManager(JavaPlugin instance){
		Bukkit.getPluginManager().registerEvents(this, instance);
		this.instance=instance;
		this.failedAttempts = new HashMap<>();
		this.failedAttemptsToLocation = new HashMap<>();
		this.petToLocation = new HashMap<>();
		this.activePetOwners = new HashMap<>();
	}
	
	public void setSetting(boolean b){
		setting=b;
		if(setting){
			setting_list=new HashMap<EntityType,PetSetting>();
		}
	}
	
	public void RemovePet(Player player, boolean removeOwner)
	  {
	    if (this.activePetOwners.containsKey(player.getName()))
	    {
	      org.bukkit.entity.Creature pet = (org.bukkit.entity.Creature)this.activePetOwners.get(player.getName());
	      pet.remove();

	      if (removeOwner)
	      {
	        this.activePetOwners.remove(player.getName());
	      }
	    }
	  }
	
	public boolean PetWithOutOwnerSetLocation(Creature pet,Location location){
		if(petToLocation.containsKey(pet)){
			petToLocation.put(pet, location);
			return true;
		}
		return false;
	}
	
	public Creature AddPetWithOutOwner(String name, EntityType entityType, Location location){
		location.getWorld().loadChunk(location.getWorld().getChunkAt(location));
	    Creature pet =(Creature) location.getWorld().spawnEntity(location, entityType);
	    pet.setCustomNameVisible(true);
	    pet.setCustomName(name);
	    this.petToLocation.put(pet,location);
	    this.failedAttemptsToLocation.put(pet,Integer.valueOf(0));
	    ClearPetGoals(pet);
	    return pet;
    }
	
	public void AddPetOwner(Player player,String name, EntityType entityType, Location location)
	  {
	    if (this.activePetOwners.containsKey(player.getName()))
	    {
	      if (((org.bukkit.entity.Creature)this.activePetOwners.get(player.getName())).getType() != entityType)
	      {
	        RemovePet(player, true);
	      }else {
	        return;
	      }
	    }
	    org.bukkit.entity.Creature pet = (org.bukkit.entity.Creature)location.getWorld().spawnEntity(location, entityType);
	    pet.setCustomNameVisible(true);
	    pet.setCustomName(name);

	    this.activePetOwners.put(player.getName(),pet);
	    this.failedAttempts.put(player.getName(), Integer.valueOf(0));
	    ClearPetGoals(pet);
	  }
	
	public org.bukkit.entity.Creature GetPet(Player player){
	    return (org.bukkit.entity.Creature)this.activePetOwners.get(player.getName());
	}
	
	private void ClearPetGoals(org.bukkit.entity.Creature pet){
	    try
	    {
	      this._goalSelector = EntityInsentient.class.getDeclaredField("goalSelector");
	      this._goalSelector.setAccessible(true);
	      this._targetSelector = EntityInsentient.class.getDeclaredField("targetSelector");
	      this._targetSelector.setAccessible(true);

	      EntityCreature creature = ((CraftCreature)pet).getHandle();

	      PathfinderGoalSelector goalSelector = new PathfinderGoalSelector(((CraftWorld)pet.getWorld()).getHandle().methodProfiler);

	      goalSelector.a(0, new PathfinderGoalLookAtPlayer(creature, EntityHuman.class, 6.0F));
	      goalSelector.a(1, new PathfinderGoalRandomLookaround(creature));

	      this._goalSelector.set(creature, goalSelector);
	      this._targetSelector.set(creature, new PathfinderGoalSelector(((CraftWorld)pet.getWorld()).getHandle().methodProfiler));
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
	
	  @EventHandler(priority=EventPriority.LOWEST)
	  public void onEntityDamage(EntityDamageEvent event)
	  {
	    if (((event.getEntity() instanceof org.bukkit.entity.Creature)) && (this.petToLocation.containsKey((org.bukkit.entity.Creature)event.getEntity()))){
	      event.setCancelled(true);
	    }
	  }
	  
	  Player owner;
	  Creature pet;
	  EntityCreature ec;
	  Navigation nav;
	  int xDiff;
	  int yDiff;
	  int zDiff;
	  int xIndex;
      int zIndex;
      Block targetBlock;
	  Location ownerSpot;
	  Location petSpot;
	  @EventHandler
	  public void onUpdateTo(UpdateEvent event)
	  {
	    if (event.getType() != UpdateType.FASTER)return;

	    for(String playerName : activePetOwners.keySet()){
	    	owner=Bukkit.getPlayer(playerName);
	    	pet=(Creature)activePetOwners.get(playerName);
	    	
	    	petSpot=pet.getLocation();
	    	ownerSpot=owner.getLocation();
	    	
	    	xDiff = Math.abs(petSpot.getBlockX() - ownerSpot.getBlockX());
	    	yDiff = Math.abs(petSpot.getBlockY() - ownerSpot.getBlockY());
	    	zDiff = Math.abs(petSpot.getBlockZ() - ownerSpot.getBlockZ());
	    	
	    	if(xDiff+zDiff+yDiff > 4){
	    		ec=((CraftCreature)pet).getHandle();
	    		nav=ec.getNavigation();
	    		
	    		xIndex=-1;
	    		zIndex=-1;
	    		targetBlock = ownerSpot.getBlock().getRelative(xIndex, -1, zIndex);
	 	        while ((targetBlock.isEmpty()) || (targetBlock.isLiquid())){
	 	          if (xIndex < 2) {
	 	            xIndex++;
	 	          } else if (zIndex < 2)
	 	          {
	 	            xIndex = -1;
	 	            zIndex++;
	 	          }
	 	          else {
	 	            return;
	 	          }
	 	          targetBlock = ownerSpot.getBlock().getRelative(xIndex, -1, zIndex);
	 	        }
	 	        
	 	       if (((Integer)this.failedAttempts.get(playerName)).intValue() > 4){
		          pet.teleport(owner);
		          this.failedAttempts.put(playerName, Integer.valueOf(0));
	 	      }else if (!nav.a(targetBlock.getX(), targetBlock.getY() + 1, targetBlock.getZ(),  2.0D)){
		          if (pet.getFallDistance() == 0.0F||pet.getLocation().distance(ownerSpot)>8){
		            this.failedAttempts.put(playerName, Integer.valueOf(((Integer)this.failedAttempts.get(playerName)).intValue() + 1));
		          }
	 	       }else{
		          this.failedAttempts.put(playerName, Integer.valueOf(0));
		       }
	    	}
	    }
//	    Iterator ownerIterator = this.activePetOwners.keySet().iterator();
//	    while (ownerIterator.hasNext())
//	    {
//	      String playerName = (String)ownerIterator.next();
//	      Player owner = Bukkit.getPlayer(playerName);
//
//	      org.bukkit.entity.Creature pet = (org.bukkit.entity.Creature)this.activePetOwners.get(playerName);
//	      Location petSpot = pet.getLocation();
//	      Location ownerSpot = owner.getLocation();
//	      int xDiff = Math.abs(petSpot.getBlockX() - ownerSpot.getBlockX());
//	      int yDiff = Math.abs(petSpot.getBlockY() - ownerSpot.getBlockY());
//	      int zDiff = Math.abs(petSpot.getBlockZ() - ownerSpot.getBlockZ());
//
//	      if (xDiff + yDiff + zDiff > 4)
//	      {
//	        EntityCreature ec = ((CraftCreature)pet).getHandle();
//	        Navigation nav = ec.getNavigation();
//
//	        int xIndex = -1;
//	        int zIndex = -1;
//	        Block targetBlock = ownerSpot.getBlock().getRelative(xIndex, -1, zIndex);
//	        while ((targetBlock.isEmpty()) || (targetBlock.isLiquid()))
//	        {
//	          if (xIndex < 2) {
//	            xIndex++;
//	          } else if (zIndex < 2)
//	          {
//	            xIndex = -1;
//	            zIndex++;
//	          }
//	          else {
//	            return;
//	          }
//	          targetBlock = ownerSpot.getBlock().getRelative(xIndex, -1, zIndex);
//	        }
//
//	        if (((Integer)this.failedAttempts.get(playerName)).intValue() > 4)
//	        {
//	          pet.teleport(owner);
//	          this.failedAttempts.put(playerName, Integer.valueOf(0));
//	        }
//	       
//	        else if (!nav.a(targetBlock.getX(), targetBlock.getY() + 1, targetBlock.getZ(), 1.5D))
//	        {
//	          if (pet.getFallDistance() == 0.0F)
//	          {
//	            this.failedAttempts.put(playerName, Integer.valueOf(((Integer)this.failedAttempts.get(playerName)).intValue() + 1));
//	          }
//	        }
//	        else
//	        {
//	          this.failedAttempts.put(playerName, Integer.valueOf(0));
//	        }
//	      }
//	    }
	  }
	  
	  public boolean moveEntity(Entity e,Location l,float speed){
		   if(((CraftEntity)e).getHandle() instanceof EntityCreature || ((CraftEntity)e) instanceof CraftCreature || e instanceof CraftCreature){
		     EntityCreature entity = ((CraftCreature) e).getHandle();
		     Navigation nav = entity.getNavigation();
		     nav.a(true);
		     PathEntity path = nav.a(l.getX(),l.getY(),l.getZ());
		     entity.pathEntity = path;
		     return nav.a(path, speed);
		   }else if(((CraftEntity)e).getHandle() instanceof EntityInsentient){
		    EntityInsentient ei = ((EntityInsentient)((CraftEntity) e).getHandle());
		    Navigation nav = ei.getNavigation();
		    nav.a(true);
		    ei.getControllerMove().a(l.getX(),l.getY(),l.getZ(),(float)speed);
		    return nav.a(l.getX(),l.getY(),l.getZ(),(float)speed);
		   }else if(((CraftEntity)e).getHandle() instanceof EntityLiving){
		    EntityLiving entity = (EntityLiving)((CraftEntity)e).getHandle();
		    entity.move(l.getX(), l.getY(),l.getZ());
		    entity.e((float)l.getX(), (float)l.getZ());
		    return true;
		   }else{
		    System.out.println("Keiner");
		    return false;
		   }
		  }
	 
	  @EventHandler
	  public void onUpdate(UpdateEvent event){
	    if (event.getType() != UpdateType.FASTER)return;
	    
	    for(Creature pet : petToLocation.keySet()){
	    	petSpot=pet.getLocation();
	    	ownerSpot=(Location)petToLocation.get(pet);
	    	
	    	xDiff = Math.abs(petSpot.getBlockX() - ownerSpot.getBlockX());
		    yDiff = Math.abs(petSpot.getBlockY() - ownerSpot.getBlockY());
		    zDiff = Math.abs(petSpot.getBlockZ() - ownerSpot.getBlockZ());
	    	
		    if(xDiff + yDiff + zDiff > 4){
		    	ec = ((CraftCreature)pet).getHandle();
		        nav = ec.getNavigation();
		    	xIndex = -1;
		        zIndex = -1;
		        targetBlock = ownerSpot.getBlock().getRelative(xIndex, -1, zIndex);
		        while ((targetBlock.isEmpty()) || (targetBlock.isLiquid())){
		          if (xIndex < 2) {
		            xIndex++;
		          } else if (zIndex < 2){
		            xIndex = -1;
		            zIndex++;
		          }else {
		            break;
		          }
		          targetBlock = ownerSpot.getBlock().getRelative(xIndex, -1, zIndex);
		        }
		        
		        if (((Integer)this.failedAttemptsToLocation.get(pet)).intValue() > 6){
		          pet.teleport(ownerSpot);
		          Bukkit.getPluginManager().callEvent(new PetWithOutOwnerLocationEvent(pet,ownerSpot));
		          this.failedAttemptsToLocation.put(pet, Integer.valueOf(0));
		        }else if (!nav.a(targetBlock.getX(), targetBlock.getY() + 1, targetBlock.getZ(), 2.0D)){
		          if (pet.getFallDistance() == 0.0F||pet.getLocation().distance(ownerSpot)>8){
		            this.failedAttemptsToLocation.put(pet, Integer.valueOf(((Integer)this.failedAttemptsToLocation.get(pet)).intValue() + 1));
		          }
		        }else{
		          Bukkit.getPluginManager().callEvent(new PetWithOutOwnerLocationEvent(pet,ownerSpot));
		          this.failedAttemptsToLocation.put(pet, Integer.valueOf(0));
		        }
		    }
	    }    
//	    Iterator ownerIterator = this.petToLocation.keySet().iterator();
//	    while (ownerIterator.hasNext())
//	    {
//	      Creature pet = (Creature)ownerIterator.next();
//
//	      Location ownerSpot = (Location)this.petToLocation.get(pet);
//	      Location petSpot = pet.getLocation();
//	      
//	      if(petSpot.distance(ownerSpot) > 6){
//	    	  pet.teleport(ownerSpot);
//	      }
//	      
//	      int xDiff = Math.abs(petSpot.getBlockX() - ownerSpot.getBlockX());
//	      int yDiff = Math.abs(petSpot.getBlockY() - ownerSpot.getBlockY());
//	      int zDiff = Math.abs(petSpot.getBlockZ() - ownerSpot.getBlockZ());
//
//	      if (xDiff + yDiff + zDiff > 4)
//	      {
//	        EntityCreature ec = ((CraftCreature)pet).getHandle();
//	        nav = ec.getNavigation();
//	        int xIndex = -1;
//	        int zIndex = -1;
//	        Block targetBlock = ownerSpot.getBlock().getRelative(xIndex, -1, zIndex);
//	        while ((targetBlock.isEmpty()) || (targetBlock.isLiquid()))
//	        {
//	          if (xIndex < 2) {
//	            xIndex++;
//	          } else if (zIndex < 2){
//	            xIndex = -1;
//	            zIndex++;
//	          }else {
//	            return;
//	          }
//	          targetBlock = ownerSpot.getBlock().getRelative(xIndex, -1, zIndex);
//	        }
//	        if (((Integer)this.failedAttemptsToLocation.get(pet)).intValue() > 6)
//	        {
//	          pet.teleport(petToLocation.get(pet));
//	          this.failedAttemptsToLocation.put(pet, Integer.valueOf(0));
//	        }
//	        else if (/*!nav.a(petSpot.getX(), petSpot.getY() + 1, petSpot.getZ(), 2.0D)*/!moveEntity(pet,targetBlock.getLocation(),2.0F))
//	        {
//	          if (pet.getFallDistance() == 0.0F)
//	          {
//	            this.failedAttemptsToLocation.put(pet, Integer.valueOf(((Integer)this.failedAttemptsToLocation.get(pet)).intValue() + 1));
//	          }
//	        }
//	        else
//	        {
//	          this.failedAttemptsToLocation.put(pet, Integer.valueOf(0));
//	        }
//	      }
//	    }
	  }
	  
	Creature c;
	@EventHandler
	public void Interdact(PlayerInteractEntityEvent ev){
		 if(isSetting()){
			 if(getActivePetOwners().containsKey(ev.getPlayer().getName())){
				c=getActivePetOwners().get(ev.getPlayer().getName());
				if(c.getEntityId()==ev.getRightClicked().getEntityId()){
					if(getSetting_list().containsKey(c.getType())){
						ev.getPlayer().openInventory( getSetting_list().get(c.getType()).getMain() );
					}
				}
			 }
		 }
	}
	
	@EventHandler
	public void onEntityTarget(EntityTargetEvent ev){
		if (((ev.getEntity() instanceof org.bukkit.entity.Creature)) && (this.activePetOwners.containsValue((org.bukkit.entity.Creature)ev.getEntity()))){
	      ev.setCancelled(true);
		}
//		else if (((ev.getEntity() instanceof org.bukkit.entity.Creature)) && (this.petToLocation.containsKey((org.bukkit.entity.Creature)ev.getEntity()))){
//		  ev.setCancelled(true);
//		}
	}
	
}
