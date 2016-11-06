package eu.epicpvp.kcore.lottery;

import eu.epicpvp.kcore.Inventory.Inventory.InventoryCopy;
import eu.epicpvp.kcore.Inventory.InventoryPageBase;
import eu.epicpvp.kcore.Inventory.Item.Buttons.ButtonCopy;
import eu.epicpvp.kcore.Util.InventorySplit;
import eu.epicpvp.kcore.Util.UtilItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class LotteryInventory extends InventoryCopy {

	public LotteryInventory(Lottery lottery) {
		super(3 * 9, "Lotterie");

		addButton(InventorySplit._18.getMiddle() - 2, new ButtonCopy((player, type, object) -> {
			long pot = lottery.getCurrentPot();
			if (pot == 0) {
				((InventoryPageBase) object).setItem(InventorySplit._18.getMiddle() - 2, UtilItem.RenameItem(new ItemStack(Material.BUCKET), "§cDer Pot ist leer"));
			} else {
				((InventoryPageBase) object).setItem(InventorySplit._18.getMiddle() - 2, UtilItem.RenameItem(new ItemStack(Material.LAVA_BUCKET), "§7Derzeit sind §6" + pot + " Epic's §7im Pot."));
			}
		}, (player, type, object) -> {
		}, UtilItem.RenameItem(new ItemStack(Material.BUCKET), "§cDer Pot ist leer")));

		addButton(InventorySplit._18.getMiddle(), new ButtonCopy((player, type, object) -> {
			int endTime = lottery.getEndTime();
			((InventoryPageBase) object).setItem(InventorySplit._18.getMiddle(), UtilItem.RenameItem(new ItemStack(Material.COMPASS), "§7Auslosung in §6" + endTime + " Minuten"));
		}, (player, type, object) -> {
		}, UtilItem.RenameItem(new ItemStack(Material.COMPASS), "§7Auslosung in §6?? Minuten")));

		addButton(InventorySplit._18.getMiddle() + 2, new ButtonCopy((player, type, object) -> {
			if (lottery.hasBid(player)) {
				((InventoryPageBase) object).setItem(InventorySplit._18.getMiddle() + 2, UtilItem.RenameItem(new ItemStack(Material.INK_SACK, 1, (short) 1), "§cDu hast bereits mit §6" + lottery.getBid(player) + " Epic's §cteilgenommen!"));
			} else {
				((InventoryPageBase) object).setItem(InventorySplit._18.getMiddle() + 2, UtilItem.addEnchantmentGlow(UtilItem.RenameItem(new ItemStack(Material.NETHER_STAR), "§aMitmachen")));
			}
		}, (player, type, object) -> {
			if (!lottery.hasBid(player)) {
				new LotteryChooseAmountInventory(lottery, player);
			}
		}, UtilItem.addEnchantmentGlow(UtilItem.RenameItem(new ItemStack(Material.NETHER_STAR), "§aMitmachen"))));

		fill(Material.STAINED_GLASS_PANE, 7);
		setCreate_new_inv(true);
	}
}
