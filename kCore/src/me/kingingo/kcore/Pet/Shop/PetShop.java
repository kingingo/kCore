package me.kingingo.kcore.Pet.Shop;

import lombok.Getter;
import me.kingingo.kcore.Inventory.InventoryBase;
import me.kingingo.kcore.Inventory.Inventory.InventoryBuy;
import me.kingingo.kcore.Inventory.Item.Click;
import me.kingingo.kcore.Inventory.Item.SalesPackageBase;
import me.kingingo.kcore.Permission.Permission;
import me.kingingo.kcore.Permission.PermissionManager;
import me.kingingo.kcore.Pet.PetManager;
import me.kingingo.kcore.Pet.Setting.PetSetting;
import me.kingingo.kcore.Util.Coins;
import me.kingingo.kcore.Util.Tokens;
import me.kingingo.kcore.Util.UtilEvent.ActionType;
import me.kingingo.kcore.Util.UtilItem;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PetShop extends InventoryBase{

	@Getter
	private PetManager manager;
	
	public PetShop(final PetManager manager,final PermissionManager permManager,final Coins coins,final Tokens tokens){
		super(manager.getInstance(),27,"Pet-Shop");
		this.manager=manager;
		this.manager.setSetting(true);
		
		this.manager.getSetting_list().put(EntityType.IRON_GOLEM, new PetSetting(manager,EntityType.IRON_GOLEM,UtilItem.RenameItem(new ItemStack(Material.IRON_BLOCK), "§aIronGolem")));
		this.manager.getSetting_list().put(EntityType.PIG, new PetSetting(manager,EntityType.PIG,UtilItem.RenameItem(new ItemStack(Material.MONSTER_EGG,1,(byte) 90), "§aPig")));
		this.manager.getSetting_list().put(EntityType.WOLF, new PetSetting(manager,EntityType.WOLF,UtilItem.RenameItem(new ItemStack(Material.MONSTER_EGG,1,(byte) 95), "§aWolf")));
		
		getMain().setItem(0, UtilItem.RenameItem(new ItemStack(160,1,(byte)7)," "));
		getMain().setItem(1, UtilItem.RenameItem(new ItemStack(160,1,(byte)7)," "));
		getMain().setItem(2, UtilItem.RenameItem(new ItemStack(160,1,(byte)7)," "));
		getMain().setItem(3, UtilItem.RenameItem(new ItemStack(160,1,(byte)7)," "));
		getMain().setItem(4, UtilItem.RenameItem(new ItemStack(160,1,(byte)7)," "));
		getMain().setItem(5, UtilItem.RenameItem(new ItemStack(160,1,(byte)7)," "));
		getMain().setItem(6, UtilItem.RenameItem(new ItemStack(160,1,(byte)7)," "));
		getMain().setItem(7, UtilItem.RenameItem(new ItemStack(160,1,(byte)7)," "));
		getMain().setItem(8, UtilItem.RenameItem(new ItemStack(160,1,(byte)7)," "));	
		getMain().setItem(9, UtilItem.RenameItem(new ItemStack(160,1,(byte)7)," "));
		getMain().setItem(17, UtilItem.RenameItem(new ItemStack(160,1,(byte)7)," "));	
		getMain().setItem(18, UtilItem.RenameItem(new ItemStack(160,1,(byte)7)," "));
		getMain().setItem(19, UtilItem.RenameItem(new ItemStack(160,1,(byte)7)," "));
		getMain().setItem(20, UtilItem.RenameItem(new ItemStack(160,1,(byte)7)," "));
		getMain().setItem(21, UtilItem.RenameItem(new ItemStack(160,1,(byte)7)," "));
		getMain().setItem(22, UtilItem.RenameItem(new ItemStack(160,1,(byte)7)," "));
		getMain().setItem(23, UtilItem.RenameItem(new ItemStack(160,1,(byte)7)," "));
		getMain().setItem(24, UtilItem.RenameItem(new ItemStack(160,1,(byte)7)," "));
		getMain().setItem(25, UtilItem.RenameItem(new ItemStack(160,1,(byte)7)," "));
		getMain().setItem(26, UtilItem.RenameItem(new ItemStack(160,1,(byte)7)," "));
		
		getMain().addButton(10, new SalesPackageBase(new Click(){
			public void onClick(Player player, ActionType type,Object object) {
				if(permManager.hasPermission(player, Permission.PET_IRON_GOLEM)){
					manager.AddPetOwner(player, "IronGolem", EntityType.IRON_GOLEM, player.getLocation());
					player.closeInventory();
				}else{
					InventoryBuy buy = new InventoryBuy(new Click(){
					@Override
					public void onClick(Player player, ActionType type,Object object) {
						permManager.addPermission(player, Permission.PET_IRON_GOLEM);
					}
					
				},"Kaufen",coins,6000,tokens,150);
				player.openInventory(buy);
				addAnother(buy);
				}
			}
			
		}, Material.IRON_BLOCK, "IronGolem", new String[]{""}));
		
		getMain().addButton(11, new SalesPackageBase(new Click(){
			public void onClick(Player player, ActionType type,Object object) {
				if(permManager.hasPermission(player, Permission.PET_WOLF)){
					manager.AddPetOwner(player, "Wolf", EntityType.WOLF, player.getLocation());
					player.closeInventory();
				}else{
					InventoryBuy buy = new InventoryBuy(new Click(){

						@Override
						public void onClick(Player player, ActionType type,Object object) {
							permManager.addPermission(player, Permission.PET_WOLF);
						}
						
					},"Kaufen",coins,4000,tokens,100);
					player.openInventory(buy);
					addAnother(buy);
				}
			}
			
		}, Material.MONSTER_EGG,95, "Wolf", new String[]{""}));
		
		getMain().addButton(12, new SalesPackageBase(new Click(){
			public void onClick(Player player, ActionType type,Object object) {
				if(permManager.hasPermission(player, Permission.PET_PIG)){
					manager.AddPetOwner(player, "Pig", EntityType.PIG, player.getLocation());
					player.closeInventory();
				}else{
					InventoryBuy buy = new InventoryBuy(new Click(){

					@Override
					public void onClick(Player player, ActionType type,Object object) {
						permManager.addPermission(player, Permission.PET_PIG);
					}
					
					},"Kaufen",coins,4000,tokens,100);
					player.openInventory(buy);
					addAnother(buy);
				}
			}
			
		}, Material.MONSTER_EGG,90, "Pig", new String[]{""}));
	}
	
}
