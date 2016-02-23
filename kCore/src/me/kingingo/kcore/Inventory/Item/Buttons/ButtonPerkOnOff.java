package me.kingingo.kcore.Inventory.Item.Buttons;
import lombok.Getter;
import me.kingingo.kcore.Inventory.InventoryPageBase;
import me.kingingo.kcore.Inventory.Item.Click;
import me.kingingo.kcore.Kit.Perk;
import me.kingingo.kcore.Packet.Packets.PLAYER_LANGUAGE_CHANGE;
import me.kingingo.kcore.Util.UtilEvent;
import me.kingingo.kcore.Util.UtilItem;
import me.kingingo.kcore.Util.UtilPlayer;
import me.kingingo.kcore.Util.UtilServer;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ButtonPerkOnOff extends ButtonCopy{
	
  public ButtonPerkOnOff(int slot, int toggle_slot, Perk perk)
  {
    super(new Click(){
      public void onClick(Player player, UtilEvent.ActionType type, Object object){
    	  
    	((InventoryPageBase)object).setItem(slot, perk.getItem());
    	  
        if (((object instanceof InventoryPageBase)) &&   (player.hasPermission(perk.getPermission().getPermissionToString())) &&  (UtilServer.getUserData().getConfigs().containsKey(UtilPlayer.getRealUUID(player)))){
        	if(UtilServer.getUserData().getConfig(player).contains("perks." + perk.getName())){
        		if (UtilServer.getUserData().getConfig(player).getString("perks." + perk.getName()).equalsIgnoreCase("true")){
            		((InventoryPageBase)object).setItem(toggle_slot, UtilItem.RenameItem(new ItemStack(351,1,(byte)10), "§aON"));
            		return;
            	}
        	}
        }
        ((InventoryPageBase)object).setItem(toggle_slot, UtilItem.RenameItem(new ItemStack(351,1,(byte)8), "§cOff"));
      }
    }
    , new Click(){
      public void onClick(Player player, UtilEvent.ActionType type, Object object){
        if (player.hasPermission(perk.getPermission().getPermissionToString())){
        	if (player.getOpenInventory().getItem(toggle_slot).getData().getData()==10) {
            	player.getOpenInventory().setItem(toggle_slot, UtilItem.RenameItem(new ItemStack(351,1,(byte)8), "§cOff"));
                UtilServer.getUserData().getConfig(player).set("perks." + perk.getName(), "false");
                UtilServer.getPerkManager().removePlayer(perk, player);
            }else{
            	UtilServer.getUserData().getConfig(player).set("perks." + perk.getName(), "true");
            	UtilServer.getPerkManager().addPlayer(perk, player);
            	player.getOpenInventory().setItem(toggle_slot, UtilItem.RenameItem(new ItemStack(351,1,(byte)10), "§aON"));
           }
        	player.updateInventory();
        } 
      }
    }
    , perk.getItem());
  }
}