package eu.epicpvp.kcore.MysteryBox;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import eu.epicpvp.datenserver.definitions.permissions.GroupTyp;
import eu.epicpvp.kcore.Listener.kListener;
import eu.epicpvp.kcore.MysteryBox.Events.PlayerUseMysteryBoxEvent;
import eu.epicpvp.kcore.MysteryBox.Items.MysteryItem;
import eu.epicpvp.kcore.MysteryBox.Templates.Building;
import eu.epicpvp.kcore.Update.UpdateType;
import eu.epicpvp.kcore.Update.Event.UpdateEvent;
import eu.epicpvp.kcore.Util.UtilBlock;
import eu.epicpvp.kcore.Util.UtilMath;
import eu.epicpvp.kcore.Util.UtilNumber;
import eu.epicpvp.kcore.Util.UtilPlayer;
import eu.epicpvp.kcore.Util.UtilServer;
import eu.epicpvp.kcore.kConfig.kConfig;
import lombok.Getter;

public class MysteryBox extends kListener {

	public static MysteryBox createChest(MysteryBoxManager chestManager, String template, ItemStack item,
			String name) {
		MysteryBox chest = new MysteryBox(chestManager);
		chest.setData(item, name, template);
		return chest;
	}

	@Getter
	private ItemStack item;
	@Getter
	private String name;
	@Getter
	private int Shards;
	@Getter
	private kConfig config;
	private ArrayList<MysteryItem> items = new ArrayList<>();
	@Getter
	private HashMap<Player, MysteryBoxSession> sessions = new HashMap<>();
	private Building building;
	private int counter = 0;
	private int canOpen = 2;
	private double total;
	@Getter
	private MysteryBoxManager manager;

	public MysteryBox(MysteryBoxManager chestManager) {
		super(chestManager.getInstance(), "MysteryBox");
	}

	public MysteryBox(MysteryBoxManager manager, String configName) {
		this(manager, new File(MysteryBoxManager.chestPath, configName + ".yml"), configName);
	}

	public MysteryBox(MysteryBoxManager manager, File configFile, String configName) {
		this(manager, new kConfig(configFile), configName);
	}

	public MysteryBox(MysteryBoxManager manager, kConfig config, String configName) {
		super(manager.getInstance(), "MysteryBox|" + configName);
		this.manager=manager;
		this.config = config;
		this.name = configName;
		this.item = config.getItemStack("MysteryBox.item");
		this.Shards = config.getInt("MysteryBox.shards");
		this.canOpen = config.getInt("MysteryBox.canOpen");
		loadTemplate(config.getString("MysteryBox.template"));
		loadItems();
		logMessage("load Chest " + getName());
	}

	public void start(Player player) {
		Bukkit.getPluginManager().callEvent(new PlayerUseMysteryBoxEvent(player, this));
		UtilPlayer.setMove(player, false);
		this.sessions.put(player, new MysteryBoxSession(player, building, randomItems()));
	}

	public void loadTemplate(String template) {
		building = new Building(new File(MysteryBoxManager.templatePath, template + ".dat"));
	}

	public void setData(ItemStack item, String name, String template) {
		this.config = new kConfig(new File(MysteryBoxManager.chestPath, name + ".yml"));
		this.config.setItemStack("MysteryBox.item", item);
		this.config.set("MysteryBox.template", template);
		this.config.set("MysteryBox.canOpen", 2);
		this.config.set("MysteryBox.shards", 25);
		this.config.save();
		this.name = name;
		this.item = item;
		this.Shards=2000;

		loadTemplate(template);
		loadItems();
	}

	public MysteryItem[] randomItems() {
		ArrayList<MysteryItem> list = new ArrayList<>();
		ArrayList<MysteryItem> all_items = new ArrayList<>(items);
		double itotal = total;
		for (int i = 0; i < canOpen; i++) {
			double add_chance = 0;
			double chance = UtilMath.RandomDouble(0, itotal);
			for (MysteryItem item : all_items) {
				if (add_chance <= chance && (add_chance+item.getChance()) >= chance) {
					list.add(item);
					all_items.remove(item);
					itotal-=item.getChance();
					break;
				}
				add_chance += item.getChance();
			}
		}
		return list.toArray(new MysteryItem[] {});
	}

