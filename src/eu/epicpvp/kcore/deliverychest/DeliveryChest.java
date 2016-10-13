package eu.epicpvp.kcore.deliverychest;

import javax.annotation.Nullable;

import eu.epicpvp.datenclient.client.LoadedPlayer;
import eu.epicpvp.kcore.UserDataConfig.UserDataConfig;
import eu.epicpvp.kcore.Util.UtilServer;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;

public class DeliveryChest {

	@Getter
	@NonNull
	private final JavaPlugin plugin;
	@Getter
	@NonNull
	private final UserDataConfig userDataConfig;
	@Getter(AccessLevel.PACKAGE)
	@Nullable
	private ItemModifier itemModifier;
	@Getter
	private BlockVector deliveryChestLocation;
	@Getter(AccessLevel.PACKAGE)
	private DeliveryChestHandler deliveryChestHandler;
	@Getter
	private CommandSetDelivery setDeliveryCommand = new CommandSetDelivery(this);
	@Getter
	private CommandGiveShopItem giveShopItemCommand = new CommandGiveShopItem(this);

	/**
	 * Reads the location out of the plugin config.
	 */
	public DeliveryChest(@NonNull JavaPlugin plugin, @NonNull UserDataConfig userDataConfig, boolean registerCommands) {
		this(plugin, userDataConfig, null, registerCommands);
	}

	/**
	 * Reads the location out of the plugin config.
	 */
	public DeliveryChest(@NonNull JavaPlugin plugin, @NonNull UserDataConfig userDataConfig, @Nullable ItemModifier itemModifier, boolean registerCommands) {
		this(plugin, userDataConfig, itemModifier, null, registerCommands);
	}

	public DeliveryChest(@NonNull JavaPlugin plugin, @NonNull UserDataConfig userDataConfig, @Nullable ItemModifier itemModifier, @Nullable BlockVector deliveryChestLocation, boolean registerCommands) {
		this.plugin = plugin;
		this.userDataConfig = userDataConfig;
		this.itemModifier = itemModifier;
		if (deliveryChestLocation != null) {
			this.deliveryChestLocation = deliveryChestLocation;
		} else {
			this.deliveryChestLocation = plugin.getConfig().getVector("deliveryChest.chestLocation").toBlockVector();
		}
		plugin.getServer().getPluginManager().registerEvents(deliveryChestHandler = new DeliveryChestHandler(this), plugin);
		if (registerCommands) {
			UtilServer.getCommandHandler().register(CommandSetDelivery.class, setDeliveryCommand);
			UtilServer.getCommandHandler().register(CommandGiveShopItem.class, giveShopItemCommand);
		}
	}

	public void setDeliveryChestLocation(@Nullable BlockVector deliveryChestLocation) {
		this.deliveryChestLocation = deliveryChestLocation;
		plugin.getConfig().set("deliveryChest.chestLocation", deliveryChestLocation);
		plugin.saveConfig();
	}

	public void disable() {
		deliveryChestHandler.closeAll();
		HandlerList.unregisterAll(deliveryChestHandler);
	}

	public void reEnable() {
		disable();
		plugin.getServer().getPluginManager().registerEvents(deliveryChestHandler = new DeliveryChestHandler(this), plugin);
	}

	public void reloadConfig() {
		Vector vector = plugin.getConfig().getVector("shop.delivery.chestLocation");
		if (vector != null) {
			deliveryChestLocation = vector.toBlockVector();
		}
	}

	LoadedPlayer getPlayerAndLoad(Player player) {
		return UtilServer.getClient().getPlayerAndLoad(player.getUniqueId());
	}

	LoadedPlayer getPlayerAndLoad(String name) {
		return UtilServer.getClient().getPlayerAndLoad(name);
	}
}
