package eu.epicpvp.kcore.Util;
import java.io.File;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.util.Vector;

import com.google.common.base.Charsets;
import com.mojang.authlib.GameProfile;

import dev.wolveringer.client.Callback;
import dev.wolveringer.client.LoadedPlayer;
import dev.wolveringer.dataserver.gamestats.StatsKey;
import eu.epicpvp.kcore.Enum.Zeichen;
import eu.epicpvp.kcore.PacketAPI.kPacket;
import eu.epicpvp.kcore.PacketAPI.Packets.kPacketPlayOutChat;
import eu.epicpvp.kcore.PacketAPI.Packets.kPacketPlayOutEntityEquipment;
import eu.epicpvp.kcore.Permission.PermissionType;
import eu.epicpvp.kcore.StatsManager.StatsManager;
import eu.epicpvp.kcore.Translation.TranslationHandler;
import eu.epicpvp.kcore.UserDataConfig.UserDataConfig;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PlayerInteractManager;
import us.myles.ViaVersion.api.ViaVersion;
import us.myles.ViaVersion.api.ViaVersionAPI;

public class UtilPlayer
{
	
	public static CraftPlayer getCraftPlayer(LivingEntity player){
		return (CraftPlayer)player;
	}
	
	public static void sendHovbarText(Player player,String text){
		sendPacket(player, new kPacketPlayOutChat(text.replaceAll("&", "§"),kPacketPlayOutChat.ChatMode.HOVBAR));
	}
	
	public static void sendPacket(Player player,Packet packet){
		if(packet==null)return;
		getCraftPlayer(player).getHandle().playerConnection.sendPacket(packet);
	}
	
	public static Map<String, Boolean> getPermissionList(PermissionAttachment attachment){
	    try{
	        Field pField = PermissionAttachment.class.getDeclaredField("permissions");
	        pField.setAccessible(true);
	        
	        return (Map)pField.get(attachment);
	    } catch (Exception e) {
	      throw new RuntimeException(e);
	    }
	}
	
	public static boolean hasPermission(Player p,String perm){
		if(p.hasPermission(perm) || p.hasPermission(PermissionType.ALL_PERMISSION.getPermissionToString()))return true;
		return false;
	}

	public static Player loadPlayer(String playerName){
		return loadPlayer(UtilServer.getClient().getPlayerAndLoad(playerName));
	}

	public static Player loadPlayer(int playerId){
		return loadPlayer(UtilServer.getClient().getPlayerAndLoad(playerId));
	}

	public static Player loadPlayer(LoadedPlayer loadedplayer){
		return loadPlayer(loadedplayer.getUUID());
	}
	
	public static Player loadPlayer(UUID uuid){
	    try
	    {
	      OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
	      if ((player == null) || (!player.hasPlayedBefore())) {
	        return null;
	      }
	      GameProfile profile = new GameProfile(uuid, player.getName());
	      CraftServer cserver = ((CraftServer)Bukkit.getServer());
	      MinecraftServer server = cserver.getServer();

	      EntityPlayer entity = new EntityPlayer(server, server.getWorldServer(0), profile, new PlayerInteractManager(server.getWorldServer(0)));

	      Player target = entity.getBukkitEntity();
	      if (target != null)
	      {
	        target.loadData();

	        return target;
	      }
	    }
	    catch (Exception e) {
	      e.printStackTrace();
	    }

	    return null;
	  }
	
	public static int getPlayerPing(Player player){
		return (int)UtilReflection.getValue("ping", ((EntityPlayer)getCraftPlayer(player).getHandle()));
    }
	
	public static boolean hasPermission(Player p,PermissionType perm){
		if(p.hasPermission(perm.getPermissionToString()) || p.hasPermission(PermissionType.ALL_PERMISSION.getPermissionToString()))return true;
		return false;
	}
	
