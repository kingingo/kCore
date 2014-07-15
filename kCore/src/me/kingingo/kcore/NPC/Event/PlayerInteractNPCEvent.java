package me.kingingo.kcore.NPC.Event;

import lombok.Getter;
import me.kingingo.kcore.NPC.NPC;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerInteractNPCEvent extends Event
{
	  private static final HandlerList handlers = new HandlerList();
	  @Getter
	  private Player player;
	  @Getter
	  private NPC npc;

	  public PlayerInteractNPCEvent(Player p,NPC npc)
	  {
	    this.player=p;
	    this.npc=npc;
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
