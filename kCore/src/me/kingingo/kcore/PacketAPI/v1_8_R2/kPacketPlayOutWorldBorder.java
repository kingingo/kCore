package me.kingingo.kcore.PacketAPI.v1_8_R2;

import lombok.Getter;
import me.kingingo.kcore.PacketAPI.kPacket;
import me.kingingo.kcore.Util.UtilReflection;
import net.minecraft.server.v1_8_R2.PacketPlayOutWorldBorder;
import net.minecraft.server.v1_8_R2.WorldBorder;
import net.minecraft.server.v1_8_R2.PacketPlayOutWorldBorder.EnumWorldBorderAction;

public class kPacketPlayOutWorldBorder implements kPacket{
	@Getter
	private PacketPlayOutWorldBorder packet;
	private String WORLD_BORDER_ACTION = "a";
	private String X = "c";
	private String Z = "d";
	private String SIZE = "f";
	private String DAMAGE_BUFFER = "e";
	private String DAMAGE_AMOUNT = "g";
	private String WARNING_DISTANCE2 = "b";
	private String WARNING_DISTANCE = "i";
	private String WARNING_TIME = "h";
	
	public kPacketPlayOutWorldBorder(){
		packet=new PacketPlayOutWorldBorder();
	}
	
	public kPacketPlayOutWorldBorder(WorldBorder wb,EnumWorldBorderAction action){
		packet=new PacketPlayOutWorldBorder(wb,action);
	}
	
	public EnumWorldBorderAction getAction(){
		return ((EnumWorldBorderAction)UtilReflection.getValue(WORLD_BORDER_ACTION, packet));
	}
	
	public void setAction(EnumWorldBorderAction action){
		UtilReflection.setValue(WORLD_BORDER_ACTION,packet, action);
	}
	
	public void setWorldBorder(WorldBorder wb){
		UtilReflection.setValue(X,packet,wb.getCenterX());
		UtilReflection.setValue(Z,packet,wb.getCenterZ());
		UtilReflection.setValue(SIZE,packet,wb.getSize());
		UtilReflection.setValue(DAMAGE_BUFFER,packet,wb.j());
		UtilReflection.setValue(DAMAGE_AMOUNT,packet,wb.i());
		UtilReflection.setValue(WARNING_DISTANCE2,packet,wb.l());
		UtilReflection.setValue(WARNING_DISTANCE,packet,wb.getWarningDistance());
		UtilReflection.setValue(WARNING_TIME,packet,wb.getWarningTime());
	}

}
