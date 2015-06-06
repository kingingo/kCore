package me.kingingo.kcore.Versus;

import org.bukkit.inventory.ItemStack;

public class VersusKit {
public ItemStack helm;
public ItemStack chestplate;
public ItemStack leggings;
public ItemStack boots;
public ItemStack[] inv;

public ItemStack[] toItemArray(){
	ItemStack[] items = new ItemStack[inv.length+4];
	items[0]=helm;
	items[1]=chestplate;
	items[2]=leggings;
	items[3]=boots;
	
	int i=4;
	for(ItemStack item : inv){
		items[i]=item;
		i++;
	}
	return items;
}

public void fromItemArray(ItemStack[] items){
	helm=items[0];
	helm=items[1];
	helm=items[2];
	helm=items[3];
	
	inv = new ItemStack[items.length-4];
	
	int i = 4;
	for(ItemStack item : items){
		inv[i]=item;
		i++;
	}
}

}
