package eu.epicpvp.kcore.lottery;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import eu.epicpvp.datenserver.definitions.dataserver.gamestats.GameType;
import eu.epicpvp.datenserver.definitions.dataserver.gamestats.StatsKey;
import eu.epicpvp.kcore.StatsManager.StatsManager;
import eu.epicpvp.kcore.StatsManager.StatsManagerRepository;
import eu.epicpvp.kcore.Util.UtilInv;
import eu.epicpvp.kcore.Util.UtilServer;
import lombok.Getter;

public class Lottery {

	@Getter
	private final JavaPlugin plugin;
	private final GameType gameType;
	private final StatsKey statsKey;
	private Map<Integer, Integer> data;
	@Getter
	private long endTime;
	private BukkitTask task;
	@Getter
	private LotteryInventory lotteryInventory;

	@SuppressWarnings("unchecked")
	public Lottery(JavaPlugin plugin, GameType gameType, StatsKey statsKey) {
		this.plugin = plugin;
		this.gameType = gameType;
		this.statsKey = statsKey;
		if (statsKey.getType() != int.class && statsKey.getType() != double.class) {
			throw new IllegalArgumentException("invalid statskey");
		}
		Object o = plugin.getConfig().get("lottery.data");
		if (o != null) {
			data = (Map<Integer, Integer>) o;
		} else {
			data = new HashMap<>();
		}
		lotteryInventory = new LotteryInventory(this);
		UtilInv.getBase().addPage( lotteryInventory );

		task = plugin.getServer().getScheduler().runTaskTimer(plugin, this::drawWinner, 2 * 60 * 60 * 20, 2 * 60 * 60 * 20);
	}

	private void drawWinner() {
		Map<Integer, Integer> map = new HashMap<>();
		data.forEach((playerId, value) -> {
			map.put(playerId, value);
		});
		int pot = 0;
		for (Integer value : map.values()) {
			pot += value;
		}
		int winnerPos = new SecureRandom().nextInt(pot);
		int pos = 0;
		int winnerId = -1;
		for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
			if (winnerPos >= pos && winnerPos < entry.getValue()) {
				winnerId = entry.getKey();
				break;
			}
			pos += entry.getValue();
		}
		if (winnerId == -1) {
			Bukkit.broadcastMessage("§c§lFehler bei der Auslosung des Lotteriesystems. Schalte ab...");
			System.out.println("data = " + data);
			System.out.println("map = " + map);
		}
		StatsManager statsManager = StatsManagerRepository.getStatsManager(gameType);
		if (statsKey.getType() == int.class) {
			statsManager.set(winnerId, statsKey, statsManager.getInt(winnerId, statsKey) + getCurrentPot());
		} else {
			statsManager.set(winnerId, statsKey, statsManager.getDouble(winnerId, statsKey) + getCurrentPot());
		}
		//TODO send messages
		data.clear();
	}

	public void onDisable() {
		task.cancel();
		task = null;
		plugin.getConfig().set("lottery.data", data);
		plugin.saveConfig();
	}

	public int getCurrentPot() {
		int pot = 0;
		for (Integer value : data.values()) {
			pot += value;
		}
		return pot;
	}

	public boolean hasBid(Player player) {
		return !data.containsKey(UtilServer.getClient().getPlayerAndLoad(player.getUniqueId()).getPlayerId());
	}

	public boolean bid(Player player, int amount) {
		int playerId = UtilServer.getClient().getPlayerAndLoad(player.getUniqueId()).getPlayerId();
		StatsManager statsManager = StatsManagerRepository.getStatsManager(gameType);
		double currentAmount = statsManager.getDouble(playerId, statsKey);
		if (currentAmount < amount) {
			return false;
		} else {
			if (statsKey.getType() == int.class) {
				statsManager.addInt(player, -amount, statsKey);
			} else {
				statsManager.addDouble(player, -amount, statsKey);
			}
			data.put(playerId, amount);
			plugin.getConfig().set("lottery.data", data);
			plugin.saveConfig();
			return true;
		}
	}

	public int getBid(Player player) {
		int playerId = UtilServer.getClient().getPlayerAndLoad(player.getUniqueId()).getPlayerId();
		return data.getOrDefault(playerId, 0);
	}
}