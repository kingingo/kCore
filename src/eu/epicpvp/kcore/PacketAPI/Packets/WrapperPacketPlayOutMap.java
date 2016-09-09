package eu.epicpvp.kcore.PacketAPI.Packets;

import eu.epicpvp.kcore.PacketAPI.PacketWrapper;
import eu.epicpvp.kcore.Util.UtilReflection;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.v1_8_R3.MapIcon;
import net.minecraft.server.v1_8_R3.PacketPlayOutMap;

public class WrapperPacketPlayOutMap implements PacketWrapper{
	@Getter
	@Setter
	private PacketPlayOutMap packet;
	private String MAP_ID = "a";
	private String SCALE_VALUE = "b";
	private String MAP_ICONS = "c";
	private String BUFFER = "h";
	
	public WrapperPacketPlayOutMap(){
		this(new PacketPlayOutMap());
	}
	
	public WrapperPacketPlayOutMap(PacketPlayOutMap packet){
		this.packet=packet;
	}
	
	public byte[] getBuffer(){
		return (byte[])UtilReflection.getValue(BUFFER, packet);
	}
	
	public void setBuffer(byte buffer){
		UtilReflection.setValue(BUFFER, packet, buffer);
	}
	
	public MapIcon[] getMapIcons(){
		return (MapIcon[])UtilReflection.getValue(MAP_ICONS, packet);
	}
	
	public void setMapIcons(MapIcon[] icons){
		UtilReflection.setValue(MAP_ICONS, packet, icons);
	}
	
	public byte getScaleValue(){
		return (byte)UtilReflection.getValue(SCALE_VALUE, packet);
	}
	
	public void setScaleValue(byte value){
		UtilReflection.setValue(SCALE_VALUE, packet, value);
	}
	
	public int getMapId(){
		return (int)UtilReflection.getValue(MAP_ID, packet);
	}
	
	public void setMapId(int mapid){
		UtilReflection.setValue(MAP_ID, packet, mapid);
	}
}
