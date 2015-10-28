package me.kingingo.kcore.PacketAPI.Packets;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.PacketAPI.kPacket;
import me.kingingo.kcore.Util.UtilReflection;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityEquipment;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class kPacketPlayOutEntityEquipment implements kPacket{
	@Getter
	@Setter
	private PacketPlayOutEntityEquipment packet;
	private String ID = "a";
	private String SLOT = "b";
	private String ITEMSTACK = "c";
	
	public kPacketPlayOutEntityEquipment(PacketPlayOutEntityEquipment packet){
		this.packet=packet;
	}
	
	public kPacketPlayOutEntityEquipment(){
		this(new PacketPlayOutEntityEquipment());
	}
	
	public kPacketPlayOutEntityEquipment(int entityID,EquipmentSlot slot,Material material){
		this(entityID,slot.getS(),material);
	}
	
	public kPacketPlayOutEntityEquipment(int entityID,EquipmentSlot slot,ItemStack item){
		this(entityID,slot.getS(),item);
	}
	
	public kPacketPlayOutEntityEquipment(int entityID,EquipmentSlot slot,net.minecraft.server.v1_8_R3.ItemStack item){
		this(entityID,slot.getS(),item);
	}
	
	public kPacketPlayOutEntityEquipment(int entityID,int slot,Material material){
		this(entityID,slot,new ItemStack(material));
	}
	
	public kPacketPlayOutEntityEquipment(int entityID,int slot,ItemStack item){
		this(entityID,slot,CraftItemStack.asNMSCopy(item));
	}
		
	public kPacketPlayOutEntityEquipment(int entityID,int slot,net.minecraft.server.v1_8_R3.ItemStack item){
		this(new PacketPlayOutEntityEquipment());
		setEntityID(entityID);
		setSlot(slot);
		setItemStack(item);
	}
	
	public ItemStack getItemStack(){
		return CraftItemStack.asBukkitCopy(((net.minecraft.server.v1_8_R3.ItemStack) UtilReflection.getValue(ITEMSTACK, packet)));
	}
	
	public void setItemStack(net.minecraft.server.v1_8_R3.ItemStack item){
		UtilReflection.setValue(ITEMSTACK, packet, item);
	}
	
	public void setItemStack(ItemStack item){
		UtilReflection.setValue(ITEMSTACK, packet, CraftItemStack.asNMSCopy(item));
	}
	
	public EquipmentSlot getEquipmentSlot(){
		int s = getSlot();
		for(EquipmentSlot e : EquipmentSlot.values()){
			if(e.getS()==s)return e;
		}
		return null;
	}
	
	public int getSlot(){
		return (int)UtilReflection.getValue(SLOT, packet);
	}
	
	public void setSlot(EquipmentSlot s){
		setSlot(s.getS());
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
	
	public enum EquipmentSlot{
		HAND(0),
		HELM(4),
		CHESTPLATE(3),
		LEGGINGS(2),
		BOOTS(1);
		
		@Getter
		private int s=0;
		private EquipmentSlot(int s){
			this.s=s;
		}
	}
}
