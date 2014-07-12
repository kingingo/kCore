package me.kingingo.kcore.Minecraft;

import java.lang.reflect.Field;

import lombok.Getter;
import me.kingingo.kcore.Util.UtilServer;
import net.minecraft.server.v1_7_R3.DataWatcher;
import net.minecraft.server.v1_7_R3.EntityPlayer;
import net.minecraft.server.v1_7_R3.PacketPlayOutBed;
import net.minecraft.server.v1_7_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_7_R3.PacketPlayOutEntityHeadRotation;
import net.minecraft.server.v1_7_R3.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_7_R3.PacketPlayOutEntityTeleport;
import net.minecraft.server.v1_7_R3.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_7_R3.PacketPlayOutRelEntityMoveLook;
import net.minecraft.server.v1_7_R3.PlayerInteractManager;
import net.minecraft.server.v1_7_R3.WorldServer;
import net.minecraft.util.com.mojang.authlib.GameProfile;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R3.CraftServer;
import org.bukkit.craftbukkit.v1_7_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class NPC {
	
	@Getter
	Location loc;
	@Getter
	String Name;
	@Getter
	EntityPlayer p;
	
	public NPC(String Name,Location loc){
		this.loc=loc;
		this.Name=Name;
	}
	
	public PacketPlayOutNamedEntitySpawn verdeckt(String Name){
		if(p==null){
			CraftServer s = (CraftServer)Bukkit.getServer();
			GameProfile g = new GameProfile(null, Name);
			WorldServer w = ((CraftWorld)loc.getWorld()).getHandle();
			PlayerInteractManager i = new PlayerInteractManager(w);
			EntityPlayer p = new EntityPlayer( s.getServer() , w, g, i);
			
			this.p=p;
		}
		p.locX=loc.getX();
		p.locY=loc.getY();
		p.locZ=loc.getZ();
		PacketPlayOutNamedEntitySpawn npc = new PacketPlayOutNamedEntitySpawn( p );
		setValue("a",npc,p.getId());
		
		for(Player p : UtilServer.getPlayers()){
			((CraftPlayer)p).getHandle().playerConnection.sendPacket(npc);
		}
		return npc;
	}
	
	public void aufdecken(){
		remove();
		this.p=null;
		spawn();
	}
	
	public PacketPlayOutNamedEntitySpawn spawn(){
		if(p==null){
			CraftServer s = (CraftServer)Bukkit.getServer();
			GameProfile g = new GameProfile(null, Name);
			WorldServer w = ((CraftWorld)loc.getWorld()).getHandle();
			PlayerInteractManager i = new PlayerInteractManager(w);
			EntityPlayer p = new EntityPlayer( s.getServer() , w, g, i);
			
			this.p=p;
		}
		p.locX=loc.getX();
		p.locY=loc.getY();
		p.locZ=loc.getZ();
		PacketPlayOutNamedEntitySpawn npc = new PacketPlayOutNamedEntitySpawn( p );
		setValue("a",npc,p.getId());
		
		for(Player p : UtilServer.getPlayers()){
			((CraftPlayer)p).getHandle().playerConnection.sendPacket(npc);
		}
		return npc;
	}
	
	public void setName(String s) {
	
        DataWatcher d = new DataWatcher(null);
        d.a(0, (Object) (byte) 0);
        d.a(1, (Object) (short) 0);
        d.a(8, (Object) (byte) 0);
        d.a(10, (Object) (String) s);
        //d.a(11, (Object) (byte) 0);
        PacketPlayOutEntityMetadata packet40 = new PacketPlayOutEntityMetadata(p.getId(), d, true);
        for (Player p : Bukkit.getOnlinePlayers()) {
            ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet40);
        }
    }
	
	public void teleport(Location loc) {
	     PacketPlayOutEntityTeleport tp = new PacketPlayOutEntityTeleport();
	     setValue("a", tp, p.getId());
	     setValue("b", tp, ((int) (loc.getX() * 32)));
	     setValue("c", tp, ((int) (loc.getY() * 32)));
	     setValue("d", tp, ((int) (loc.getZ() * 32)));
	     this.loc=loc;
	     for (Player p : Bukkit.getOnlinePlayers()) {
	            ((CraftPlayer)p).getHandle().playerConnection.sendPacket(tp);
	     }
	}
	
	public void walk(double x, double y, double z) {
	        walk(x, y, z, getLoc().getYaw(), getLoc().getPitch());
	}
	
    private byte getCompressedAngle(float value) {
        return (byte) ((value * 256.0F) / 360.0F);
    }
 
    private byte getCompressedAngle2(float value) {
        return (byte) ((value * 256.0F) / 360.0F);
    }
	
	public void walk(double a, double b, double c, float yaw, float pitch) {
	        byte x = (byte) a;
	        byte y = (byte) b;
	        byte z = (byte) c;
	        PacketPlayOutRelEntityMoveLook packet = new PacketPlayOutRelEntityMoveLook(p.getId(),x,y,z,getCompressedAngle(yaw),getCompressedAngle2(pitch));
//	        setValue("a", packet, p.getId());
//	        setValue("b", packet, x);
//	        setValue("c", packet, y);
//	        setValue("d", packet, z);
//	        setValue("e", packet, getCompressedAngle(yaw));
//	        setValue("f", packet, getCompressedAngle2(pitch));
	 
	        PacketPlayOutEntityHeadRotation p2 = new PacketPlayOutEntityHeadRotation();
	        setValue("a", p2, p.getId());
	        setValue("b", p2, getCompressedAngle(yaw));
	 
	        for (Player p : Bukkit.getOnlinePlayers()) {
	            ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
	            ((CraftPlayer) p).getHandle().playerConnection.sendPacket(p2);
	        }
	        this.loc.setPitch(pitch);
	        this.loc.setYaw(yaw);
	        this.loc.add(a, b, c);
	    }
	  
    public void remove() {
        PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(p.getId());
        for (Player p : Bukkit.getOnlinePlayers()) {
            ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
        }
    }
	
	public PacketPlayOutBed sleep() {
		PacketPlayOutBed bed = new PacketPlayOutBed();

		setValue("a", bed, 1337);
		setValue("b", bed,(int) loc.getX());
		setValue("c", bed,(int) loc.getY());
		setValue("d", bed,(int) loc.getZ());
		
		for(Player p : UtilServer.getPlayers()){
			((CraftPlayer)p).getHandle().playerConnection.sendPacket(bed);
		}
		return bed;
	}
	
	private void setValue(String name, Object instance, Object value){
		try{
			Field field = instance.getClass().getDeclaredField(name);
			field.setAccessible(true);
			field.set(instance, value);
		}catch(Exception e){
			
		}
	}
	
}
