package eu.epicpvp.kcore.enchantment;

import eu.epicpvp.kcore.Command.CommandHandler;
import eu.epicpvp.kcore.Util.InventorySize;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

@RequiredArgsConstructor
public class CommandEnchantments implements CommandExecutor {

	@CommandHandler.Command(command = "ce")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!sender.isOp()) {
			return true;
		}
		if (!(sender instanceof Player)) {
			sender.sendMessage("§cNur für Spieler!");
			return true;
		}
		Player plr = (Player) sender;
		Inventory inv = Bukkit.createInventory(null, InventorySize._54.getSize());

		for (CustomEnchantment ce : CustomEnchantment.getCustomEnchantments()) {
			for (int i = 1; i <= 4; i++) inv.addItem(ce.toBook(i));
		}

		plr.openInventory(inv);
		return true;
	}
}
