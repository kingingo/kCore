package eu.epicpvp.kcore.Util;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.player.PlayerInteractEvent;

import net.minecraft.server.v1_8_R3.PacketPlayInFlying.PacketPlayInPosition;

public class UtilEvent
{
  public static boolean movedTooQuickly(PacketPlayInPosition packet,Player player){
//	  EntityPlayer eplayer = (EntityPlayer)UtilPlayer.getCraftPlayer(player);
	  
	  
	  return false;
  }
	
  public static boolean isAction(PlayerInteractEvent event, ActionType action)
  {
    if (action == ActionType.LEFT) {
      return (event.getAction() == Action.LEFT_CLICK_AIR) || (event.getAction() == Action.LEFT_CLICK_BLOCK);
    }
    if (action == ActionType.LEFT_AIR) {
      return event.getAction() == Action.LEFT_CLICK_AIR;
    }
    if (action == ActionType.LEFT_BLOCK) {
      return event.getAction() == Action.LEFT_CLICK_BLOCK;
    }
    if (action == ActionType.RIGHT) {
      return (event.getAction() == Action.RIGHT_CLICK_AIR) || (event.getAction() == Action.RIGHT_CLICK_BLOCK);
    }
    if (action == ActionType.RIGHT_AIR) {
      return event.getAction() == Action.RIGHT_CLICK_AIR;
    }
    if (action == ActionType.RIGHT_BLOCK) {
      return event.getAction() == Action.RIGHT_CLICK_BLOCK;
    }
    if (action == ActionType.BLOCK) {
        return (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_BLOCK);
    }
    if (action == ActionType.AIR) {
        return (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_AIR);
    }
    if(action == ActionType.PHYSICAL){
    	return (event.getAction() == Action.PHYSICAL);
    }
    return false;
  }

  public static LivingEntity GetDamagerEntity(EntityDamageEvent event, boolean ranged)
  {
    if (!(event instanceof EntityDamageByEntityEvent)) {
      return null;
    }
    EntityDamageByEntityEvent eventEE = (EntityDamageByEntityEvent)event;

    if ((eventEE.getDamager() instanceof LivingEntity)) {
      return (LivingEntity)eventEE.getDamager();
    }
    if (!ranged) {
      return null;
    }
    if (!(eventEE.getDamager() instanceof Projectile)) {
      return null;
    }
    Projectile projectile = (Projectile)eventEE.getDamager();

    if (projectile.getShooter() == null) {
      return null;
    }
    if (!(projectile.getShooter() instanceof LivingEntity)) {
      return null;
    }
    return (LivingEntity)projectile.getShooter();
  }
  
  public static ActionType getActionType(ClickType type){
	  switch(type){
	  case CONTROL_DROP: return ActionType.CONTROL_DROP;
	  case CREATIVE: return ActionType.CREATIVE;
	  case DOUBLE_CLICK: return ActionType.DOUBLE_CLICK;
	  case DROP: return ActionType.DROP;
	  case LEFT: return ActionType.LEFT;
	  case RIGHT: return ActionType.RIGHT;
	  case MIDDLE: return ActionType.MIDDLE;
	  case NUMBER_KEY: return ActionType.NUMBER_KEY;
	  case SHIFT_LEFT: return ActionType.SHIFT_LEFT;
	  case SHIFT_RIGHT: return ActionType.SHIFT_RIGHT;
	  case UNKNOWN: return ActionType.UNKOWN;
	  case WINDOW_BORDER_LEFT: return ActionType.WINDOW_BORDER_LEFT;
	  case WINDOW_BORDER_RIGHT: return ActionType.WINDOW_BORDER_RIGHT;
	  default: return ActionType.UNKOWN;
	  }
  }

  public static enum ActionType
  {
	CONTROL_DROP,
	DROP,
	CREATIVE,
	DOUBLE_CLICK,
	MIDDLE,
	NUMBER_KEY,
	SHIFT_LEFT,
	SHIFT_RIGHT,
	UNKOWN,
	WINDOW_BORDER_RIGHT,
	WINDOW_BORDER_LEFT,
	PHYSICAL,
    LEFT, 
    LEFT_AIR, 
    LEFT_BLOCK, 
    BLOCK,
    AIR,
    RIGHT, 
    RIGHT_AIR, 
    RIGHT_BLOCK;
  }
}