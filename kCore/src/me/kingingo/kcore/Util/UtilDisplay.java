package me.kingingo.kcore.Util;
import java.util.HashMap;
import java.util.Map;

import me.kingingo.kcore.PacketWrapper.WrapperPlayServerEntityDestroy;
import me.kingingo.kcore.PacketWrapper.WrapperPlayServerEntityMetadata;
import me.kingingo.kcore.PacketWrapper.WrapperPlayServerEntityTeleport;
import me.kingingo.kcore.PacketWrapper.WrapperPlayServerSpawnEntityLiving;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.comphenix.protocol.wrappers.WrappedDataWatcher;

public class UtilDisplay {
 
    public static Map<String, UtilDisplay> dragonplayers = new HashMap<String, UtilDisplay>();
    public static final int MAX_HEALTH = 200;
    public boolean visible;
    public int EntityID;
    public int x;
    public int y;
    public int z;
    public int pitch = 0;
    public int head_pitch = 0;
    public int yaw = 0;
    public byte xvel = 0;
    public byte yvel = 0;
    public byte zvel = 0;
    public float health;
    public String name;
    public Location sloc;
    private WrapperPlayServerSpawnEntityLiving dragon;
 
    public UtilDisplay(String name, int EntityID, Location loc){
        this(name, EntityID, (int) Math.floor(loc.getBlockX() * 32.0D), (int) Math.floor(loc.getBlockY() * 32.0D), (int) Math.floor(loc.getBlockZ() * 32.0D));
        this.sloc=loc;
    }
 
    public UtilDisplay(String name, int EntityID, Location loc, float health, boolean visible){
        this(name, EntityID, (int) Math.floor(loc.getBlockX() * 32.0D), (int) Math.floor(loc.getBlockY() * 32.0D), (int) Math.floor(loc.getBlockZ() * 32.0D), health, visible);
        this.sloc=loc;
    }
 
    public UtilDisplay(String name, int EntityID, int x, int y, int z){
        this(name, EntityID, x, y, z, MAX_HEALTH, false);
    }
 
    public UtilDisplay(String name, int EntityID, int x, int y, int z, float health, boolean visible){
        this.name=name;
        this.EntityID=EntityID;
        this.x=x;
        this.y=y;
        this.z=z;
        this.health=health;
        this.visible=visible;
    }
 
    public void getSpawnPacket(Player player){
      dragon = new WrapperPlayServerSpawnEntityLiving();
      dragon.setEntityID( EntityID );
      dragon.setType(EntityType.ENDER_DRAGON);
      dragon.setX(Integer.valueOf(x));
      dragon.setY(Integer.valueOf(y));
      dragon.setZ(Integer.valueOf(z));
      WrappedDataWatcher wdw = new WrappedDataWatcher();	
      wdw.setObject(0, Byte.valueOf((byte) 32));
      wdw.setObject(2,name);
      wdw.setObject(3, (byte) 1);
      wdw.setObject(6, Float.valueOf(this.health));
      wdw.setObject(7, Integer.valueOf(0));
      wdw.setObject(8,  Byte.valueOf("0"));
      dragon.setMetadata(wdw);
      dragon.sendPacket(player);
    }

    public void getDestroyPacket(Player player){
      WrapperPlayServerEntityDestroy packet = new WrapperPlayServerEntityDestroy();
      packet.setEntities(new int[]{EntityID});
      packet.sendPacket(player);
    }
    
    public void getMetaPacket(Player player,WrappedDataWatcher watcher){
      WrapperPlayServerEntityMetadata packet = new WrapperPlayServerEntityMetadata();
      packet.setEntityId(EntityID);
      packet.setEntityMetadata(watcher.getWatchableObjects());
      packet.sendPacket(player);
    }

