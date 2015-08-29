package me.kingingo.kcore.Monitor;
import java.util.HashSet;

import lombok.Getter;
import me.kingingo.kcore.Language.Language;
import me.kingingo.kcore.Listener.kListener;
import me.kingingo.kcore.Permission.kPermission;
import me.kingingo.kcore.Update.UpdateType;
import me.kingingo.kcore.Update.Event.UpdateEvent;
import me.kingingo.kcore.Util.UtilServer;
import net.minecraft.server.v1_8_R3.EntityPlayer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class LagMeter extends kListener
{
  @Getter
  private long _lastRun = -1L;
  @Getter
  private int _count;
  private double _ticksPerSecond;
  @Getter
  private double _ticksPerSecondAverage;
  @Getter
  private long _lastAverage;
  @Getter
  private HashSet<Player> _monitoring = new HashSet();

  public LagMeter(JavaPlugin plugin)
  {
    super(plugin, "LagMeter");
    this._lastRun = System.currentTimeMillis();
    this._lastAverage = System.currentTimeMillis();
  }

  @EventHandler
  public void onPlayerCommandPreProcess(PlayerCommandPreprocessEvent event)
  {
    if (event.getPlayer().hasPermission(kPermission.MONITOR.getPermissionToString()))
    {
      if (event.getMessage().trim().equalsIgnoreCase("/lag"))
      {
        sendUpdate(event.getPlayer());
        event.setCancelled(true);
      }
      else if (event.getMessage().trim().equalsIgnoreCase("/monitor"))
      {
        if (this._monitoring.contains(event.getPlayer()))
          this._monitoring.remove(event.getPlayer());
        else {
          this._monitoring.add(event.getPlayer());
        }
        event.setCancelled(true);
      }else if (event.getMessage().trim().equalsIgnoreCase("/clearentities")){
    	  int a = 0;
          for(World world : Bukkit.getWorlds()){
        	  for(Entity e : world.getEntities()){
        		  if(!(e instanceof Player)){
        			  e.remove();
        			  a++;
        		  }
        	  }
          }
          event.getPlayer().sendMessage(Language.getText(event.getPlayer(), "PREFIX")+" clear Entities: "+a);
          event.setCancelled(true);
        }else if (event.getMessage().trim().equalsIgnoreCase("/unloadchunks"))
          {
          	  int a = 0;
                for(World world : Bukkit.getWorlds()){
                	for(Chunk ch : world.getLoadedChunks()){
                		if(ch.unload(true, true)){
                			a++;
                		}
                	}
                }
                
                event.getPlayer().sendMessage(Language.getText(event.getPlayer(), "PREFIX")+" unloaded Chunks: "+a);
                event.setCancelled(true);
              }
    }
  }

  @EventHandler
  public void playerQuit(PlayerQuitEvent event)
  {
    this._monitoring.remove(event.getPlayer());
  }

  @EventHandler
  public void update(UpdateEvent event){
    if (event.getType() == UpdateType.SEC) {
    
    long now = System.currentTimeMillis();
    this._ticksPerSecond = (1000.0D / (now - this._lastRun) * 20.0D);

    sendUpdates();

    if (this._count % 30 == 0)
    {
      this._ticksPerSecondAverage = (30000.0D / (now - this._lastAverage) * 20.0D);
      this._lastAverage = now;
    }

    this._lastRun = now;

    this._count += 1;
    }
  }

  public double getTicksPerSecond()
  {
    return this._ticksPerSecond;
  }

  private void sendUpdates()
  { 
    for (Player player : this._monitoring)
    {
      sendUpdate(player);
    }
  }

  private void sendUpdate(Player player){
    player.sendMessage(" ");
    player.sendMessage(" ");
    player.sendMessage(" ");
    player.sendMessage(" ");
    player.sendMessage(" ");
    player.sendMessage(Language.getText(player,"PREFIX")+ ChatColor.GRAY + "Online-Players: " + ChatColor.YELLOW + UtilServer.getPlayers().size());
    player.sendMessage(Language.getText(player,"PREFIX")+ ChatColor.GRAY + "Live: " + ChatColor.YELLOW + String.format("%.00f", new Object[] { Double.valueOf(this._ticksPerSecond) }));
    player.sendMessage(Language.getText(player,"PREFIX")+  ChatColor.GRAY + "Avg: " + ChatColor.YELLOW + String.format("%.00f", new Object[] { Double.valueOf(this._ticksPerSecondAverage * 20.0D) }));
    player.sendMessage(Language.getText(player,"PREFIX")+ ChatColor.GRAY + "Free-Mem: " + ChatColor.YELLOW + Runtime.getRuntime().freeMemory() / 1048576L + "MB");
    player.sendMessage(Language.getText(player,"PREFIX")+ new StringBuilder().append(ChatColor.GRAY).append("Max-Mem: ").append(ChatColor.YELLOW).append(Runtime.getRuntime().maxMemory() / 1048576L).toString() + "MB");
    player.sendMessage(Language.getText(player,"PREFIX")+ "Tracking-Player-Range: §7"+ ((CraftPlayer)player).getHandle().world.spigotConfig.playerTrackingRange );
    player.sendMessage(Language.getText(player,"PREFIX")+ "View-Distance: §7"+ ((CraftPlayer)player).getHandle().world.spigotConfig.viewDistance );
    
    for(World world : Bukkit.getWorlds()){
    	int tileEntities = 0;
        try
        {
          for (Chunk chunk : world.getLoadedChunks())
          {
            tileEntities += chunk.getTileEntities().length;
          }
        }
        catch (ClassCastException ex)
        {
      	 ex.printStackTrace(); 
        }  
    	player.sendMessage(Language.getText(player,"PREFIX")+ ChatColor.GRAY + "World §7"+world.getName()+"§7: Chunks:§e"+world.getLoadedChunks().length+" §7Entities:§e "+world.getEntities().size()+" §7Tile:§e "+tileEntities);
    }
  }
}