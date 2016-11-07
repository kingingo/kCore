package eu.epicpvp.kcore.Listener.FlyListener;

import java.util.ArrayList;

import com.google.common.collect.Lists;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import eu.epicpvp.kcore.Listener.kListener;
import eu.epicpvp.kcore.Update.Event.UpdateEvent;
import eu.epicpvp.kcore.Update.UpdateType;
import eu.epicpvp.kcore.Util.UtilMath;
import eu.epicpvp.kcore.Util.UtilParticle;
import eu.epicpvp.kcore.Util.UtilServer;
import eu.epicpvp.kcore.Util.UtilWorldGuard;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;

public class FlyListener extends kListener {

	private ArrayList<Player> list = Lists.newArrayList();

	public FlyListener() {
		super(UtilServer.getPluginInstance(), "FlyListener");
	}

	@EventHandler
	public void move(PlayerMoveEvent ev) {
		Player player = ev.getPlayer();
		if (player.isOp() || player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) {
			return;
		}
		if (list.contains(player) || player.getAllowFlight()) {
			if (UtilWorldGuard.RegionFlag(player, DefaultFlag.PVP)) {
				player.setFlying(false);
				player.setAllowFlight(false);
				list.remove(player);
			}
		} else if (!player.getAllowFlight()) {
			if (UtilWorldGuard.RegionFlag(player, DefaultFlag.PVP)) {
				player.setAllowFlight(true);
			}
		}
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent ev) {
		list.remove(ev.getPlayer());
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent ev) {
		ev.getPlayer().setAllowFlight(true);
	}

	@EventHandler
	public void onUpdate(UpdateEvent ev) {
		if (ev.getType() == UpdateType.FASTEST) {
			Player player;
			for (int a = 0; a < list.size(); a++) {
				player = list.get(a);

				if (player.isOnline()) {
					try {
						for (int i = 0; i < 10; i++)
							UtilParticle.REDSTONE.sendColor(Bukkit.getOnlinePlayers(), player.getLocation().add(UtilMath.RandomDouble(.5, -.5), UtilMath.RandomDouble(-.1, -.5), UtilMath.RandomDouble(.5, -.5)), Color.WHITE);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					list.remove(player);
				}
			}
		}
	}

	@EventHandler
	public void onToggleFlight(PlayerToggleFlightEvent ev) {
		if (ev.getPlayer().getGameMode() != GameMode.CREATIVE) {
			if (!UtilWorldGuard.RegionFlag(ev.getPlayer(), DefaultFlag.PVP)) {
				if (ev.getPlayer().isFlying()) {
					ev.getPlayer().setFlying(false);
					list.remove(ev.getPlayer());
				} else {
					ev.getPlayer().setFlying(true);
					list.add(ev.getPlayer());
				}
			} else {
				ev.setCancelled(true);
			}
		}
	}
}
