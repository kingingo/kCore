package eu.epicpvp.kcore.deliverychest;

import java.util.List;
import java.util.Set;

import eu.epicpvp.kcore.Command.CommandHandler;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class CommandSetDelivery implements CommandExecutor {

	private final DeliveryChest module;

	@CommandHandler.Command(command = "setdelivery")
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
		List<Block> blocks = plr.getLineOfSight((Set<Material>) null, 6);
		for (Block block : blocks) {
			if (block == null || block.getType() == Material.AIR) {
				continue;
			}
			if (block.getType() == Material.CHEST || block.getType() == Material.TRAPPED_CHEST) {
				module.setDeliveryChestLocation(block.getLocation().toVector().toBlockVector());
				sender.sendMessage("§aDie Deliverychest ist nun bei " + module.getDeliveryChestLocation());
				return true;
			}
		}
		sender.sendMessage("§cDu musst die Kiste ansehen!");
		return true;
	}
}
