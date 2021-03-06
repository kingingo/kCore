package eu.epicpvp.kcore.DeliveryPet;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import eu.epicpvp.datenserver.definitions.dataserver.gamestats.ServerType;
import eu.epicpvp.kcore.Hologram.Hologram;
import eu.epicpvp.kcore.Hologram.nametags.NameTagMessage;
import eu.epicpvp.kcore.Inventory.InventoryBase;
import eu.epicpvp.kcore.Inventory.Inventory.DeliveryInventoryPage;
import eu.epicpvp.kcore.Inventory.Inventory.InventoryLotto2;
import eu.epicpvp.kcore.Inventory.Inventory.InventoryLotto2.InventoryLotto2Type;
import eu.epicpvp.kcore.Inventory.Item.Click;
import eu.epicpvp.kcore.Inventory.Item.Get;
import eu.epicpvp.kcore.Inventory.Item.Buttons.ButtonBase;
import eu.epicpvp.kcore.Inventory.Item.Buttons.LottoPackage;
import eu.epicpvp.kcore.Listener.kListener;
import eu.epicpvp.kcore.MySQL.MySQL;
import eu.epicpvp.kcore.MySQL.MySQLErr;
import eu.epicpvp.kcore.MySQL.Events.MySQLErrorEvent;
import eu.epicpvp.kcore.Translation.TranslationHandler;
import eu.epicpvp.kcore.Update.UpdateType;
import eu.epicpvp.kcore.Update.Event.UpdateEvent;
import eu.epicpvp.kcore.Util.InventorySize;
import eu.epicpvp.kcore.Util.UtilDebug;
import eu.epicpvp.kcore.Util.UtilEffect;
import eu.epicpvp.kcore.Util.UtilEnt;
import eu.epicpvp.kcore.Util.UtilEvent.ActionType;
import eu.epicpvp.kcore.Util.UtilList;
import eu.epicpvp.kcore.Util.UtilMath;
import eu.epicpvp.kcore.Util.UtilParticle;
import eu.epicpvp.kcore.Util.UtilPlayer;
import eu.epicpvp.kcore.Util.UtilServer;
import eu.epicpvp.kcore.Util.UtilTime;
import eu.epicpvp.thread.ThreadFactory;
import lombok.Getter;
import lombok.Setter;

public class DeliveryPet extends kListener {

	@Getter
	private InventoryBase base;
	@Getter
	private InventoryLotto2 lotto;
	@Getter
	private Creature entity;
	@Getter
	private Creature jockey;
	@Getter
	private HashMap<String, DeliveryObject> objects;
	private HashMap<Player, DeliveryInventoryPage> players;
	private HashMap<Player, NameTagMessage> players_hm;
	private HashMap<Player, Integer> players_hm_reward;
	private HashMap<Integer, HashMap<String, Long>> players_obj;
	private ServerType serverType;
	@Getter
	private HashMap<InventoryLotto2Type, ArrayList<LottoPackage>> packages;
	@Getter
	private MySQL mysql;
	@Getter
	private Hologram hologramm;
	@Getter
	@Setter
	private Location location;
	@Getter
	private String name;
	@Getter
	private EntityType type;

