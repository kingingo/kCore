package me.kingingo.kcore.Pet.NEW.Pet;

import java.lang.reflect.Field;

import lombok.Getter;
import me.kingingo.kcore.Pet.NEW.PetManager;
import me.kingingo.kcore.Update.UpdateType;
import me.kingingo.kcore.Update.Event.UpdateEvent;
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
import org.bukkit.entity.Creature;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PetLiving implements Listener {

	@Getter
	private EntityType type;
	@Getter
	private Player player;
	@Getter
	private String name;
	@Getter
	private PetManager petManager;
	private Field _goalSelector;
	private Field _targetSelector;
	
	public PetLiving(PetManager petManager,Player player,String name,EntityType type){
		this.player=player;
		this.name=name;
		this.type=type;
		this.petManager=petManager;
		Bukkit.getPluginManager().registerEvents(this, getPetManager().getInstance());
	}
	
	public void spawn(Location loc){
		Creature pet = (Creature)loc.getWorld().spawnCreature(loc, getType());
		pet.setCustomNameVisible(true);
		pet.setCustomName(getName());
		ClearPetGoals(pet);
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
	
}
