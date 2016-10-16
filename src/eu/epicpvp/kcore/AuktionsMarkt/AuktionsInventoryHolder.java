package eu.epicpvp.kcore.AuktionsMarkt;

import eu.epicpvp.datenclient.client.LoadedPlayer;
import eu.epicpvp.kcore.Inventory.InventoryPageBase;
import eu.epicpvp.kcore.kConfig.kConfig;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

@RequiredArgsConstructor
public class AuktionsInventoryHolder implements InventoryHolder {

	@Getter
	@Setter
	private Inventory inventory;
	@Getter
	@Setter
	private int page = 1;
	@Getter
	@NonNull
	private final kConfig config;
}