	public DeliveryPet(InventoryBase base, HashMap<InventoryLotto2Type, ArrayList<LottoPackage>> pack, DeliveryObject[] objects, String name, EntityType type, Location location, ServerType serverType, Hologram hm, MySQL mysql) {
		super(mysql.getInstance(), "DeliveryPet");
		UtilServer.setDeliveryPet(this);
		this.packages = pack;
		this.mysql = mysql;
		this.type = type;
		this.location = location;
		this.name = name;
		getMysql().Update("CREATE TABLE IF NOT EXISTS delivery_" + serverType.name() + "(playerId int, obj varchar(30),timestamp timestamp)");
		this.serverType = serverType;
		this.hologramm = hm;
		this.objects = new HashMap<>();
		for (DeliveryObject obj : objects)
			this.objects.put(obj.displayname, obj);
		this.players_obj = new HashMap<>();
		this.players = new HashMap<>();
		this.players_hm = new HashMap<>();
		this.players_hm_reward = new HashMap<>();
		this.base = base;
		if (pack != null)
			this.lotto = new InventoryLotto2("Play a Round!", new Get() {
				@Override
				public Object onGet(Player player) {
					LottoPackage[] ps = new LottoPackage[18];

					int common = 8;
					int uncommon = 5;
					int rare = 3;
					int legendary = 1;
					int divine = 0;

					if (UtilMath.randomInteger(100000) == 5356) {
						divine++;
					} else {
						legendary++;
					}

					for (int i = 0; i < ps.length; i++) {
						if (common != 0) {
							ps[i] = packages.get(InventoryLotto2Type.COMMON).get(UtilMath.randomInteger(packages.get(InventoryLotto2Type.COMMON).size()));

							if (ps[i].hasPlayer(player)) {
								ps[i] = null;
								i--;
							} else {
								common--;
							}
						} else if (uncommon != 0) {
							ps[i] = packages.get(InventoryLotto2Type.UNCOMMON).get(UtilMath.randomInteger(packages.get(InventoryLotto2Type.UNCOMMON).size()));

							if (ps[i].hasPlayer(player)) {
								ps[i] = null;
								i--;
							} else {
								uncommon--;
							}
						} else if (rare != 0) {
							ps[i] = packages.get(InventoryLotto2Type.RARE).get(UtilMath.randomInteger(packages.get(InventoryLotto2Type.RARE).size()));

							if (ps[i].hasPlayer(player)) {
								ps[i] = null;
								i--;
							} else {
								rare--;
							}
						} else if (legendary != 0) {
							ps[i] = packages.get(InventoryLotto2Type.LEGENDARY).get(UtilMath.randomInteger(packages.get(InventoryLotto2Type.LEGENDARY).size()));

							if (ps[i].hasPlayer(player)) {
								ps[i] = null;
								i--;
							} else {
								legendary--;
							}
						} else if (divine != 0) {
							ps[i] = packages.get(InventoryLotto2Type.DIVINE).get(UtilMath.randomInteger(packages.get(InventoryLotto2Type.DIVINE).size()));

							if (ps[i].hasPlayer(player)) {
								ps[i] = null;
								i--;
							} else {
								divine--;
							}
						}
					}

					LottoPackage[] ps1 = new LottoPackage[ps.length];
					int r;
					for (int i = 0; i < ps.length; i++) {
						r = UtilMath.randomInteger(ps.length);
						if (ps1[r] != null) {
							i--;
							continue;
						}
						ps1[r] = ps[i];
					}
					return ps1;
				}

			}, 4, 7, UtilServer.getPluginInstance());
		if (pack != null)
			this.base.addPage(lotto);
		createPet();
		UtilServer.getDeliveryPet(this);
	}

	public void teleportPet(Location location) {
		if (this.jockey != null) {
			setLocation(location.clone());
			if (this.jockey.getPassenger() != null) {
				this.jockey.eject();
				this.jockey.teleport(getLocation());
				this.entity.teleport(getLocation());
				this.jockey.setPassenger(this.entity);
				UtilEnt.setNoAI(entity, true);
				UtilEnt.setSilent(entity, true);
			} else {
				this.jockey.teleport(getLocation());
			}

			UtilEnt.setNoAI(jockey, true);
			UtilEnt.setSilent(jockey, true);
			for (Player player : this.players_hm.keySet()) {
				this.players_hm.get(player).move(player, this.jockey.getLocation().add(0, 2.1, 0));
			}
		}
	}

	public void createPet() {
		if (this.jockey == null || this.jockey.isDead()) {
			this.jockey = (Creature) getLocation().getWorld().spawnCreature(getLocation(), getType());
			this.jockey.setCustomName("");
			this.jockey.setCanPickupItems(false);
			this.jockey.setCustomNameVisible(true);
			this.jockey.setRemoveWhenFarAway(false);
			UtilEnt.setNoAI(jockey, true);
			UtilEnt.setSilent(jockey, true);

			if (jockey.getType() == EntityType.ENDERMAN) {
				Enderman e = (Enderman) this.jockey;
				e.setCanPickupItems(false);
				e.setRemoveWhenFarAway(false);
				e.setTarget(null);
			} else if (jockey.getType() == EntityType.CHICKEN) {
				UtilEnt.setChickenDropEgg(((Chicken) this.jockey), true);

				this.entity = (Creature) getLocation().getWorld().spawnCreature(getLocation(), EntityType.ZOMBIE);
				UtilEnt.setSilent(entity, true);
				this.entity.setCustomName("");
				this.entity.setCustomNameVisible(true);
				this.entity.setRemoveWhenFarAway(false);
				this.entity.setCanPickupItems(false);
				Zombie zombie = (Zombie) entity;
				zombie.setBaby(true);
				zombie.setVillager(false);
				UtilEnt.setNoAI(entity, true);
				this.jockey.setPassenger(this.entity);
			}

			for (Player player : this.players_hm.keySet()) {
				this.players_hm.get(player).move(player, this.jockey.getLocation().add(0, 2.1, 0));
			}
		}
	}