	public static void Knockback(Location center, Player player,double speed, double high) {
		Vector unitVector = center.toVector().subtract(player.getLocation().toVector()).normalize();
		player.setVelocity(unitVector.multiply(speed));
		unitVector.setY(unitVector.getY() + high);
		player.setVelocity(unitVector);
	}
	
	public static String getPlayerLiveString(Player player){
		String s="§c";
		for(int i = 0; i<getHealth(player); i++){
			s+=Zeichen.BIG_HERZ.getIcon();
		}
		s+="§f";
		for(int i = 0; i<(getMaxHealth(player)-getHealth(player)); i++){
			s+=Zeichen.BIG_HERZ.getIcon();
		}
		return s;
	}
	
	public static void setTab(Player player,String server){
		TabTitle.setHeaderAndFooter(player, "§c§lClashMC.eu §8| §e"+server, "§eTeamSpeak: §7ts.ClashMC.eu §8| §eWebsite: §7www.ClashMC.eu");
	}
	
	public static void setSkyBlockScoreboard(Player player,StatsManager money,StatsManager statsManager, UserDataConfig userData){
		UtilScoreboard.addBoard(player.getScoreboard(),DisplaySlot.SIDEBAR, UtilScoreboard.getScoreboardDisplayname());
		UtilScoreboard.setScore(player.getScoreboard(),TranslationHandler.getText(player, "SCOREBOARD_GEMS"), DisplaySlot.SIDEBAR, 9);
		UtilScoreboard.setScore(player.getScoreboard(),"§7Loading...§e", DisplaySlot.SIDEBAR, 8);
		UtilScoreboard.setScore(player.getScoreboard(),"     ", DisplaySlot.SIDEBAR, 7);
		UtilScoreboard.setScore(player.getScoreboard(),"§eMoney", DisplaySlot.SIDEBAR, 6);
		UtilScoreboard.setScore(player.getScoreboard(),"§7Loading...§c", DisplaySlot.SIDEBAR, 5);
		UtilScoreboard.setScore(player.getScoreboard()," ", DisplaySlot.SIDEBAR, 4);
		UtilScoreboard.setScore(player.getScoreboard(),"§eUserstore-Slots", DisplaySlot.SIDEBAR, 3);
		UtilScoreboard.setScore(player.getScoreboard(),""+(player.isOp()?"§7UNLIMITED":userData.getConfig(player).getInt("Stores")), DisplaySlot.SIDEBAR, 2);
		UtilScoreboard.setScore(player.getScoreboard(),"§8----------------", DisplaySlot.SIDEBAR, 1);
		player.setScoreboard(player.getScoreboard());
		
		statsManager.getAsync(player, StatsKey.MONEY, new Callback<Object>() {
			@Override
			public void call(Object obj) {
				Bukkit.getScheduler().runTask(money.getInstance(), new Runnable() {
					public void run() {
						UtilScoreboard.resetScore(player.getScoreboard(), 5, DisplaySlot.SIDEBAR);
						UtilScoreboard.setScore(player.getScoreboard(),"§1§r§7"+UtilMath.trim(2, ((double)obj))+"$", DisplaySlot.SIDEBAR, 5);
					}
				});
			}
		});
		
		money.getAsync(player, StatsKey.GEMS, new Callback<Object>() {
			@Override
			public void call(Object obj) {
				Bukkit.getScheduler().runTask(money.getInstance(), new Runnable() {
					public void run() {
						UtilScoreboard.resetScore(player.getScoreboard(), 8, DisplaySlot.SIDEBAR);
						UtilScoreboard.setScore(player.getScoreboard(),"§a§r§7"+((int)obj), DisplaySlot.SIDEBAR, 8);
					}
				});
			}
		});
	}
	