	public void loadItems() {
		if (this.config.contains("MysteryBox.items")) {
			this.counter = 0;
			for (String s : this.config.getPathList("MysteryBox.items").keySet()) {
				if (UtilNumber.toInt(s) > counter) {
					counter = UtilNumber.toInt(s);
				}

				total += this.config.getDouble("MysteryBox.items." + s + ".chance");
				
				items.add(new MysteryItem(this.config.getItemStack("MysteryBox.items." + s + ".item"),
						this.config.getInt("MysteryBox.items." + s + ".shards"),
						this.config.getDouble("MysteryBox.items." + s + ".chance"),
						this.config.getString("MysteryBox.items." + s + ".Permission"),
						GroupTyp.values()[this.config.getInt("MysteryBox.items." + s + ".GroupTyp")],
						this.config.getString("MysteryBox.items." + s + ".Command")));
			}
			Collections.shuffle(items);

			logMessage("Total Chance: " + total);
		}
	}

	public boolean removeItem(int id) {
		this.config.set("MysteryBox.items." + id, null);
		return true;
	}

	public int addItem(ItemStack item,int Shards, double chance, String permission, GroupTyp typ, String CMD) {
		this.items.add(new MysteryItem(item,Shards, chance, CMD, typ, permission));
		this.counter++;
		this.total+=chance;
		this.config.setItemStack("MysteryBox.items." + counter + ".item", item);
		this.config.set("MysteryBox.items." + counter + ".shards", Shards);
		this.config.set("MysteryBox.items." + counter + ".chance", chance);
		this.config.set("MysteryBox.items." + counter + ".Command", CMD);
		this.config.set("MysteryBox.items." + counter + ".Permission", permission);
		this.config.set("MysteryBox.items." + counter + ".GroupTyp", typ.ordinal());
		this.config.save();
		return counter;
	}
	
	@EventHandler
	public void move(UpdateEvent ev){
		if(ev.getType() == UpdateType.TICK){
			if(!sessions.isEmpty()){
				for(Player session : sessions.keySet()){
					if(!session.isOnline())continue;
					if(UtilPlayer.canMove(session))continue;
					
					if(session.getLocation().distance(sessions.get(session).getLocation()) > 1){
						session.teleport(UtilBlock.getBlockCenterUP(sessions.get(session).getLocation()));
					}
					
					for(Player player : UtilServer.getPlayers()){
						if(session.getUniqueId() != player.getUniqueId()){
							if(player.getLocation().distanceSquared(session.getPlayer().getLocation()) < 20){
								UtilPlayer.Knockback(session.getLocation(), player, 0.4, 2);
							}
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void quit(PlayerQuitEvent ev){
		if (sessions.containsKey(ev.getPlayer())) {
			UtilPlayer.setMove(ev.getPlayer(), true);
		}
	}
	
	@EventHandler
	public void interact(PlayerInteractEvent ev) {
		if (sessions.containsKey(ev.getPlayer())) {
			ev.setCancelled(true);
			if (ev.getAction() == Action.RIGHT_CLICK_BLOCK) {
				if (ev.getClickedBlock().getType() == Material.CHEST) {
					sessions.get(ev.getPlayer()).drop(ev.getClickedBlock());
				}
			}
		}
	}

	ArrayList<MysteryBoxSession> remove;
	@EventHandler
	public void updater(UpdateEvent ev) {
		if (ev.getType() == UpdateType.TICK && !sessions.isEmpty()) {
			if (remove == null)
				remove = new ArrayList<>();
			for (MysteryBoxSession session : sessions.values()) {
				if (!session.next()) {
					remove.add(session);
				}
			}

			if (!remove.isEmpty()) {
				for (MysteryBoxSession session : remove) {
					sessions.remove(session.getPlayer());
					session.remove();
				}
				remove.clear();
			}
		}
	}
}