	public void onDisable() {
		this.jockey.remove();
		if (this.entity != null)
			this.entity.remove();
	}

	@EventHandler
	public void join(PlayerJoinEvent ev) {
		if (players_hm.containsKey(ev.getPlayer())) {
			players_hm.get(ev.getPlayer()).sendToPlayer(ev.getPlayer());
		} else {
			players_hm_reward.put(ev.getPlayer(), getRewards(ev.getPlayer()));
			players_hm.put(ev.getPlayer(), getHologramm().setCreatureName(ev.getPlayer(), (this.entity != null ? this.entity : this.jockey), new String[]
			{ TranslationHandler.getText(ev.getPlayer(), (players_hm_reward.get(ev.getPlayer()) > 1 ? "DELIVERY_HM_1_MORE" : "DELIVERY_HM_1"), "§f§l" + players_hm_reward.get(ev.getPlayer())), name, TranslationHandler.getText(ev.getPlayer(), "DELIVERY_HM_3") }));
		}
	}

	public void playEffect() {
		UtilEffect.playHelix(getLocation(), UtilParticle.FIREWORKS_SPARK);
	}

	public int getRewards(Player player) {
		if (players_obj.containsKey(UtilPlayer.getPlayerId(player))) {
			int i = 0;

			for (String obj : players_obj.get(UtilPlayer.getPlayerId(player)).keySet()) {
				if (!(players_obj.get(UtilPlayer.getPlayerId(player)).get(obj) > System.currentTimeMillis())) {
					i++;
				}
			}

			return i;
		}
		return 0;
	}

	@EventHandler
	public void quit(PlayerQuitEvent ev) {
		if (players.containsKey(ev.getPlayer())) {
			players.get(ev.getPlayer()).clear();
			players.get(ev.getPlayer()).remove();
			players.remove(ev.getPlayer());
		}
		if (players_hm.containsKey(ev.getPlayer())) {
			players_hm.get(ev.getPlayer()).remove();
			players_hm.remove(ev.getPlayer());
			players_hm_reward.remove(ev.getPlayer());
		}
		if (UtilServer.getClient().getHandle().isConnected() || UtilPlayer.getPlayerId(ev.getPlayer()) != -1) {
			if (players_obj.containsKey(UtilPlayer.getPlayerId(ev.getPlayer()))) {
				for (int i = 0; i < players_obj.get(UtilPlayer.getPlayerId(ev.getPlayer())).size(); i++)
					players_obj.get(UtilPlayer.getPlayerId(ev.getPlayer())).remove(i);
				players_obj.remove(UtilPlayer.getPlayerId(ev.getPlayer()));
			}
		} else
			ThreadFactory.getFactory().createThread(() -> {
				System.out.println("Loopback deleivery pat!");
				while (!UtilServer.getClient().getHandle().isConnected()) {
					try {
						Thread.sleep(500);
					} catch (Exception e) {
					}
				}
				if (players_obj.containsKey(UtilPlayer.getPlayerId(ev.getPlayer()))) {
					for (int i = 0; i < players_obj.get(UtilPlayer.getPlayerId(ev.getPlayer())).size(); i++)
						players_obj.get(UtilPlayer.getPlayerId(ev.getPlayer())).remove(i);
					players_obj.remove(UtilPlayer.getPlayerId(ev.getPlayer()));
				}
			}).start();
		;
	}

	@EventHandler
	public void teleport(EntityTeleportEvent ev) {
		if (ev.getEntity().getEntityId() == this.jockey.getEntityId()) {
			ev.setCancelled(true);
		}
		if (this.entity != null && ev.getEntity().getEntityId() == this.entity.getEntityId()) {
			ev.setCancelled(true);
		}
	}

