package me.kingingo.kcore.PrivatServer.Interface;

import java.util.HashMap;

import lombok.Getter;
import me.kingingo.kcore.Inventory.Inventory.InventoryChoose;
import me.kingingo.kcore.Inventory.Inventory.InventoryYesNo;
import me.kingingo.kcore.Inventory.Item.ButtonBase;
import me.kingingo.kcore.Inventory.Item.Click;
import me.kingingo.kcore.Packet.Packets.SERVER_SETTINGS;
import me.kingingo.kcore.PrivatServer.Interface.Button.GameInterface;
import me.kingingo.kcore.PrivatServer.Interface.Button.MainInterface;
import me.kingingo.kcore.PrivatServer.Interface.Button.UtilInterface;
import me.kingingo.kcore.Util.UtilEvent.ActionType;
import me.kingingo.kcore.Util.UtilItem;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class DeathGamesInterface extends GameInterface{
	@Getter
	private InventoryYesNo On_Or_Off;
	@Getter
	private HashMap<Player,Boolean> kits = new HashMap<>();
	@Getter
	private InventoryChoose inv_chest_Anzahl;
	@Getter
	private HashMap<Player,Integer> chest_anzahl = new HashMap<>();
	private DeathGamesInterface m;
	
	public DeathGamesInterface(MainInterface main){
		super(main,Material.CHEST,"DeathGames");
		this.m=this;
		
		getMenu().addButton(8, new ButtonBase(new Click(){

			@Override
			public void onClick(Player player, ActionType type, Object object) {
				getMain().start(player,new SERVER_SETTINGS("DeathGames",UtilInterface.DGtoString(m,player),player.getName(),getMain().getServer(), (getApublic().containsKey(player) ? getApublic().get(player) : false) ));
			}
			
		},UtilItem.Item(new ItemStack(Material.ARROW), new String[]{""}, "§aStart")));
		
		On_Or_Off = new InventoryYesNo("Kits On/Off", new Click(){

			@Override
			public void onClick(Player player, ActionType type, Object object) {
				if(kits.containsKey(player))kits.remove(player);
				kits.put(player, true);
				player.closeInventory();
				player.openInventory(getMenu());
			}}, new Click(){

			@Override
			public void onClick(Player player, ActionType type,Object object) {
				if(kits.containsKey(player))kits.remove(player);
				kits.put(player, false);
				player.closeInventory();
				player.openInventory(getMenu());
			}}
		);
		getMain().getMain_page().addPage(On_Or_Off);
		
		getMenu().addButton(3, new ButtonBase(new Click(){

			@Override
			public void onClick(Player player, ActionType type, Object object) {
				player.openInventory(getOn_Or_Off());
			}
			
		}, UtilItem.Item(new ItemStack(Material.LEVER), new String[]{" "}, "§bKits §aAn§7/§cAus§7")));
		
		inv_chest_Anzahl=new InventoryChoose(new Click(){

			@Override
			public void onClick(Player player, ActionType type, Object object) {
				if(object instanceof ItemStack){
					if(chest_anzahl.containsKey(player))chest_anzahl.remove(player);
					chest_anzahl.put(player, ((ItemStack)object).getAmount());
				}
				player.closeInventory();
				player.openInventory(getMenu());
			}
			
		}, "Chest Anzahl:", 9, new ItemStack[]{UtilItem.RenameItem(new ItemStack(Material.CHEST), "50"),UtilItem.RenameItem(new ItemStack(Material.CHEST), "60"),UtilItem.RenameItem(new ItemStack(Material.CHEST), "70"),UtilItem.RenameItem(new ItemStack(Material.CHEST), "80"),UtilItem.RenameItem(new ItemStack(Material.CHEST), "90"),UtilItem.RenameItem(new ItemStack(Material.CHEST), "100"),UtilItem.RenameItem(new ItemStack(Material.CHEST), "110"),UtilItem.RenameItem(new ItemStack(Material.CHEST), "120"),UtilItem.RenameItem(new ItemStack(Material.CHEST), "130")});
		getMain().getMain_page().addPage(inv_chest_Anzahl);
		
		getMenu().addButton(4, new ButtonBase(new Click(){

			@Override
			public void onClick(Player player, ActionType type, Object object) {
				player.openInventory(inv_chest_Anzahl);
			}
			
		}, UtilItem.Item(new ItemStack(Material.WORKBENCH), new String[]{" "}, "§bChest Anzahl")));
		
		getMenu().fill(Material.STAINED_GLASS_PANE,(byte)15);
	}
	
//	public DeathGamesInterface(){
//		super(null,Material.APPLE,"null");
//		
//	}
	
}
