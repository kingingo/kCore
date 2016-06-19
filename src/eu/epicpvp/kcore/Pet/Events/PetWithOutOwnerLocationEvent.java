package eu.epicpvp.kcore.Pet.Events;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import lombok.Getter;

public class PetWithOutOwnerLocationEvent extends Event
{
  private static final HandlerList handlers = new HandlerList();
  @Getter
  private LivingEntity pet;
  @Getter
  private Location location;

  public PetWithOutOwnerLocationEvent(LivingEntity pet,Location location){
	this.pet=pet;
	this.location=location;
  }

  public HandlerList getHandlers()
  {
    return handlers;
  }

  public static HandlerList getHandlerList()
  {
    return handlers;
  }
}