	public static void setScoreboardGems(Player player,StatsManager money){
		UtilScoreboard.addBoard(player.getScoreboard(),DisplaySlot.SIDEBAR, UtilScoreboard.getScoreboardDisplayname());
		UtilScoreboard.setScore(player.getScoreboard(),TranslationHandler.getText(player, "SCOREBOARD_GEMS"), DisplaySlot.SIDEBAR, 12);
		UtilScoreboard.setScore(player.getScoreboard(),"§7Loading...§c", DisplaySlot.SIDEBAR, 11);
		UtilScoreboard.setScore(player.getScoreboard(),"     ", DisplaySlot.SIDEBAR, 10);
		UtilScoreboard.setScore(player.getScoreboard(),TranslationHandler.getText(player, "SCOREBOARD_FORUM"), DisplaySlot.SIDEBAR, 9);
		UtilScoreboard.setScore(player.getScoreboard(),"§7www.EpicPvP.me", DisplaySlot.SIDEBAR, 8);
		UtilScoreboard.setScore(player.getScoreboard(),"  ", DisplaySlot.SIDEBAR, 7);
		UtilScoreboard.setScore(player.getScoreboard(),TranslationHandler.getText(player, "SCOREBOARD_ONLINE_STORE"), DisplaySlot.SIDEBAR, 6);
		UtilScoreboard.setScore(player.getScoreboard(),"§7Shop.ClashMC.eu", DisplaySlot.SIDEBAR, 5);
		UtilScoreboard.setScore(player.getScoreboard()," ", DisplaySlot.SIDEBAR, 4);
		UtilScoreboard.setScore(player.getScoreboard(),TranslationHandler.getText(player, "SCOREBOARD_TS"), DisplaySlot.SIDEBAR, 3);
		UtilScoreboard.setScore(player.getScoreboard(),"§7Ts.ClashMC.eu", DisplaySlot.SIDEBAR, 2);
		UtilScoreboard.setScore(player.getScoreboard(),"§8----------------", DisplaySlot.SIDEBAR, 1);
		player.setScoreboard(player.getScoreboard());
		
		money.getAsync(player, StatsKey.GEMS, new Callback<Object>() {
			@Override
			public void call(Object obj) {
				Bukkit.getScheduler().runTask(money.getInstance(), new Runnable() {
					public void run() {
						UtilScoreboard.resetScore(player.getScoreboard(), 11, DisplaySlot.SIDEBAR);
						UtilScoreboard.setScore(player.getScoreboard(),"§a§r§7"+((int)obj), DisplaySlot.SIDEBAR, 11);
					}
				});
			}
		});
	}
	
