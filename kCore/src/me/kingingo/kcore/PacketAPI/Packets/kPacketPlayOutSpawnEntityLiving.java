package me.kingingo.kcore.PacketAPI.Packets;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.PacketAPI.UtilPacket;
import me.kingingo.kcore.PacketAPI.kPacket;
import me.kingingo.kcore.Util.UtilReflection;
import net.minecraft.server.v1_8_R3.DataWatcher;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityLiving;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;

public class kPacketPlayOutSpawnEntityLiving implements kPacket{

	private String X = "c";
	private String Y = "d";
	private String Z = "e";
	private String YAW = "i";
	private String PITCH = "j";
	private String DATAWATCHER = "l";
	private String TYPE = "b";
	private String ID = "a";
	
	@Getter
	@Setter
	private PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving();
	
	public kPacketPlayOutSpawnEntityLiving(){
		packet = new PacketPlayOutSpawnEntityLiving();
	}
	
	public kPacketPlayOutSpawnEntityLiving(int entityID ,EntityType type ,Location location){
		packet = new PacketPlayOutSpawnEntityLiving();
		setEntityID(entityID);
		setEntityType(type);
		setLocation(location);
	}
	
	public kPacketPlayOutSpawnEntityLiving(EntityLiving living){
		packet = new PacketPlayOutSpawnEntityLiving(living);
	}
	
	public EntityType getEntityType(){
		return EntityType.fromId( ((int)UtilReflection.getValue(TYPE, packet )) );
	}
	
	public void setEntityType(EntityType type){
		UtilReflection.setValue(TYPE, packet, (int) type.getTypeId());
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
