package me.kingingo.kcore.Pet.Setting;

import me.kingingo.kcore.Inventory.InventoryBase;
import me.kingingo.kcore.Inventory.Inventory.InventoryRename;
import me.kingingo.kcore.Inventory.Item.ButtonBase;
import me.kingingo.kcore.Inventory.Item.Click;
import me.kingingo.kcore.Pet.PetManager;
import me.kingingo.kcore.Util.AnvilGUI.AnvilClickEvent;
import me.kingingo.kcore.Util.AnvilGUI.AnvilClickEventHandler;
import me.kingingo.kcore.Util.UtilEvent.ActionType;

import org.bukkit.Material;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PetSetting extends InventoryBase{

	private InventoryRename inv_rename;
	
	public PetSetting(final PetManager manager,ItemStack type) {
		super(manager.getInstance(),9, "PetSetting");
		this.inv_rename=new InventoryRename(new AnvilClickEventHandler(){

			@Override
			public void onAnvilClick(AnvilClickEvent event) {
				Creature c = manager.getActivePetOwners().get(event.getPlayer().getName());
				c.setCustomName(event.getName().replaceAll("&", "§"));
				c.setCustomNameVisible(true);
				event.setWillDestroy(false);
			}
			
		}, manager.getInstance(), "Name Ändern");
		
		getMain().setItem(1,type);
		getMain().addButton(3, new ButtonBase(new Click(){
			@Override
			public void onClick(Player player, ActionType type) {
				player.closeInventory();
				inv_rename.open(player);
			}
		}, Material.ANVIL, "§aNamen Ändern"));
		getMain().fill(Material.STAINED_GLASS_PANE,1);
	}

}
