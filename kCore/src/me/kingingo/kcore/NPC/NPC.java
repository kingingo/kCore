package me.kingingo.kcore.NPC;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.PacketWrapper.WrapperPlayServerEntityMoveLook;
import me.kingingo.kcore.Util.UtilMath;
import me.kingingo.kcore.Util.UtilPlayer;
import me.kingingo.kcore.Util.UtilServer;
import net.minecraft.server.v1_7_R4.DataWatcher;
import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.minecraft.server.v1_7_R4.PacketPlayOutBed;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityDestroy;
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

import com.comphenix.protocol.ProtocolLibrary;

public class NPC {
	
	@Getter
	@Setter
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
	
	public void setTab(ArrayList<Player> list,boolean b){
		if(p!=null){
			for(Player player : list)UtilPlayer.setTab(p.getName(), player,b);
		}
	}
	
	public void setTab(Player player,boolean b){
		if(p!=null){
			UtilPlayer.setTab(p.getName(), player,b);
		}
	}
	
	public void setTab(boolean b){
		if(p!=null){
			for(Player player : UtilServer.getPlayers())UtilPlayer.setTab(p.getName(), player,b);
		}
	}
	
	public void destroyTab(){
		
	}
	
	public PacketPlayOutNamedEntitySpawn spawn(Location spawn){
		loc=spawn;
		if(p==null){
			CraftServer s = (CraftServer)Bukkit.getServer();
			GameProfile g = new GameProfile(UUID.randomUUID(), Name);
			WorldServer w = ((CraftWorld)loc.getWorld()).getHandle();
			PlayerInteractManager i = new PlayerInteractManager(w);
			this.p=new EntityPlayer( s.getServer() , w, g, i);
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
	
	public void walk(Location newLoc) {
	        walk(newLoc, getLoc().getYaw(), getLoc().getPitch());
	}
	
    private byte getCompressedAngle(float value) {
        return (byte) ((value * 256.0F) / 360.0F);
    }
    
	public void walk(Location newLoc, float yaw, float pitch) {
//		WrapperPlayServerEntityMoveLook packet = new WrapperPlayServerEntityMoveLook();
//		double x = ((newLoc.getBlockX() - getLoc().getBlockX()) * 32);
//		double y = ((newLoc.getBlockY() - getLoc().getBlockY()) * 32);
//		double z = ((newLoc.getBlockZ() - getLoc().getBlockZ()) * 32);
//		packet.setDx(x);
//		packet.setDy(y);
//		packet.setDz(z);
//		packet.setEntityID(p.getId());
//		packet.setPitch(UtilMath.getCompressedAngle(pitch));
//		packet.setYaw(UtilMath.getCompressedAngle(yaw));
//		getLoc().add(x,y,z);
//        for (Player pl : UtilServer.getPlayers()) {
//			if(pl.getName().equalsIgnoreCase(getName()))continue;
//			try {
//				ProtocolLibrary.getProtocolManager().sendServerPacket(pl, packet.getHandle());
//			} catch (InvocationTargetException e) {
//				e.printStackTrace();
//			}
//        }
   
			byte x = (byte)((newLoc.getBlockX() - getLoc().getBlockX()) * 32);
			byte y = (byte)((newLoc.getBlockY() - getLoc().getBlockY()) * 32);
			byte z = (byte)((newLoc.getBlockZ() - getLoc().getBlockZ()) * 32);
			loc.add((newLoc.getBlockX() - getLoc().getBlockX()), (newLoc.getBlockY() - getLoc().getBlockY()), (newLoc.getBlockZ() - getLoc().getBlockZ()));
	        PacketPlayOutRelEntityMoveLook packet = new PacketPlayOutRelEntityMoveLook(p.getId(),x,y,z,getCompressedAngle(yaw),getCompressedAngle(pitch),true);
	 
	        for (Player pl : UtilServer.getPlayers()) {
				if(pl.getName().equalsIgnoreCase(getName()))continue;
	            ((CraftPlayer) pl).getHandle().playerConnection.sendPacket(packet);
	           // ((CraftPlayer) pl).getHandle().playerConnection.sendPacket(p2);
	        }
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
