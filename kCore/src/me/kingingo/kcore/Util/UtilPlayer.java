package me.kingingo.kcore.Util;
import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.UUID;

import me.kingingo.kcore.MySQL.MySQL;
import me.kingingo.kcore.Nick.Events.PlayerListNameChangeEvent;
import me.kingingo.kcore.Nick.Events.PlayerSendMessageEvent;
import me.kingingo.kcore.PacketAPI.kPacket;
import me.kingingo.kcore.PacketAPI.v1_8_R2.kPacketPlayOutEntityEquipment;
import me.kingingo.kcore.Permission.kPermission;
import net.minecraft.server.v1_8_R2.Packet;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftPlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.google.common.base.Charsets;

public class UtilPlayer
{
	
	public static void sendPacket(Player player,Packet packet){
		((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
	}
	
	public static boolean hasPermission(Player p,String perm){
		if(p.hasPermission(perm) || p.hasPermission(kPermission.ALL_PERMISSION.getPermissionToString()))return true;
		return false;
	}
	
	public static boolean hasPermission(Player p,kPermission perm){
		if(p.hasPermission(perm.getPermissionToString()) || p.hasPermission(kPermission.ALL_PERMISSION.getPermissionToString()))return true;
		return false;
	}
	
	public static void Knockback(Location center, Player player,double speed, double high) {
		Vector unitVector = center.toVector().subtract(player.getLocation().toVector()).normalize();
		player.setVelocity(unitVector.multiply(speed));
		unitVector.setY(unitVector.getY() + high);
		player.setVelocity(unitVector);
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
		sendPacket(player, packet.getPacket());
	}
	
	public static UUID getRealUUID(String player,UUID uuid){
		if(UUID.nameUUIDFromBytes(new StringBuilder().append("OfflinePlayer:").append(player).toString().getBytes(Charsets.UTF_8)).equals(uuid)){
			uuid=getOfflineUUID(player.toLowerCase());
		}
		return uuid;
	}
	
	public static UUID getRealUUID(Player player){
		UUID uuid = player.getUniqueId();
		if(UUID.nameUUIDFromBytes(new StringBuilder().append("OfflinePlayer:").append(player.getName()).toString().getBytes(Charsets.UTF_8)).equals(uuid)){
			uuid=getOfflineUUID(player.getName().toLowerCase());
		}
		return uuid;
	}
	
	public static UUID getOfflineUUID(String player){
		return UUID.nameUUIDFromBytes(new StringBuilder().append("OfflinePlayer:").append(player.toLowerCase()).toString().getBytes(Charsets.UTF_8));
	}
	
	public static UUID getOnlineUUID(String player){
		try {
			return UtilUUID.getUUIDOf(player);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static UUID getUUID(String player,MySQL mysql){
		UUID uuid = getOnlineUUID(player);
		if(uuid!=null){
			String p = mysql.getString("SELECT premium FROM list_premium WHERE uuid='"+uuid+"'");
			if(p.equalsIgnoreCase("null")||p.equalsIgnoreCase("false")){
				uuid=getOfflineUUID(player);
			}
		}else{
			uuid=getOfflineUUID(player);
		}
		return uuid;
	}

	public static void setPlayerFakeEquipment(Player player,Player to,ItemStack item,short slot){
		kPacketPlayOutEntityEquipment packet = new kPacketPlayOutEntityEquipment();
		packet.setEntityID(player.getEntityId());
		packet.setItemStack(item);
		packet.setSlot(slot);
		UtilPlayer.sendPacket(to, packet);
	}
	
	public static double getMaxHealth(Player player){
		return ((CraftPlayer)player).getMaxHealth();
	}
	
	public static double getHealth(Player player){
		return ((CraftPlayer)player).getHealth();
	}
	
//  public static void sendPacket(Player player,PacketContainer packet){
//	  try {
//		ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
//	} catch (InvocationTargetException e) {
//		e.printStackTrace();
//	}
//  }
	
  public static boolean isZoom(Player p){
		return p.hasPotionEffect(PotionEffectType.SLOW);
  }
  
//  public static int getVersion(Player player){
//	  return ((CraftPlayer)player).getHandle().playerConnection.
//  }
  
//  public static void setTab(String tab,Player p){
//	  WrapperPlayServerPlayerInfo t = new WrapperPlayServerPlayerInfo();
//	  t.setPlayerName(tab);
//	  t.setPing(((short)9999));
//	  t.sendPacket(p);
//	  
////	  PacketPlayOutPlayerInfo t = new PacketPlayOutPlayerInfo();
////	  UtilReflection.setValue("username", t, tab);
////	  UtilReflection.setValue("ping", t, 9999);
////	  ((CraftPlayer)p).getHandle().playerConnection.sendPacket(t);
//  }
//  
//  public static void setTab(String[] tab,Player p){
//	  for(String t : tab)setTab(t, p);
//  }
  
  public static void setPlayerListName(Player player,String nick){
	  PlayerListNameChangeEvent ev = new PlayerListNameChangeEvent(player,nick);
	  Bukkit.getPluginManager().callEvent(ev);
	  player.setPlayerListName(ev.getNick());
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

    PlayerSendMessageEvent ev = new PlayerSendMessageEvent(((Player)client),message);
    Bukkit.getPluginManager().callEvent(ev);
    ((Player)client).sendMessage(ev.getMessage());
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

  public static String searchCollection(Player caller, String player, Collection<String> coll, String collName, boolean inform)
  {
    LinkedList<String> matchList = new LinkedList<>();

    for (String cur : coll)
    {
      if (cur.equalsIgnoreCase(player)) {
        return cur;
      }
      if (cur.toLowerCase().contains(player.toLowerCase())) {
        matchList.add(cur);
      }
    }

    if (matchList.size() != 1)
    {
      if (!inform) {
        return null;
      }

//      message(caller, F.main(collName + " Search", 
//        C.mCount + matchList.size() + 
//        C.mBody + " matches for [" + 
//        C.mElem + player + 
//        C.mBody + "]."));

      if (matchList.size() > 0)
      {
        String matchString = "";
        for (String cur : matchList) {
          matchString = matchString + cur + " ";
        }
//        message(caller, F.main(collName + " Search", 
//          C.mBody + " Matches [" + 
//          C.mElem + matchString + 
//          C.mBody + "]."));
      }

      return null;
    }

    return (String)matchList.get(0);
  }

  public static Player searchOnline(Player caller, String player, boolean inform)
  {
    LinkedList<Player> matchList = new LinkedList<>();

    for (Player cur : UtilServer.getPlayers())
    {
      if (cur.getName().equalsIgnoreCase(player)) {
        return cur;
      }
      if (cur.getName().toLowerCase().contains(player.toLowerCase())) {
        matchList.add(cur);
      }
    }

    if (matchList.size() != 1)
    {
      if (!inform) {
        return null;
      }

//      message(caller, F.main("Online Player Search", 
//        C.mCount + matchList.size() + 
//        C.mBody + " matches for [" + 
//        C.mElem + player + 
//        C.mBody + "]."));

      if (matchList.size() > 0)
      {
        String matchString = "";
        for (Player cur : matchList)
          matchString = matchString + F.elem(cur.getName()) + ", ";
        if (matchString.length() > 1) {
          matchString = matchString.substring(0, matchString.length() - 2);
        }
//        message(caller, F.main("Online Player Search", 
//          C.mBody + "Matches [" + 
//          C.mElem + matchString + 
//          C.mBody + "]."));
      }

      return null;
    }

    return (Player)matchList.get(0);
  }

  public static LinkedList<Player> matchOnline(Player caller, String players, boolean inform)
  {
    LinkedList matchList = new LinkedList();

    String failList = "";

    for (String cur : players.split(","))
    {
      Player match = searchOnline(caller, cur, inform);

      if (match != null) {
        matchList.add(match);
      }
      else {
        failList = failList + cur + " ";
      }
    }
    if ((inform) && (failList.length() > 0))
    {
      failList = failList.substring(0, failList.length() - 1);
//      message(caller, F.main("Online Player(s) Search", 
//        C.mBody + "Invalid [" + 
//        C.mElem + failList + 
//        C.mBody + "]."));
    }

    return matchList;
  }

  public static LinkedList<Player> getNearby(Location loc, double maxDist)
  {
    LinkedList nearbyMap = new LinkedList();

    for (Player cur : loc.getWorld().getPlayers())
    {
      if (cur.getGameMode() != GameMode.CREATIVE)
      {
        if (!cur.isDead())
        {
          double dist = loc.toVector().subtract(cur.getLocation().toVector()).length();

          if (dist <= maxDist)
          {
            for (int i = 0; i < nearbyMap.size(); i++)
            {
              if (dist < loc.toVector().subtract(((Player)nearbyMap.get(i)).getLocation().toVector()).length())
              {
                nearbyMap.add(i, cur);
                break;
              }
            }

            if (!nearbyMap.contains(cur))
              nearbyMap.addLast(cur); 
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
      if (cur.getGameMode() != GameMode.CREATIVE)
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
      if (cur.getGameMode() != GameMode.CREATIVE)
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

    for (Player cur : loc.getWorld().getPlayers())
    {
      if (cur.getGameMode() != GameMode.CREATIVE)
      {
        double offset = UtilMath.offset(loc, cur.getLocation());

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
	  health(player, -((prozent/100)*((CraftPlayer)player).getHealth()));
  }
  
  public static void health(LivingEntity player, double mod)
  {
    if (player.isDead()) {
      return;
    }
    
    if(mod<0){
    	player.damage(0);
    }
    
    double health = ((CraftPlayer)player).getHealth() + mod;

    if (health < 0.0D) {
      health = 0.0D;
    }
    if (health > ((CraftPlayer)player).getMaxHealth()) {
      health = ((CraftPlayer)player).getMaxHealth();
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
    
    double health = ((CraftPlayer)player).getHealth() + mod;

    if (health < 0.0D) {
      health = 0.0D;
    }
    if (health > ((CraftPlayer)player).getMaxHealth()) {
      health = ((CraftPlayer)player).getMaxHealth();
    }
    player.setHealth(health);
  }
  
  public static void addPotionEffect(Player p,PotionEffectType typ, int time,int stärke){
	  p.addPotionEffect(new PotionEffect(typ,time*20,stärke));
  }

  public static void hunger(Player player, int mod)
  {
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