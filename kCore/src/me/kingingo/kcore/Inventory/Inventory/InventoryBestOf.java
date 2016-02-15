package me.kingingo.kcore.Inventory.Inventory;

import me.kingingo.kcore.Inventory.InventoryPageBase;
import me.kingingo.kcore.Inventory.Item.Click;
import me.kingingo.kcore.Inventory.Item.Buttons.ButtonBase;
import me.kingingo.kcore.Inventory.Item.Buttons.ButtonBestOfSwitch;
import me.kingingo.kcore.Inventory.Item.Buttons.ButtonUpDown;
import me.kingingo.kcore.Util.InventorySplit;
import me.kingingo.kcore.Util.UtilEvent.ActionType;
import me.kingingo.kcore.Util.UtilItem;
import me.kingingo.kcore.Util.UtilServer;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class InventoryBestOf extends InventoryCopy{
	
	public InventoryBestOf(int size, String title, InventoryPageBase base) {
		super(size, title);
		
		for(int i = InventorySplit._9.getMin(); i<=InventorySplit._9.getMax(); i++){
			addButton(i, new ButtonBestOfSwitch(i));
		}
		
		addButton(17, new ButtonBestOfSwitch(17));
		
		addButton(new ButtonUpDown(new Click(){

			@Override
			public void onClick(Player p, ActionType a, Object o) {
				if(((ItemStack)o).getAmount()==0){
					((ItemStack)o).setAmount(UtilServer.getUserData().getConfig(p).getInt("BestOf.Rounds"));
				}else{
					UtilServer.getUserData().getConfig(p).set("BestOf.Rounds", ((ItemStack)o).getAmount());
				}
				
				for(int i = InventorySplit._9.getMin(); i<=InventorySplit._9.getMax(); i++){
					if(((ItemStack)o).getAmount()>i){
						if(i==0){
							p.getOpenInventory().setItem(i, UtilItem.addEnchantmentGlow(UtilItem.RenameItem(new ItemStack(Material.STAINED_GLASS_PANE,1,(byte)5), "§aRunde§7 »§e"+(i+1)+" §7[§cEdit§7]")));
							ButtonBestOfSwitch.setGame(p, UtilServer.getUserData().getConfig(p).getString("BestOf.Round."+(i+1)));
						}else{
							p.getOpenInventory().setItem(i, UtilItem.RenameItem(new ItemStack(Material.STAINED_GLASS_PANE,1,(byte)5), "§aRunde§7 »§e"+(i+1)+" §7[§cEdit§7]"));
						}
					}else{
						p.getOpenInventory().setItem(i, UtilItem.Item(new ItemStack(Material.STAINED_GLASS_PANE,1,(byte)14), new String[]{"§eWähle mehr Runden aus fürs Bearbeiten."},"§aRunde§7 »§e"+(i+1)+" §7[§cLocked§7]"));
					}
				}
				
				if(((ItemStack)o).getAmount()%2==0){
					p.getOpenInventory().setItem(17, UtilItem.RenameItem(new ItemStack(Material.STAINED_GLASS_PANE,1,(byte)1), "§6Entscheidungsrunde§7 »§e §7[§cEdit§7]"));
				}else{
					p.getOpenInventory().setItem(17, UtilItem.RenameItem(new ItemStack(Material.STAINED_GLASS_PANE,1,(byte)1), "§6Entscheidungsrunde§7 »§e §7[§cLocked§7]"));
				}
			}
			
		}, true,this, UtilItem.RenameItem(new ItemStack(Material.NETHER_STAR,0), "§fRounds"), 22, 3, 9));
		
		addButton(38, new ButtonBase(new Click() {
			
			@Override
			public void onClick(Player player, ActionType type, Object object) {
				setRoundGameItems( player );
				player.getOpenInventory().setItem(38, UtilItem.addEnchantmentGlow(UtilItem.SetDescriptions(((ItemStack)object), new String[]{"§7» §aAusgewählt"})));
				UtilServer.getUserData().getConfig(player).set("BestOf.Round."+getRound(player), "Bedwars1vs1");
			}
		}, UtilItem.RenameItem(new ItemStack(Material.GOLD_SWORD), "§aBedwars 1vs1")));
		
		addButton(39, new ButtonBase(new Click() {
			
			@Override
			public void onClick(Player player, ActionType type, Object object) {
				setRoundGameItems( player );
				player.getOpenInventory().setItem(39, UtilItem.addEnchantmentGlow(UtilItem.SetDescriptions(((ItemStack)object), new String[]{"§7» §aAusgewählt"})));
				UtilServer.getUserData().getConfig(player).set("BestOf.Round."+getRound(player), "Versus");
			}
		}, UtilItem.RenameItem(new ItemStack(Material.DIAMOND_SWORD), "§aVersus 1vs1")));
		
		addButton(41, new ButtonBase(new Click() {
			
			@Override
			public void onClick(Player player, ActionType type, Object object) {
				setRoundGameItems( player );
				player.getOpenInventory().setItem(41, UtilItem.addEnchantmentGlow(UtilItem.SetDescriptions(((ItemStack)object), new String[]{"§7» §aAusgewählt"})));
				UtilServer.getUserData().getConfig(player).set("BestOf.Round."+getRound(player), "SkyWars1vs1");
			}
		}, UtilItem.RenameItem(new ItemStack(Material.IRON_AXE), "§aSkyWars 1vs1")));
		
		addButton(42, new ButtonBase(new Click() {
			
			@Override
			public void onClick(Player player, ActionType type, Object object) {
				setRoundGameItems( player );
				player.getOpenInventory().setItem(42, UtilItem.addEnchantmentGlow(UtilItem.SetDescriptions(((ItemStack)object), new String[]{"§7» §aAusgewählt"})));
				UtilServer.getUserData().getConfig(player).set("BestOf.Round."+getRound(player), "SurvivalGames1vs1");
			}
		}, UtilItem.RenameItem(new ItemStack(Material.BOW), "§aSurvivalGames 1vs1")));
		
		addButton(49, new ButtonBase(new Click() {
			
			@Override
			public void onClick(Player player, ActionType type, Object object) {
				setRoundGameItems( player );
				player.getOpenInventory().setItem(49, UtilItem.addEnchantmentGlow(UtilItem.SetDescriptions(((ItemStack)object), new String[]{"§7» §aAusgewählt"})));
				UtilServer.getUserData().getConfig(player).set("BestOf.Round."+getRound(player), "Random");
			}
		}, UtilItem.RenameItem(new ItemStack(Material.EXP_BOTTLE), "§aRandom")));
		
		addButton(53, new ButtonBase(new Click() {
			
			@Override
			public void onClick(Player player, ActionType type, Object object) {
				player.openInventory(base);
			}
		}, UtilItem.RenameItem(new ItemStack(Material.STAINED_CLAY,1,(byte)5), "§aFertig")));
		
		fill(Material.STAINED_GLASS_PANE,7);
	}

	public static int getRound(Player player){
		for(int i = InventorySplit._9.getMin(); i<=InventorySplit._9.getMax(); i++){
			if(UtilItem.isEnchantmentGlow(player.getOpenInventory().getItem(i))){
				return i+1;
			}
		}
		
		if(UtilItem.isEnchantmentGlow(player.getOpenInventory().getItem(17))){
			return 0;
		}
		
		return -1;
	}
	
	public static void setRoundGameItems(Player player){
		player.getOpenInventory().setItem(38, UtilItem.RenameItem(new ItemStack(Material.GOLD_SWORD), "§aBedwars 1vs1"));
		player.getOpenInventory().setItem(39, UtilItem.RenameItem(new ItemStack(Material.DIAMOND_SWORD), "§aVersus 1vs1"));
		player.getOpenInventory().setItem(41, UtilItem.RenameItem(new ItemStack(Material.IRON_AXE), "§aSkyWars 1vs1"));
		player.getOpenInventory().setItem(42, UtilItem.RenameItem(new ItemStack(Material.BOW), "§aSurvivalGames 1vs1"));
		player.getOpenInventory().setItem(49, UtilItem.RenameItem(new ItemStack(Material.EXP_BOTTLE), "§aRandom 1vs1"));
	}
}