	public static void setScoreboardGemsAndCoins(Player player,StatsManager money){
		UtilScoreboard.addBoard(player.getScoreboard(),DisplaySlot.SIDEBAR, UtilScoreboard.getScoreboardDisplayname());
		UtilScoreboard.setScore(player.getScoreboard(),TranslationHandler.getText(player, "SCOREBOARD_GEMS"), DisplaySlot.SIDEBAR, 15);
		UtilScoreboard.setScore(player.getScoreboard(),"§7Loading...§c", DisplaySlot.SIDEBAR, 14);
		UtilScoreboard.setScore(player.getScoreboard(),"     ", DisplaySlot.SIDEBAR, 13);
		UtilScoreboard.setScore(player.getScoreboard(),TranslationHandler.getText(player, "SCOREBOARD_COINS"), DisplaySlot.SIDEBAR, 12);
		UtilScoreboard.setScore(player.getScoreboard(),"§7Loading...§a", DisplaySlot.SIDEBAR, 11);
		UtilScoreboard.setScore(player.getScoreboard(),"    ", DisplaySlot.SIDEBAR, 10);
		UtilScoreboard.setScore(player.getScoreboard(),TranslationHandler.getText(player, "SCOREBOARD_FORUM"), DisplaySlot.SIDEBAR, 9);
		UtilScoreboard.setScore(player.getScoreboard(),"§7www.ClashMC.eu", DisplaySlot.SIDEBAR, 8);
		UtilScoreboard.setScore(player.getScoreboard(),"  ", DisplaySlot.SIDEBAR, 7);
		UtilScoreboard.setScore(player.getScoreboard(),TranslationHandler.getText(player, "SCOREBOARD_ONLINE_STORE"), DisplaySlot.SIDEBAR, 6);
		UtilScoreboard.setScore(player.getScoreboard(),"§7Shop.ClashMC.eu", DisplaySlot.SIDEBAR, 5);
		UtilScoreboard.setScore(player.getScoreboard()," ", DisplaySlot.SIDEBAR, 4);
		UtilScoreboard.setScore(player.getScoreboard(),TranslationHandler.getText(player, "SCOREBOARD_TS"), DisplaySlot.SIDEBAR, 3);
		UtilScoreboard.setScore(player.getScoreboard(),"§7Ts.ClashMC.eu", DisplaySlot.SIDEBAR, 2);
		UtilScoreboard.setScore(player.getScoreboard(),"§8----------------", DisplaySlot.SIDEBAR, 1);
		player.setScoreboard(player.getScoreboard());

		money.getAsync(player, StatsKey.COINS, new Callback<Object>() {
			@Override
			public void call(Object obj) {
				Bukkit.getScheduler().runTask(money.getInstance(), new Runnable() {
					public void run() {
						UtilScoreboard.resetScore(player.getScoreboard(), 11, DisplaySlot.SIDEBAR);
						UtilScoreboard.setScore(player.getScoreboard(),"§c§r§7"+((int)obj), DisplaySlot.SIDEBAR, 11);
					}
				});
			}
		});
		
		money.getAsync(player, StatsKey.GEMS, new Callback<Object>() {
			@Override
			public void call(Object obj) {
				Bukkit.getScheduler().runTask(money.getInstance(), new Runnable() {
					public void run() {
						UtilScoreboard.resetScore(player.getScoreboard(), 14, DisplaySlot.SIDEBAR);
						UtilScoreboard.setScore(player.getScoreboard(),"§a§r§7"+((int)obj), DisplaySlot.SIDEBAR, 14);
					}
				});
			}
		});
	}
	
	public static void setWorldChangeUUID(World world,UUID old,UUID uuid){
		File file = new File(world.getName() + "/playerdata/"+old+".dat");
	    if(file.exists()){
	    	File new_file = new File(world.getName() + "/playerdata/" + uuid + ".dat");
	    	file.renameTo(new_file);
	    	System.out.println("[kCore] File from "+old+" renamed to "+uuid);
	    }
	}
	
	public static void sendPacket(Player player,kPacket packet){
		if(packet==null)return;
		sendPacket(player, packet.getPacket());
	}
	
	public static int getPlayerId(Player player){
		return getPlayerId(player.getName());
	}
	
	public static int getPlayerId(String playerName){
		return UtilServer.getClient().getPlayerAndLoad(playerName).getPlayerId();
	}
	
	public static UUID getOfflineUUID(String player){
		return UUID.nameUUIDFromBytes(new StringBuilder().append("OfflinePlayer:").append(player.toLowerCase()).toString().getBytes(Charsets.UTF_8));
	}

	public static void setPlayerFakeEquipment(Player player,Player to,ItemStack item,short slot){
		kPacketPlayOutEntityEquipment packet = new kPacketPlayOutEntityEquipment();
		packet.setEntityID(player.getEntityId());
		packet.setItemStack(item);
		packet.setSlot(slot);
		UtilPlayer.sendPacket(to, packet);
	}
	
	public static double getMaxHealth(Player player){
		return getCraftPlayer(player).getMaxHealth();
	}
	
	public static String getHealthBar(Player player){
		String bar="";
		double health = getHealth(player);
		for(int i=0;i<20;i++){
			if(i<=health && i%2==0){
				bar+=ChatColor.RED+"<3";
			}else if(health==i && i%2!=0){
				bar+=ChatColor.RED+"<3";
			}else if(i%2==0){
				bar+=ChatColor.GRAY+"<3";
			}
		}
		
		bar+=" §7(§e"+UtilMath.trim(2,(health/2))+"§7)";
		return bar;
	}
	
