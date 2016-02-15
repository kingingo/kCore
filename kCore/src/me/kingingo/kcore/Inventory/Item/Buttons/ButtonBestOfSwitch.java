package me.kingingo.kcore.Inventory.Item.Buttons;

import lombok.Getter;
import me.kingingo.kcore.Inventory.InventoryPageBase;
import me.kingingo.kcore.Inventory.Item.Click;
import me.kingingo.kcore.Inventory.Item.IButtonOneSlot;
import me.kingingo.kcore.Util.InventorySplit;
import me.kingingo.kcore.Util.UtilItem;
import me.kingingo.kcore.Util.UtilServer;
import me.kingingo.kcore.Util.UtilEvent.ActionType;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ButtonBestOfSwitch extends ButtonCopy{
	
	public ButtonBestOfSwitch(int slot) {
		super(new Click(){

			@Override
			public void onClick(Player player, ActionType type,Object object) {
				if(UtilServer.getUserData().getConfig(player).getInt("BestOf.Rounds")>slot){
					if(slot==0){
						((InventoryPageBase)object).setItem(slot,UtilItem.addEnchantmentGlow( UtilItem.RenameItem(new ItemStack(Material.STAINED_GLASS_PANE,1,(byte)5), "§aRunde§7 »§e"+(slot+1)+" §7[§cEdit§7]")));
						setGame(player, UtilServer.getUserData().getConfig(player).getString("BestOf.Round."+(slot+1)));
					}else if(slot==17){
						if(((InventoryPageBase)object).getItem(22).getAmount()%2==0){
							((InventoryPageBase)object).setItem(17, UtilItem.RenameItem(new ItemStack(Material.STAINED_GLASS_PANE,1,(byte)1), "§6Entscheidungsrunde§7 »§e §7[§cEdit§7]"));
						}else{
							((InventoryPageBase)object).setItem(17, UtilItem.RenameItem(new ItemStack(Material.STAINED_GLASS_PANE,1,(byte)1), "§6Entscheidungsrunde§7 »§e §7[§cLocked§7]"));
						}
					}else{
						((InventoryPageBase)object).setItem(slot, UtilItem.RenameItem(new ItemStack(Material.STAINED_GLASS_PANE,1,(byte)5), "§aRunde§7 »§e"+(slot+1)+" §7[§cEdit§7]"));
					}
				}else{
					((InventoryPageBase)object).setItem(slot, UtilItem.Item(new ItemStack(Material.STAINED_GLASS_PANE,1,(byte)14), new String[]{"§eWähle mehr Runden aus fürs Bearbeiten."},"§aRunde§7 »§e"+(slot+1)+" §7[§cLocked§7]"));
				}
			}
			
		}, new Click(){

			@Override
			public void onClick(Player player, ActionType type,Object object) {
				if(((ItemStack)object).getData().getData()==5){
					removeEnchant(player);
					setGame(player, UtilServer.getUserData().getConfig(player).getString("BestOf.Round."+(slot+1)));
					player.getOpenInventory().setItem(slot, UtilItem.addEnchantmentGlow(((ItemStack)object)));;
				}else if(((ItemStack)object).getData().getData()==1){
					if(((ItemStack)object).getItemMeta().getDisplayName().contains("Edit")){
						removeEnchant(player);
						setGame(player, UtilServer.getUserData().getConfig(player).getString("BestOf.Round."+0));
						player.getOpenInventory().setItem(slot, UtilItem.addEnchantmentGlow(((ItemStack)object)));
					}
				}
			}
			
		}, new ItemStack(Material.BEDROCK));
		
	}
	
	public static void removeEnchant(Player player){
		for(int i = InventorySplit._9.getMin(); i<=InventorySplit._9.getMax(); i++){
			if(player.getOpenInventory().getItem(22).getAmount()>i){
				player.getOpenInventory().setItem(i, UtilItem.RenameItem(new ItemStack(Material.STAINED_GLASS_PANE,1,(byte)5), "§aRunde§7 »§e"+(i+1)+" §7[§cEdit§7]"));
			}else{
				player.getOpenInventory().setItem(i, UtilItem.Item(new ItemStack(Material.STAINED_GLASS_PANE,1,(byte)14), new String[]{"§eWähle mehr Runden aus fürs Bearbeiten."},"§aRunde§7 »§e"+(i+1)+" §7[§cLocked§7]"));
			}
		}
		
		if(player.getOpenInventory().getItem(22).getAmount()%2==0){
			player.getOpenInventory().setItem(17, UtilItem.RenameItem(new ItemStack(Material.STAINED_GLASS_PANE,1,(byte)1), "§6Entscheidungsrunde§7 »§e §7[§cEdit§7]"));
		}else{
			player.getOpenInventory().setItem(17, UtilItem.RenameItem(new ItemStack(Material.STAINED_GLASS_PANE,1,(byte)1), "§6Entscheidungsrunde§7 »§e §7[§cLocked§7]"));
		}
	}

	public static void setGame(Player player, String game){
		if(game.equalsIgnoreCase("Bedwars1vs1")){
			player.getOpenInventory().setItem(38, UtilItem.addEnchantmentGlow(UtilItem.RenameItem(new ItemStack(Material.GOLD_SWORD), "§aBedwars 1vs1")));
		}else{
			player.getOpenInventory().setItem(38, UtilItem.RenameItem(new ItemStack(Material.GOLD_SWORD), "§aBedwars 1vs1"));
		}
		
		if(game.equalsIgnoreCase("Versus")){
			player.getOpenInventory().setItem(39, UtilItem.addEnchantmentGlow(UtilItem.RenameItem(new ItemStack(Material.DIAMOND_SWORD), "§aVersus 1vs1")));
		}else{
			player.getOpenInventory().setItem(39, UtilItem.RenameItem(new ItemStack(Material.DIAMOND_SWORD), "§aVersus 1vs1"));
		}
		
		if(game.equalsIgnoreCase("SkyWars1vs1")){
			player.getOpenInventory().setItem(41, UtilItem.addEnchantmentGlow(UtilItem.RenameItem(new ItemStack(Material.IRON_AXE), "§aSkyWars 1vs1")));
		}else{
			player.getOpenInventory().setItem(41, UtilItem.RenameItem(new ItemStack(Material.IRON_AXE), "§aSkyWars 1vs1"));
		}
		
		if(game.equalsIgnoreCase("SurvivalGames1vs1")){
			player.getOpenInventory().setItem(42, UtilItem.addEnchantmentGlow(UtilItem.RenameItem(new ItemStack(Material.BOW), "§aSurvivalGames 1vs1")));
		}else{
			player.getOpenInventory().setItem(42, UtilItem.RenameItem(new ItemStack(Material.BOW), "§aSurvivalGames 1vs1"));
		}
		
		if(game.equalsIgnoreCase("Random")){
			player.getOpenInventory().setItem(49, UtilItem.addEnchantmentGlow(UtilItem.RenameItem(new ItemStack(Material.EXP_BOTTLE), "§aRandom 1vs1")));
		}else{
			player.getOpenInventory().setItem(49, UtilItem.RenameItem(new ItemStack(Material.EXP_BOTTLE), "§aRandom 1vs1"));
		}
	}
	
}
