package eu.epicpvp.kcore.GagdetShop;

import org.bukkit.entity.Player;

import eu.epicpvp.kcore.GagdetShop.Gagdet.Gadget;
import eu.epicpvp.kcore.Inventory.InventoryPageBase;
import eu.epicpvp.kcore.Inventory.Item.Click;
import eu.epicpvp.kcore.Inventory.Item.Buttons.ButtonCopy;
import eu.epicpvp.kcore.Translation.TranslationHandler;
import eu.epicpvp.kcore.Util.UtilEvent.ActionType;
import eu.epicpvp.kcore.Util.UtilItem;

public class GadgetButton extends ButtonCopy{

	public GadgetButton(int slot, Gadget gadget) {
		super(new Click(){

			@Override
			public void onClick(Player player, ActionType type, Object object) {
				if(gadget.getActive_player().containsKey(player)){
					((InventoryPageBase)object).setItem(slot, UtilItem.setLore(UtilItem.addEnchantmentGlow(gadget.getItem()), new String[]{"§aOwn: §e"+gadget.getAmount(player)}));
				}else{
			    	((InventoryPageBase)object).setItem(slot, UtilItem.setLore(gadget.getItem(), new String[]{"§aOwn: §e"+gadget.getAmount(player)}));
				}
			}
			
		}, new Click(){

			@Override
			public void onClick(Player player, ActionType type, Object object) {
				if(gadget.getActive_player().containsKey(player)){
					gadget.removePlayer(player);
				}else if(gadget.getAmount(player) > 0 || player.isOp()){
					gadget.addPlayer(player, (player.isOp() ? 999 : gadget.getAmount(player)));
				}else{
					player.sendMessage(TranslationHandler.getPrefixAndText(player, "IN_MYSTERYBOXES"));
				}

				player.closeInventory();
			}
			
		}, gadget.getItem());
	}

}