	@EventHandler
	public void flame(EntityCombustEvent ev) {
		if (ev.getEntity().getEntityId() == this.jockey.getEntityId())
			ev.setCancelled(true);
		if (this.entity != null && ev.getEntity().getEntityId() == this.entity.getEntityId())
			ev.setCancelled(true);
	}

	@EventHandler
	public void Damage(EntityDamageByEntityEvent ev) {
		if (ev.getEntity().getEntityId() == this.jockey.getEntityId())
			ev.setCancelled(true);
		if (ev.getDamager().getEntityId() == this.jockey.getEntityId())
			ev.setCancelled(true);
		if (this.entity != null && ev.getEntity().getEntityId() == this.entity.getEntityId())
			ev.setCancelled(true);
		if (this.entity != null && ev.getDamager().getEntityId() == this.entity.getEntityId())
			ev.setCancelled(true);
	}

	@EventHandler
	public void Damage(EntityDamageEvent ev) {
		if (ev.getEntity().getEntityId() == this.jockey.getEntityId())
			ev.setCancelled(true);
		if (this.entity != null && ev.getEntity().getEntityId() == this.entity.getEntityId())
			ev.setCancelled(true);
	}

	@EventHandler
	public void target(EntityTargetEvent ev) {
		if (ev.getEntity().getEntityId() == this.jockey.getEntityId())
			ev.setCancelled(true);
		if (this.entity != null && ev.getEntity().getEntityId() == this.entity.getEntityId())
			ev.setCancelled(true);
	}

	@EventHandler
	public void HologrammUpdater(UpdateEvent ev) {
		if (ev.getType() == UpdateType.SEC) {
			for (Player player : players_hm.keySet()) {
				try {
					if (player.getLocation().getWorld() == getLocation().getWorld() && player.getLocation().distance(getLocation()) < 25 && players_hm_reward.containsKey(player)) {
						if (players_hm_reward.get(player) == 0) {
							if (!players_hm.get(player).getLines()[0].startsWith("§7")) {
								players_hm.get(player).setLines(0, TranslationHandler.getText(player, (players_hm_reward.get(player) > 1 ? "DELIVERY_HM_1_MORE" : "DELIVERY_HM_1"), "§7" + players_hm_reward.get(player)));
								players_hm.get(player).clear(player);
								players_hm.get(player).sendToPlayer(player);
							}
						} else {
							if (players_hm.get(player).getLines()[0].startsWith("§f§l") || players_hm.get(player).getLines()[0].startsWith("§7")) {
								players_hm.get(player).setLines(0, TranslationHandler.getText(player, (players_hm_reward.get(player) > 1 ? "DELIVERY_HM_1_MORE" : "DELIVERY_HM_1"), "§c§l" + players_hm_reward.get(player)));
							} else {
								players_hm.get(player).setLines(0, TranslationHandler.getText(player, (players_hm_reward.get(player) > 1 ? "DELIVERY_HM_1_MORE" : "DELIVERY_HM_1"), "§f§l" + players_hm_reward.get(player)));
							}
							players_hm.get(player).clear(player);
							players_hm.get(player).sendToPlayer(player);
						}
					}
				} catch (ConcurrentModificationException e) {
				}
			}
		}
	}

	@EventHandler
	public void InventoryUpdate(UpdateEvent ev) {
		if (ev.getType() == UpdateType.SEC) {
			for (Player player : players.keySet()) {
				if (player.isOnline() && !players.get(player).getViewers().isEmpty()) {
					if (players_obj.containsKey(UtilPlayer.getPlayerId(player))) {
						for (String obj : players_obj.get(UtilPlayer.getPlayerId(player)).keySet()) {
							if (!objects.containsKey(obj))
								continue;
							if (players_obj.get(UtilPlayer.getPlayerId(player)).get(obj) > System.currentTimeMillis()) {
								players.get(player).getButtonOneSlot(objects.get(obj).slot).setDescription(descriptionUSED(player, obj));
								if (players.get(player).getButtonOneSlot(objects.get(obj).slot).getItemStack().getType() != objects.get(obj).delay_material) {
									players.get(player).getButtonOneSlot(objects.get(obj).slot).setMaterial(objects.get(obj).delay_material, objects.get(obj).delay_data);
								}

								players.get(player).getButtonOneSlot(objects.get(obj).slot).refreshItemStack();
							} else {
								if (players.get(player).getButtonOneSlot(objects.get(obj).slot).getItemStack().getType() == objects.get(obj).delay_material && players.get(player).getButtonOneSlot(objects.get(obj).slot).getItemStack().getData().getData() == objects.get(obj).delay_data) {
									players.get(player).getButtonOneSlot(objects.get(obj).slot).setDescription(objects.get(obj).description);
									players.get(player).getButtonOneSlot(objects.get(obj).slot).setMaterial(objects.get(obj).material, objects.get(obj).data);
									players.get(player).getButtonOneSlot(objects.get(obj).slot).refreshItemStack();
								}
							}
						}
					} else {
						logMessage("UpdaterEvent Spieler " + player.getName() + " nicht gefunden...");
					}
				}
			}
		}
	}

