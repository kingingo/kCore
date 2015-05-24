package me.kingingo.kcore.Interface;

import lombok.Getter;
import me.kingingo.kcore.Interface.Button.GameInterface;
import me.kingingo.kcore.Interface.Button.MainInterface;
import me.kingingo.kcore.Interface.Button.UtilInterface;
import me.kingingo.kcore.Inventory.Inventory.InventoryChoose;
import me.kingingo.kcore.Inventory.Inventory.InventoryYesNo;
import me.kingingo.kcore.Inventory.Item.ButtonBase;
import me.kingingo.kcore.Inventory.Item.Click;
import me.kingingo.kcore.Packet.Packets.SERVER_SETTINGS;
import me.kingingo.kcore.Util.UtilEvent.ActionType;
import me.kingingo.kcore.Util.UtilItem;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class DeathGamesInterface extends GameInterface{
	@Getter
	private InventoryYesNo On_Or_Off;
	@Getter
	private boolean kits=true;
	@Getter
	private InventoryChoose inv_chest_Anzahl;
	@Getter
	private int chest_anzahl=120;
	private DeathGamesInterface m;
	
	public DeathGamesInterface(MainInterface main){
		super(main);
		this.m=this;
		getMenu().setItem(0,UtilItem.Item(new ItemStack(Material.CHEST), new String[]{""}, "§6DeathGames"));
		
		getMenu().addButton(8, new ButtonBase(new Click(){

			@Override
			public void onClick(Player player, ActionType type, Object object) {
				getMain().start(player,new SERVER_SETTINGS("DeathGames",UtilInterface.DGtoString(m),player.getName(),getMain().getServer(),isApublic()));
			}
			
		},UtilItem.Item(new ItemStack(Material.ARROW), new String[]{""}, "§aStart")));
		
		On_Or_Off = new InventoryYesNo("Kits On/Off", new Click(){

			@Override
			public void onClick(Player player, ActionType type, Object object) {
				kits=true;
				player.closeInventory();
				player.openInventory(getMenu());
			}}, new Click(){

			@Override
			public void onClick(Player player, ActionType type,Object object) {
				kits=false;
				player.closeInventory();
				player.openInventory(getMenu());
			}}
		);
		
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
					chest_anzahl=((ItemStack)object).getAmount();
				}
				player.closeInventory();
				player.openInventory(getMenu());
			}
			
		}, "Chest Anzahl:", 9, new ItemStack[]{new ItemStack(Material.CHEST,50),new ItemStack(Material.CHEST,60),new ItemStack(Material.CHEST,70),new ItemStack(Material.CHEST,80),new ItemStack(Material.CHEST,90),new ItemStack(Material.CHEST,100),new ItemStack(Material.CHEST,110),new ItemStack(Material.CHEST,120),new ItemStack(Material.CHEST,130)});
		
		getMenu().addButton(4, new ButtonBase(new Click(){

			@Override
			public void onClick(Player player, ActionType type, Object object) {
				player.openInventory(inv_chest_Anzahl);
			}
			
		}, UtilItem.Item(new ItemStack(Material.WORKBENCH), new String[]{" "}, "§bChest Anzahl")));
		
		getMenu().fill(Material.STAINED_GLASS_PANE,(byte)15);
	}
	
	public DeathGamesInterface(int chest,boolean kits){
		super(null);
		this.chest_anzahl=chest;
		this.kits=kits;
	}
}
