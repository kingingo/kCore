package eu.epicpvp.kcore.Listener.AntiCrashListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import dev.wolveringer.arrays.CachedArrayList;
import dev.wolveringer.client.ClientWrapper;
import dev.wolveringer.client.LoadedPlayer;
import dev.wolveringer.hashmaps.CachedHashMap;
import eu.epicpvp.kcore.Listener.kListener;
import eu.epicpvp.kcore.MySQL.MySQL;
import eu.epicpvp.kcore.PacketAPI.packetlistener.event.PacketListenerReceiveEvent;
import eu.epicpvp.kcore.Util.UtilException;
import eu.epicpvp.kcore.Util.UtilLocation;
import eu.epicpvp.kcore.Util.UtilPlayer;
import lombok.Getter;
import lombok.Setter;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import net.minecraft.server.v1_8_R3.PacketPlayOutPosition;

public class AntiCrashListener extends kListener {
	private final Map<String, Long> switchavg;
	private final Map<String, Long> hitsavg;
	private final Map<String, Integer> switchcount;
	private final Map<String, Integer> hitscount;
	private final Map<String, Long> switchlast;
	private final Map<String, Long> hitslast;
	private final ArrayList<String> kick;
	private ClientWrapper client;
	private MySQL mysql;
	private CachedArrayList<Player> whitelist = new CachedArrayList<>(500, TimeUnit.MILLISECONDS);
	private CachedHashMap<Player, Integer> movementHits = new CachedHashMap<>(2000, TimeUnit.MILLISECONDS);
	private CachedArrayList<Player> kicked = new CachedArrayList<>(2000, TimeUnit.MILLISECONDS);
	@Getter
	@Setter
	private static boolean movement = false;

	public AntiCrashListener(ClientWrapper client, MySQL mysql) {
		super(mysql.getInstance(), "AntiCrashListener");
		this.hitsavg = new HashMap<>();
		this.switchavg = new HashMap<>();

		this.switchcount = new HashMap<>();
		this.hitscount = new HashMap<>();

		this.switchlast = new HashMap<>();
		this.hitslast = new HashMap<>();

		this.kick = new ArrayList<>();

		this.mysql = mysql;
		this.client = client;
	}

	private static final int KICK_LIMIT = 30;
	private static final int POS_RESEND = 12;
	private static final double MAX_POS_DIFF_MULTIPLIER = 17; //15 seems slightly not enough

	@EventHandler
	public void a(PlayerJoinEvent e) {
		this.whitelist.add(e.getPlayer());
	}

	@EventHandler
	public void a(PlayerRespawnEvent e) {
		this.whitelist.add(e.getPlayer());
	}

	@EventHandler
	public void a(PlayerDeathEvent e) {
		this.whitelist.add(e.getEntity());
	}

	@EventHandler
	public void a(PlayerTeleportEvent e) {
		this.whitelist.add(e.getPlayer());
	}

	@EventHandler
	public void a(final PacketListenerReceiveEvent e) {
		if (!movement) {
			return;
		}

		Player player = e.getPlayer();
		if ((e.getPacket() == null) || (player == null) || (player.isDead())) {
			return;
		}

		if ((e.getPacket() instanceof PacketPlayInFlying)) {
			PacketPlayInFlying packet = (PacketPlayInFlying) e.getPacket();

			double max = getMaxDiff(player);

			double packetX = packet.a();
			double packetY = packet.b();
			double packetZ = packet.c();
			World playerWorld = player.getWorld();
			Location packetLocation = new Location(playerWorld, packetX, packetY, packetZ);

			boolean packetHasPos = packet.g();
			Location location = player.getLocation();
			if (location != null && packetHasPos) {
				double diff = 0;
				boolean hit = false;
				boolean instandKick = false;

				if (!this.whitelist.contains(player) || player.getGameMode() == GameMode.CREATIVE) { //ignore these for gm 1 as they are way faster
					if (packetLocation.distanceSquared(new Location(playerWorld, 8.5D, 65.0D, 8.5D)) < 10.0D) {
						logMessage("Special Position player: " + player.getName() + " loc: " + packetLocation);
					}
					if ((diff = Math.abs(location.getX() - packetX)) > max) {
						logMessage("Wrong X movement (" + diff + ">" + max + ")");
						hit = true;
					} else if ((diff = Math.abs(location.getY() - packetY)) > 15) {
						logMessage("Wrong Y movement (" + diff + ">" + max + ")");
						hit = true;
					} else if ((diff = Math.abs(location.getZ() - packetZ)) > max) {
						logMessage("Wrong Z movement (" + diff + ">" + max + ")");
						hit = true;
					}
				}

				if (Double.isInfinite(packetX) || Double.isInfinite(packetY) || Double.isInfinite(packetZ)) {
					logMessage("Infinite var in pos! (" + packetX + "," + packetY + "," + packetZ + ")");
					hit = true;
					instandKick = true;
				}
				if (packetY >= 2E9) {
					logMessage("Duck at " + player.getName());
				}
				if (packetY > Short.MAX_VALUE) {
					logMessage("Invalid Y coordinate");
					hit = true;
					instandKick = true;
				}
				if (hit) {
					hitWrongMove(e, max, packetX, packetY, packetZ, instandKick);
					e.setCancelled(true);
				}
			}
		}
	}

	private double getMaxDiff(Player player) {
		double factor = player.isFlying() ? player.getFlySpeed() * 3 : player.getWalkSpeed();
		return MAX_POS_DIFF_MULTIPLIER * factor;
	}

