package eu.epicpvp.kcore.Command.Admin;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.UnknownDependencyException;
import org.bukkit.plugin.java.JavaPlugin;

import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Listener.kListener;
import eu.epicpvp.kcore.Permission.PermissionType;
import eu.epicpvp.kcore.Translation.TranslationHandler;

public class CommandToggle extends kListener implements CommandExecutor{

	boolean chat=true;
	long online;

	public CommandToggle(JavaPlugin instance){
		super(instance, "PluginManager");
		this.online = System.currentTimeMillis();
	}

	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "pluginmanager", sender = Sender.PLAYER, alias = {"plugman", "toggle"})
	public boolean onCommand(CommandSender cs, Command cmd, String alias,String[] args) {
		if(cs instanceof Player){
			Player p = (Player)cs;
			if(p.hasPermission(PermissionType.COMMAND_TOGGLE.getPermissionToString())){
				if (args.length == 0) {
					sendHelp(p, alias);
					return false;
				} else if(args[0].equalsIgnoreCase("list")){
					StringBuilder sb = new StringBuilder();
					for (Plugin pl : Bukkit.getPluginManager().getPlugins()) {
						if(pl.isEnabled()){
							sb.append("§a");
						}else{
							sb.append("§c");
						}
						sb.append(pl.getName()).append("§7, ");
					}
					p.sendMessage(TranslationHandler.getText(p, "PREFIX") + "§7Plugins: " + sb.substring(0, sb.length() - 4));
					return false;
				} else if (args.length >= 2) {
					if (args[0].equalsIgnoreCase("load")) {
						String input = args[1];
						File pluginFile = new File(input);
						if (!pluginFile.exists() || !pluginFile.isFile()) {
							cs.sendMessage(TranslationHandler.getText(p, "PREFIX") + "§cDie Datei §6" + input + "§c konnte nicht gefunden werden!");
							return true;
						}
						try {
							Plugin loadedPlugin = Bukkit.getPluginManager().loadPlugin(pluginFile);
							if (loadedPlugin == null) {
								cs.sendMessage(TranslationHandler.getText(p, "PREFIX") + "§cDatei ungültig: §6" + input);
							} else {
								p.sendMessage(TranslationHandler.getText(p, "PREFIX") + "§7Das Plugin §6" + loadedPlugin.getName() + "§7 wurde §ageladen§7!");
							}
						} catch (UnknownDependencyException | InvalidPluginException | InvalidDescriptionException e) {
							e.printStackTrace();
							p.sendMessage(TranslationHandler.getText(p, "PREFIX") + "§cDas Plugin konnte nicht geladen werden: §6" + e.getLocalizedMessage());
						}
					} else if (args[0].equalsIgnoreCase("toggle")) {
						String input = args[1];
						Plugin pl = Bukkit.getPluginManager().getPlugin(input);
						if (pl == null) {
							pl = findPlugin(input);
						}
						if (pl == null) {
							cs.sendMessage(TranslationHandler.getText(p, "PREFIX") + "§cDas Plugin §6" + input + "§c konnte nicht gefunden werden.");
							return true;
						}
						String plName = pl.getName();
						if (plName.equalsIgnoreCase("kHub") || plName.equalsIgnoreCase("kSkyBlock") || plName.equalsIgnoreCase("kCore") || plName.equalsIgnoreCase("kPvP")
								|| plName.equalsIgnoreCase("kArcade") || plName.equalsIgnoreCase("WarZ")) {
							p.sendMessage(TranslationHandler.getText(p, "PREFIX") + "§cDu kannst dieses Plugin nicht ausschalten!");
							return false;
						}

						if (pl.isEnabled()) {
							try {
								Bukkit.getPluginManager().disablePlugin(pl);
								p.sendMessage(TranslationHandler.getText(p, "PREFIX") + "§7Das Plugin §6" + plName + "§7 wurde §causgeschaltet§7!");
							} catch (Exception ex) {
								p.sendMessage(TranslationHandler.getText(p, "PREFIX") + "§cFehler beim Ausschalten des Plugins §6" + plName + "§c: §6" + ex.getLocalizedMessage());
							}
						} else {
							if (pl.getName().equalsIgnoreCase("logblock") || (args.length >= 3 && args[2].equalsIgnoreCase("onLoad"))) {
								pl.onLoad();
							}
							try {
								Bukkit.getPluginManager().enablePlugin(pl);
								p.sendMessage(TranslationHandler.getText(p, "PREFIX") + "§7Das Plugin §6" + plName + "§7 wurde §aeingeschaltet§7!");
							} catch (Exception ex) {
								p.sendMessage(TranslationHandler.getText(p, "PREFIX") + "§cFehler beim Einschalten des Plugins §6" + plName + "§c: §6" + ex.getLocalizedMessage());
							}
						}
						return false;
					}
				} else {
					sendHelp(p, alias);
					return true;
				}
			}
		}
		return false;
	}

	private void sendHelp(Player p, String alias) {
		p.sendMessage(TranslationHandler.getText(p, "PREFIX") + "§a/" + alias + " list");
		p.sendMessage(TranslationHandler.getText(p, "PREFIX") + "§a/" + alias + " toggle <Plugin> [onload]");
		p.sendMessage(TranslationHandler.getText(p, "PREFIX") + "§a/" + alias + " load <Datei>");
	}

	private static Plugin findPlugin(String name) {
		for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
			if (plugin.getName().equalsIgnoreCase(name)) {
				return plugin;
			}
		}
		return null;
	}
}

