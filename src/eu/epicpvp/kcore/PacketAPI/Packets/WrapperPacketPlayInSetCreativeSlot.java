package eu.epicpvp.kcore.PacketAPI.Packets;

import eu.epicpvp.kcore.PacketAPI.PacketWrapper;
import eu.epicpvp.kcore.Util.UtilReflection;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.v1_8_R3.ItemStack;
import net.minecraft.server.v1_8_R3.PacketPlayInSetCreativeSlot;

public class WrapperPacketPlayInSetCreativeSlot implements PacketWrapper{
	@Getter
	@Setter
	private PacketPlayInSetCreativeSlot packet;
	private String ITEM = "b";
	private String SLOT = "slot";
	
	public WrapperPacketPlayInSetCreativeSlot(){
		packet=new PacketPlayInSetCreativeSlot();
	}
	
	public WrapperPacketPlayInSetCreativeSlot(PacketPlayInSetCreativeSlot packet){
		this.packet=packet;
	}
	
	public void setSlot(int slot){
		UtilReflection.setValue(SLOT, packet, slot);
	}
	
	public void setItem(ItemStack item){
		UtilReflection.setValue(ITEM, packet, item);
	}

	public int getSlot(){
		return (int)UtilReflection.getValue(SLOT, packet);
	}
	
	public ItemStack getItem(){
		return (ItemStack)UtilReflection.getValue(ITEM, packet);
	}
	
}
