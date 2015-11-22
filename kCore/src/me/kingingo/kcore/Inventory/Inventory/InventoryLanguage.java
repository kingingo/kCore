package me.kingingo.kcore.Inventory.Inventory;

import java.util.HashMap;

import me.kingingo.kcore.Inventory.InventoryPageBase;
import me.kingingo.kcore.Inventory.Item.IButton;
import me.kingingo.kcore.Language.LanguageType;
import me.kingingo.kcore.Util.InventorySize;

import org.bukkit.Material;

public class InventoryLanguage{
	
	HashMap<LanguageType,InventoryPageBase> list;
	
	public InventoryLanguage(InventorySize size,String title) {
		for(LanguageType type : LanguageType.values()){
			list.put(type, new InventoryPageBase("InventoryLanguage",size.getSize(), title));
		}
	}
	
	public InventoryPageBase[] getInventorys(){
		return (InventoryPageBase[])list.values().toArray();
	}
	
	public void remove(){
		for(InventoryPageBase page : getInventorys())page.remove();
	}
	
	public void fill(Material material){
		for(InventoryPageBase page : getInventorys())page.fill(material);
	}
	
	public void fill(Material material,int data){
		for(InventoryPageBase page : getInventorys())page.fill(material, data);
	}
	
	public IButton getButton(int slot){
		return list.get(0).getButton(slot);
	}
	
	public void addButton(IButton button){
		for(InventoryPageBase page : getInventorys())page.addButton(button);
	}
	
	public void addButton(int slot,IButton button){
		for(InventoryPageBase page : getInventorys())page.addButton(slot, button);
	}
}
