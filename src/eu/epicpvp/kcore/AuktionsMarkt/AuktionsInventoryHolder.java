package eu.epicpvp.kcore.AuktionsMarkt;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import eu.epicpvp.kcore.kConfig.kConfig;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

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
