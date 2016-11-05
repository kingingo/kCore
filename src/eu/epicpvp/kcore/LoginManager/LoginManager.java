package eu.epicpvp.kcore.LoginManager;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Map;

import eu.epicpvp.dataserver.protocoll.packets.PacketOutPlayerSettings;
import eu.epicpvp.datenclient.client.Callback;
import eu.epicpvp.datenclient.client.ClientWrapper;
import eu.epicpvp.datenclient.client.LoadedPlayer;
import eu.epicpvp.datenserver.definitions.dataserver.player.Setting;
import eu.epicpvp.kcore.Command.Admin.CommandLocations;
import eu.epicpvp.kcore.Command.CommandHandler;
import eu.epicpvp.kcore.Hologram.nametags.NameTagMessage;
import eu.epicpvp.kcore.Hologram.nametags.NameTagType;
import eu.epicpvp.kcore.Listener.kListener;
import eu.epicpvp.kcore.LoginManager.Commands.CommandLogin;
import eu.epicpvp.kcore.LoginManager.Commands.CommandRegister;
import eu.epicpvp.kcore.Translation.TranslationHandler;
import eu.epicpvp.kcore.Update.Event.UpdateEvent;
import eu.epicpvp.kcore.Update.UpdateType;
import eu.epicpvp.kcore.Util.TimeSpan;
import eu.epicpvp.kcore.Util.Title;
import eu.epicpvp.kcore.Util.UtilPlayer;
import eu.epicpvp.kcore.Util.UtilServer;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class LoginManager extends kListener {

	@Getter
	private ClientWrapper client;
	@Getter
	private JavaPlugin instance;
	private CommandHandler commandHandler;
	@Getter
	private HashMap<String, String> login;
	@Getter
	private ArrayList<String> register;
	@Getter
	private ArrayList<String> jumpAndRun;
	@Getter
	private HashMap<Player, Long> timer;
	private Title loginTitle = new Title("§c/login [Password]", "");
	private Title registerTitle = new Title("§cRegister", "");
	private Location spawn;

	private NameTagMessage hm1 = new NameTagMessage(NameTagType.PACKET, CommandLocations.getLocation("HM1"), new String[]{"§eWillkommen auf §6EpicPvP.eu", "§7Bitte absolviere das §aJump n' Run§7 um zu", "§7verifizieren, dass du kein Bot bist."});
	private NameTagMessage hm2 = new NameTagMessage(NameTagType.PACKET, CommandLocations.getLocation("HM2"), new String[]{"§7Bitte stelle dich auf die §6Goldplatte§7 um", "§7dich registrieren zu können."});

	public LoginManager(JavaPlugin instance, CommandHandler commandHandler, ClientWrapper client) {
		super(instance, "LoginManager");
		this.instance = instance;
		this.client = client;
		this.commandHandler = commandHandler;
		this.login = new HashMap<>();
		this.register = new ArrayList<>();
		this.jumpAndRun = new ArrayList<>();
		this.timer = new HashMap<>();
		this.loginTitle.setFadeInTime(0);
		this.registerTitle.setFadeInTime(0);
		this.spawn = CommandLocations.getLocation("spawn").add(0, 0.5, 0);

		this.commandHandler.register(CommandLogin.class, new CommandLogin(this));
		this.commandHandler.register(CommandRegister.class, new CommandRegister(this));
		Bukkit.getMessenger().registerOutgoingPluginChannel(instance, "login");
	}

	public void addPlayerToBGList(Player player) {
		try {
			ByteArrayOutputStream b = new ByteArrayOutputStream();
			DataOutputStream out = new DataOutputStream(b);
			out.writeUTF(player.getName());
			player.sendPluginMessage(instance, "login", b.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void load(String playername) {
		if (this.login.containsKey(playername))
			this.login.remove(playername);
		if (this.register.contains(playername))
			this.register.remove(playername);
		if (this.jumpAndRun.contains(playername))
			this.jumpAndRun.remove(playername);

		LoadedPlayer loadedplayer = this.client.getPlayerAndLoad(playername);
		loadedplayer.getSettings(Setting.PASSWORD).getAsync(new Callback<PacketOutPlayerSettings.SettingValue[]>() {

			@Override
			public void call(PacketOutPlayerSettings.SettingValue[] response, Throwable exception) {
				logMessage("STEP 1 " + loadedplayer.getName());
				if (response.length == 1 && response[0].getSetting() == Setting.PASSWORD) {
					logMessage("STEP 2 " + loadedplayer.getName());
					if (response[0].getValue() != null) {
						logMessage("STEP 3 " + loadedplayer.getName());
						if (UtilPlayer.isOnline(playername)) {
							logMessage("STEP IS ONLINE " + loadedplayer.getName());
							loginTitle.setSubtitle(TranslationHandler.getText(Bukkit.getPlayer(playername), "LOGIN_MESSAGE"));
							loginTitle.send(Bukkit.getPlayer(playername));
						}
						logMessage("STEP ADD TO LOGIN " + loadedplayer.getName());

						login.put(playername.toLowerCase(), response[0].getValue());
						return;
					}
				}
				if (UtilPlayer.isOnline(playername)) {
					logMessage("STEP JUMP IS ONLINE " + loadedplayer.getName());
					Bukkit.getPlayer(playername).teleport(CommandLocations.getLocation("JAR"));
					hm1.sendToPlayer(Bukkit.getPlayer(playername));
					hm2.sendToPlayer(Bukkit.getPlayer(playername));
				}

				jumpAndRun.add(playername.toLowerCase());
				logMessage("STEP ADD TO JUMP AND RUN " + loadedplayer.getName());
			}
		});
	}

	@EventHandler
	public void message(UpdateEvent ev) {
		if (ev.getType() == UpdateType.SEC_3) {
			for (Player player : UtilServer.getPlayers()) {
				if (this.login.containsKey(player.getName())) {
					loginTitle.setSubtitle(TranslationHandler.getText(player, "LOGIN_MESSAGE"));
					loginTitle.send(player);
					player.sendMessage(TranslationHandler.getText(player, "PREFIX") + "§c/login [Password]");
				} else if (this.register.contains(player.getName())) {
					registerTitle.setSubtitle(TranslationHandler.getText(player, "REGISTER_MESSAGE"));
					registerTitle.send(player);
					player.sendMessage(TranslationHandler.getText(player, "PREFIX") + TranslationHandler.getText(player, "REGISTER_MESSAGE"));
				}
			}
		}
	}

	ArrayList<Player> list;

	@EventHandler
	public void timer(UpdateEvent ev) {
		if (ev.getType() == UpdateType.SEC_2) {
			if (list == null) {
				this.list = new ArrayList<>();
			} else {
				this.list.clear();
			}
			try {

				for (Map.Entry<Player, Long> entry : this.timer.entrySet()) {
					Player plr = entry.getKey();
					if (plr.isOnline()) {
						if ((System.currentTimeMillis() - entry.getValue()) > TimeSpan.MINUTE * 3) {
							plr.kickPlayer("§cDie Zeit ist abgelaufen!");
						} else {
							continue;
						}
					}
					list.add(plr);
				}
			} catch (ConcurrentModificationException e) {

			}

			for (Player player : list)
				this.timer.remove(player);
		}
	}

	@EventHandler
	public void jumpAndRunDone(PlayerInteractEvent ev) {
		if (ev.getAction() == Action.PHYSICAL) {
			if (ev.getClickedBlock().getType() == Material.GOLD_PLATE) {
				if (this.jumpAndRun.contains(ev.getPlayer().getName().toLowerCase())) {
					this.jumpAndRun.remove(ev.getPlayer().getName().toLowerCase());
					this.register.add(ev.getPlayer().getName().toLowerCase());
					this.timer.put(ev.getPlayer(), System.currentTimeMillis());

					this.hm1.clear(ev.getPlayer());
					this.hm2.clear(ev.getPlayer());
					logMessage("Jump and Run beendet und Register wird eingeleitet " + ev.getPlayer().getName());
					ev.getPlayer().sendMessage(TranslationHandler.getText(ev.getPlayer(), "PREFIX") + TranslationHandler.getText(ev.getPlayer(), "REGISTER_MESSAGE"));
				}
			}
		}
	}

	@EventHandler
	public void quit(PlayerQuitEvent ev) {
		this.login.remove(ev.getPlayer().getName().toLowerCase());
		this.register.remove(ev.getPlayer().getName().toLowerCase());
		this.jumpAndRun.remove(ev.getPlayer().getName().toLowerCase());
		this.timer.remove(ev.getPlayer());
	}

	@EventHandler
	public void move(PlayerMoveEvent ev) {
		if (!ev.getPlayer().isOnGround()) {
			if (ev.getPlayer().getLocation().getY() < (spawn.getY() - 20)) {
				if (this.login.containsKey(ev.getPlayer().getName().toLowerCase())) {
					ev.getPlayer().teleport(spawn);
				}

				if (this.register.contains(ev.getPlayer().getName().toLowerCase())) {
					ev.getPlayer().teleport(spawn);
				}

				if (this.jumpAndRun.contains(ev.getPlayer().getName().toLowerCase())) {
					ev.getPlayer().teleport(CommandLocations.getLocation("JAR"));
				}
			}
		}
	}

	@EventHandler
	public void join(PlayerJoinEvent ev) {
		if (this.login.containsKey(ev.getPlayer().getName().toLowerCase())) {
			loginTitle.setSubtitle(TranslationHandler.getText(ev.getPlayer(), "LOGIN_MESSAGE"));
			loginTitle.send(ev.getPlayer());
			ev.getPlayer().teleport(spawn);
			logMessage("Join " + ev.getPlayer().getName() + " to login!");
		}

		if (this.jumpAndRun.contains(ev.getPlayer().getName().toLowerCase())) {
			ev.getPlayer().teleport(CommandLocations.getLocation("JAR"));
			hm1.sendToPlayer(ev.getPlayer());
			hm2.sendToPlayer(ev.getPlayer());
			logMessage("Join " + ev.getPlayer().getName() + " to Jump And Run!");
		}

		this.timer.put(ev.getPlayer(), System.currentTimeMillis());
	}

	@EventHandler
	public void login(AsyncPlayerPreLoginEvent ev) {
		load(ev.getName());
	}
}
