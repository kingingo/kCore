package me.kingingo.kcore.Util;

import net.minecraft.server.v1_7_R3.EntityPlayer;
import net.minecraft.server.v1_7_R3.MinecraftServer;
import net.minecraft.server.v1_7_R3.PlayerInteractManager;
import net.minecraft.server.v1_7_R3.WorldServer;
import net.minecraft.util.com.mojang.authlib.GameProfile;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R3.CraftServer;
import org.bukkit.craftbukkit.v1_7_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_7_R3.scoreboard.CraftScoreboard;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;

public class VirtualPlayer extends CraftPlayer
{
  Player keepInformed;
  boolean online = true;
  double health = 20.0D;
  boolean isop = true;
  static boolean enableMessages = true;
  boolean showMessages = true;
  boolean showTeleports = true;
  GameMode gamemode = GameMode.SURVIVAL;
  Location loc;
  CraftScoreboard scoreboard;

  public VirtualPlayer(CraftServer cserver, MinecraftServer mcserver, WorldServer worldServer, GameProfile gameProfile, PlayerInteractManager pim)
  {
    super(cserver, new EntityPlayer(mcserver, worldServer, gameProfile, pim));
    this.loc = getLocation();
  }

  public VirtualPlayer(CraftServer cserver, EntityPlayer ep)
  {
    super(cserver, ep);
    this.loc = getLocation();
  }

  public InventoryView openInventory(Inventory inv)
  {
    return null;
  }

  public void removePotionEffect(PotionEffectType effect)
  {
  }

  public void closeInventory()
  {
  }

  public void updateInventory()
  {
  }

  public void setGameMode(GameMode gamemode)
  {
    try
    {
      super.setGameMode(gamemode);
    }
    catch (Exception e) {
    }
    this.gamemode = gamemode;
  }

  public GameMode getGameMode()
  {
    return this.gamemode;
  }

//  public double getHealth()
//  {
//    return (double) this.health;
//  }

  public void setHealth(double h)
  {
    if (h < 0.0D) h = 0.0D;
    this.health = h;
    try { super.setHealth(h); } catch (Exception e) {
    }try { getHandle().setHealth((float)h); } catch (Exception e) { e.printStackTrace(); }

  }

  public boolean isDead()
  {
    return (super.isDead()) || (this.health <= 0.0D);
  }

  public void sendMessage(String s)
  {
    //if ((this.showMessages) && (enableMessages))
     // Util.sendMessage(this, (!isOnline() ? "&4(Offline)&b" : "") + getName() + " gettingMessage= " + s);
  }

  public void moveTo(Location loc)
  {
    this.entity.move(loc.getX(), loc.getY(), loc.getZ());
  }

  
  
  public boolean teleport(Location l, boolean respawn)
  {
    if (isDead())
      return false;
    try
    {
      boolean changedWorlds = !this.loc.getWorld().getName().equals(l.getWorld().getName());

      String teleporting = respawn ? "respawning" : "teleporting";
      if ((this.showTeleports) && (enableMessages))
      {
        String fromWorld = "";
        String toWorld = "";
        if (changedWorlds)
        {
          fromWorld = "&5" + this.loc.getWorld().getName() + "&4,";
          toWorld = "&5" + l.getWorld().getName() + "&4,";
        }
        //Util.sendMessage(this, getName() + "&e " + teleporting + " from &4" + fromWorld + UtilLocation.getLocString(this.loc) + " &e-> &4" + toWorld + UtilLocation.getLocString(l));
      }

      this.loc = l.clone();
      if (changedWorlds)
      {
        PlayerChangedWorldEvent pcwe = new PlayerChangedWorldEvent(this, l.getWorld());

        CraftServer cserver = (CraftServer)Bukkit.getServer();
        cserver.getPluginManager().callEvent(pcwe);

        this.entity.world = ((CraftWorld)this.loc.getWorld()).getHandle();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return true;
  }

  public boolean teleport(Location location, PlayerTeleportEvent.TeleportCause cause)
  {
    if (isDead())
      return false;
    super.teleport(location, cause);
    teleport(location, false);
    return true;
  }

  public boolean teleport(Location l)
  {
    return teleport(l, PlayerTeleportEvent.TeleportCause.UNKNOWN);
  }

  public void respawn(Location loc)
  {
    this.health = 20.0D;
    boolean changedWorlds = !this.loc.getWorld().getName().equals(loc.getWorld().getName());

    CraftServer cserver = (CraftServer)Bukkit.getServer();
    PlayerRespawnEvent respawnEvent = new PlayerRespawnEvent(this, loc, false);

    cserver.getPluginManager().callEvent(respawnEvent);
    if (changedWorlds)
    {
      PlayerChangedWorldEvent pcwe = new PlayerChangedWorldEvent(this, loc.getWorld());

      cserver.getPluginManager().callEvent(pcwe);
    }
  }

  public Location getLocation()
  {
    return this.loc;
  }

  public boolean isOnline()
  {
    return this.online;
  }

  public void setOnline(boolean b)
  {
//    if (enableMessages) {
//      Util.sendMessage(this, getName() + " is " + (b ? "connecting" : "disconnecting"));
//    }
    this.online = b;
  }

  public boolean isOp()
  {
    return this.isop;
  }

  public void setOp(boolean b)
  {
    this.isop = b;
  }

  public String toString()
  {
    String world = "&5" + this.loc.getWorld().getName() + ",";
    return getName() + "&e h=&2" + getHealth() + "&e o=&5" + isOnline() + "&e d=&7" + isDead() + "&e loc=&4" + world + "&4" + UtilLocation.getLocString(this.loc) + "&e gm=&8" + getGameMode();
  }

  public void setScoreboard(Scoreboard scoreboard)
  {
//    Object s = null;
//    this.scoreboard = ((CraftScoreboard)scoreboard);
//    if (scoreboard != null) {
//      if ((Bukkit.getScoreboardManager().getMainScoreboard() != null) && (scoreboard.equals(Bukkit.getScoreboardManager().getMainScoreboard())))
//      {
//        s = "BukkitMainScoreboard";
//      } else if (scoreboard.getObjective(DisplaySlot.SIDEBAR) != null)
//        s = scoreboard.getObjective(DisplaySlot.SIDEBAR).getName();
//      else if (scoreboard.getObjective(DisplaySlot.PLAYER_LIST) != null) {
//        s = scoreboard.getObjective(DisplaySlot.PLAYER_LIST).getName();
//      }
//    }
//    if (enableMessages)
//      Util.sendMessage(this, getName() + " setting scoreboard " + s);
  }

  public CraftScoreboard getScoreboard()
  {
    return this.scoreboard;
  }

  public void setLocation(Location l) {
    this.loc = l;
  }

  public Player getInformed()
  {
    return this.keepInformed;
  }

  public static void setGlobalMessages(boolean enable) {
    enableMessages = enable;
  }
}