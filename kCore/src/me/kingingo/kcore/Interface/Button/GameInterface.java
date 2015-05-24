package me.kingingo.kcore.Interface.Button;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.Inventory.InventoryPageBase;
import me.kingingo.kcore.Inventory.Inventory.InventoryChoose;
import me.kingingo.kcore.Inventory.Inventory.InventoryYesNo;
import me.kingingo.kcore.Inventory.Item.ButtonBase;
import me.kingingo.kcore.Inventory.Item.Click;
import me.kingingo.kcore.Util.UtilItem;
import me.kingingo.kcore.Util.UtilEvent.ActionType;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class GameInterface {


	@Getter
	private MainInterface main;
	@Getter
	private InventoryPageBase menu;
	@Getter
	private InventoryYesNo inv_public;
	@Getter
	private boolean apublic=true;
	
	public GameInterface(MainInterface main){
		this.main=main;
		this.menu=new InventoryPageBase(main.getInstance(), 9, "Menue:");
		
		inv_public = new InventoryYesNo("Public On/Off", new Click(){

			@Override
			public void onClick(Player player, ActionType type, Object object) {
				apublic=true;
				player.closeInventory();
				player.openInventory(getMenu());
			}}, new Click(){

			@Override
			public void onClick(Player player, ActionType type,Object object) {
				apublic=false;
				player.closeInventory();
				player.openInventory(getMenu());
			}}
		);
		
		getMenu().addButton(2, new ButtonBase(new Click(){

			@Override
			public void onClick(Player player, ActionType type, Object object) {
				player.openInventory(inv_public);
			}
			
		}, UtilItem.Item(new ItemStack(Material.CAKE), new String[]{" "}, "§bPublic §aAn§7/§cAus§7")));
	}
}
