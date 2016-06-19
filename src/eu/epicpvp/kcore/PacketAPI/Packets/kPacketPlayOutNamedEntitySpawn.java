package eu.epicpvp.kcore.PacketAPI.Packets;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;

import eu.epicpvp.kcore.PacketAPI.UtilPacket;
import eu.epicpvp.kcore.PacketAPI.kPacket;
import eu.epicpvp.kcore.Util.UtilReflection;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.v1_8_R3.DataWatcher;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.PacketPlayOutNamedEntitySpawn;

public class kPacketPlayOutNamedEntitySpawn implements kPacket{
	@Getter
	@Setter
	private PacketPlayOutNamedEntitySpawn packet;
	private String X = "c";
	private String Y = "d";
	private String Z = "e";
	private String YAW = "f";
	private String PITCH = "g";
	private String DATAWATCHER = "i";
	private String UUID = "b";
	private String ID = "a";
	private String ITEM = "h";
		
	public kPacketPlayOutNamedEntitySpawn(){
		this(new PacketPlayOutNamedEntitySpawn());
	}
	
	public kPacketPlayOutNamedEntitySpawn(PacketPlayOutNamedEntitySpawn packet){
		this.packet = packet;
	}
	
	public kPacketPlayOutNamedEntitySpawn(EntityHuman human){
		packet = new PacketPlayOutNamedEntitySpawn(human);
	}
	
	public int getItemInHand(){
		return (int)UtilReflection.getValue(ITEM, packet);
	}
	
	public void setItemInHand(Material item){
		UtilReflection.setValue(ITEM, packet, item == null ? 0 : item.getId());
	}
	
	public UUID getUUID(){
		return ((UUID)UtilReflection.getValue(UUID, packet ));
	}
	
	public void setUUID(UUID uuid){
		UtilReflection.setValue(UUID, packet, uuid);
	}
	
	public int getEntityID(){
		return (int)UtilReflection.getValue(ID, packet);
	}
	
	public void setEntityID(int id){
		UtilReflection.setValue(ID, packet, id);
	}
	
	public DataWatcher getDataWatcher(){
		return (DataWatcher) UtilReflection.getValue(DATAWATCHER, packet);
	}
	
	public void setDataWatcher(DataWatcher watcher){
		UtilReflection.setValue(DATAWATCHER, packet, watcher);
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
		return (float) UtilReflection.getValue(PITCH, packet);
	}
	
	public float getYaw(){
		return (float) UtilReflection.getValue(YAW, packet);
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
