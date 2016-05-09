package eu.epicpvp.kcore.Particle;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import eu.epicpvp.kcore.Util.UtilParticle;
import lombok.Getter;
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

public class PlayerParticle<E extends Enum<E>, V> implements Listener, Runnable {

	private final UUID uuid;
	@Getter
	private ParticleShape<E, V> shape;
	private ParticleShape.ValueHolder<V> valueHolder;
	private int taskId = -1;
	private JavaPlugin plugin;

	public PlayerParticle(Player player, ParticleShape<E, V> shape) {
		this.uuid = player.getUniqueId();
		this.shape = shape;
		valueHolder = shape.createValueHolder();
	}

	public void start(JavaPlugin plugin) {
		this.plugin = plugin;
		taskId = plugin.getServer().getScheduler().runTaskAsynchronously(plugin, this).getTaskId();
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	public void stop() {
		HandlerList.unregisterAll(this);
		if (taskId > -1) {
			taskId = -1;
			plugin.getServer().getScheduler().cancelTask(taskId);
		}
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
		boolean show = shape.transformPerTick(player, to, to.toVector(), valueHolder, from);

		if (!show) {
			return;
		}

		shape.getPositions().entrySet().parallelStream().forEach(entry -> {
			E value = entry.getValue();
			Vector vector = entry.getKey().clone();
			Color color = shape.transformPerParticle(player, to, vector, value, valueHolder);
			Location location = vector.toLocation(player.getWorld());
			sendToAll(color, location);
		});
	}

	@Override
	public void run() {
		long start;
		Player player;
		while (taskId > -1) {
			start = System.nanoTime();
			player = Bukkit.getPlayer(uuid);
			if (player != null) {
				display(player, null, player.getLocation());
			}
			try {
				long dur = System.nanoTime() - start;
				Thread.sleep(50 - TimeUnit.NANOSECONDS.toMillis(dur));
			} catch (InterruptedException e) {
				e.printStackTrace();
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
		Bukkit.getOnlinePlayers().parallelStream().forEach(plr -> {
			trySendParticle(plr, location, color);
		});
	}

	private static void trySendParticle(Player plr, Location location, Color color) {
		try {
			UtilParticle.REDSTONE.sendColor(plr, location, color);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
