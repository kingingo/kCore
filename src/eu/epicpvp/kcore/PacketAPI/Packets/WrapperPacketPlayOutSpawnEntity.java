package eu.epicpvp.kcore.PacketAPI.Packets;

import org.bukkit.Location;

import eu.epicpvp.kcore.PacketAPI.PacketWrapper;
import eu.epicpvp.kcore.PacketAPI.UtilPacket;
import eu.epicpvp.kcore.Util.UtilReflection;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntity;

public class WrapperPacketPlayOutSpawnEntity implements PacketWrapper{
	@Getter
	@Setter
	private PacketPlayOutSpawnEntity packet;
//	private String X = "b";
//	private String Y = "c";
//	private String Z = "d";
//	private String YAW = "i";
//	private String PITCH = "h";
//	private String DATAWATCHER = "l";
//	private String TYPE = "j";
//	private String ID = "a";
//	private String OBJECT_DATA = "k";

	private String X = "x";
	private String Y = "y";
	private String Z = "z";
	private String YAW = "yaw";
	private String PITCH = "pitch";
	private String TYPE = "typeId";
	private String ID = "eId";
	private String OBJECT_DATA = "data";

	private String velX = "velX";
	private String velY = "velY";
	private String velZ = "velZ";
	
	public WrapperPacketPlayOutSpawnEntity(){
		this.packet=new PacketPlayOutSpawnEntity();
	}
	
	public WrapperPacketPlayOutSpawnEntity(PacketPlayOutSpawnEntity packet){
		this.packet=packet;
	}
	
	public int getObjectData(){
		return ((int)UtilReflection.getValue(OBJECT_DATA, packet ) );
	}
	
	public void setObjectData(int i){
		UtilReflection.setValue(OBJECT_DATA, packet, i);
	}
	
	public int getEntityType(){
		return ((int)UtilReflection.getValue(TYPE, packet ) );
	}
	
	public void setEntityType(int i){
		UtilReflection.setValue(TYPE, packet, i);
	}
	
	public int getEntityID(){
		return (int)UtilReflection.getValue(ID, packet);
	}
	
	public void setEntityID(int id){
		UtilReflection.setValue(ID, packet, id);
	}
	
//	public DataWatcher getDataWatcher(){
//		return (DataWatcher) UtilReflection.getValue(DATAWATCHER, packet);
//	}
//	
//	public void setDataWatcher(DataWatcher watcher){
//		UtilReflection.setValue(DATAWATCHER, packet, watcher);
//	}
	
	public int getVelX(){
		return (int)UtilReflection.getValue(velX, packet);
	}
	
	public int getVelY(){
		return (int)UtilReflection.getValue(velY, packet);
	}
	
	public int getVelZ(){
		return (int)UtilReflection.getValue(velZ, packet);
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
