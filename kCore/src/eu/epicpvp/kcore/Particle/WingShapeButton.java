package eu.epicpvp.kcore.Particle;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.epicpvp.kcore.Inventory.InventoryPageBase;
import eu.epicpvp.kcore.Inventory.Item.Click;
import eu.epicpvp.kcore.Inventory.Item.Buttons.ButtonCopy;
import eu.epicpvp.kcore.Util.UtilEvent.ActionType;
import eu.epicpvp.kcore.Util.UtilItem;
import eu.epicpvp.kcore.Util.UtilServer;

public class WingShapeButton extends ButtonCopy{

	public WingShapeButton(int slot, WingShop shop,WingShape wing) {
		super(new Click(){

			@Override
			public void onClick(Player player, ActionType type, Object object) {
				if(UtilServer.getPermissionManager().hasPermission(player, wing.getPermission())){
					((InventoryPageBase)object).setItem(slot, UtilItem.addEnchantmentGlow(wing.getItem()));
				}else{
					((InventoryPageBase)object).setItem(slot, wing.getItem());
				}
			}
			
		}, new Click(){

			@Override
			public void onClick(Player player, ActionType type, Object object) {
				if(player.isOp() || UtilServer.getPermissionManager().hasPermission(player, wing.getPermission())){
					player.closeInventory();
					if(shop.getPlayers().containsKey(player)){
						PlayerParticle p = shop.getPlayers().get(player);
						shop.getPlayers().remove(player);
						p.stop();
						if(p.getShape().equals(wing))return;
					}
					shop.getPlayers().put(player, new PlayerParticle<>(player, wing));
					shop.getPlayers().get(player).start(shop.getInstance());
				}
			}
			
		}, wing.getItem());
	}

}
