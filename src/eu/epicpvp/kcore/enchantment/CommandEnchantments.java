package eu.epicpvp.kcore.enchantment;

import java.util.HashMap;

import eu.epicpvp.kcore.Command.CommandHandler;
import eu.epicpvp.kcore.Util.InventorySize;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor
public class CommandEnchantments implements CommandExecutor {

	@CommandHandler.Command(command = "ce")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!sender.isOp()) {
			return true;
		}
		if (!(sender instanceof Player)) {
			Player plr = Bukkit.getPlayerExact(args[2]);
			CustomEnchantment enchantment = CustomEnchantment.getEnchantment(args[0]);
			int lvl = -1;
			try {
				lvl = Integer.parseInt(args[1]);
			} catch (Exception ex) {
				sender.sendMessage("Konnte Level " + args[1] + " nicht parsen. Enchantment: " + args[0] + " Spieler: " + args[2]);
				return true;
			}
			if (lvl < 1) {
				sender.sendMessage("Level " + args[1] + " ist invalid. Enchantment: " + args[0] + " Spieler: " + args[2]);
				return true;
			}
			if (plr == null) {
				sender.sendMessage("Konnte Spieler " + args[2] + " nicht finden. Enchantment: " + args[0] + " Level: " + args[1]);
				return true;
			}
			if (enchantment == null) {
				sender.sendMessage("Konnte Enchantment " + args[0] + " nicht finden. Spieler: " + args[2] + " Level: " + args[1]);
				return true;
			}
			HashMap<Integer, ItemStack> notStored = plr.getInventory().addItem(enchantment.toBook(lvl));
			for (ItemStack item : notStored.values()) {
				plr.getLocation().getWorld().dropItem(plr.getLocation(), item);
			}
			sender.sendMessage("Spieler " + plr.getName() + " (" + plr.getUniqueId() + ") wurde Buch für Enchantment " + enchantment.getName() + " mit Level " + lvl + " ins Inventar gelegt.");
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
