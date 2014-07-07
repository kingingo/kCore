package me.kingingo.kcore.Minecraft;

import net.minecraft.server.v1_7_R3.EntityPlayer;
import net.minecraft.server.v1_7_R3.PacketPlayOutBed;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.comphenix.protocol.wrappers.WrappedDataWatcher;

import me.kingingo.kcore.Hologram.wrapper.WrapperPlayServerNamedEntitySpawn;

public class Test {
	//https://www.youtube.com/watch?v=ZMPIGlPzzZY
	public static int entityId = 12345;
	
	public static int spawnSleepNPC(Player player, Location loc, String name) {
	    WrapperPlayServerNamedEntitySpawn spawned = new WrapperPlayServerNamedEntitySpawn();
	    
	    spawned.setEntityID(entityId++); // Must be a unique ID
	    spawned.setPosition(loc.toVector());
	    spawned.setPlayerName(name);
	    spawned.setPlayerUUID("0-0-0-0-0");
	    
	    // The rotation of the player's head (in degrees)
	    spawned.setYaw(0);
	    spawned.setPitch(-45);
	    
	    // Documentation: 
	    // http://mc.kev009.com/Entities#Entity_Metadata_Format
	    WrappedDataWatcher watcher = new WrappedDataWatcher();
	    watcher.setObject(0, (byte) 0); // Flags. Must be a byte.
	    
	    // Specify potion color (blue = 255 (FF), green = 0 (00), red = 255 (FF))
	    watcher.setObject(7, (int)0xFF00AA);
	    
	    spawned.setMetadata(watcher);
	    spawned.sendPacket(player);
	    EntityPlayer s = ((EntityPlayer)spawned.getEntity(loc.getWorld()));
	    PacketPlayOutBed packet = new PacketPlayOutBed( s ,loc.getBlockX(),loc.getBlockY()+1,loc.getBlockZ());
        for(Player a:Bukkit.getOnlinePlayers()){
           if(a.getEntityId() != spawned.getEntityID()){
              ((CraftPlayer)a).getHandle().playerConnection.sendPacket(packet);
           }
        }
	    
	    return spawned.getEntityID();
	}
	
}