	@EventHandler
	public void Clean(UpdateEvent ev) {
		if (ev.getType() == UpdateType.MIN_16) {
			UtilList.CleanList(players, base);
			UtilList.CleanList(players_obj);
			UtilList.CleanList(players_hm);
			UtilList.CleanList(players_hm_reward);
			createPet();
		}
	}

	public void deliveryBlock(Player player, String name) {
		if (objects.containsKey(name)) {
			if (players.containsKey(player) && players.get(player) != null)
				players.get(player).getButtonOneSlot(objects.get(name).slot).setMaterial(objects.get(name).delay_material, objects.get(name).delay_data);
			if (players.containsKey(player) && players.get(player) != null)
				players.get(player).getButtonOneSlot(objects.get(name).slot).refreshItemStack();
			getMysql().Update("UPDATE delivery_" + serverType.name() + " SET `timestamp`='" + UtilTime.when((System.currentTimeMillis() + objects.get(name).getTime())) + "' WHERE `playerId`='" + UtilPlayer.getPlayerId(player) + "' AND `obj`='" + name + "'");
			players_obj.get(UtilPlayer.getPlayerId(player)).remove(objects.get(name).displayname);
			players_obj.get(UtilPlayer.getPlayerId(player)).put(objects.get(name).displayname, System.currentTimeMillis() + objects.get(name).getTime());
			players_hm_reward.remove(player);
			players_hm_reward.put(player, getRewards(player));
			playEffect();
			player.closeInventory();
		}
	}

	public void deliveryUSE(int playerId, String dis) {
		if (objects.containsKey(name)) {
			if (objects.get(dis).displayname.equalsIgnoreCase(dis)) {
				getMysql().Update("UPDATE delivery_" + serverType.name() + " SET `timestamp`='" + UtilTime.when((System.currentTimeMillis() + objects.get(dis).getTime())) + "' WHERE `playerId`='" + playerId + "' AND `obj`='" + dis + "'");
			}
		}
	}

	public void deliveryUSE(Player player, String dis, boolean b) {
		if (objects.containsKey(dis)) {
			if (objects.get(dis).displayname.equalsIgnoreCase(dis)) {
				if (objects.get(dis).permission != null) {
					if (!player.hasPermission(objects.get(dis).permission.getPermissionToString())) {
						return;
					}
				}

				if (b || objects.get(dis).byClickBlock) {
					deliveryBlock(player, dis);
				}

				objects.get(dis).click.onClick(player, ActionType.RIGHT, objects.get(dis));
			}
		} else {
			UtilDebug.debug("deliveryUSE(Player,String,boolean)", new String[]
			{ "objects.containsKey(name) == FALSE", "Displayname: " + dis });
		}
	}

