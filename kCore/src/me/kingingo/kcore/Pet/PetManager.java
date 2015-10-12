package me.kingingo.kcore.Pet;

import java.util.HashMap;
import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.Util.UtilEnt;
import me.kingingo.kcore.Pet.Setting.PetSetting;
import me.kingingo.kcore.Pet.Shop.PlayerPetHandler;
import me.kingingo.kcore.Pet.Events.PetCreateEvent;
import me.kingingo.kcore.Pet.Events.PetWithOutOwnerLocationEvent;
import me.kingingo.kcore.Update.UpdateType;
import me.kingingo.kcore.Update.Event.UpdateEvent;
import net.minecraft.server.v1_8_R3.EntityCreature;
import net.minecraft.server.v1_8_R3.NavigationAbstract;
import org.bukkit.entity.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftCreature;
import org.bukkit.block.Block;
import org.bukkit.entity.Creature;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
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
	@Getter
	private boolean setting=false;
	@Getter
	private HashMap<EntityType,PetSetting> setting_list;
	@Getter
	@Setter
	private boolean EntityDamageEvent = false;
	@Getter
	@Setter
	double distance=8;
	@Getter
	@Setter
	private PlayerPetHandler handler; 
	
	public PetManager(JavaPlugin instance){
		Bukkit.getPluginManager().registerEvents(this, instance);
		this.instance=instance;
		this.failedAttempts = new HashMap<>();
		this.failedAttemptsToLocation = new HashMap<>();
		this.petToLocation = new HashMap<>();
		this.activePetOwners = new HashMap<>();
	}
	
	public boolean isPet(Creature c){
		for(Creature cs : this.activePetOwners.values())if(cs.getEntityId() == c.getEntityId())return true;
		
		return false;
	}
	
	public void setSetting(boolean b){
		setting=b;
		if(setting){
			setting_list=new HashMap<EntityType,PetSetting>();
		}
	}
	
	public void RemovePet(Player player, boolean removeOwner)
	  {
	    if (this.activePetOwners.containsKey(player.getName().toLowerCase()))
	    {
	      org.bukkit.entity.Creature pet = (org.bukkit.entity.Creature)this.activePetOwners.get(player.getName().toLowerCase());
	      if(pet.getPassenger()!=null&&pet.getPassenger().getType() != EntityType.PLAYER){
	    	  Entity e = pet.getPassenger();
	    	  e.leaveVehicle();
	    	  e.remove();
	      }
	      pet.remove();

	      if (removeOwner)
	      {
	        this.activePetOwners.remove(player.getName().toLowerCase());
	        this.failedAttempts.remove(player.getName().toLowerCase());
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
	
	public Creature AddPetWithOutOwner(String name,boolean clear_goal, EntityType entityType, Location location){
		location.getWorld().loadChunk(location.getWorld().getChunkAt(location));
	    Creature pet =(Creature) location.getWorld().spawnEntity(location, entityType);
	    pet.setCustomNameVisible(true);
	    pet.setCustomName(name);
	    this.petToLocation.put(pet,location);
	    this.failedAttemptsToLocation.put(pet,Integer.valueOf(0));
	    if(clear_goal)UtilEnt.ClearGoals(pet);
	    Bukkit.getPluginManager().callEvent(new PetCreateEvent(this,pet,null));
	    return pet;
    }
	
	public void AddPetOwner(Player player,String name, EntityType entityType, Location location)
	  {
	    if (this.activePetOwners.containsKey(player.getName().toLowerCase()))
	    {
	      if (((org.bukkit.entity.Creature)this.activePetOwners.get(player.getName().toLowerCase())).getType() != entityType ||
	    		  (((org.bukkit.entity.Creature)this.activePetOwners.get(player.getName().toLowerCase())).getPassenger()!=null&& ((org.bukkit.entity.Creature)this.activePetOwners.get(player.getName().toLowerCase())).getPassenger().getType() != entityType) )
	      {
	        RemovePet(player, true);
	      }else {
	        return;
	      }
	    }
	    org.bukkit.entity.Creature pet = (org.bukkit.entity.Creature)location.getWorld().spawnEntity(location, entityType);
	    pet.setCustomNameVisible(true);
	    pet.setCustomName(name);

	    this.activePetOwners.put(player.getName().toLowerCase(),pet);
	    this.failedAttempts.put(player.getName().toLowerCase(), Integer.valueOf(0));
	    UtilEnt.ClearGoals(pet);
	    Bukkit.getPluginManager().callEvent(new PetCreateEvent(this,pet,player));
	  }
	
	public boolean hasPlayer(Player player){
		return this.activePetOwners.containsKey(player.getName().toLowerCase());
	}
	
	public org.bukkit.entity.Creature GetPet(Player player){
	    return (org.bukkit.entity.Creature)this.activePetOwners.get(player.getName().toLowerCase());
	}
	
	  @EventHandler(priority=EventPriority.LOWEST)
	  public void onEntityDamage(EntityDamageEvent event)
	  {
		if(EntityDamageEvent)return;
	    if (((event.getEntity() instanceof org.bukkit.entity.Creature)) && (this.petToLocation.containsKey((org.bukkit.entity.Creature)event.getEntity()))){
	      event.setCancelled(true);
	    }
	  }
	  
	  Player owner;
	  Entity passenger;
	  Creature pet;
	  EntityCreature ec;
	  NavigationAbstract nav;
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
	    	
	    	if(!petSpot.getWorld().getName().equalsIgnoreCase(ownerSpot.getWorld().getName()))pet.teleport(owner);
	    	
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
	 	    	  if(pet.getPassenger() != null){
		        	  passenger = pet.getPassenger();
		        	  passenger.leaveVehicle();
		        	  
		        	  passenger.teleport(owner);
		        	  pet.teleport(owner);
		        	  pet.setPassenger(passenger);
		          }else{
		        	  pet.teleport(owner);
		          }
	 	    	  
		          this.failedAttempts.put(playerName, Integer.valueOf(0));
	 	      }else if (!nav.a(targetBlock.getX(), targetBlock.getY() + 1, targetBlock.getZ(), 1.2D)){
		          if (pet.getFallDistance() == 0.0F||pet.getLocation().distance(ownerSpot)>distance){
		            this.failedAttempts.put(playerName, Integer.valueOf(((Integer)this.failedAttempts.get(playerName)).intValue() + 1));
		          }
	 	       }else{
		          this.failedAttempts.put(playerName, Integer.valueOf(0));
		       }
	    	}
	    }
	  }
	  
