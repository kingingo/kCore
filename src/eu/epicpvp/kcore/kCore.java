package eu.epicpvp.kcore;

import java.util.function.Predicate;

import dev.wolveringer.client.debug.Debugger;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Team;

import eu.epicpvp.kcore.Listener.Skin.SkinUpdateListener;
import eu.epicpvp.kcore.Listener.nick.NickChangeListener;
import eu.epicpvp.kcore.Util.UtilServer;
import lombok.Getter;

public class kCore extends JavaPlugin {
	@Getter
	private static kCore instance;

	public static final Predicate<String> DEBUGGER_FILTER = s -> {
		s = s.toLowerCase();
		if (s.startsWith("readed packet in "))
			return false;
		if (s.startsWith("write packet "))
			return false;
		if (s.startsWith("packet sucessfull handled ("))
			return false;
		if (s.startsWith("skin data: "))
			return false;
		if (s.startsWith("reciving "))
			return false;
		if (s.startsWith("handeling "))
			return false;
		return true;
	};

	@Override
	public void onEnable() {
		instance = this;
		Debugger.setFilter(DEBUGGER_FILTER);
		getServer().getPluginManager().registerEvents(new CommandDebugListener(), this);
		NickChangeListener nickListener = new NickChangeListener(this);
		UtilServer.createPacketListener(this).addPacketHandler(nickListener);
		Bukkit.getPluginManager().registerEvents(nickListener, this);
		new SkinUpdateListener(this).registerListener(); //Needed everywhere
		
		Bukkit.getScoreboardManager().getMainScoreboard().getTeams().forEach(Team::unregister);
		for(String s : Bukkit.getScoreboardManager().getMainScoreboard().getEntries())
			Bukkit.getScoreboardManager().getMainScoreboard().resetScores(s);
	}

	private class CommandDebugListener implements Listener {
		@EventHandler
		public void onCommand(PlayerCommandPreprocessEvent event) {
			String cmd = event.getMessage().toLowerCase();
			Player plr = event.getPlayer();
			if (!cmd.startsWith("/dcdebug")) {
				return;
			}
			event.setCancelled(true);
			String arg = cmd.substring("/dcdebug".length()).trim().split(" ")[0];
			handleCommand(plr, arg);
		}

		@EventHandler
		public void onCommand(ServerCommandEvent event) {
			String cmd = event.getCommand().toLowerCase();
			CommandSender sender = event.getSender();
			if (!cmd.startsWith("dcdebug")) {
				return;
			}
			event.setCancelled(true);
			String arg = cmd.substring("dcdebug".length()).trim().split(" ")[0];
			handleCommand(sender, arg);
		}

		private void handleCommand(CommandSender sender, String arg) {
			switch (arg) {
				case "":
					sender.sendMessage("§6Sets debug logging state");
					if (Debugger.isEnabled() && Debugger.getFilter() == null)
						sender.sendMessage("§4/" + getName() + " on");
					else
						sender.sendMessage("§c/" + getName() + " on");
					if (Debugger.isEnabled() && Debugger.getFilter() != null)
						sender.sendMessage("§4/" + getName() + " filtered §7(Filters spammy messages)");
					else
						sender.sendMessage("§c/" + getName() + " filtered §7(Filters spammy messages)");
					if (!Debugger.isEnabled())
						sender.sendMessage("§4/" + getName() + " off");
					else
						sender.sendMessage("§c/" + getName() + " off");
					break;
				case "on":
					Debugger.setFilter(null);
					Debugger.setEnabled(true);
					sender.sendMessage("§6Debugger is now §aon§6.");
					break;
				case "filtered":
					Debugger.setFilter(DEBUGGER_FILTER);
					Debugger.setEnabled(true);
					sender.sendMessage("§6Debugger is now §efiltered§6. §7(Filters spammy messages)");
					break;
				case "off":
					Debugger.setFilter(null);
					Debugger.setEnabled(false);
					sender.sendMessage("§6Debugger is now §coff§6.");
					break;
				default:
					sender.sendMessage("§4Unknown state " + arg);
					break;
			}
		}
	}
}