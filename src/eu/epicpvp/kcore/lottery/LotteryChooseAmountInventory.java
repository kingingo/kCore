package eu.epicpvp.kcore.lottery;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.epicpvp.kcore.Translation.TranslationHandler;
import eu.epicpvp.kcore.Util.AnvilGUI;
import eu.epicpvp.kcore.Util.UtilItem;
import eu.epicpvp.kcore.Util.UtilPlayer;

public class LotteryChooseAmountInventory extends AnvilGUI {

	public LotteryChooseAmountInventory(Lottery lottery, Player player) {
		super(player, lottery.getPlugin(), event -> {
			if (event.getSlot() == AnvilSlot.OUTPUT) {
				try {
					int i = Integer.parseInt(event.getName());
					if(lottery.bid(player, i)){
						UtilPlayer.sendMessage(player, TranslationHandler.getText(player, "PREFIX") + "§aDu hast dein gebot von §6"+i+"§a abgegeben!");
					}else{
						UtilPlayer.sendMessage(player, TranslationHandler.getText(player, "PREFIX") + "§cDu hast leider nicht soviel geld auf deinem Konto!");
					}
				} catch (NumberFormatException e) {
					UtilPlayer.sendMessage(player, TranslationHandler.getText(player, "PREFIX") + TranslationHandler.getText(player, "NO_INTEGER"));
				}
			}
		});
		ItemStack renamed = UtilItem.RenameItem(new ItemStack(Material.NAME_TAG), "100");
		setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, renamed);
		setSlot(AnvilGUI.AnvilSlot.OUTPUT, UtilItem.RenameItem(new ItemStack(Material.NAME_TAG), "§aBetrag setzen!"));
		open();
	}
}
