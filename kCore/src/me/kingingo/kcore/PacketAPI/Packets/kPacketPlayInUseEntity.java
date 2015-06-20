package me.kingingo.kcore.PacketAPI.Packets;

import lombok.Getter;
import me.kingingo.kcore.PacketAPI.kPacket;
import me.kingingo.kcore.Util.UtilReflection;
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity.EnumEntityUseAction;
import net.minecraft.server.v1_8_R3.Vec3D;

public class kPacketPlayInUseEntity implements kPacket{
	@Getter
	private PacketPlayInUseEntity packet;
	private String ID = "a";
	private String ENUM_ENTITY_USE_ACTION = "action";
	private String VEC3D = "c";
	
	public kPacketPlayInUseEntity(PacketPlayInUseEntity packet){
		this.packet=packet;
	}
	
	public kPacketPlayInUseEntity(Object packet){
		this.packet=(PacketPlayInUseEntity)packet;
	}

	public int getEntityID(){
		return (int)UtilReflection.getValue(ID, packet);
	}
	
	public void setEntityID(int id){
		UtilReflection.setValue(ID, packet, id);
	}
	
	public EnumEntityUseAction getAction(){
		return (EnumEntityUseAction)UtilReflection.getValue(ENUM_ENTITY_USE_ACTION, packet);
	}
	
	public void setAction(EnumEntityUseAction action){
		UtilReflection.setValue(ENUM_ENTITY_USE_ACTION, packet, action);
	}
	
	public Vec3D getVec3D(){
		return (Vec3D)UtilReflection.getValue(VEC3D, packet);
	}
	
	public void setVec3D(Vec3D v){
		UtilReflection.setValue(VEC3D, packet, v);
	}
	
}