	private void hitWrongMove(final PacketListenerReceiveEvent e, double max, double packetX, double packetY, double packetZ, boolean instandKick) {
		int mc = 0;
		if (!this.movementHits.containsKey(e.getPlayer())) {
			this.movementHits.put(e.getPlayer(), mc = 1);
		} else {
			try {
				this.movementHits.put(e.getPlayer(), mc = (this.movementHits.get(e.getPlayer()) + 1));
			} catch (NullPointerException ev) {
				return;
			}
		}

		if ((mc > KICK_LIMIT) && (!this.kicked.contains(e.getPlayer())) || instandKick) {
			this.kicked.add(e.getPlayer());
			logMessage("Kicking player: " + e.getPlayer().getName() + " for wrong move!");
			Bukkit.getScheduler().runTask(this.mysql.getInstance(), () -> e.getPlayer().kickPlayer("§cWrong move!"));
		}
		if (mc > POS_RESEND) {
			UtilPlayer.getCraftPlayer(e.getPlayer()).getHandle().playerConnection.sendPacket(
					new PacketPlayOutPosition(e.getPlayer().getLocation().getX(), e.getPlayer().getLocation().getY(), e.getPlayer().getLocation().getZ(), e.getPlayer().getLocation().getYaw(), e.getPlayer().getLocation().getPitch(), new HashSet<>()));
		}
		logMessage(" (Level: " + mc + " / Kicked: " + this.kicked.contains(e.getPlayer()) + " / flying: " + e.getPlayer().isFlying() + ") The player " + e.getPlayer().getName() + " try to move wrongly! from "
				+ UtilLocation.getLocString(e.getPlayer().getLocation()) + " to " + packetX + "," + packetY + "," + packetZ);
	}

	@EventHandler
	public void onItemHeld(PlayerItemHeldEvent e) {
		if (this.kick.contains(e.getPlayer().getName())) {
			e.setCancelled(true);
		} else {
			String name = e.getPlayer().getName();
			Long average = this.switchavg.get(name);
			Long last = this.switchlast.get(name);
			Integer count = this.switchcount.get(name);
			if (count == null) {
				count = 0;
			}
			if (average == null) {
				average = 0L;
			}
			if (last == null) {
				last = 0L;
			}
			if (count > 200) {
				if (average / 200L < 50000L) {
					this.kick.add(e.getPlayer().getName());
					LoadedPlayer loadedplayer = this.client.getPlayerAndLoad(e.getPlayer().getName());
					loadedplayer.kickPlayer("§cStop!");
					logMessage("IP: " + e.getPlayer().getAddress().getAddress().getHostAddress() + " PlayerId:"
							+ loadedplayer.getPlayerId() + " UUID:" + e.getPlayer().getUniqueId());
					logMessage("Spieler " + name + " wurde wegen Item Change Crash gekickt!");
					UtilException.catchException(client.getHandle().getName(), Bukkit.getServer().getIp(), this.mysql,
							"Spieler " + name + " wurde wegen Animation Crash gekickt! " + " IP: "
									+ e.getPlayer().getAddress().getAddress().getHostAddress() + " PlayerId:"
									+ loadedplayer.getPlayerId() + " UUID:" + e.getPlayer().getUniqueId());
				} else {
					this.switchcount.remove(name);
					this.switchavg.remove(name);
				}
			} else {
				if (last != 0L) {
					this.switchavg.put(name, average + (System.nanoTime() - last));
				}
				this.switchcount.put(name, count + 1);
				this.switchlast.put(name, System.nanoTime());
			}
		}
	}

	@EventHandler
	public void onAnimation(PlayerAnimationEvent e) {
		if (kick.contains(e.getPlayer().getName())) {
			e.setCancelled(true);
		} else {
			String name = e.getPlayer().getName();
			Long avg = this.hitsavg.get(name);
			Long last = this.hitslast.get(name);
			Integer i = this.hitscount.get(name);
			if (i == null) {
				i = 0;
			}
			if (avg == null) {
				avg = 0L;
			}
			if (last == null) {
				last = 0L;
			}
			if (i > 200) {
				if (avg / 200L < 50000L) {
					this.kick.add(e.getPlayer().getName());
					LoadedPlayer loadedplayer = this.client.getPlayerAndLoad(e.getPlayer().getName());
					loadedplayer.kickPlayer("§cStop!");
					logMessage("IP: " + e.getPlayer().getAddress().getAddress().getHostAddress() + " Real-UUID:"
							+ loadedplayer.getPlayerId() + " UUID:" + e.getPlayer().getUniqueId());
					logMessage("Spieler " + name + " wurde wegen Animation Crash gekickt!");
					UtilException.catchException(client.getHandle().getName(), Bukkit.getServer().getIp(), this.mysql,
							"Spieler " + name + " wurde wegen Animation Crash gekickt! " + " IP: "
									+ e.getPlayer().getAddress().getAddress().getHostAddress() + " PlayerId:"
									+ loadedplayer.getPlayerId() + " UUID:" + e.getPlayer().getUniqueId());
				} else {
					this.hitscount.remove(name);
					this.hitsavg.remove(name);
				}
			} else {
				if (last != 0L) {
					this.hitsavg.put(name, avg + (System.nanoTime() - last));
				}
				this.hitscount.put(name, i + 1);
				this.hitslast.put(name, System.nanoTime());
			}
		}
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		this.switchavg.remove(e.getPlayer().getName());
		this.switchcount.remove(e.getPlayer().getName());
		this.switchlast.remove(e.getPlayer().getName());
		this.hitsavg.remove(e.getPlayer().getName());
		this.hitscount.remove(e.getPlayer().getName());
		this.hitslast.remove(e.getPlayer().getName());
		this.kick.remove(e.getPlayer().getName());
	}
}