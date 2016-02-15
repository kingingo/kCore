package me.kingingo.kcore.LagMeter.Chunks;

import java.util.ArrayList;

import me.kingingo.kcore.Util.UtilFile;
import me.kingingo.kcore.kConfig.kConfig;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class ChunkCleanup{
	private int interval;
	private int ignoreDistance;
	private TickLimiter limiter;
	private boolean saveChunks;
	private kConfig config;
	private JavaPlugin instance;

	private Chunk chunk;
	private ArrayList<ChunkInfo> loadedChunks;
	private ArrayList<Player> players;
	private ArrayList<ChunkInfo> ignoredChunks;
	private ArrayList<ChunkInfo> centerChunks;
	private ChunkInfo centerChunk;
	private int initiallyLoaded;
	private int chunkX;
	private int chunkZ;
	private int radius;
	private int radiusSquared;
	private double d2;
	private int cleanedChunks = 0;
	private ChunkUnloadEvent event;

	public ChunkCleanup(JavaPlugin instance) {
		this.instance=instance;
		this.config=new kConfig(UtilFile.getYMLFile(instance, "locations"));
		if(!this.config.contains("cleanup-interval"))this.config.set("cleanup-interval", 600);
		if(!this.config.contains("tick-limit"))this.config.set("tick-limit", 15);
		if(!this.config.contains("save-chunks"))this.config.set("save-chunks", "true");
		if(!this.config.contains("ignore-distance"))this.config.set("ignore-distance", 10);
		
		this.interval = this.config.getInt("cleanup-interval");
		this.ignoreDistance = this.config.getInt("ignore-distance");
		this.limiter = new TickLimiter(this.config.getInt("tick-limit"));
		this.saveChunks = this.config.getBoolean("save-chunks");

		this.loadedChunks = new ArrayList<ChunkInfo>();
		this.ignoredChunks = new ArrayList<ChunkInfo>();
		this.centerChunks = new ArrayList<ChunkInfo>();
		this.players = new ArrayList<Player>();
		this.centerChunks = new ArrayList<ChunkInfo>();
		scheduleCleanupTask();
	}

	public void scheduleCleanupTask() {
		new BukkitRunnable() {
			public void run() {
				ChunkCleanup.this.limiter.initTick();
				loadedChunks.clear();
				players.clear();
				
				for (World world : Bukkit.getWorlds()) {
					for (Chunk chunk : world.getLoadedChunks()) {
						loadedChunks.add(new ChunkInfo(chunk));
					}
					players.addAll(world.getPlayers());
				}

				initiallyLoaded = loadedChunks.size();
				ignoredChunks.clear();
				centerChunks.clear();
				
				for (Player player : players) {
					chunk = player.getLocation().getChunk();
					chunkX = ((Chunk) chunk).getX();
					chunkZ = ((Chunk) chunk).getZ();
					centerChunk = new ChunkInfo((Chunk) chunk);
					if (!centerChunks.contains(centerChunk)) {
						centerChunks.add(centerChunk);
						radius = ChunkCleanup.this.ignoreDistance;
						radiusSquared = ChunkCleanup.this.ignoreDistance * ChunkCleanup.this.ignoreDistance;
						for (int dx = -radius; dx <= radius; dx++) {
							for (int dz = -radius; dz <= radius; dz++) {
								d2 = dx * dx + dz * dz;
								if (d2 <= radiusSquared)
									ignoredChunks.add(new ChunkInfo(
											((Chunk) chunk).getWorld(), chunkX + dx, chunkZ + dz));
							}
						}
					}
				}
				
				loadedChunks.removeAll(ignoredChunks);
				System.err.println("[ChunkCleanup] Ignored " + (initiallyLoaded - loadedChunks.size())
						+ " chunks out of initial " + initiallyLoaded
						+ " in "
						+ ChunkCleanup.this.limiter.elapsedTime()
						+ "ms");
				cleanedChunks = 0;
				
				for (ChunkInfo ci : loadedChunks) {
					if (!ChunkCleanup.this.limiter.shouldContinue())break;

					event = new ChunkUnloadEvent(ci.get());
					Bukkit.getPluginManager().callEvent(event);
					if ((!event.isCancelled())&& (ci.unload(ChunkCleanup.this.saveChunks)))cleanedChunks++;
				}
				
				System.err.println("[ChunkCleanup] Unloaded " + cleanedChunks + "/" + loadedChunks.size()
								+ " non-ignored chunks in "
								+ ChunkCleanup.this.limiter.elapsedTime()
								+ "ms");
			}
		}.runTaskTimer(this.instance, this.interval, this.interval);
	}
}