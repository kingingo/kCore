package me.kingingo.kcore.NPC;

import java.lang.reflect.Field;
import java.util.UUID;

import lombok.Getter;
import me.kingingo.kcore.Util.UtilServer;
import net.minecraft.server.v1_7_R4.DataWatcher;
import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.minecraft.server.v1_7_R4.PacketPlayOutBed;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityHeadRotation;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityTeleport;
import net.minecraft.server.v1_7_R4.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_7_R4.PacketPlayOutRelEntityMoveLook;
import net.minecraft.server.v1_7_R4.PlayerInteractManager;
import net.minecraft.server.v1_7_R4.WorldServer;
import net.minecraft.util.com.mojang.authlib.GameProfile;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R4.CraftServer;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class NPC {
	
	@Getter
	Location loc;
	@Getter
	String Name;
	@Getter
	EntityPlayer p;
	@Getter
	NPCManager Manager;
	@Getter
	boolean world=false;
	
	private boolean sleep=false;
	PacketPlayOutNamedEntitySpawn spawned;
	
	public NPC(String Name,NPCManager Manager){
		this.Manager=Manager;
		this.Name=Name;
	}
	
	public PacketPlayOutNamedEntitySpawn spawn(Location spawn){
		loc=spawn;
		if(p==null){
			CraftServer s = (CraftServer)Bukkit.getServer();
			GameProfile g = new GameProfile(UUID.randomUUID(), Name);
			WorldServer w = ((CraftWorld)loc.getWorld()).getHandle();
			PlayerInteractManager i = new PlayerInteractManager(w);
			EntityPlayer p = new EntityPlayer( s.getServer() , w, g, i);
			this.p=p;
		}
		p.locX=loc.getX();
		p.locY=loc.getY();
		p.locZ=loc.getZ();
		spawned = new PacketPlayOutNamedEntitySpawn( p );
		setValue("a",spawned,p.getId());
		getManager().getNPCList().put(p.getId(), this);
		for(Player pl : UtilServer.getPlayers()){
			if(pl.getName().equalsIgnoreCase(getName()))continue;
			((CraftPlayer)pl).getHandle().playerConnection.sendPacket(spawned);
		}
		return spawned;
	}
	
	public void setName(String s) {
        DataWatcher d = new DataWatcher(null);
        d.a(0, (Object) (byte) 0);
        d.a(1, (Object) (short) 0);
        d.a(8, (Object) (byte) 0);
        d.a(10, (Object) (String) s);
        //d.a(11, (Object) (byte) 0);
        PacketPlayOutEntityMetadata packet40 = new PacketPlayOutEntityMetadata(p.getId(), d, true);
        for (Player pl : Bukkit.getOnlinePlayers()) {
            ((CraftPlayer) pl).getHandle().playerConnection.sendPacket(packet40);
        }
    }
	
	public void teleport(Location loc) {
	     PacketPlayOutEntityTeleport tp = new PacketPlayOutEntityTeleport();
	     setValue("a", tp, p.getId());
	     setValue("b", tp, ((int) (loc.getX() * 32)));
	     setValue("c", tp, ((int) (loc.getY() * 32)));
	     setValue("d", tp, ((int) (loc.getZ() * 32)));
	     this.loc=loc;
	     for (Player pl : Bukkit.getOnlinePlayers()) {
				if(pl.getName().equalsIgnoreCase(getName()))continue;
	            ((CraftPlayer)pl).getHandle().playerConnection.sendPacket(tp);
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
	        PacketPlayOutRelEntityMoveLook packet = new PacketPlayOutRelEntityMoveLook();
//	        PacketPlayOutRelEntityMoveLook packet = new PacketPlayOutRelEntityMoveLook(p.getId(),x,y,z,getCompressedAngle(yaw),getCompressedAngle2(pitch));
//	        setValue("a", packet, p.getId());
//	        setValue("b", packet, x);
//	        setValue("c", packet, y);
//	        setValue("d", packet, z);
//	        setValue("e", packet, getCompressedAngle(yaw));
//	        setValue("f", packet, getCompressedAngle2(pitch));
	 
	        PacketPlayOutEntityHeadRotation p2 = new PacketPlayOutEntityHeadRotation();
	        setValue("a", p2, p.getId());
	        setValue("b", p2, getCompressedAngle(yaw));
	 
	        for (Player pl : Bukkit.getOnlinePlayers()) {
				if(pl.getName().equalsIgnoreCase(getName()))continue;
	            ((CraftPlayer) pl).getHandle().playerConnection.sendPacket(packet);
	            ((CraftPlayer) pl).getHandle().playerConnection.sendPacket(p2);
	        }
	        this.loc.setPitch(pitch);
	        this.loc.setYaw(yaw);
	        this.loc.add(a, b, c);
	    }
	
	public void SendPlayer(Player pl){
		((CraftPlayer) pl).getHandle().playerConnection.sendPacket(spawned);
		if(sleep){
			PacketPlayOutBed bed = new PacketPlayOutBed();

			setValue("a", bed, p.getId());
			setValue("b", bed,(int) loc.getX());
			setValue("c", bed,(int) loc.getY());
			setValue("d", bed,(int) loc.getZ());
			((CraftPlayer) pl).getHandle().playerConnection.sendPacket(bed);
		}
	}
	
    public void remove() {
    	getManager().getNPCList().remove(p.getId());
        PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(p.getId());
        for (Player pl : UtilServer.getPlayers()) {
			if(pl.getName().equalsIgnoreCase(getName()))continue;
            ((CraftPlayer) pl).getHandle().playerConnection.sendPacket(packet);
        }
    }
    
	public PacketPlayOutBed sleep() {
		PacketPlayOutBed bed = new PacketPlayOutBed();

		setValue("a", bed, p.getId());
		setValue("b", bed,(int) loc.getX());
		setValue("c", bed,(int) loc.getY());
		setValue("d", bed,(int) loc.getZ());
		
		for(Player pl : UtilServer.getPlayers()){
			if(pl.getName().equalsIgnoreCase(getName()))continue;
			((CraftPlayer)pl).getHandle().playerConnection.sendPacket(bed);
		}
		return bed;
	}
	
	private void setValue(String name, Object instance, Object value){
		try{
			Field field = instance.getClass().getDeclaredField(name);
			field.setAccessible(true);
			field.set(instance, value);
		}catch(Exception e){
			System.err.println(e);
		}
	}
	
}
