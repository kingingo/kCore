package eu.epicpvp.kcore.Achievements.Handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import eu.epicpvp.datenclient.client.Callback;
import eu.epicpvp.kcore.Achievements.Events.PlayerLoadAchievementsEvent;
import eu.epicpvp.kcore.UserDataConfig.Events.UserDataConfigRemoveEvent;
import eu.epicpvp.kcore.Util.Title;
import eu.epicpvp.kcore.Util.UtilFirework;
import eu.epicpvp.kcore.Util.UtilPlayer;
import eu.epicpvp.kcore.Util.UtilServer;
import eu.epicpvp.kcore.Util.UtilString;
import eu.epicpvp.kcore.kConfig.kConfig;
import lombok.Getter;

@Getter
public class Achievement implements Listener {

	private boolean secret;
	private String name;
	private List<String> description;
	protected int maxprogress;
	private HashMap<Integer, Integer> playerProgress;
	private AchievementsHandler handler;
	private Callback<Integer> done;

	public Achievement(String name, List<String> description, Callback<Integer> done, boolean secret, int maxprogress) {
		this.name = name;
		this.description = description;
		this.maxprogress = maxprogress;
		this.playerProgress = new HashMap<>();
		this.secret = secret;
		this.done = done;
	}

	public ArrayList<String> getDescription(Player player) {
		ArrayList<String> list = new ArrayList<>(getDescription());
		int progress = getProgress(player);

		list.add("§6Fortschritt: " + (progress == maxprogress ? "§a" : "§c") + progress + "§7/§a" + maxprogress);
		return list;
	}

	public boolean done(Player player) {
		return done(UtilPlayer.getPlayerId(player));
	}

	public boolean done(int playerId) {
		if (this.playerProgress.containsKey(playerId)) {
			if (getProgress(playerId) >= maxprogress) {
				saveProgress(playerId);
				this.playerProgress.remove(playerId);
				if (done != null)
					done.call(playerId, null);

				Player player = UtilPlayer.searchExact(playerId);
				if (player != null) {
					Bukkit.getScheduler().runTask(handler.getInstance(), new Runnable() {
						@Override
						public void run() {
							Title title = new Title("§6§lErfolgreich Abgeschlossen", "" + getName());
							title.send(player);
							UtilFirework.start(-1, player.getLocation(), UtilFirework.RandomColor(), Type.BALL_LARGE);
							UtilFirework.start(-1, player.getLocation(), UtilFirework.RandomColor(), Type.BALL_LARGE);
							UtilFirework.start(-1, player.getLocation(), UtilFirework.RandomColor(), Type.BALL_LARGE);

							player.sendMessage("§a§l▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
							player.sendMessage(" ");
							player.sendMessage(UtilString.center("▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬".length(), "Du hast das Achievement erfolgreich abgeschlossen!".length()) + "§aDu hast das Achievement erfolgreich abgeschlossen!");
							player.sendMessage(" ");
							player.sendMessage("§a§l▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
						}
					});
				}

				return true;
			}
		}
		return false;
	}

	public Integer getProgress(Player player) {
		return getProgress(UtilPlayer.getPlayerId(player));
	}

	public Integer getProgress(int playerId) {
		if (this.playerProgress.containsKey(playerId))
			return this.playerProgress.get(playerId);

		if (this.handler.isConfig()) {
			kConfig config = UtilServer.getUserData().getConfig(Integer.valueOf(playerId));
			if (config.isSet("Achievements." + getClass().getSimpleName() + ".Progress")) {
				return config.getInt("Achievements." + getClass().getSimpleName() + ".Progress");
			}
		} else {

		}
		return 0;
	}

	public void saveProgress(Player player) {
		saveProgress(UtilPlayer.getPlayerId(player));
	}

	public void saveProgress(int playerId) {
		if (this.handler.isConfig() &&  getPlayerProgress().get(Integer.valueOf(playerId)) != null) {
			kConfig config = UtilServer.getUserData().getConfig(Integer.valueOf(playerId));
			Integer val = getPlayerProgress().get(Integer.valueOf(playerId));
			if(val == null)
				return;
			config.set("Achievements." + getClass().getSimpleName() + ".Progress", val);
			config.save();
		}
	}

	public void register(AchievementsHandler handler) {
		this.handler = handler;
		Bukkit.getPluginManager().registerEvents(this, handler.getInstance());
		handler.getAchievements().add(this);
	}

	@EventHandler
	public void UserDataConfigRemove(UserDataConfigRemoveEvent ev){
		if (this.playerProgress.containsKey(UtilPlayer.getPlayerId(ev.getPlayer()))) {
			saveProgress(ev.getPlayer());
			this.playerProgress.remove(UtilPlayer.getPlayerId(ev.getPlayer()));
		}
	}

//	@EventHandler
//	public void quit(PlayerQuitEvent ev) {
//		UtilServer.loopbackUntilValidDataserverConnection(() -> {
//			synchronized (playerProgress) {
//				if (this.playerProgress.containsKey(UtilPlayer.getPlayerId(ev.getPlayer()))) {
//					saveProgress(ev.getPlayer());
//					this.playerProgress.remove(UtilPlayer.getPlayerId(ev.getPlayer()));
//				}
//			}
//		}, "archievement save " + ev.getPlayer().getName(), false);
//	}

	@EventHandler
	public void join(PlayerJoinEvent ev) {
		int pprogress = getProgress(ev.getPlayer());
		if (pprogress < maxprogress) {
			this.playerProgress.put(UtilPlayer.getPlayerId(ev.getPlayer()), pprogress);
			Bukkit.getPluginManager().callEvent(new PlayerLoadAchievementsEvent(ev.getPlayer(), this));
		}
	}

}
