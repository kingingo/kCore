package eu.epicpvp.kcore.PacketAPI.Packets;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

import eu.epicpvp.kcore.PacketAPI.PacketWrapper;
import eu.epicpvp.kcore.PacketAPI.UtilPacket;
import eu.epicpvp.kcore.Util.UtilReflection;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityTeleport;

public class WrapperPacketPlayOutEntityTeleport implements PacketWrapper {

	private String ID = "a";
	private String X = "b";
	private String Y = "c";
	private String Z = "d";
	private String YAW = "e";
	private String PITCH = "f";
	private String onGROUND = "g";
	@Getter
	private PacketPlayOutEntityTeleport packet;
	
	public WrapperPacketPlayOutEntityTeleport(){
		packet = new PacketPlayOutEntityTeleport();
	}
	
	public WrapperPacketPlayOutEntityTeleport(EntityPlayer en){
		packet = new PacketPlayOutEntityTeleport();
		setEntityID(en.getId());
		setX(en.locX);
		setY(en.locY);
		setZ(en.locZ);;
		setYaw(en.yaw);;
		setPitch(en.pitch);
		setOnGround(en.onGround);
	}
	
	public WrapperPacketPlayOutEntityTeleport(Entity en){
		packet = new PacketPlayOutEntityTeleport();
		setEntityID(en.getEntityId());
		setX(en.getLocation().getX());
		setY(en.getLocation().getY());
		setZ(en.getLocation().getZ());;
		setYaw(en.getLocation().getYaw());;
		setPitch(en.getLocation().getPitch());
		setOnGround(en.isOnGround());
	}
	
	public boolean isOnGround(){
		return Boolean.valueOf((boolean)UtilReflection.getValue(onGROUND, packet ));
	}
	
	public void setOnGround(boolean flag){
		UtilReflection.setValue(onGROUND, packet, flag);
	}
	
	public int getEntityID(){
		return (int)UtilReflection.getValue(ID, packet);
	}
	
	public void setEntityID(int id){
		UtilReflection.setValue(ID, packet, id);
	}
	
	public double getX(){
		return (double)UtilReflection.getValue(X, packet);
	}
	
	public double getY(){
		return (double)UtilReflection.getValue(Y, packet);
	}
	
	public double getZ(){
		return (double)UtilReflection.getValue(Z, packet);
	}
	
	public float getPitch(){
		return (float)UtilReflection.getValue(PITCH, packet);
	}
	
	public float getYaw(){
		return (float)UtilReflection.getValue(YAW, packet);
	}
	
	public void setX(double x){
		UtilReflection.setValue(X, packet, UtilPacket.toFixedPoint(x));
	}
	
	public void setY(double x){
		UtilReflection.setValue(Y, packet, UtilPacket.toFixedPoint(x));
	}
	
	public void setZ(double x){
		UtilReflection.setValue(Z, packet, UtilPacket.toFixedPoint(x));
	}
	
	public void setYaw(float yaw){
		UtilReflection.setValue(YAW, packet, UtilPacket.toPackedByte(yaw));
	}
	
	public void setPitch(float yaw){
		UtilReflection.setValue(PITCH, packet, UtilPacket.toPackedByte(yaw));
	}
	
	public void setLocation(Location location){
		setX(location.getX());
		setY(location.getY());
		setZ(location.getZ());
		setYaw(location.getYaw());
		setPitch(location.getPitch());
	}
	
}
