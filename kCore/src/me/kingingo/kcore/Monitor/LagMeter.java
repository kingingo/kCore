package me.kingingo.kcore.Monitor;
import java.util.HashSet;

import lombok.Getter;
import me.kingingo.kcore.Command.CommandHandler;
import me.kingingo.kcore.Command.Admin.CommandEntities;
import me.kingingo.kcore.Command.Admin.CommandLagg;
import me.kingingo.kcore.Command.Admin.CommandMemFix;
import me.kingingo.kcore.Command.Admin.CommandMonitor;
import me.kingingo.kcore.Command.Admin.CommandUnloadChunks;
import me.kingingo.kcore.Command.Commands.CommandPing;
import me.kingingo.kcore.Language.Language;
import me.kingingo.kcore.Listener.kListener;
import me.kingingo.kcore.Permission.kPermission;
import me.kingingo.kcore.Update.UpdateType;
import me.kingingo.kcore.Update.Event.UpdateEvent;
import me.kingingo.kcore.Util.UtilPlayer;
import me.kingingo.kcore.Util.UtilServer;
import me.kingingo.kcore.Util.UtilTime;

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

import com.earth2me.essentials.commands.Commandunban;

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
  private long _startTime;
  @Getter
  private HashSet<Player> _monitoring = new HashSet();

  public LagMeter(CommandHandler handler){
    super(handler.getPlugin(), "LagMeter");
    this._startTime = System.currentTimeMillis();
    this._lastRun = System.currentTimeMillis();
    this._lastAverage = System.currentTimeMillis();
    handler.register(CommandLagg.class, new CommandLagg());
    handler.register(CommandMonitor.class, new CommandMonitor());
    handler.register(CommandPing.class, new CommandPing());
    handler.register(CommandUnloadChunks.class, new CommandUnloadChunks());
    handler.register(CommandEntities.class, new CommandEntities());
    handler.register(CommandMemFix.class, new CommandMemFix());
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

  public void sendUpdates(){ 
    for (Player player : this._monitoring){
      sendUpdate(player);
    }
  }
  
  public void sendUpdate(){
	  	System.out.println("Online-Players: "+ UtilServer.getPlayers().size());
	  	System.out.println("Live: " + String.format("%.00f", new Object[] { Double.valueOf(this._ticksPerSecond) }) + " Avg: " + String.format("%.00f", new Object[] { Double.valueOf(this._ticksPerSecondAverage * 20.0D) }));
	    System.out.println("Free-Mem: " + Runtime.getRuntime().freeMemory() / 1048576L + "MB Max-Mem: "+Runtime.getRuntime().maxMemory() / 1048576L+ "MB");
	    System.out.println("Online-Time: "+ UtilTime.formatMili( (System.currentTimeMillis()-this._startTime) ));
	    System.out.println("Time-Now: "+ UtilTime.now());
	    System.out.println("Worlds:");
	    
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
	        System.out.println("       "+world.getName()+": Chunks:"+world.getLoadedChunks().length+" Entities:"+world.getEntities().size()+" Tile:"+tileEntities);
	    }
	  }

  public void sendUpdate(Player player){
    player.sendMessage(" ");
    player.sendMessage(" ");
    player.sendMessage(" ");
    player.sendMessage(" ");
    player.sendMessage(" ");
    player.sendMessage(Language.getText(player,"PREFIX")+"Online-Players: §e"+ UtilServer.getPlayers().size() + " §7Your-Ping: §e"+ UtilPlayer.getPlayerPing(player));
    player.sendMessage(Language.getText(player,"PREFIX")+"Live: §e" + String.format("%.00f", new Object[] { Double.valueOf(this._ticksPerSecond) }) + " §7Avg:§e " + String.format("%.00f", new Object[] { Double.valueOf(this._ticksPerSecondAverage * 20.0D) }));
    player.sendMessage(Language.getText(player,"PREFIX")+"Free-Mem:§e " + Runtime.getRuntime().freeMemory() / 1048576L + "MB §7Max-Mem: §e"+Runtime.getRuntime().maxMemory() / 1048576L+ "MB");
    player.sendMessage(Language.getText(player,"PREFIX")+"View-Distance: §e"+ ((CraftPlayer)player).getHandle().world.spigotConfig.viewDistance+ "§7 Tracking-Player-Range: §e"+ ((CraftPlayer)player).getHandle().world.spigotConfig.playerTrackingRange );
    player.sendMessage(Language.getText(player,"PREFIX")+ "Online-Time: §e"+ UtilTime.formatMili( (System.currentTimeMillis()-this._startTime) ));
    player.sendMessage(Language.getText(player, "PREFIX")+"Time-Now: §e"+ UtilTime.now());
    player.sendMessage(Language.getText(player,"PREFIX")+ "Worlds:");
    
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
    	player.sendMessage(Language.getText(player,"PREFIX")+"       §e"+world.getName()+"§7: Chunks:§e"+world.getLoadedChunks().length+" §7Entities:§e"+world.getEntities().size()+" §7Tile:§e"+tileEntities);
    }
  }
}