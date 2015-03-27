package me.kingingo.kcore.Interface;

import lombok.Getter;
import me.kingingo.kcore.Interface.Button.InterfaceType;
import me.kingingo.kcore.Inventory.InventoryPageBase;
import me.kingingo.kcore.Inventory.Item.ButtonBase;
import me.kingingo.kcore.Inventory.Item.Click;
import me.kingingo.kcore.Util.UtilEvent.ActionType;
import me.kingingo.kcore.Util.UtilItem;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class InterfaceDeathGames{

	@Getter
	private JavaPlugin instance;
	@Getter
	private InventoryPageBase main_page;
	@Getter
	private InventoryPageBase On_Or_Off;
	@Getter
	private InventoryPageBase Spieler_Leben;
	@Getter
	private InventoryPageBase Chest_Anzahl;
	@Getter
	private InventoryPageBase YesNo;
	@Getter
	private InterfaceType type;
	
	public InterfaceDeathGames(JavaPlugin instance,InterfaceType type){
		this.instance=instance;
		this.type=type;
		this.main_page= new InventoryPageBase(9, "DeathGames Main:");
		this.main_page.setItem(0,UtilItem.Item(new ItemStack(Material.CHEST), new String[]{""}, "§6DeathGames"));
		this.main_page.addButton(2, new ButtonBase(new Click(){

			@Override
			public void onClick(Player player, ActionType type, Object object) {
				player.openInventory(getOn_Or_Off());
			}
			
		}, UtilItem.Item(new ItemStack(Material.LEVER), new String[]{" "}, "§aAn§7/§cAus§7 Einstellungen")));
		this.main_page.addButton(3, new ButtonBase(new Click(){

			@Override
			public void onClick(Player player, ActionType type, Object object) {
				player.openInventory(getSpieler_Leben());
			}
			
		}, UtilItem.Item(new ItemStack(Material.GOLDEN_APPLE), new String[]{" "}, "§7Spieler Leben")));
		this.main_page.addButton(4, new ButtonBase(new Click(){

			@Override
			public void onClick(Player player, ActionType type, Object object) {
				player.openInventory(getChest_Anzahl());
			}
			
		}, UtilItem.Item(new ItemStack(Material.WORKBENCH), new String[]{" "}, "§7Chest Anzahl")));
		
		this.main_page.addButton(8, new ButtonBase(new Click(){

			@Override
			public void onClick(Player player, ActionType type, Object object) {
				
			}
			
		},UtilItem.Item(new ItemStack(Material.ARROW), new String[]{""}, "§aStart")));
		
		this.main_page.fill(Material.STAINED_GLASS_PANE,(byte)15);
	}
	
	
	
}
