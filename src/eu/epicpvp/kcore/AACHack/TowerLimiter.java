package eu.epicpvp.kcore.AACHack;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import eu.epicpvp.kcore.AACHack.util.Rate;
import me.konsolas.aac.api.HackType;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class TowerLimiter implements Listener {

	private Map<UUID, Rate> blockPlaceRate = new HashMap<>();

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		blockPlaceRate.remove(event.getPlayer().getUniqueId());
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockPlace(BlockPlaceEvent event) {
		Player plr = event.getPlayer();
		if (plr.isFlying() || plr.isOp() || plr.getGameMode() == GameMode.CREATIVE || plr.getGameMode() == GameMode.SPECTATOR) {
			return;
		}
		Collection<PotionEffect> activePotionEffects = plr.getActivePotionEffects();
		for (PotionEffect effect : activePotionEffects) {
			if (effect.getType() == PotionEffectType.JUMP && effect.getAmplifier() > 0 && effect.getAmplifier() < 128) {
				return;
			}
		}
		Material type = event.getItemInHand().getType();
		if (!type.isSolid()) {
			return;
		}
		Block blockPlaced = event.getBlockPlaced();
		Block blockAgainst = event.getBlockAgainst();
		boolean placedAboveAnotherBlock = isSameBlockLocation(blockAgainst.getRelative(BlockFace.UP), blockPlaced);
		if (!placedAboveAnotherBlock) {
			return;
		}
		//just in case, block placing at the player's location
		Location plrLoc = plr.getLocation();
		Location blockPlacedLoc = blockPlaced.getLocation();
		if (isSameBlockLocation(plrLoc, blockPlacedLoc)) {
			System.out.println("tower detected - sameblock");
			event.setCancelled(true);
			event.setBuild(false);
			return;
		}
		int violationAmount = 2;

		double placedY = blockPlacedLoc.getY();
		double plrY = plrLoc.getY();
		boolean isExactlyOneBlockAbove = placedY + 1 == plrY;
		if (isExactlyOneBlockAbove) {
			violationAmount++;
		}
		boolean plrAboveBuiltBlock = plrY >= placedY + 1;
		if (!plrAboveBuiltBlock) {
			return;
		}
		if (!couldStandOnBlockXZ(plrLoc, blockPlacedLoc)) {
			return;
		}
		Rate rate = getRate(plr);
		double averagePerSecond = rate.getAveragePerSecond(3, TimeUnit.SECONDS);
		if (averagePerSecond > 2.1) {
			System.out.println("detected fasttower for player " + plr.getName() + " (avg3s:" + averagePerSecond + ")");
			event.setCancelled(true);
			event.setBuild(false);
			try {
				AACAccessor.increaseAllViolationsAndNotify(plr.getUniqueId(), violationAmount, HackType.FASTPLACE, "(Custom) (TowerLimiter) " + plr.getName() + " is building too fast upwards (avg3s: " + averagePerSecond + ")");
			} catch (ReflectiveOperationException e) {
				e.printStackTrace();
			}
		} else {
			rate.eventTriggered();
		}
	}

	public Rate getRate(Player plr) {
		UUID uuid = plr.getUniqueId();
		Rate rate = blockPlaceRate.get(uuid);
		if (rate == null) {
			blockPlaceRate.put(uuid, rate = new Rate(10, TimeUnit.SECONDS));
		}
		return rate;
	}

	public boolean isSameBlockLocation(Block first, Block second) {
		return isSameBlockLocation(first.getLocation(), second.getLocation());
	}

	public boolean isSameBlockLocation(Location first, Location second) {
		return first.getBlockX() == second.getBlockX() && first.getBlockY() == second.getBlockY() && first.getBlockZ() == second.getBlockZ();
	}

	public boolean couldStandOnBlockXZ(Location plrLoc, Location blockLoc) {
		return isInRange(plrLoc.getX(), blockLoc.getX() + .5, (1 + .3) / 2) && isInRange(plrLoc.getZ(), blockLoc.getZ() + .5, (1 + .3) / 2);
	}

	/**
	 * Checks if the given {@code test} value does not differ more than {@code range} from {@code expected}.
	 * @param test the value to test
	 * @param exptected the expected value
	 * @param range the maximum range
	 * @return if its in the range
	 */
	public boolean isInRange(double test, double exptected, double range) {
		return exptected - range <= test && exptected + range >= test;
	}
}
