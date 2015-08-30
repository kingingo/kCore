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
  private HashSet<Player> _monitoring = new HashSet();

  public LagMeter(CommandHandler handler){
    super(handler.getPlugin(), "LagMeter");
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

  public void sendUpdate(Player player){
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
    player.sendMessage(Language.getText(player,"PREFIX")+ "Tracking-Player-Range: §e"+ ((CraftPlayer)player).getHandle().world.spigotConfig.playerTrackingRange );
    player.sendMessage(Language.getText(player,"PREFIX")+ "View-Distance: §e"+ ((CraftPlayer)player).getHandle().world.spigotConfig.viewDistance );
    player.sendMessage(Language.getText(player,"PREFIX")+ "Your-Ping: §e"+ UtilPlayer.getPlayerPing(player));
    player.sendMessage(Language.getText(player,"PREFIX")+ "Time: §e"+ UtilTime.now());
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