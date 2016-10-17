package eu.epicpvp.kcore.enchantment;

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
public class CommandSetAnvil implements CommandExecutor {

	@CommandHandler.Command(command = "setanvil")
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
			if (block.getType() == Material.ANVIL) {
				AnvilEnchantHandler.getHandler().setAnvil(block.getLocation().toVector().toBlockVector());
				sender.sendMessage("§aDer Anvil ist nun bei " + AnvilEnchantHandler.getHandler().getAnvil());
				return true;
			}
		}
		sender.sendMessage("§cDu musst die Anvil ansehen!");
		return true;
	}
}
