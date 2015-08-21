package me.kingingo.kcore.PacketAPI.Packets;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.PacketAPI.kPacket;
import me.kingingo.kcore.Util.UtilReflection;
import net.minecraft.server.v1_8_R3.PacketPlayOutSetSlot;

import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class kPacketPlayOutSetSlot implements kPacket{

	@Getter
	@Setter
	private PacketPlayOutSetSlot packet;
	private String INV_ID = "a";
	private String SLOT = "b";
	private String ITEMSTACK = "c";
	
	public kPacketPlayOutSetSlot(PacketPlayOutSetSlot packet){
		this.packet = packet;
	}
	
	public kPacketPlayOutSetSlot(Object packet){
		this.packet = (PacketPlayOutSetSlot)packet;
	}
	
	public kPacketPlayOutSetSlot(){
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
