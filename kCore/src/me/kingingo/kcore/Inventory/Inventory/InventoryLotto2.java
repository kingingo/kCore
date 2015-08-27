package me.kingingo.kcore.Inventory.Inventory;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import lombok.Getter;
import me.kingingo.kcore.Inventory.InventoryPageBase;
import me.kingingo.kcore.Inventory.Item.LottoPackage;
import me.kingingo.kcore.Util.InventorySize;
import me.kingingo.kcore.Util.UtilItem;

public class InventoryLotto2 extends InventoryPageBase{

	public InventoryLotto2(String title,HashMap<InventoryLotto2Type,ArrayList<LottoPackage>> list) {
		super(InventorySize._27, title);
		if(list!=null&&!list.isEmpty()){
			setItem(4, UtilItem.RenameItem(new ItemStack(Material.HOPPER), " "));
		}
	}
	
	public enum InventoryLotto2Type{
		COMMON(Material.STAINED_GLASS_PANE,(byte)0,0),
		UNCOMMON(Material.STAINED_GLASS_PANE,(byte)7,1),
		RARE(Material.STAINED_GLASS_PANE,(byte)6,2),
		LEGENDARY(Material.STAINED_GLASS_PANE,(byte)5,3),
		DIVINE(Material.STAINED_GLASS_PANE,(byte)2,4);
		
		@Getter
		private Material material;
		@Getter
		private byte data;
		@Getter
		private int i;
		InventoryLotto2Type(Material material,byte data,int i){
			this.material=material;
			this.data=data;
			this.i=i;
		}
	}
}
