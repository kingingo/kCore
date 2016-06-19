package eu.epicpvp.kcore.Inventory.Inventory;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import eu.epicpvp.kcore.Inventory.InventoryPageBase;
import eu.epicpvp.kcore.Inventory.Item.Click;
import eu.epicpvp.kcore.Inventory.Item.Buttons.ButtonBase;
import eu.epicpvp.kcore.Util.InventorySize;

public class InventoryChoose extends InventoryPageBase{
	

	public InventoryChoose(Click click,int start_line,String Title,InventorySize size,ItemStack[] item) {
		this(click,start_line,Title,size.getSize(),item);
	}
	
	public InventoryChoose(Click click,String Title,InventorySize size,ItemStack[] item) {
		this(click,Title,size.getSize(),item);
	}
	
	public InventoryChoose(Click click,int start_line,String Title,int size,ItemStack[] item) {
		super("InventoryChoose",size,Title);
		for(ItemStack i : item){
			if(i==null||i.getType()==Material.AIR)continue;
			addButton(start_line,new ButtonBase(click,i));
			start_line++;
		}
		fill(Material.STAINED_GLASS_PANE,7);
	}
	
	public InventoryChoose(Click click,int start_line,String Title,ItemStack[] item) {
		super(item.length,Title);
		for(ItemStack i : item){
			if(i==null||i.getType()==Material.AIR)continue;
			addButton(start_line,new ButtonBase(click,i));
			start_line++;
		}
		fill(Material.STAINED_GLASS_PANE,7);
	}
	
	public InventoryChoose(Click click,String Title,int size,ItemStack[] item) {
		super(size,Title);
		for(ItemStack i : item){
			if(i==null||i.getType()==Material.AIR)continue;
			addButton(new ButtonBase(click,i));
		}
		fill(Material.STAINED_GLASS_PANE,7);
	}
}
