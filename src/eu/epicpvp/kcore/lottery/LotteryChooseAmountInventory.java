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
					lottery.bid(player, i);
				} catch (NumberFormatException e) {
					UtilPlayer.sendMessage(player, TranslationHandler.getText(player, "PREFIX") + TranslationHandler.getText(player, "NO_INTEGER"));
				}
			}
		});
		ItemStack renamed = UtilItem.RenameItem(new ItemStack(Material.NAME_TAG), "Epic's");
		setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, renamed);
		setSlot(AnvilGUI.AnvilSlot.OUTPUT, UtilItem.RenameItem(new ItemStack(Material.NAME_TAG), "§aBetrag setzen!"));
		open();
	}
}
