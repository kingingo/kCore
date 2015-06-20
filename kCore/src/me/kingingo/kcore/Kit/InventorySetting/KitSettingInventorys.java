package me.kingingo.kcore.Kit.InventorySetting;

import java.util.HashMap;

import lombok.Getter;
import me.kingingo.kcore.Inventory.InventoryBase;
import me.kingingo.kcore.Inventory.InventoryPageBase;
import me.kingingo.kcore.Inventory.Inventory.InventoryChoose;
import me.kingingo.kcore.Inventory.Item.ButtonBase;
import me.kingingo.kcore.Inventory.Item.Click;
import me.kingingo.kcore.Listener.kListener;
import me.kingingo.kcore.Util.InventorySize;
import me.kingingo.kcore.Util.UtilEvent.ActionType;
import me.kingingo.kcore.Util.UtilItem;
import me.kingingo.kcore.Versus.VersusKit;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class KitSettingInventorys extends InventoryBase{

	@Getter
	private JavaPlugin instance;
	@Getter
	private HashMap<Player,InventoryPageBase> inventorys;
	private InventoryChoose helm_choose;
	private int helm_place=4;
	private InventoryChoose chestplate_choose;
	private int chestplate_place=13;
	private InventoryChoose leggings_choose;
	private int leggings_place=22;
	private InventoryChoose boots_choose;
	private int boots_place=31;
	private InventoryChoose main_item_inventory;
	private int main_place_1 = 12;
	private int main_place_2=11;
	private InventoryChoose second_item;
	private int second_place_1=14;
	private int second_place_2=15;
	private int second_place_3=16;
	
	public KitSettingInventorys(JavaPlugin instance){
		super(instance,"KitSettingInventorys");
		this.instance=instance;
		this.inventorys=new HashMap<>();
		this.helm_choose=new InventoryChoose(new Click(){

			@Override
			public void onClick(Player player, ActionType type, Object obj) {
				if(inventorys.containsKey(player)){
					inventorys.get(player).getButton(helm_place).setItemStack( ((ItemStack)obj).clone() );
				}
			}
			
		}, "Helm:", InventorySize._9.getSize(), new ItemStack[]{new ItemStack(Material.LEATHER_HELMET),new ItemStack(Material.IRON_HELMET),new ItemStack(Material.GOLD_HELMET),new ItemStack(Material.DIAMOND_HELMET)});
		
	}
	
	public void addKitInventory(Player player,VersusKit kit){
		InventoryPageBase page = new InventoryPageBase(InventorySize._36.getSize(), player.getName()+"s Kit:");
		page.addButton(helm_place, new ButtonBase(new Click(){

			@Override
			public void onClick(Player player, ActionType type, Object object) {
				if(inventorys.containsKey(player)){
					player.openInventory(helm_choose);
				}
			}
			
		}, kit.helm));
		page.addButton(chestplate_place, new ButtonBase(new Click(){

			@Override
			public void onClick(Player player, ActionType type, Object object) {
				if(inventorys.containsKey(player)){
					player.openInventory(chestplate_choose);
				}
			}
			
		}, kit.chestplate));
		page.addButton(leggings_place, new ButtonBase(new Click(){

			@Override
			public void onClick(Player player, ActionType type, Object object) {
				if(inventorys.containsKey(player)){
					player.openInventory(leggings_choose);
				}
			}
			
		}, kit.leggings));
		page.addButton(boots_place, new ButtonBase(new Click(){

			@Override
			public void onClick(Player player, ActionType type, Object object) {
				if(inventorys.containsKey(player)){
					player.openInventory(boots_choose);
				}
			}
			
		}, kit.boots));
		page.addButton(main_place_1, new ButtonBase(new Click(){

			@Override
			public void onClick(Player player, ActionType type, Object object) {
				if(inventorys.containsKey(player)){
					player.openInventory(main_item_inventory);
				}
			}
			
		}, kit.inv[0]));
		page.addButton(main_place_2, new ButtonBase(new Click(){

			@Override
			public void onClick(Player player, ActionType type, Object object) {
				if(inventorys.containsKey(player)){
					player.openInventory(main_item_inventory);
				}
			}
			
		}, kit.inv[1]));
		page.addButton(second_place_1, new ButtonBase(new Click(){

			@Override
			public void onClick(Player player, ActionType type, Object object) {
				if(inventorys.containsKey(player)){
					player.openInventory(second_item);
				}
			}
			
		}, kit.inv[2]));
		page.addButton(second_place_2, new ButtonBase(new Click(){

			@Override
			public void onClick(Player player, ActionType type, Object object) {
				if(inventorys.containsKey(player)){
					player.openInventory(second_item);
				}
			}
			
		}, kit.inv[3]));
		page.addButton(second_place_3, new ButtonBase(new Click(){

			@Override
			public void onClick(Player player, ActionType type, Object object) {
				if(inventorys.containsKey(player)){
					player.openInventory(second_item);
				}
			}
			
		}, kit.inv[3]));
		inventorys.put(player, page);
	}
}
