package eu.epicpvp.kcore.enchantment;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

@RequiredArgsConstructor
public class AnvilInventoryHolder implements InventoryHolder {

	@Getter
	@Setter
	private Inventory inventory;
}
