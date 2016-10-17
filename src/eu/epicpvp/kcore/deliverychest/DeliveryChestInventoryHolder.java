package eu.epicpvp.kcore.deliverychest;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import eu.epicpvp.datenclient.client.LoadedPlayer;
import eu.epicpvp.kcore.kConfig.kConfig;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
public class DeliveryChestInventoryHolder implements InventoryHolder {

	@Getter
	@Setter
	private Inventory inventory;
	@Getter
	@NonNull
	private final LoadedPlayer profile;
	@Getter
	@NonNull
	private final kConfig config;
}
