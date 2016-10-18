package eu.epicpvp.kcore.enchantment;

import eu.epicpvp.kcore.Listener.kListener;
import eu.epicpvp.kcore.Util.InventorySize;
import eu.epicpvp.kcore.Util.InventorySplit;
import eu.epicpvp.kcore.Util.UtilItem;
import eu.epicpvp.kcore.Util.UtilServer;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BlockVector;

public class AnvilEnchantHandler extends kListener {

	public static AnvilEnchantHandler handler;

	public static AnvilEnchantHandler getHandler() {
		if (handler == null) handler = new AnvilEnchantHandler();
		return handler;
	}

	@Getter
	private BlockVector anvil;

	public AnvilEnchantHandler() {
		super(UtilServer.getPluginInstance(), "AnvilEnchantHandler");

		if (getPlugin().getConfig().contains("anvilenchanthandler.anvil"))
			this.anvil = getPlugin().getConfig().getVector("anvilenchanthandler.anvil").toBlockVector();
		UtilServer.getCommandHandler().register(CommandSetAnvil.class, new CommandSetAnvil());
	}

	public void setAnvil(BlockVector block) {
		anvil = block;
		getPlugin().getConfig().set("anvilenchanthandler.anvil", block);
		getPlugin().saveConfig();
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if (!(event.getView().getTopInventory().getHolder() instanceof AnvilInventoryHolder)) {
			return;
		}

		switch (event.getAction()) {
			case HOTBAR_MOVE_AND_READD:
			case COLLECT_TO_CURSOR:
			case CLONE_STACK:
			case HOTBAR_SWAP:
			case DROP_ONE_SLOT:
			case DROP_ALL_SLOT:
			case DROP_ALL_CURSOR:
			case DROP_ONE_CURSOR:
			case NOTHING:
			case UNKNOWN:
				event.setCancelled(true);
				break;
			case PICKUP_ALL:
			case PICKUP_ONE:
			case PICKUP_SOME:
			case PLACE_ALL:
			case PLACE_SOME:
			case PLACE_ONE:
				if (event.getClickedInventory().getHolder() instanceof AnvilInventoryHolder) {
					if (event.getSlot() != (InventorySplit._18.getMiddle() - 2) && event.getSlot() != (InventorySplit._18.getMiddle() + 2)) {
						event.setCancelled(true);
					}

					if (event.getSlot() == InventorySplit._18.getMiddle()) {
						ItemStack tool = event.getClickedInventory().getItem(InventorySplit._18.getMiddle() - 2);
						ItemStack book = event.getClickedInventory().getItem(InventorySplit._18.getMiddle() + 2);

						if (tool != null && book != null) {
							if (book.getType() == Material.ENCHANTED_BOOK) {
								CustomEnchantment ce = CustomEnchantment.byBook(book);
								
								ce.addEnchantment(tool, ce.getLevel(book));
								event.getClickedInventory().setItem(InventorySplit._18.getMiddle() - 2,tool);
								event.getClickedInventory().setItem(InventorySplit._18.getMiddle() + 2,null);
								getPlugin().getServer().getScheduler().runTask(getPlugin(), () -> ((Player) event.getWhoClicked()).updateInventory());
							}
						}
					}
				}
				break;
			case SWAP_WITH_CURSOR:
				break;
		}
		if (event.isCancelled()) {
			getPlugin().getServer().getScheduler().runTask(getPlugin(), () -> ((Player) event.getWhoClicked()).updateInventory());
		}
	}

	@EventHandler
	public void click(PlayerInteractEvent ev) {
		if (anvil != null && ev.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (ev.getClickedBlock() != null && ev.getClickedBlock().getType() == Material.ANVIL) {
				if (ev.getClickedBlock().getLocation().toVector().toBlockVector().equals(anvil)) {
					AnvilInventoryHolder holder = new AnvilInventoryHolder();
					Inventory inv = Bukkit.createInventory(holder, InventorySize._27.getSize(), "§7Anvil");
					holder.setInventory(inv);
					for (int i = 0; i < InventorySize._27.getSize(); i++)
						inv.setItem(i, UtilItem.RenameItem(new ItemStack(Material.STAINED_GLASS_PANE, 1, ((byte) 15)), ""));
					inv.setItem(InventorySplit._18.getMiddle() - 2, null);
					inv.setItem(InventorySplit._18.getMiddle() + 2, null);
					inv.setItem(InventorySplit._18.getMiddle(), UtilItem.RenameItem(new ItemStack(Material.EXP_BOTTLE), "§aenchant"));

					ev.getPlayer().openInventory(inv);
					ev.setCancelled(true);
				}
			}
		}
	}
}
