package eu.epicpvp.kcore.enchantment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import eu.epicpvp.kcore.Util.UtilItem;
import eu.epicpvp.kcore.Util.UtilServer;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.ToString;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@Getter
@ToString(of = "name")
@EqualsAndHashCode(of = "name")
public class CustomEnchantment {

	@Getter
	private static final Set<CustomEnchantment> customEnchantments = new HashSet<>();
	private Cache<UUID, Boolean> cooldowns;
	private final String name;
	private final EnchantmentListener listener;
	private long cooldown = 0;

	@SneakyThrows(IllegalStateException.class)
	private static void register(CustomEnchantment customEnchantment) {
		if (customEnchantments.add(customEnchantment)) {
			Bukkit.getPluginManager().registerEvents(customEnchantment.getListener(), UtilServer.getPluginInstance());
		} else {
			throw new IllegalStateException("Double-registering an enchantment");
		}
	}

	public static void unregister(CustomEnchantment customEnchantment) {
		customEnchantments.remove(customEnchantment);
		HandlerList.unregisterAll(customEnchantment.getListener());
	}

	public static CustomEnchantment byBook(ItemStack item) {
		if (item == null || item.getType() != Material.ENCHANTED_BOOK) {
			return null;
		}
		if (!item.hasItemMeta()) {
			return null;
		}
		ItemMeta meta = item.getItemMeta();
		if (!meta.hasLore()) {
			return null;
		}
		List<String> lore = meta.getLore();
		for (CustomEnchantment ce : customEnchantments) {
			for (String line : lore) {
				if (line.contains(ce.getAddedLorePart())) {
					return ce;
				}
			}
		}
		return null;
	}

	public static CustomEnchantment getEnchantment(String name) {
		for (CustomEnchantment ce : customEnchantments) {
			if (ce.getName().equalsIgnoreCase(name)) {
				return ce;
			}
		}
		return null;
	}

	private static List<CustomEnchantment> getEnchantments() {
		return new ArrayList<>(customEnchantments);
	}

	public CustomEnchantment(String enchantmentLore, EnchantmentListener listener) {
		this(enchantmentLore, 0, listener);
	}

	public CustomEnchantment(String enchantmentLore, long cooldownMs, EnchantmentListener listener) {
		this.name = enchantmentLore;
		this.listener = listener;
		this.cooldown = cooldownMs;
		if (cooldownMs != 0) {
			cooldowns = CacheBuilder.newBuilder().expireAfterWrite(cooldownMs, TimeUnit.MILLISECONDS).build();
		} else {
			cooldowns = null;
		}

		listener.setCustomEnchantment(this);
		register(this);
	}

	public boolean hasCooldown(Player plr) {
		if (cooldown == 0) {
			return false;
		}
		if (cooldowns.getIfPresent(plr.getUniqueId()) != null) {
			return true;
		}
		cooldowns.put(plr.getUniqueId(), Boolean.TRUE);

		return true; //TODO ADD COOLDOWN!
	}

	public void setCooldown(long amount, TimeUnit timeUnit) {
		setCooldown(timeUnit.toMillis(amount));
	}

	public boolean contains(ItemStack item) {
		if (item.hasItemMeta() && item.getItemMeta().hasLore()) {
			for (String line : item.getItemMeta().getLore()) {
				if (line.contains(getAddedLorePart())) {
					return true;
				}
			}
		}
		return false;
	}

	public ItemStack toBook(int lvl) {
		return UtilItem.addEnchantmentGlow(UtilItem.setLore(new ItemStack(Material.ENCHANTED_BOOK), new String[]{getAddedLorePart() + getRomanNumber(lvl)}));
	}

	public int getLevel(ItemStack item) {
		if (item.hasItemMeta()) {
			ItemMeta meta = item.getItemMeta();
			if (meta.hasLore()) {
				for (String lore : meta.getLore()) {
					if (lore.contains(getAddedLorePart())) {
						String level = lore.substring(getAddedLorePart().length());
						return getNormalNumber(level);
					}
				}
			}
		}
		return 0;
	}

	@NonNull
	public String getAddedLorePart() {
		return "§7" + name + " ";
	}

	public void addEnchantment(ItemStack item, int lvl) {
		ItemMeta meta;
		if (item.hasItemMeta()) {
			meta = item.getItemMeta();
		} else {
			meta = Bukkit.getItemFactory().getItemMeta(item.getType());
		}
		List<String> lore;
		if (meta.hasLore()) {
			lore = meta.getLore();
		} else {
			lore = new ArrayList<>();
		}

		if (contains(item)) {
			lore = lore.stream().filter(e -> !e.contains(getAddedLorePart())).collect(Collectors.toList());
		}

		lore.add(getAddedLorePart() + getRomanNumber(lvl));
		meta.setLore(lore);
		item.setItemMeta(meta);
	}

	public static String getRomanNumber(int i) {
		switch (i) {
			case 1:
				return "I";
			case 2:
				return "II";
			case 3:
				return "III";
			case 4:
				return "IV";
			case 5:
				return "V";
			case 6:
				return "VI";
			case 7:
				return "VII";
			case 8:
				return "VIII";
			case 9:
				return "IX";
			case 10:
				return "X";
			default:
				return String.valueOf(i);
		}
	}

	public static int getNormalNumber(String str) {
		switch (str) {
			case "I":
				return 1;
			case "II":
				return 2;
			case "III":
				return 3;
			case "IV":
				return 4;
			case "V":
				return 5;
			case "VI":
				return 6;
			case "VII":
				return 7;
			case "VIII":
				return 8;
			case "IX":
				return 9;
			case "X":
				return 10;
			default:
				return Integer.parseInt(str);
		}
	}

	public void setCooldown(long cooldown) {
		this.cooldown = cooldown;
		if (cooldown != 0) {
			cooldowns = CacheBuilder.newBuilder().expireAfterWrite(cooldown, TimeUnit.MILLISECONDS).build();
		} else {
			cooldowns = null;
		}
	}
}
