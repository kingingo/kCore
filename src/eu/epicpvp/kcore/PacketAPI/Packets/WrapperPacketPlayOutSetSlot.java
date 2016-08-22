package eu.epicpvp.kcore.PacketAPI.Packets;

import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import eu.epicpvp.kcore.PacketAPI.PacketWrapper;
import eu.epicpvp.kcore.Util.UtilReflection;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.v1_8_R3.PacketPlayOutSetSlot;

public class WrapperPacketPlayOutSetSlot implements PacketWrapper{

	@Getter
	@Setter
	private PacketPlayOutSetSlot packet;
	private String INV_ID = "a";
	private String SLOT = "b";
	private String ITEMSTACK = "c";
	
	public WrapperPacketPlayOutSetSlot(PacketPlayOutSetSlot packet){
		this.packet = packet;
	}
	
	public WrapperPacketPlayOutSetSlot(Object packet){
		this.packet = (PacketPlayOutSetSlot)packet;
	}
	
	public WrapperPacketPlayOutSetSlot(){
		packet = new PacketPlayOutSetSlot();
	}
	
	public void setItemStack(ItemStack item){
		UtilReflection.setValue(ITEMSTACK, packet, CraftItemStack.asNMSCopy(item));
	}
	
	public ItemStack getItemStack(){
		return (ItemStack)CraftItemStack.asBukkitCopy(((net.minecraft.server.v1_8_R3.ItemStack)UtilReflection.getValue(ITEMSTACK, packet)));
	}
	
	public void setInventory(int windowID){
		UtilReflection.setValue(INV_ID, packet,windowID);
	}
	
	public int getInventory(){
		return (int)UtilReflection.getValue(INV_ID, packet);
	}
	
	public void setSlot(int slot){
		UtilReflection.setValue(SLOT, packet,slot);
	}
	
	public int getSlot(){
		return (int)UtilReflection.getValue(SLOT, packet);
	}
	
}
