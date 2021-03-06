package eu.epicpvp.kcore.Util;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import lombok.Getter;

public enum InventorySplit {
_9(0,8),
_18(9,17),
_27(18,26),
_36(27,35),
_45(36,44),
_54(45,53);
	
@Getter
private int min;
@Getter
private int max;
@Getter
private int middle;

private InventorySplit(int min,int max){
	this.min=min;
	this.max=max;
	this.middle=this.min+4;
}

public void setLine(Material material, byte data,Inventory inv){
	for(int i = getMin(); i <= getMax(); i++)inv.setItem(i, UtilItem.Item(new ItemStack(material,1,data), new String[]{""}, " "));
}

public static int getSlotBorder(int slot){
	for(InventorySplit split : values()){
		if(split.getMin()<=slot&&split.getMax()>=slot){
			if(slot==split.getMax()){
				slot+=3;
			}else{
				slot+=1;
			}
			
			return slot;
		}
	}
	
	return -1;
}

public static int getSlotRelfect(int slot){
	for(InventorySplit split : values()){
		if(split.getMiddle()==slot){
			return slot;
		}else if(slot>=split.getMin()&&slot<split.getMiddle()){
			if(split.getMin()==slot)return slot+8;
			return split.getMax()-(slot-split.getMin());
		}else if(slot<=split.getMax()&&slot>split.getMiddle()){
			if(split.getMax()==slot)return slot-8;
			return split.getMin()+(split.getMax()-slot);
		}
	}
	return -1;
}

public static InventorySplit split(int slot){
	for(InventorySplit split : values())
		if(split.getMin()<=slot&&split.getMax()>=slot)return split;
	return null;
}

}
