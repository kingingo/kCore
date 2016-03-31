package eu.epicpvp.kcore.Inventory.Item.Buttons;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.epicpvp.kcore.Inventory.InventoryPageBase;
import eu.epicpvp.kcore.Inventory.Item.Click;
import eu.epicpvp.kcore.Util.InventorySplit;
import eu.epicpvp.kcore.Util.UtilEvent.ActionType;
import eu.epicpvp.kcore.Util.UtilItem;
import eu.epicpvp.kcore.Util.UtilServer;

public class ButtonBestOfSwitch extends ButtonCopy{
	
	public ButtonBestOfSwitch(int slot) {
		super(new Click(){

			@Override
			public void onClick(Player player, ActionType type,Object object) {
				if(UtilServer.getUserData().getConfig(player).getInt("BestOf.Rounds")>slot){
					if(slot==0){
						((InventoryPageBase)object).setItem(slot,UtilItem.addEnchantmentGlow( UtilItem.RenameItem(new ItemStack(Material.STAINED_GLASS_PANE,1,(byte)5), "ßaRundeß7 ªße"+(slot+1)+" ß7[ßcEditß7]")));
						setGame(player, UtilServer.getUserData().getConfig(player).getString("BestOf.Round."+(slot+1)));
					}else if(slot==17){
						if(((InventoryPageBase)object).getItem(22).getAmount()%2==0){
							((InventoryPageBase)object).setItem(17, UtilItem.RenameItem(new ItemStack(Material.STAINED_GLASS_PANE,1,(byte)1), "ß6Entscheidungsrundeß7 ªße ß7[ßcEditß7]"));
						}else{
							((InventoryPageBase)object).setItem(17, UtilItem.RenameItem(new ItemStack(Material.STAINED_GLASS_PANE,1,(byte)1), "ß6Entscheidungsrundeß7 ªße ß7[ßcLockedß7]"));
						}
					}else{
						((InventoryPageBase)object).setItem(slot, UtilItem.RenameItem(new ItemStack(Material.STAINED_GLASS_PANE,1,(byte)5), "ßaRundeß7 ªße"+(slot+1)+" ß7[ßcEditß7]"));
					}
				}else{
					((InventoryPageBase)object).setItem(slot, UtilItem.Item(new ItemStack(Material.STAINED_GLASS_PANE,1,(byte)14), new String[]{"ßeW√§hle mehr Runden aus f√ºrs Bearbeiten."},"ßaRundeß7 ªße"+(slot+1)+" ß7[ßcLockedß7]"));
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
				player.getOpenInventory().setItem(i, UtilItem.RenameItem(new ItemStack(Material.STAINED_GLASS_PANE,1,(byte)5), "ßaRundeß7 ªße"+(i+1)+" ß7[ßcEditß7]"));
			}else{
				player.getOpenInventory().setItem(i, UtilItem.Item(new ItemStack(Material.STAINED_GLASS_PANE,1,(byte)14), new String[]{"ßeW√§hle mehr Runden aus f√ºrs Bearbeiten."},"ßaRundeß7 ªße"+(i+1)+" ß7[ßcLockedß7]"));
			}
		}
		
		if(player.getOpenInventory().getItem(22).getAmount()%2==0){
			player.getOpenInventory().setItem(17, UtilItem.RenameItem(new ItemStack(Material.STAINED_GLASS_PANE,1,(byte)1), "ß6Entscheidungsrundeß7 ªße ß7[ßcEditß7]"));
		}else{
			player.getOpenInventory().setItem(17, UtilItem.RenameItem(new ItemStack(Material.STAINED_GLASS_PANE,1,(byte)1), "ß6Entscheidungsrundeß7 ªße ß7[ßcLockedß7]"));
		}
	}

	public static void setGame(Player player, String game){
		if(game.equalsIgnoreCase("Bedwars1vs1")){
			player.getOpenInventory().setItem(38, UtilItem.addEnchantmentGlow(UtilItem.RenameItem(new ItemStack(Material.GOLD_SWORD), "ßaBedwars 1vs1")));
		}else{
			player.getOpenInventory().setItem(38, UtilItem.RenameItem(new ItemStack(Material.GOLD_SWORD), "ßaBedwars 1vs1"));
		}
		
		if(game.equalsIgnoreCase("Versus")){
			player.getOpenInventory().setItem(39, UtilItem.addEnchantmentGlow(UtilItem.RenameItem(new ItemStack(Material.DIAMOND_SWORD), "ßaVersus 1vs1")));
		}else{
			player.getOpenInventory().setItem(39, UtilItem.RenameItem(new ItemStack(Material.DIAMOND_SWORD), "ßaVersus 1vs1"));
		}
		
		if(game.equalsIgnoreCase("SkyWars1vs1")){
			player.getOpenInventory().setItem(41, UtilItem.addEnchantmentGlow(UtilItem.RenameItem(new ItemStack(Material.IRON_AXE), "ßaSkyWars 1vs1")));
		}else{
			player.getOpenInventory().setItem(41, UtilItem.RenameItem(new ItemStack(Material.IRON_AXE), "ßaSkyWars 1vs1"));
		}
		
		if(game.equalsIgnoreCase("SurvivalGames1vs1")){
			player.getOpenInventory().setItem(42, UtilItem.addEnchantmentGlow(UtilItem.RenameItem(new ItemStack(Material.BOW), "ßaSurvivalGames 1vs1")));
		}else{
			player.getOpenInventory().setItem(42, UtilItem.RenameItem(new ItemStack(Material.BOW), "ßaSurvivalGames 1vs1"));
		}
		
		if(game.equalsIgnoreCase("Random")){
			player.getOpenInventory().setItem(49, UtilItem.addEnchantmentGlow(UtilItem.RenameItem(new ItemStack(Material.EXP_BOTTLE), "ßaRandom 1vs1")));
		}else{
			player.getOpenInventory().setItem(49, UtilItem.RenameItem(new ItemStack(Material.EXP_BOTTLE), "ßaRandom 1vs1"));
		}
	}
	
}
