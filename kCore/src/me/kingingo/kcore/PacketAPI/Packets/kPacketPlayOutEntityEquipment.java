package me.kingingo.kcore.PacketAPI.Packets;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import me.kingingo.kcore.PacketAPI.kPacket;
import me.kingingo.kcore.Util.UtilReflection;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityEquipment;
import lombok.Getter;
import lombok.Setter;

public class kPacketPlayOutEntityEquipment implements kPacket{
	@Getter
	@Setter
	private PacketPlayOutEntityEquipment packet;
	private String ID = "a";
	private String SLOT = "b";
	private String ITEMSTACK = "c";
	
	public kPacketPlayOutEntityEquipment(){
		packet = new PacketPlayOutEntityEquipment();
	}
	
	public kPacketPlayOutEntityEquipment(int entityID,int slot,Material material){
		packet = new PacketPlayOutEntityEquipment();
		setEntityID(entityID);
		setSlot(slot);
		setMaterial(material);
	}
	
	public kPacketPlayOutEntityEquipment(int entityID,int slot,net.minecraft.server.v1_8_R3.ItemStack item){
		packet = new PacketPlayOutEntityEquipment();
		setEntityID(entityID);
		setSlot(slot);
		setItemStack(item);
	}
	
	public ItemStack getMaterial(){
		return CraftItemStack.asBukkitCopy(((net.minecraft.server.v1_8_R3.ItemStack) UtilReflection.getValue(ITEMSTACK, packet)));
	}
	
	public void setItemStack(net.minecraft.server.v1_8_R3.ItemStack item){
		UtilReflection.setValue(ITEMSTACK, packet, item);
	}
	
	public void setItemStack(ItemStack item){
		UtilReflection.setValue(ITEMSTACK, packet, CraftItemStack.asNMSCopy(item));
	}
	
	public void setMaterial(Material material){
		UtilReflection.setValue(ITEMSTACK, packet, material == Material.AIR || material == null ? CraftItemStack.asNMSCopy(new ItemStack(Material.AIR)) : CraftItemStack.asNMSCopy(new ItemStack(material)));
	}
	
	public int getSlot(){
		return (int)UtilReflection.getValue(SLOT, packet);
	}
	
	public void setSlot(int s){
		UtilReflection.setValue(SLOT, packet, s);
	}
	
	public int getEntityID(){
		return (int)UtilReflection.getValue(ID, packet);
	}
	
	public void setEntityID(int id){
		UtilReflection.setValue(ID, packet, id);
	}
}
