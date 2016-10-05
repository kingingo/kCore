package eu.epicpvp.kcore.Command.Admin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Permission.PermissionType;
import eu.epicpvp.kcore.Translation.TranslationHandler;
import eu.epicpvp.kcore.Util.UtilItem;
import eu.epicpvp.kcore.Util.UtilNumber;
import eu.epicpvp.kcore.Util.UtilPlayer;

public class CommandGive implements CommandExecutor {

	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "give", sender = Sender.EVERYONE)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;

			if (!player.hasPermission(PermissionType.COMMAND_GIVE.getPermissionToString()))
				return false;

			if (args.length < 2) {
				player.sendMessage(TranslationHandler.getText(player, "PREFIX") + "/give [Player] [ID / ID:METADATA] [ANZAHL]");
				player.sendMessage(TranslationHandler.getText(player, "PREFIX") + "/give [Player] [ID / ID:METADATA] [ANZAHL] player:[NAME]");
				player.sendMessage(TranslationHandler.getText(player, "PREFIX") + "/give [Player] [ID / ID:METADATA] [ANZAHL] <enchant>:<level>");

				player.sendMessage(TranslationHandler.getText(player, "PREFIX") + "Firework give Command: ");
				player.sendMessage(TranslationHandler.getText(player, "PREFIX") + "/give [Player] [ID / ID:METADATA] [ANZAHL] power:<firework power>");
				player.sendMessage(TranslationHandler.getText(player, "PREFIX") + "/give [Player] [ID / ID:METADATA] [ANZAHL] color:<color[,color,..]>");
				player.sendMessage(TranslationHandler.getText(player, "PREFIX") + "/give [Player] [ID / ID:METADATA] [ANZAHL] fade:<color[,color,..]>]");
				player.sendMessage(TranslationHandler.getText(player, "PREFIX") + "/give [Player] [ID / ID:METADATA] [ANZAHL] shape:<star|ball|large|creeper|burst>");
				player.sendMessage(TranslationHandler.getText(player, "PREFIX") + "/give [Player] [ID / ID:METADATA] [ANZAHL] effect:<trail|twinkle>[,<trail|twinkle>]");
			} else {
				Player target;
				if (args[0].equalsIgnoreCase(player.getName())) {
					target = player;
				} else {
					if (!UtilPlayer.isOnline(args[0])) {
						player.sendMessage(TranslationHandler.getText(player, "PREFIX") + TranslationHandler.getText(player, "PLAYER_IS_OFFLINE", args[0]));
						return false;
					}
					target = Bukkit.getPlayer(args[0]);
				}

				int id;
				int metadata = 0;
				int anzahl = (args.length > 2 ? UtilNumber.toInt(args[2]) : 1);

				if (args[1].contains(":")) {
					String[] splitt = args[1].split(":");

					id = UtilNumber.toInt(splitt[0]);
					metadata = UtilNumber.toInt(splitt[1]);
				} else
					id = UtilNumber.toInt(args[1]);

				if (id == 0 || (args[1].contains(":") ? metadata == 0 : false)) {
					player.sendMessage(TranslationHandler.getText(player, "PREFIX") + TranslationHandler.getText(player, "MONEY_NO_DOUBLE"));
					return false;
				}

				ItemStack item = new ItemStack(id, anzahl, (byte) metadata);

				if (args.length > 3) {
					int metaStart = UtilNumber.isInt(args[3]) ? 4 : 3;

					if (args.length > metaStart) {
						try {
							item = UtilItem.parseStringMeta(item, metaStart, args);
						} catch (Exception e) {
							player.sendMessage(TranslationHandler.getText(player, "PREFIX") + e.getMessage());
							return false;
						}
					}
				}

				target.getInventory().addItem(item);
				target.updateInventory();
			}
		} else {
			if (args.length < 2) {
				System.out.println("/give [Player] [ID / ID:METADATA] [ANZAHL]");
				System.out.println("/give [Player] [ID / ID:METADATA] [ANZAHL] player:[NAME]");
				System.out.println("/give [Player] [ID / ID:METADATA] [ANZAHL] <enchant>:<level>");

				System.out.println("Firework give Command: ");
				System.out.println("/give [Player] [ID / ID:METADATA] [ANZAHL] power:<firework power>");
				System.out.println("/give [Player] [ID / ID:METADATA] [ANZAHL] color:<color[,color,..]>");
				System.out.println("/give [Player] [ID / ID:METADATA] [ANZAHL] fade:<color[,color,..]>]");
				System.out.println("/give [Player] [ID / ID:METADATA] [ANZAHL] shape:<star|ball|large|creeper|burst>");
				System.out.println("/give [Player] [ID / ID:METADATA] [ANZAHL] effect:<trail|twinkle>[,<trail|twinkle>]");
			} else {
				Player target;

				if (!UtilPlayer.isOnline(args[0])) {
					System.out.println(TranslationHandler.getText("PLAYER_IS_OFFLINE", args[0]));
					return false;
				}
				target = Bukkit.getPlayer(args[0]);

				int id;
				int metadata = 0;
				int anzahl = (args.length > 2 ? UtilNumber.toInt(args[2]) : 1);

				if (args[1].contains(":")) {
					String[] splitt = args[1].split(":");

					id = UtilNumber.toInt(splitt[0]);
					metadata = UtilNumber.toInt(splitt[1]);
				} else
					id = UtilNumber.toInt(args[1]);

				if (id == 0 || (args[1].contains(":") ? metadata == 0 : false)) {
					System.out.println(TranslationHandler.getText("MONEY_NO_DOUBLE"));
					return false;
				}

				ItemStack item = new ItemStack(id, anzahl, (byte) metadata);

				if (args.length > 3) {
					int metaStart = UtilNumber.isInt(args[3]) ? 4 : 3;

					if (args.length > metaStart) {
						try {
							item = UtilItem.parseStringMeta(item, metaStart, args);
						} catch (Exception e) {
							System.out.println(e.getMessage());
							return false;
						}
					}
				}

				target.getInventory().addItem(item);
				target.updateInventory();
			}
		}
		return false;
	}
}