	public String[] descriptionUSED(Player player, String obj) {
		return new String[]
		{ TranslationHandler.getText(player, "DELIVERY_USED", UtilTime.formatMili(players_obj.get(UtilPlayer.getPlayerId(player)).get(obj) - System.currentTimeMillis())) };
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void Login(AsyncPlayerPreLoginEvent ev) {
		if (!players_obj.containsKey(UtilPlayer.getPlayerId(ev.getName())))
			players_obj.put(UtilPlayer.getPlayerId(ev.getName()), new HashMap<String, Long>());

		try {
			ResultSet rs = getMysql().Query("SELECT `obj`, `timestamp` FROM `delivery_" + serverType.name() + "` WHERE playerId='" + UtilPlayer.getPlayerId(ev.getName()) + "';");

			while (rs.next()) {
				players_obj.get(UtilPlayer.getPlayerId(ev.getName())).put(rs.getString(1), rs.getTimestamp(2).getTime());
			}
			rs.close();
		} catch (Exception err) {
			Bukkit.getPluginManager().callEvent(new MySQLErrorEvent(MySQLErr.QUERY, err, getMysql()));
		}

		if (players_obj.get(UtilPlayer.getPlayerId(ev.getName())).isEmpty()) {
			for (DeliveryObject obj : objects.values()) {
				getMysql().Update("INSERT INTO `delivery_" + serverType.name() + "` (`playerId`,`obj`) VALUES ('" + UtilPlayer.getPlayerId(ev.getName()) + "','" + obj.displayname + "');");
				players_obj.get(UtilPlayer.getPlayerId(ev.getName())).put(obj.displayname, System.currentTimeMillis());
			}
		} else {
			for (String obj : objects.keySet()) {
				if (!players_obj.get(UtilPlayer.getPlayerId(ev.getName())).containsKey(obj)) {
					getMysql().Update("INSERT INTO `delivery_" + serverType.name() + "` (`playerId`,`obj`) VALUES ('" + UtilPlayer.getPlayerId(ev.getName()) + "','" + objects.get(obj).displayname + "');");
					players_obj.get(UtilPlayer.getPlayerId(ev.getName())).put(objects.get(obj).displayname, System.currentTimeMillis());
				}
			}
		}
	}

	@EventHandler
	public void Open(PlayerInteractEntityEvent ev) {
		if (ev.getRightClicked().getEntityId() == jockey.getEntityId() || (this.entity != null && ev.getRightClicked().getEntityId() == entity.getEntityId())) {
			ev.setCancelled(true);
			if (!players.containsKey(ev.getPlayer())) {
				players.put(ev.getPlayer(), new DeliveryInventoryPage(InventorySize._45.getSize(), ev.getPlayer().getName() + " " + "Delivery", this));
				base.addPage(players.get(ev.getPlayer()));

				if (this.packages != null) {
					players.get(ev.getPlayer()).addButton(31, new ButtonBase(new Click() {

						@Override
						public void onClick(Player player, ActionType type, Object object) {
							if (lotto.getWin() == null) {
								lotto.newRound(player);
							} else {
								player.sendMessage(TranslationHandler.getText(player, "PREFIX") + TranslationHandler.getText(player, "DELIVERY_LOTTO_USED"));
							}
						}

					}, Material.JUKEBOX, "§7Lotto"));
				}

				if (!players_obj.containsKey(UtilPlayer.getPlayerId(ev.getPlayer()))) {
					logMessage("players_obj Spieler " + ev.getPlayer() + " nicht gefunden!");
				} else if (players_obj.get(UtilPlayer.getPlayerId(ev.getPlayer())).isEmpty()) {
					logMessage("players_obj Spieler " + ev.getPlayer() + " liste ist leer!");
				} else {
					for (String obj : players_obj.get(UtilPlayer.getPlayerId(ev.getPlayer())).keySet()) {
						UtilDebug.debug(new String[]
						{ "O:" + obj, "C:" + objects.containsKey(obj) });
						if (!objects.containsKey(obj))
							continue;
						if (players_obj.get(UtilPlayer.getPlayerId(ev.getPlayer())).get(obj) > System.currentTimeMillis()) {
							players.get(ev.getPlayer()).addButton(objects.get(obj).slot, new ButtonBase(objects.get(obj).click, objects.get(obj).delay_material, objects.get(obj).delay_data, objects.get(obj).displayname, descriptionUSED(ev.getPlayer(), obj)));
						} else {
							players.get(ev.getPlayer()).addButton(objects.get(obj).slot, new ButtonBase(objects.get(obj).click, objects.get(obj).material, objects.get(obj).data, objects.get(obj).displayname, objects.get(obj).description));
						}
					}
				}

				players.get(ev.getPlayer()).fill(Material.STAINED_GLASS_PANE, 7);
			}
			ev.getPlayer().openInventory(players.get(ev.getPlayer()));
		}
	}
}
