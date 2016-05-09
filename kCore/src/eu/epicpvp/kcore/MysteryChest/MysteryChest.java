package eu.epicpvp.kcore.MysteryChest;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import eu.epicpvp.kcore.Listener.kListener;
import eu.epicpvp.kcore.MysteryChest.Events.PlayerUseMysteryChestEvent;
import eu.epicpvp.kcore.MysteryChest.Items.MysteryItem;
import eu.epicpvp.kcore.MysteryChest.Templates.Building;
import eu.epicpvp.kcore.Permission.Group.GroupTyp;
import eu.epicpvp.kcore.Update.UpdateType;
import eu.epicpvp.kcore.Update.Event.UpdateEvent;
import eu.epicpvp.kcore.Util.UtilBlock;
import eu.epicpvp.kcore.Util.UtilInv;
import eu.epicpvp.kcore.Util.UtilItem;
import eu.epicpvp.kcore.Util.UtilLocation;
import eu.epicpvp.kcore.Util.UtilMath;
import eu.epicpvp.kcore.Util.UtilNumber;
import eu.epicpvp.kcore.Util.UtilPlayer;
import eu.epicpvp.kcore.Util.UtilServer;
import eu.epicpvp.kcore.kConfig.kConfig;
import lombok.Getter;

public class MysteryChest extends kListener {

	public static MysteryChest createChest(MysteryChestManager chestManager, String template, ItemStack item,
			String name) {
		MysteryChest chest = new MysteryChest(chestManager);
		chest.setData(item, name, template);
		return chest;
	}

	@Getter
	private ItemStack item;
	@Getter
	private String name;
	@Getter
	private kConfig config;
	private ArrayList<MysteryItem> items = new ArrayList<>();
	private HashMap<Player, MysteryChestSession> sessions = new HashMap<>();
	private Building building;
	private int counter = 0;
	private int canOpen = 2;
	@Getter
	private int gems = 0;
	private double total;
	@Getter
	private MysteryChestManager chestManager;

	public MysteryChest(MysteryChestManager chestManager) {
		super(chestManager.getInstance(), "MysteryChest");
	}

	public MysteryChest(MysteryChestManager chestManager, String configName) {
		this(chestManager, new File(MysteryChestManager.chestPath, configName + ".yml"), configName);
	}

	public MysteryChest(MysteryChestManager chestManager, File configFile, String configName) {
		this(chestManager, new kConfig(configFile), configName);
	}

	public MysteryChest(MysteryChestManager chestManager, kConfig config, String configName) {
		super(chestManager.getInstance(), "MysteryChest|" + configName);
		this.chestManager=chestManager;
		this.config = config;
		this.name = configName;
		this.item = config.getItemStack("MysteryChest.item");
		this.gems = config.getInt("MysteryChest.Gems");
		this.canOpen = config.getInt("MysteryChest.canOpen");
		loadTemplate(config.getString("MysteryChest.template"));
		loadItems();
		logMessage("load Chest " + getName());
	}

	public void start(Player player) {
		Bukkit.getPluginManager().callEvent(new PlayerUseMysteryChestEvent(player, this));
		UtilPlayer.setMove(player, false);
		this.sessions.put(player, new MysteryChestSession(player, building, randomItems(player)));
	}

	public void loadTemplate(String template) {
		building = new Building(new File(MysteryChestManager.templatePath, template + ".dat"));
	}

	public void setData(ItemStack item, String name, String template) {
		this.config = new kConfig(new File(MysteryChestManager.chestPath, name + ".yml"));
		this.config.setItemStack("MysteryChest.item", item);
		this.config.set("MysteryChest.template", template);
		this.config.set("MysteryChest.canOpen", 2);
		this.config.set("MysteryChest.Gems", 2000);
		this.config.save();
		this.name = name;
		this.item = item;
		this.gems=2000;

		loadTemplate(template);
		loadItems();
	}

	public MysteryItem[] randomItems(Player player) {
		ArrayList<MysteryItem> list = new ArrayList<>();
		for (int i = 0; i < canOpen; i++) {
			double last_chance = 0;
			double chance = UtilMath.RandomDouble(0, total);

			for (MysteryItem item : items) {
				if (item.getChance() <= chance && (last_chance + item.getChance()) >= chance) {
					list.add(item);
					break;
				}
				last_chance += item.getChance();
			}
		}
		return list.toArray(new MysteryItem[] {});
	}

	public void loadItems() {
		if (this.config.contains("MysteryChest.items")) {
			this.counter = 0;
			for (String s : this.config.getPathList("MysteryChest.items").keySet()) {
				if (UtilNumber.toInt(s) > counter) {
					counter = UtilNumber.toInt(s);
				}

				total += this.config.getDouble("MysteryChest.items." + s + ".chance");
				items.add(new MysteryItem(this.config.getItemStack("MysteryChest.items." + s + ".item"),
						this.config.getDouble("MysteryChest.items." + s + ".chance"),
						this.config.getString("MysteryChest.items." + s + ".Permission"),
						GroupTyp.values()[this.config.getInt("MysteryChest.items." + s + ".GroupTyp")],
						this.config.getString("MysteryChest.items." + s + ".Command")));
			}

			logMessage("Total Chance: " + total);
		}
	}

	public boolean removeItem(int id) {
		this.config.set("MysteryChest.items." + id, null);
		return true;
	}

	public int addItem(ItemStack item, double chance, String permission, GroupTyp typ, String CMD) {
		this.items.add(new MysteryItem(item, chance, CMD, typ, permission));
		this.counter++;
		this.total+=chance;
		this.config.setItemStack("MysteryChest.items." + counter + ".item", item);
		this.config.set("MysteryChest.items." + counter + ".chance", chance);
		this.config.set("MysteryChest.items." + counter + ".Command", CMD);
		this.config.set("MysteryChest.items." + counter + ".Permission", permission);
		this.config.set("MysteryChest.items." + counter + ".GroupTyp", typ.ordinal());
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
		}else if (ev.getPlayer() != null && ev.getPlayer().getItemInHand() != null) {
			if (ev.getPlayer().getItemInHand().hasItemMeta() 
					&& ev.getPlayer().getItemInHand().getItemMeta().hasDisplayName() 
					&& ev.getPlayer().getItemInHand().getItemMeta().getDisplayName().startsWith("§c"+getName())
					&& !chestManager.isBlocked(ev.getPlayer().getLocation())) {
				int amount = chestManager.getAmount(ev.getPlayer(), getName());
				amount--;
				chestManager.setAmount(ev.getPlayer(), amount, getName());
				ev.getPlayer().setItemInHand(UtilItem.RenameItem(getItem(), "§c"+getName()+" §e(§7"+amount+"§e)"));
				start(ev.getPlayer());
			}
		}
	}

	ArrayList<MysteryChestSession> remove;
	@EventHandler
	public void updater(UpdateEvent ev) {
		if (ev.getType() == UpdateType.TICK && !sessions.isEmpty()) {
			if (remove == null)
				remove = new ArrayList<>();
			for (MysteryChestSession session : sessions.values()) {
				if (!session.next()) {
					remove.add(session);
				}
			}

			if (!remove.isEmpty()) {
				for (MysteryChestSession session : remove) {
					sessions.remove(session.getPlayer());
					session.remove();
				}
				remove.clear();
			}
		}
	}
}