//	  public boolean moveEntity(Entity e,Location l,float speed){
//		   if(((CraftEntity)e).getHandle() instanceof EntityCreature || ((CraftEntity)e) instanceof CraftCreature || e instanceof CraftCreature){
//		     EntityCreature entity = ((CraftCreature) e).getHandle();
//		     NavigationAbstract nav = entity.getNavigation();
//		     nav.a(true);
//		     PathEntity path = nav.a(l.getX(),l.getY(),l.getZ());
//		     entity.pathEntity = path;
//		     return nav.a(path, speed);
//		   }else if(((CraftEntity)e).getHandle() instanceof EntityInsentient){
//		    EntityInsentient ei = ((EntityInsentient)((CraftEntity) e).getHandle());
//		    NavigationAbstract nav = ei.getNavigation();
//		    nav.a(true);
//		    ei.getControllerMove().a(l.getX(),l.getY(),l.getZ(),(float)speed);
//		    return nav.a(l.getX(),l.getY(),l.getZ(),(float)speed);
//		   }else if(((CraftEntity)e).getHandle() instanceof EntityLiving){
//		    EntityLiving entity = (EntityLiving)((CraftEntity)e).getHandle();
//		    entity.move(l.getX(), l.getY(),l.getZ());
//		    entity.e((float)l.getX(), (float)l.getZ());
//		    return true;
//		   }else{
//		    System.out.println("Keiner");
//		    return false;
//		   }
//		  }
	 
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
		          if(pet.getPassenger() != null){
		        	  passenger = pet.getPassenger();
		        	  passenger.leaveVehicle();
		        	  
		        	  passenger.teleport(ownerSpot);
		        	  pet.teleport(ownerSpot);
		        	  pet.setPassenger(passenger);
		          }else{
		        	  pet.teleport(ownerSpot);
		          }
		          
		          Bukkit.getPluginManager().callEvent(new PetWithOutOwnerLocationEvent(pet,ownerSpot));
		          this.failedAttemptsToLocation.put(pet, Integer.valueOf(0));
		        }else if (!nav.a(targetBlock.getX(), targetBlock.getY() + 1, targetBlock.getZ(), 1.2D)){
		          if (pet.getFallDistance() == 0.0F||pet.getLocation().distance(ownerSpot)>distance){
		            this.failedAttemptsToLocation.put(pet, Integer.valueOf(((Integer)this.failedAttemptsToLocation.get(pet)).intValue() + 1));
		          }
		        }else{
		          Bukkit.getPluginManager().callEvent(new PetWithOutOwnerLocationEvent(pet,ownerSpot));
		          this.failedAttemptsToLocation.put(pet, Integer.valueOf(0));
		        }
		    }
	    }
	  }
	 
	@EventHandler
	public void Creeper(EntityExplodeEvent ev){
		if(ev.getEntity().getType()!=null&&ev.getEntity().getType()!=EntityType.PRIMED_TNT&&ev.getEntity().getType() == EntityType.CREEPER){
			if (((ev.getEntity() instanceof org.bukkit.entity.Creature)) && ((this.activePetOwners.containsValue((org.bukkit.entity.Creature)ev.getEntity())) || (this.petToLocation.containsKey((org.bukkit.entity.Creature)ev.getEntity())) ) ){
			    	ev.setCancelled(true);
			}
		}
	}

	Creature c;
	@EventHandler
	public void Interdact(PlayerInteractEntityEvent ev){
		 if(isSetting()){
			 if(getActivePetOwners().containsKey(ev.getPlayer().getName().toLowerCase())){
				c=getActivePetOwners().get(ev.getPlayer().getName().toLowerCase());
				if(c.getEntityId()==ev.getRightClicked().getEntityId()){
					if(c.getPassenger() == null){
						if(getSetting_list().containsKey(c.getType())){
							ev.getPlayer().openInventory( getSetting_list().get(c.getType()) );
						}
					}
				}
			 }else if(ev.getRightClicked() instanceof Horse){
				 ev.setCancelled(true);
			 }
		 }
	}
	
	@EventHandler
	public void EntityCombust(EntityCombustEvent ev){
		if (((ev.getEntity() instanceof org.bukkit.entity.Creature)) && ((this.activePetOwners.containsValue((org.bukkit.entity.Creature)ev.getEntity())) || (this.petToLocation.containsKey((org.bukkit.entity.Creature)ev.getEntity())) ) ){
			  ev.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onEntityTarget(EntityTargetEvent ev){
		if (((ev.getEntity() instanceof org.bukkit.entity.Creature)) && ((this.activePetOwners.containsValue((org.bukkit.entity.Creature)ev.getEntity())) || (this.petToLocation.containsKey((org.bukkit.entity.Creature)ev.getEntity())) ) ){
				ev.setCancelled(true);
		}
	}
	
}
