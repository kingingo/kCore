package me.kingingo.kcore.DeliveryPet;

import me.kingingo.kcore.Inventory.Item.Click;
import me.kingingo.kcore.Permission.kPermission;

import org.bukkit.Material;

public class DeliveryObject {
 String[] description;
 int slot;
 String displayname;
 Material material;
 kPermission permission;
 Click click;
 int time; // in STD
 
 public DeliveryObject(String[] description,kPermission permission,int slot,String displayname,Material material,Click click,int time){
	 this.description=description;
	 this.permission=permission;
	 this.slot=slot;
	 this.displayname=displayname;
	 this.material=material;
	 this.click=click;
	 this.time=time;
 }
 
}
