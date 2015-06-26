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
	int i=0;
	for(ItemStack item : inv){
		items[i]=item;
		i++;
	}
	
	items[i]=helm;
	items[i+1]=chestplate;
	items[i+2]=leggings;
	items[i+3]=boots;
	return items;
}

public VersusKit fromItemArray(ItemStack[] items){
	inv = new ItemStack[ ((items.length-4)<5 ? 5 : items.length-4) ];
	int i = 0;
	for(int e = 0; e < items.length-4; e++){
		inv[i]=items[e];
		i++;
	}
	
	helm=items[i];
	chestplate=items[i+1];
	leggings=items[i+2];
	boots=items[i+3];
	return this;
}

}
