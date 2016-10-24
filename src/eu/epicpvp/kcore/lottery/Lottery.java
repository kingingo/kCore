package eu.epicpvp.kcore.lottery;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

import eu.epicpvp.datenclient.client.Callback;
import eu.epicpvp.datenserver.definitions.dataserver.gamestats.GameType;
import eu.epicpvp.datenserver.definitions.dataserver.gamestats.StatsKey;
import eu.epicpvp.kcore.StatsManager.StatsManager;
import eu.epicpvp.kcore.StatsManager.StatsManagerRepository;
import eu.epicpvp.kcore.Translation.TranslationHandler;
import eu.epicpvp.kcore.Util.UtilInv;
import eu.epicpvp.kcore.Util.UtilServer;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public class Lottery {

//	private static final int LOTTERY_DRAW_TICKS = 2 * 60 * 60 * 20;
	private static final int LOTTERY_DRAW_TICKS = 60 * 20;
	@Getter
	private final JavaPlugin plugin;
	private final GameType gameType;
	private final StatsKey statsKey;
	private Map<Integer, Integer> data = new HashMap<>();
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
		ConfigurationSection section = plugin.getConfig().getConfigurationSection("lottery.data");
		for (String playerId : section.getKeys(false)) {
			data.put(Integer.valueOf(playerId), section.getInt(playerId));
		}
		lotteryInventory = new LotteryInventory(this);
		UtilInv.getBase().addPage(lotteryInventory);
		UtilServer.getCommandHandler().register(CommandLottery.class, new CommandLottery(this));

		task = plugin.getServer().getScheduler().runTaskTimer(plugin, this::drawWinner, LOTTERY_DRAW_TICKS, LOTTERY_DRAW_TICKS);
		endTime = System.currentTimeMillis() + LOTTERY_DRAW_TICKS;
	}

	private void drawWinner() {
		if (data.isEmpty()) {
			Bukkit.broadcastMessage(TranslationHandler.getText("PREFIX")+"§cEs hat niemand an der Lotterie teilgenommen.");
		}else if (data.size() <= 5) {
			Bukkit.broadcastMessage(TranslationHandler.getText("PREFIX")+"§cEs müssen mehr als 5 Spieler teilnehmen!.");
		}else{
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
				Bukkit.broadcastMessage("§c§lFehler bei der Auslosung des Lotteriesystems. Mache gar nichts.");
				System.err.println("Fehler bei der Auslosung des Lotteriesystems. Mache gar nichts.");
				System.out.println("data = " + data);
				System.out.println("map = " + map);
				return;
			}
			StatsManager statsManager = StatsManagerRepository.getStatsManager(gameType);
			int currentPot = getCurrentPot();
			if (statsKey.getType() == int.class) {
				if(statsManager.isLoaded(winnerId)){
					statsManager.set(winnerId, statsKey, statsManager.getInt(winnerId, statsKey) + currentPot);
				}else{
					statsManager.loadPlayer(winnerId, new Callback<Integer>() {
						
						@Override
						public void call(Integer id, Throwable exception) {
							statsManager.set(id, statsKey, statsManager.getInt(id, statsKey) + currentPot);
							statsManager.save(id);
						}
					});
				}
			} else {
				if(statsManager.isLoaded(winnerId)){
					statsManager.set(winnerId, statsKey, statsManager.getDouble(winnerId, statsKey) + currentPot);
				}else{
					statsManager.loadPlayer(winnerId, new Callback<Integer>() {
						
						@Override
						public void call(Integer id, Throwable exception) {
							statsManager.set(id, statsKey, statsManager.getDouble(id, statsKey) + currentPot);
							statsManager.save(id);
						}
					});
				}
			}
			Bukkit.broadcastMessage(TranslationHandler.getText("PREFIX")+"§aDer Spieler §6" + UtilServer.getClient().getPlayerAndLoad(winnerId).getName() + "§a hat die Lotterie gewonnen und §6" + currentPot + " Epic's §abekommen.");
			//TODO send messages
			data.clear();
			endTime = System.currentTimeMillis() + LOTTERY_DRAW_TICKS;
		}
	}

	public void onDisable() {
		task.cancel();
		task = null;

		save();
	}

	private void save() {
		if (data.isEmpty()) {
			plugin.getConfig().set("lottery.data", null);
		} else {
			data.forEach((playerId, amount) -> {
				plugin.getConfig().set("lottery.data." + playerId, amount);
			});
		}
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
		return data.containsKey(UtilServer.getClient().getPlayerAndLoad(player.getUniqueId()).getPlayerId());
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
