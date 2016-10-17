package eu.epicpvp.kcore.lottery;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import eu.epicpvp.kcore.Inventory.InventoryPageBase;
import eu.epicpvp.kcore.Inventory.Inventory.InventoryCopy;
import eu.epicpvp.kcore.Inventory.Item.Buttons.ButtonCopy;
import eu.epicpvp.kcore.Util.UtilItem;

public class LotteryInventory extends InventoryCopy {

//	private final Lottery lottery;

	public LotteryInventory(Lottery lottery) {
		super(3 * 9, "Lotterie");
//		this.lottery = lottery;
		for (int i = 0; i < super.getSize(); i++) {
			super.setItem(i, UtilItem.RenameItem(new ItemStack(Material.STAINED_GLASS_PANE, 7), "§7"));
		}
		addButton(10, new ButtonCopy((player, type, object) -> {
			int pot = lottery.getCurrentPot();
			if (pot == 0) {
				((InventoryPageBase) object).setItem(10, UtilItem.RenameItem(new ItemStack(Material.BUCKET), "§cDer Pot ist leer"));
			} else {
				((InventoryPageBase) object).setItem(10, UtilItem.RenameItem(new ItemStack(Material.LAVA_BUCKET), "§7Derzeit sind §6" + pot + " Epic's §7im Pot."));
			}
		}, (player, type, object) -> {
		}, UtilItem.RenameItem(new ItemStack(Material.BUCKET), "§cDer Pot ist leer")));

		addButton(12, new ButtonCopy((player, type, object) -> {
			long endTime = lottery.getEndTime();
			long dur = endTime - System.currentTimeMillis();
			dur /= 1000 * 60;
			((InventoryPageBase) object).setItem(12, UtilItem.RenameItem(new ItemStack(Material.COMPASS), "§7Auslosung in §6" + dur + " Minuten"));
		}, (player, type, object) -> {
		}, UtilItem.RenameItem(new ItemStack(Material.COMPASS), "§7Auslosung in §6?? Minuten")));

		addButton(14, new ButtonCopy((player, type, object) -> {
			if (lottery.hasBid(player)) {
				((InventoryPageBase) object).setItem(12, UtilItem.RenameItem(new ItemStack(Material.INK_SACK, 1, (short)1), "§cDu hast bereits mit §6" + lottery.getBid(player) + " Epic's §cteilgenommen!"));
			} else {
				((InventoryPageBase) object).setItem(12, UtilItem.addEnchantmentGlow(UtilItem.RenameItem(new ItemStack(Material.NETHER_STAR), "§aMitmachen")));
			}
		}, (player, type, object) -> {
			if (!lottery.hasBid(player)) {
				new LotteryChooseAmountInventory(lottery, player);
			}
		}, UtilItem.addEnchantmentGlow(UtilItem.RenameItem(new ItemStack(Material.NETHER_STAR), "§aMitmachen"))));
	}
}
