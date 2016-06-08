package eu.epicpvp.kcore.Particle;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import eu.epicpvp.kcore.Util.UtilParticle;
import lombok.Getter;

public class PlayerParticleDisplayer<P extends Enum<P>, V> implements Listener, Runnable {

	private final UUID uuid;
	@Getter
	private final ParticleShape<P, V> shape;
	private final ParticleShape.ValueHolder<V> valueHolder;
	private int taskId = -1;
	private JavaPlugin plugin;

	public PlayerParticleDisplayer(Player player, ParticleShape<P, V> shape) {
		this.uuid = player.getUniqueId();
		this.shape = shape;
		valueHolder = shape.createValueHolder();
	}

	public boolean start(JavaPlugin plugin) {
		if (taskId > -1) {
			return false;
		}
		this.plugin = plugin;
		taskId = plugin.getServer().getScheduler().runTaskAsynchronously(plugin, this).getTaskId();
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		return true;
	}

	public boolean stop() {
		HandlerList.unregisterAll(this);
		if (taskId > -1) {
			plugin.getServer().getScheduler().cancelTask(taskId);
			plugin = null;
			taskId = -1;
			return true;
		}
		return false;
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		if (!player.getUniqueId().equals(uuid)) {
			return;
		}
		Location to = event.getTo();
		Location from = event.getFrom();
		display(player, from, to);
	}

	private synchronized void display(Player player, Location from, Location to) {
		boolean show = shape.transformPerTick(player, to, valueHolder, from);

		if (!show) {
			return;
		}

		shape.getPositions().entrySet().parallelStream().forEach(entry -> {
			P shapePart = entry.getValue();
			Vector particlePos = entry.getKey().clone();
			Color color = shape.transformPerParticle(player, to, particlePos, shapePart, valueHolder);
			if (color != null) {
				if (shape.isPlayerSpecificTransform()) {
					Bukkit.getOnlinePlayers().parallelStream().forEach(plr -> {
						Vector finalParticlePos = particlePos.clone();
						Color finalColor = shape.transformPerParticleAndPlayer(player, plr, to, finalParticlePos, shapePart, valueHolder, color);
						
						if (finalColor != null) {
							Location particleLoc = finalParticlePos.toLocation(player.getWorld());
							trySendParticle(plr, particleLoc, finalColor);
						}
					});
				} else {
					Location particleLoc = particlePos.toLocation(player.getWorld());
					sendToAll(color, particleLoc);
				}
			}
		});
	}

	@Override
	public void run() {
		while (taskId > -1) {
			long start = System.currentTimeMillis();
			Player player = Bukkit.getPlayer(uuid);
			if (player == null) {
				stop();
				return;
			}
			display(player, null, player.getLocation());
			long milliDur = System.currentTimeMillis() - start;
			if (milliDur >= 50) {
				System.out.println(shape.getName() + " particles for " + player.getName() + " took too long: " + milliDur + "ms");
			} else {
				try {
					Thread.sleep(50 - milliDur);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onQuit(PlayerQuitEvent event) {
		if (event.getPlayer().getUniqueId().equals(uuid)) {
			stop();
		}
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onKick(PlayerKickEvent event) {
		if (event.getPlayer().getUniqueId().equals(uuid)) {
			stop();
		}
	}

	private static void sendToAll(Color color, Location location) {
		Bukkit.getOnlinePlayers().parallelStream().forEach(plr -> trySendParticle(plr, location, color));
	}

	private static void trySendParticle(Player plr, Location location, Color color) {
		try {
			UtilParticle.REDSTONE.sendColor(plr, location, color);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
