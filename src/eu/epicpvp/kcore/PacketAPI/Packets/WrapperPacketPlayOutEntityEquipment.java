package eu.epicpvp.kcore.PacketAPI.Packets;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import eu.epicpvp.kcore.PacketAPI.PacketWrapper;
import eu.epicpvp.kcore.Util.UtilReflection;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityEquipment;

public class WrapperPacketPlayOutEntityEquipment implements PacketWrapper{
	@Getter
	@Setter
	private PacketPlayOutEntityEquipment packet;
	private String ID = "a";
	private String SLOT = "b";
	private String ITEMSTACK = "c";
	
	public WrapperPacketPlayOutEntityEquipment(PacketPlayOutEntityEquipment packet){
		this.packet=packet;
	}
	
	public WrapperPacketPlayOutEntityEquipment(){
		this(new PacketPlayOutEntityEquipment());
	}
	
	public WrapperPacketPlayOutEntityEquipment(int entityID,EquipmentSlot slot,Material material){
		this(entityID,slot.getS(),material);
	}
	
	public WrapperPacketPlayOutEntityEquipment(int entityID,EquipmentSlot slot,ItemStack item){
		this(entityID,slot.getS(),item);
	}
	
	public WrapperPacketPlayOutEntityEquipment(int entityID,EquipmentSlot slot,net.minecraft.server.v1_8_R3.ItemStack item){
		this(entityID,slot.getS(),item);
	}
	
	public WrapperPacketPlayOutEntityEquipment(int entityID,int slot,Material material){
		this(entityID,slot,new ItemStack(material));
	}
	
	public WrapperPacketPlayOutEntityEquipment(int entityID,int slot,ItemStack item){
		this(entityID,slot,CraftItemStack.asNMSCopy(item));
	}
		
	public WrapperPacketPlayOutEntityEquipment(int entityID,int slot,net.minecraft.server.v1_8_R3.ItemStack item){
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
