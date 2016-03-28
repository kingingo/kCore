package eu.epicpvp.kcore.DeliveryPet;

import org.bukkit.Material;

import eu.epicpvp.kcore.Inventory.Item.Click;
import eu.epicpvp.kcore.Permission.PermissionType;
import eu.epicpvp.kcore.Util.UtilTime;
import lombok.Getter;

public class DeliveryObject {
 String[] description;
 int slot;
 String displayname;
 Material material;
 byte data;
 @Getter
 Material delay_material;
 byte delay_data;
 PermissionType permission;
 Click click;
 long time;
 boolean byClickBlock=false;
 
 public DeliveryObject(String[] description,PermissionType permission,boolean byClickBlock,int slot,String displayname,Material material,Material delay_material,Click click,long time){
	 this(description,permission,byClickBlock,slot,displayname,material,0,delay_material,0,click,time);
 }
 
 public DeliveryObject(String[] description,PermissionType permission,boolean byClickBlock,int slot,String displayname,Material material,Click click,long time){
	 this(description,permission,byClickBlock,slot,displayname,material,0,Material.REDSTONE_BLOCK,0,click,time);
 }
 
 public DeliveryObject(String[] description,PermissionType permission,boolean byClickBlock,int slot,String displayname,Material material,int data,Click click,long time){
	 this(description,permission,byClickBlock,slot,displayname,material,data,Material.REDSTONE_BLOCK,0,click,time);
 }
 
 public DeliveryObject(String[] description,PermissionType permission,boolean byClickBlock,int slot,String displayname,Material material,int data,Material delay_material,int delay_data,Click click,long time){
	 this.description=description;
	 this.permission=permission;
	 this.byClickBlock=byClickBlock;
	 this.slot=slot;
	 this.delay_material=delay_material;
	 this.delay_data=(byte)delay_data;
	 this.displayname=displayname;
	 this.material=material;
	 this.data=(byte)data;
	 this.click=click;
	 this.time=time;
 }
 
 public long getTime(){
	 if(time==-1){
		 return UtilTime.getEndOfDay().getTime()-System.currentTimeMillis();
	 }else{
		 return time;
	 }
 }
 
}
