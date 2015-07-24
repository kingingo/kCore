package me.kingingo.kcore.PacketAPI.Packets;

import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityVelocity;
import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.PacketAPI.kPacket;
import me.kingingo.kcore.Util.UtilReflection;

public class kPacketPlayOutEntityVelocity implements kPacket{
	@Getter
	@Setter
	private PacketPlayOutEntityVelocity packet;
	private String ENTITY_ID = "a";
	private String X = "b";
	private String Y = "c";
	private String Z = "d";
	
	public kPacketPlayOutEntityVelocity(){
		this.packet=new PacketPlayOutEntityVelocity();
	}
	
	public kPacketPlayOutEntityVelocity(PacketPlayOutEntityVelocity packet){
		this.packet=packet;
	}
	
	public int getX(){
		return (int)UtilReflection.getValue(X, packet);
	}
	
	public void setX(int x){
		UtilReflection.setValue(X, packet, x);
	}
	
	public int getY(){
		return (int)UtilReflection.getValue(Y, packet);
	}
	
	public void setY(int y){
		UtilReflection.setValue(Y, packet, y);
	}
	
	public int getZ(){
		return (int)UtilReflection.getValue(Z, packet);
	}
	
	public void setZ(int z){
		UtilReflection.setValue(Z, packet, z);
	}
	
	public int getEntityID(){
		return (int)UtilReflection.getValue(ENTITY_ID, packet);
	}
	
	public void setEntityID(int id){
		UtilReflection.setValue(ENTITY_ID, packet, id);
	}
	
}