	public static double getHealth(Player player){
		return getCraftPlayer(player).getHealth();
	}

  public static boolean is1_9(Player player){
	  return getProtocol(player)>47;
  }

  @Getter
  private static ViaVersionAPI viaVersion = null;
  public static int getProtocol(Player player){
	  if(viaVersion==null){
		  if(Bukkit.getPluginManager().getPlugin("ViaVersion")==null)return 47;
		  viaVersion=ViaVersion.getInstance();
	  }
	  return viaVersion.getPlayerVersion(player);
  }
  
  public static boolean is1_8(Player player){
	  return getProtocol(player)<=47;
  }
	
  public static boolean isZoom(Player p){
		return p.hasPotionEffect(PotionEffectType.SLOW);
  }
  
  public static void setPlayerListName(Player player,String nick){
	  player.setPlayerListName(nick);
  }
  
  public static void RespawnNow(final Player p,JavaPlugin plugin){
      Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
    	   public void run() {
    		   p.spigot().respawn();
    	   }
    	   
    	  }, 10L);
}

  public static void sendMessage(org.bukkit.entity.Entity client, String message)
  {
    if (client == null) {
      return;
    }
    if (!(client instanceof Player)) {
      return;
    }
    ((Player)client).sendMessage(message);
  }

  public static Player searchExact(int playerId)
  {
	  for(LoadedPlayer loadedplayer : UtilServer.getClient().getPlayers()){
		  if(loadedplayer.getPlayerId()==playerId){
			  return searchExact(loadedplayer.getName());
		  }
	  }
	  return null;
  }
  
  public static Player searchExact(UUID uuid)
  {
    for (Player cur : UtilServer.getPlayers()) {
      if (cur.getUniqueId().equals(uuid))
        return cur;
    }
    return null;
  }
  
  public static Player searchExact(String name)
  {
    for (Player cur : UtilServer.getPlayers()) {
      if (cur.getName().equalsIgnoreCase(name))
        return cur;
    }
    return null;
  }
  
  public static TreeMap<Double, Player> getNearby(Location loc, double maxDist){
	  return getNearby(loc, maxDist, null);
  }

  public static TreeMap<Double, Player> getNearby(Location loc, double maxDist,PermissionType ignore){
	  TreeMap nearbyMap = new TreeMap();

    for (Player cur : loc.getWorld().getPlayers()) {
      if(ignore!=null&&!cur.hasPermission(ignore.getPermissionToString())){
          if (cur.getGameMode() != GameMode.CREATIVE && cur.getGameMode() != GameMode.SPECTATOR) {
              if (!cur.isDead()) {
                double dist = loc.toVector().subtract(cur.getLocation().toVector()).length();

                if (dist <= maxDist) {
                  for (int i = 0; i < nearbyMap.size(); i++) {
                    if (dist < loc.toVector().subtract(((Player)nearbyMap.get(i)).getLocation().toVector()).length()) {
                      nearbyMap.put(dist, cur);
                      break;
                    }
                  }

                }
              }
            }
      }
    }
    return nearbyMap;
  }

  public static Player getClosest(Location loc, Collection<Player> ignore)
  {
    Player best = null;
    double bestDist = 0.0D;

    for (Player cur : loc.getWorld().getPlayers())
    {
      if (cur.getGameMode() != GameMode.CREATIVE || cur.getGameMode() != GameMode.SPECTATOR)
      {
        if (!cur.isDead())
        {
          if ((ignore == null) || (!ignore.contains(cur)))
          {
            double dist = UtilMath.offset(cur.getLocation(), loc);

            if ((best == null) || (dist < bestDist))
            {
              best = cur;
              bestDist = dist;
            }
          }
        }
      }
    }
    return best;
  }
  
  public static Player getClosest(Location loc, org.bukkit.entity.Entity ignore)
  {
    Player best = null;
    double bestDist = 0.0D;

    for (Player cur : loc.getWorld().getPlayers())
    {
      if (cur.getGameMode() != GameMode.CREATIVE || cur.getGameMode() != GameMode.SPECTATOR)
      {
        if (!cur.isDead())
        {
          if ((ignore == null) || (!ignore.equals(cur)))
          {
            double dist = UtilMath.offset(cur.getLocation(), loc);

            if ((best == null) || (dist < bestDist))
            {
              best = cur;
              bestDist = dist;
            }
          }
        }
      }
    }
    return best;
  }

  public static void kick(Player player, String module, String message)
  {
    kick(player, module, message, true);
  }

  public static void kick(Player player, String module, String message, boolean log)
  {
    if (player == null) {
      return;
    }
    String out = ChatColor.RED + module + 
      ChatColor.WHITE + " - " + 
      ChatColor.YELLOW + message;
    player.kickPlayer(out);

    if (log)
      System.out.println("Kicked Client [" + player.getName() + "] for [" + module + " - " + message + "]");
  }

  public static HashMap<Player, Double> getInRadius(Location loc, double dR)
  {
    HashMap players = new HashMap();
    double offset;
    
    for (Player cur : loc.getWorld().getPlayers()){
      if (cur.getGameMode() != GameMode.CREATIVE || cur.getGameMode() != GameMode.SPECTATOR){
        offset = UtilMath.offset(loc, cur.getLocation());

        if (offset < dR)
          players.put(cur, Double.valueOf(1.0D - offset / dR));
      }
    }
    return players;
  }
  
  public static void damage(LivingEntity player, double prozent){
	  health(player, -((prozent/100)*((CraftLivingEntity)player).getHealth()));
  }
  
  public static void damage(Player player, double prozent){
	  health(player, -((prozent/100)*getCraftPlayer(player).getHealth()));
  }
  
  public static void health(LivingEntity player, double mod)
  {
    if (player.isDead()) {
      return;
    }
    
    if(mod<0){
    	player.damage(0);
    }
    
    double health = getCraftPlayer(player).getHealth() + mod;

    if (health < 0.0D) {
      health = 0.0D;
    }
    if (health > getCraftPlayer(player).getMaxHealth()) {
      health = getCraftPlayer(player).getMaxHealth();
    }
    player.setHealth(health);
  }
  
  public static void health(Player player, double mod)
  {
    if (player.isDead()) {
      return;
    }
    
    if(mod<0){
    	player.damage(0);
    }
    
    double health = getCraftPlayer(player).getHealth() + mod;

    if (health < 0.0D) {
      health = 0.0D;
    }
    if (health > getCraftPlayer(player).getMaxHealth()) {
      health = getCraftPlayer(player).getMaxHealth();
    }
    player.setHealth(health);
  }
  
  public static void addPotionEffect(Player p,PotionEffectType typ, int time,int staerke){
	  p.addPotionEffect(new PotionEffect(typ,time*20,staerke),true);
  }

  public static void hunger(Player player, int mod){
    if (player.isDead()) {
      return;
    }
    int hunger = player.getFoodLevel() + mod;

    if (hunger < 0) {
      hunger = 0;
    }
    if (hunger > 20) {
      hunger = 20;
    }
    player.setFoodLevel(hunger);
  }
  
  public static boolean isOnline(int playerId)
  {
    return searchExact(playerId) != null;
  }
  
  public static boolean isOnline(UUID uuid)
  {
    return searchExact(uuid) != null;
  }

  public static boolean isOnline(String name)
  {
    return searchExact(name) != null;
  }

  public static String safeNameLength(String name)
  {
    if (name.length() > 16) {
      name = name.substring(0, 16);
    }
    return name;
  }

  public static boolean isChargingBow(Player player)
  {
    if (!UtilGear.isMat(player.getItemInHand(), Material.BOW)) {
      return false;
    }
    return (((CraftEntity)player).getHandle().getDataWatcher().getByte(0) & 0x10) != 0;
  }
}