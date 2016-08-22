package eu.epicpvp.kcore.PacketAPI.Packets;

import eu.epicpvp.kcore.PacketAPI.PacketWrapper;
import eu.epicpvp.kcore.Util.UtilReflection;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldBorder;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldBorder.EnumWorldBorderAction;
import net.minecraft.server.v1_8_R3.WorldBorder;

public class WrapperPacketPlayOutWorldBorder implements PacketWrapper{
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
	
	public WrapperPacketPlayOutWorldBorder(){
		packet=new PacketPlayOutWorldBorder();
	}
	
	public WrapperPacketPlayOutWorldBorder(WorldBorder wb,EnumWorldBorderAction action){
		packet=new PacketPlayOutWorldBorder(wb,action);
	}
	
	public EnumWorldBorderAction getAction(){
		return ((EnumWorldBorderAction)UtilReflection.getValue(WORLD_BORDER_ACTION, packet));
	}
	
	public void setAction(EnumWorldBorderAction action){
		UtilReflection.setValue(WORLD_BORDER_ACTION,packet, action);
	}
	
	public int getWarningTime(){
		return (int) UtilReflection.getValue(WARNING_TIME, packet);
	}
	
	public int getDamageBuffer(){
		return (int) UtilReflection.getValue(DAMAGE_BUFFER, packet);
	}
	
	public int getDamageAmount(){
		return (int) UtilReflection.getValue(DAMAGE_AMOUNT, packet);
	}
	
	public int getWarningDistance(){
		return (int) UtilReflection.getValue(WARNING_DISTANCE, packet);
	}
	
	public double getSize(){
		return (double) UtilReflection.getValue(SIZE, packet);
	}
	
	public double getMidZ(){
		return (double) UtilReflection.getValue(Z, packet);
	}
	
	public double getMidX(){
		return (double) UtilReflection.getValue(X, packet);
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
