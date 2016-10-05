package eu.epicpvp.kcore.Command.Admin;

import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Permission.PermissionType;
import eu.epicpvp.kcore.Translation.TranslationHandler;
import eu.epicpvp.thread.ThreadFactory;
import eu.epicpvp.thread.ThreadRunner;

public class CommandTpAll implements CommandExecutor {
	private ThreadRunner current = null;

	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "tpall", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender cs, Command cmd, String arg2, String[] args) {
		if (cs.hasPermission(PermissionType.PLAYER_TELEPORT_ALL.getPermissionToString())) {
			if (args.length == 2) {
				if (current != null) {
					cs.sendMessage("§aAlredy a teleport in progress");
					return true;
				}
				int playerPerTick = Integer.parseInt(args[0]);
				int sleep = Integer.parseInt(args[1]);
				Location loc = ((Player) cs).getLocation();
				(current = ThreadFactory.getFactory().createThread(() -> {
					cs.sendMessage("§aTeleporting players... (Eta. " + getDurationBreakdown(((Bukkit.getOnlinePlayers().size() / playerPerTick) * sleep), "underknown") + ")");
					Iterator<? extends Player> player = Bukkit.getOnlinePlayers().iterator();
					int size = Bukkit.getOnlinePlayers().size();

					int teleported = 0;
					int tickTeleportet = 0;
					while (player.hasNext()) {
						while (tickTeleportet < playerPerTick) {
							player.next().teleport(loc);
							tickTeleportet++;
							teleported++;
						}
						tickTeleportet = 0;
						try {
							Thread.sleep(sleep);
						} catch (Exception e) {
						}
						cs.sendMessage("§6Teleporting [" + teleported + "/" + size + "] (" + (teleported / size * 100) + "%)");
					}
					cs.sendMessage("§aTeleport done!");
				})).start();
			} else if (args[0].equalsIgnoreCase("stop")) {
				if (current == null)
					cs.sendMessage("§cNo teleport in progress");
				else {
					cs.sendMessage("§aStoped teleport!");
					current.stop();
				}
			}
			else
			{
				cs.sendMessage(TranslationHandler.getText((Player) cs, "PREFIX") + "§6/tpall <playerPerLimit> <SleepTime>");
				cs.sendMessage(TranslationHandler.getText((Player) cs, "PREFIX") + "§6/tpall stop");
			}
		}
		return true;
	}

	public static String getDurationBreakdown(long millis, String no) {
		if (millis < 0) {
			return "millis<0";
		}
		if (millis == 0)
			return no;
		long days = TimeUnit.MILLISECONDS.toDays(millis);
		millis -= TimeUnit.DAYS.toMillis(days);
		long hours = TimeUnit.MILLISECONDS.toHours(millis);
		millis -= TimeUnit.HOURS.toMillis(hours);
		long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
		millis -= TimeUnit.MINUTES.toMillis(minutes);
		long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);

		StringBuilder sb = new StringBuilder(64);
		if (days > 0) {
			sb.append(days);
			sb.append(" day" + (days == 1 ? "" : "s") + " ");
		}
		if (hours > 0) {
			sb.append(hours);
			sb.append(" hour" + (hours == 1 ? "" : "s") + " ");
		}
		if (minutes > 0) {
			sb.append(minutes);
			sb.append(" minute" + (minutes == 1 ? "" : "s") + " ");
		}
		if (seconds > 0) {
			sb.append(seconds);
			sb.append(" second" + (seconds == 1 ? "" : "s") + "");
		}
		return (sb.toString());
	}
}
