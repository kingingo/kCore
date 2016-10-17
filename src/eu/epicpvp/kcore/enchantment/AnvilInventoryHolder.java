package eu.epicpvp.kcore.enchantment;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import eu.epicpvp.datenclient.client.LoadedPlayer;
import eu.epicpvp.kcore.kConfig.kConfig;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
public class AnvilInventoryHolder  implements InventoryHolder {

	@Getter
	@Setter
	private Inventory inventory;
}