    public void getTeleportPacket(Player player,int entityid,Location loc){
    	WrapperPlayServerEntityTeleport packet = new WrapperPlayServerEntityTeleport();
    	packet.setEntityID(entityid);
    	packet.setX(loc.getX());
    	packet.setY(loc.getY());
    	packet.setZ(loc.getZ());
    	packet.setYaw(loc.getYaw());
    	packet.setPitch(loc.getPitch());
      	packet.sendPacket(player);
    }
    
    public WrappedDataWatcher getWatcher(){
        dragon.getMetadata().setObject(0, Byte.valueOf((byte) 32));
        dragon.getMetadata().setObject(2,name);
        dragon.getMetadata().setObject(3, (byte) 1);
        dragon.getMetadata().setObject(6, Float.valueOf(this.health));
        dragon.getMetadata().setObject(7, Integer.valueOf(0));
        dragon.getMetadata().setObject(8,  Byte.valueOf("0"));
      return dragon.getMetadata();
    }
    
    public static void setStatus(Player player, String text, int healthpercent){
        UtilDisplay dragon = null;
        if(dragonplayers.containsKey(player.getName())){
            dragon = dragonplayers.get(player.getName());
        }else if(!text.equals("")){
            dragon = new UtilDisplay(text, 6000, player.getLocation().add(0, -100, 0));
            dragonplayers.put(player.getName(), dragon);
            dragon.getSpawnPacket(player);
        }
     
        if(text.equals("") && dragonplayers.containsKey(player.getName())){
            dragon.getDestroyPacket(player);
            dragonplayers.remove(player.getName());
        }else{
        	
            dragon.name=text;
            dragon.health=(healthpercent/100f)*UtilDisplay.MAX_HEALTH;
            dragon.getMetaPacket(player,dragon.getWatcher());
            dragon.getTeleportPacket(player,dragon.EntityID,player.getLocation().add(0, -100, 0));   
        }
    }
 
    public static void displayDragonTextBar(JavaPlugin plugin, String text, final Player player, long length){
        setStatus(player, text, 100);
 
        new BukkitRunnable(){
            @Override
            public void run(){
                setStatus(player, "", 100);
            }
        }.runTaskLater(plugin, length);
    }
 
    public static void displayDragonLoadingBar(final JavaPlugin plugin, final String text, final String completeText, final Player player, final int healthAdd, final long delay, final boolean loadUp){
        setStatus(player, "", (loadUp ? 1 : 100));
 
        new BukkitRunnable(){
            int health = (loadUp ? 1 : 100);
 
            @Override
            public void run(){
                if((loadUp ? health < 100 : health > 1)){
                    setStatus(player, text, health);
                    if(loadUp){
                        health += healthAdd;
                    } else {
                        health -= healthAdd;
                    }
                } else {
                    setStatus(player, completeText, (loadUp ? 100 : 1));
                    new BukkitRunnable(){
                        @Override
                        public void run(){
                            setStatus(player, "", (loadUp ? 100 : 1));
                        }
                    }.runTaskLater(plugin, 20);
 
                    this.cancel();
                }
            }
        }.runTaskTimer(plugin, delay, delay);
    }
 
    public static void displayDragonLoadingBar(final JavaPlugin plugin, final String text, final String completeText, final Player player, final int secondsDelay, final boolean loadUp){
        final int healthChangePerSecond = 100 / secondsDelay / 4;
 
        displayDragonLoadingBar(plugin, text, completeText, player, healthChangePerSecond, 5L, loadUp);
    }
 
    
    public static void displayTextBarPercent(Player p,String text,int time){
    	try{
    	setStatus(p, "", time);
    	} catch (Exception error){}
    	setStatus(p, text, time);
    }
    
    
    public static void displayTextBar(Player p,String text){
    	try{
    	setStatus(p, "", 100);
    	} catch (Exception error){}
    	setStatus(p, text, 100);
    }
    
    
    public static void displayTextBar(String text,Player p){
    	try{
    	setStatus(p, "", 100);
    	} catch (Exception error){}
    	setStatus(p, text, 100);
    }
    
}
