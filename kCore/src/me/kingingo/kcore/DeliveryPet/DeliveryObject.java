package me.kingingo.kcore.DeliveryPet;

import me.kingingo.kcore.Inventory.Item.Click;
import me.kingingo.kcore.Permission.kPermission;
import me.kingingo.kcore.Util.UtilTime;

import org.bukkit.Material;

public class DeliveryObject {
 String[] description;
 int slot;
 String displayname;
 Material material;
 byte data;
 kPermission permission;
 Click click;
 long time;
 boolean byClickBlock=false;
 
 public DeliveryObject(String[] description,kPermission permission,boolean byClickBlock,int slot,String displayname,Material material,Click click,long time){
	 this.description=description;
	 this.permission=permission;
	 this.byClickBlock=byClickBlock;
	 this.slot=slot;
	 this.displayname=displayname;
	 this.material=material;
	 this.click=click;
	 this.time=time;
 }
 
 public DeliveryObject(String[] description,kPermission permission,boolean byClickBlock,int slot,String displayname,Material material,int data,Click click,long time){
	 this.description=description;
	 this.permission=permission;
	 this.byClickBlock=byClickBlock;
	 this.slot=slot;
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
