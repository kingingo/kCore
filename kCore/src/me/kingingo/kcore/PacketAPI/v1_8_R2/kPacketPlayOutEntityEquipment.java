package me.kingingo.kcore.PacketAPI.v1_8_R2;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R2.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import me.kingingo.kcore.PacketAPI.kPacket;
import me.kingingo.kcore.Util.UtilReflection;
import net.minecraft.server.v1_8_R2.PacketPlayOutEntityEquipment;
import lombok.Getter;

public class kPacketPlayOutEntityEquipment implements kPacket{
	@Getter
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
	
	public ItemStack getMaterial(){
		return CraftItemStack.asBukkitCopy(((net.minecraft.server.v1_8_R2.ItemStack) UtilReflection.getValue(ITEMSTACK, packet)));
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
