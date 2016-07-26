package eu.epicpvp.kcore.Command.Commands;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Inventory.InventoryBase;
import eu.epicpvp.kcore.Inventory.Inventory.InventoryTrade;
import eu.epicpvp.kcore.Listener.kListener;
import eu.epicpvp.kcore.Translation.TranslationHandler;
import eu.epicpvp.kcore.Util.UtilPlayer;
import eu.epicpvp.kcore.Util.UtilServer;

public class CommandHandel extends kListener implements CommandExecutor {

	private InventoryBase base;
	private HashMap<Player, InventoryTrade> list;
	private HashMap<Player, Player> anfrage;

	public CommandHandel(JavaPlugin instance) {
		this(instance, new InventoryBase(instance, ""));
	}

	public CommandHandel(JavaPlugin instance, InventoryBase base) {
		super(instance, "CommandHandel");
		this.base = base;
		this.list = new HashMap<>();
		this.anfrage = new HashMap<>();
	}

	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "handel", alias = { "trade" }, sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		Player player = (Player) sender;

		if (args.length == 0) {
			player.sendMessage(TranslationHandler.getText(player, "PREFIX") + "/handel [Player]");
		} else {
			Player targetPlayer = Bukkit.getPlayer(args[0]);
			if(targetPlayer == null)
				targetPlayer = UtilServer.getNickedPlayer(args[0]);
			if (targetPlayer.getUniqueId() == player.getUniqueId())
				return false;
			if (targetPlayer != null) {

				if (anfrage.containsKey(player) && anfrage.get(player).getUniqueId() == targetPlayer.getUniqueId()) {
					InventoryTrade t = new InventoryTrade(player, targetPlayer);
					list.put(player, t);
					list.put(targetPlayer, t);
					this.base.addAnother(t);
					anfrage.remove(player);
				} else {
					if (anfrage.containsKey(targetPlayer) && anfrage.get(targetPlayer).getUniqueId() == player.getUniqueId()) {
						player.sendMessage(TranslationHandler.getText(targetPlayer, "PREFIX") + "§cDu hast diesen Spieler bereits eine Anfrage gesendet!");
						return false;
					}

					anfrage.remove(targetPlayer);
					anfrage.put(targetPlayer, player);
					player.sendMessage(TranslationHandler.getText(targetPlayer, "PREFIX") + "§aDu hast §7" + targetPlayer.getName() + "§a eine anfrage gesendet!");
					targetPlayer.sendMessage(TranslationHandler.getText(targetPlayer, "PREFIX") + "§aDu hast von §7" + player.getName() + "§a eine Handel anfrage erhalten!");
					targetPlayer.sendMessage(TranslationHandler.getText(targetPlayer, "PREFIX") + "§azum Annehmen §7/Handel {player_" + player.getName()+"}");
				}
			} else {
				player.sendMessage(TranslationHandler.getText(player, "PREFIX") + TranslationHandler.getText(player, "PLAYER_IS_OFFLINE", args[0]));
			}
		}
		return false;
	}

	@EventHandler
	public void quit(PlayerQuitEvent ev) {
		anfrage.remove(ev.getPlayer());
	}

	@EventHandler
	public void close(InventoryCloseEvent ev) {
		if (list.containsKey(ev.getPlayer())) {
			InventoryTrade t = list.get(ev.getPlayer());
			list.remove(t.getT());
			list.remove(t.getT1());
			t.done();
		}
	}
}
