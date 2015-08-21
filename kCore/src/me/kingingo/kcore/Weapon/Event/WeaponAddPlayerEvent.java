package me.kingingo.kcore.Weapon.Event;

import lombok.Getter;
import me.kingingo.kcore.Weapon.Weapon;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class WeaponAddPlayerEvent extends Event
{
  private static final HandlerList handlers = new HandlerList();
  
  @Getter
  Player player;
  @Getter
  Weapon weapon;
  @Getter
  int pos;
  
  public WeaponAddPlayerEvent(Player player, Weapon weapon, int pos){
	  this.player=player;
	  this.weapon=weapon;
	  this.pos=pos;
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
