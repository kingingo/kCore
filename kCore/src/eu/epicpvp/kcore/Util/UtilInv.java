package eu.epicpvp.kcore.Util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import eu.epicpvp.kcore.Inventory.InventoryBase;
import lombok.Setter;

public class UtilInv {
	@Setter
	private static InventoryBase base;

	public static InventoryBase getBase() {
		if (base == null)
			new InventoryBase(UtilServer.getPermissionManager().getInstance());
		return base;
	}

	public static Integer[] getSlotsBorder(InventorySize size, InventorySplit startLine) {
		return getSlotsBorder(size, startLine, size.getLines()[size.getLines().length-1]);
	}
	
	public static Integer[] getSlotsBorder(InventorySize size, InventorySplit startLine, InventorySplit endLine) {
		ArrayList<Integer> list = new ArrayList<>();
		for (int slot = (startLine.getMin() + 1); slot < size.getSize(); slot++) {
			InventorySplit split = InventorySplit.split(slot);

			if (split.getMin() <= slot && split.getMax() >= slot) {
				if (slot == split.getMax()) {
					slot += 2;
				}

				if (endLine.getMax() < slot)
					break;
				list.add(slot);
			}
		}

		return list.toArray(new Integer[list.size()]);
	}
	
	public static Integer[] getInventoryBorder(InventorySize size){
		ArrayList<Integer> slots = new ArrayList<>();
		
		for(InventorySplit split : size.getLines()){
			if(split == InventorySplit._9){
				for(int i = 0; i<=split.getMax(); i++)slots.add(i);
			}else if(split == size.getLastLine()){
				for(int i = split.getMin(); i<=split.getMax(); i++)slots.add(i);
			}else{
				slots.add(split.getMin());
				slots.add(split.getMax());
			}
		}
		
		return slots.toArray(new Integer[slots.size()]);
	}

	public static ArrayList<ItemStack> getItems(Inventory inventory, int amount) {
		ArrayList<ItemStack> items = new ArrayList<>();
		int count = 0;
		for (int i = 0; i < inventory.getContents().length; i++) {
			ItemStack item = inventory.getContents()[i];

			if (item != null && item.getType() != Material.AIR) {
				if ((item.getAmount() + count) >= amount) {
					int rest = amount - count;

					ItemStack item2 = item.clone();
					item2.setAmount(rest);
					items.add(item2);

					item.setAmount(item.getAmount() - rest);
					inventory.setItem(i, item);
					count = amount;
				} else {
					items.add(item);
					count += item.getAmount();
					inventory.setItem(i, null);
				}
			}
		}
		return items;
	}

	public static ItemStack getFirstItem(Inventory inventory, int id, byte data) {
		for (ItemStack i : inventory.getContents()) {
			if (i != null && i.getType().getId() == id && i.getData().getData() == data)
				return i;
		}
		return null;
	}

	public static ItemStack getFirstItem(Inventory inventory) {
		for (ItemStack i : inventory.getContents()) {
			if (i != null && i.getType() != Material.AIR)
				return i;
		}

		return null;
	}

	public static boolean itemsAllSame(Inventory inventory) {
		ItemStack item = null;
		for (ItemStack i : inventory.getContents()) {
			if (i != null && i.getType() != Material.AIR) {
				if (item == null)
					item = i;

				if (item != null) {
					if (i.getType() != item.getType() || i.getData().getData() != item.getData().getData()) {
						return false;
					}
				}
			}
		}

		return true;
	}

	public static boolean insert(Player player, ItemStack stack) {
		player.getInventory().addItem(new ItemStack[] { stack });
		player.updateInventory();
		return true;
	}

	public static boolean contains(Inventory inv, Material item, byte data, int required) {
		for (Iterator localIterator = inv.all(item).keySet().iterator(); localIterator.hasNext();) {
			int i = ((Integer) localIterator.next()).intValue();

			if (required <= 0) {
				return true;
			}
			ItemStack stack = inv.getItem(i);

			if ((stack != null) && (stack.getAmount() > 0)
					&& ((stack.getData() == null) || (stack.getData().getData() == data))) {
				required -= stack.getAmount();
			}
		}

		if (required <= 0) {
			return true;
		}

		return false;
	}

	public static boolean contains(Player player, Material item, byte data, int required) {
		for (Iterator localIterator = player.getInventory().all(item).keySet().iterator(); localIterator.hasNext();) {
			int i = ((Integer) localIterator.next()).intValue();

			if (required <= 0) {
				return true;
			}
			ItemStack stack = player.getInventory().getItem(i);

			if ((stack != null) && (stack.getAmount() > 0)
					&& ((stack.getData() == null) || (stack.getData().getData() == data))) {
				required -= stack.getAmount();
			}
		}

		if (required <= 0) {
			return true;
		}

		return false;
	}

	public static int getAnzahlInventory(Player player) {
		int i = 0;
		for (ItemStack item : player.getInventory().getContents()) {
			if (item != null && item.getType() != Material.AIR)
				i++;
		}
		for (ItemStack item : player.getInventory().getArmorContents()) {
			if (item != null && item.getType() != Material.AIR)
				i++;
		}
		return i;
	}

	public static String PlayerInventorytoBase64(PlayerInventory inventory) throws IllegalStateException {
		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

			// Write the size of the inventory
			dataOutput.writeInt(inventory.getSize());

			// Save every element in the list
			for (int i = 0; i < inventory.getSize(); i++) {
				dataOutput.writeObject(inventory.getItem(i));
			}

			// Serialize that array
			dataOutput.close();
			return Base64Coder.encodeLines(outputStream.toByteArray());
		} catch (Exception e) {
			throw new IllegalStateException("Unable to save item stacks.", e);
		}
	}

	public static PlayerInventory PlayerInventoryfromBase64(String data) throws IOException {
		try {
			ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
			BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
			PlayerInventory inventory = (PlayerInventory) Bukkit.getServer().createInventory(null,
					InventoryType.PLAYER);

			// Read the serialized inventory
			for (int i = 0; i < inventory.getSize(); i++) {
				inventory.setItem(i, (ItemStack) dataInput.readObject());
			}

			dataInput.close();
			return inventory;
		} catch (ClassNotFoundException e) {
			throw new IOException("Unable to decode class type.", e);
		}
	}

	public static Inventory fromBase64(String data) throws IOException {
		try {
			ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
			BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
			Inventory inventory = Bukkit.getServer().createInventory(null, dataInput.readInt());

			// Read the serialized inventory
			for (int i = 0; i < inventory.getSize(); i++) {
				inventory.setItem(i, (ItemStack) dataInput.readObject());
			}

			dataInput.close();
			return inventory;
		} catch (ClassNotFoundException e) {
			throw new IOException("Unable to decode class type.", e);
		}
	}

	public static String toBase64(Inventory inventory) throws IllegalStateException {
		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

			// Write the size of the inventory
			dataOutput.writeInt(inventory.getSize());

			// Save every element in the list
			for (int i = 0; i < inventory.getSize(); i++) {
				dataOutput.writeObject(inventory.getItem(i));
			}

			// Serialize that array
			dataOutput.close();
			return Base64Coder.encodeLines(outputStream.toByteArray());
		} catch (Exception e) {
			throw new IllegalStateException("Unable to save item stacks.", e);
		}
	}

	public static String itemStackToBase64(ItemStack item) throws IllegalStateException {
		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

			// Write the size of the inventory
			dataOutput.writeInt(1);

			// Save every element in the list
			dataOutput.writeObject(item);

			// Serialize that array
			dataOutput.close();
			return Base64Coder.encodeLines(outputStream.toByteArray());
		} catch (Exception e) {
			throw new IllegalStateException("Unable to save item stacks.", e);
		}
	}

	public static ItemStack itemStackFromBase64(String data) throws IOException {
		try {
			ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
			BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
			dataInput.readInt();
			// Read the serialized inventory
			ItemStack item = (ItemStack) dataInput.readObject();

			dataInput.close();
			return item;
		} catch (ClassNotFoundException e) {
			throw new IOException("Unable to decode class type.", e);
		}
	}

	public static ItemStack[] itemStackArrayFromBase64(String data) throws IOException {
		try {
			ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
			BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
			ItemStack[] items = new ItemStack[dataInput.readInt()];

			// Read the serialized inventory
			for (int i = 0; i < items.length; i++) {
				items[i] = (ItemStack) dataInput.readObject();
			}

			dataInput.close();
			return items;
		} catch (ClassNotFoundException e) {
			throw new IOException("Unable to decode class type.", e);
		}
	}

	public static String itemStackArrayToBase64(ItemStack[] items) throws IllegalStateException {
		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

			// Write the size of the inventory
			dataOutput.writeInt(items.length);

			// Save every element in the list
			for (int i = 0; i < items.length; i++) {
				dataOutput.writeObject(items[i]);
			}

			// Serialize that array
			dataOutput.close();
			return Base64Coder.encodeLines(outputStream.toByteArray());
		} catch (Exception e) {
			throw new IllegalStateException("Unable to save item stacks.", e);
		}
	}

	public static boolean remove(Player player, ItemStack remove, int toRemove) {
		for (int i = 0; i < player.getInventory().getContents().length; i++) {
			if (toRemove > 0) {
				ItemStack stack = player.getInventory().getItem(i);

				if (stack != null && UtilItem.ItemNameEquals(stack, remove)) {
					int foundAmount = stack.getAmount();

					if (toRemove >= foundAmount) {
						toRemove -= foundAmount;
						player.getInventory().setItem(i, null);
					} else {
						stack.setAmount(foundAmount - toRemove);
						player.getInventory().setItem(i, stack);
						toRemove = 0;
					}
				}
			}
		}
		player.updateInventory();
		return true;
	}

	public static boolean remove(Inventory inv, Material item, byte data, int toRemove) {
		if (!contains(inv, item, data, toRemove)) {
			return false;
		}
		for (Iterator localIterator = inv.all(item).keySet().iterator(); localIterator.hasNext();) {
			int i = ((Integer) localIterator.next()).intValue();

			if (toRemove > 0) {
				ItemStack stack = inv.getItem(i);

				if ((stack.getData() == null) || (stack.getData().getData() == data)) {
					int foundAmount = stack.getAmount();

					if (toRemove >= foundAmount) {
						toRemove -= foundAmount;
						inv.setItem(i, null);
					} else {
						stack.setAmount(foundAmount - toRemove);
						inv.setItem(i, stack);
						toRemove = 0;
					}
				}
			}
		}
		return true;
	}

	public static boolean remove(Player player, int item, byte data, int toRemove) {
		return remove(player, Material.getMaterial(item), data, toRemove);
	}

	public static boolean remove(Player player, Material item, byte data, int toRemove) {
		if (!contains(player, item, data, toRemove)) {
			return false;
		}
		for (Iterator localIterator = player.getInventory().all(item).keySet().iterator(); localIterator.hasNext();) {
			int i = ((Integer) localIterator.next()).intValue();

			if (toRemove > 0) {
				ItemStack stack = player.getInventory().getItem(i);

				if ((stack.getData() == null) || (stack.getData().getData() == data)) {
					int foundAmount = stack.getAmount();

					if (toRemove >= foundAmount) {
						toRemove -= foundAmount;
						player.getInventory().setItem(i, null);
					} else {
						stack.setAmount(foundAmount - toRemove);
						player.getInventory().setItem(i, stack);
						toRemove = 0;
					}
				}
			}
		}
		player.updateInventory();
		return true;
	}

	public static void Clear(Player player) {
		PlayerInventory inv = player.getInventory();

		inv.clear();
		inv.clear(inv.getSize() + 0);
		inv.clear(inv.getSize() + 1);
		inv.clear(inv.getSize() + 2);
		inv.clear(inv.getSize() + 3);

		player.saveData();
	}

	public static void drop(Player player, boolean clear) {
		for (ItemStack cur : player.getInventory().getContents()) {
			if (cur != null) {
				if (cur.getType() != Material.AIR) {
					player.getWorld().dropItemNaturally(player.getLocation(), cur);
				}
			}
		}
		for (ItemStack cur : player.getInventory().getArmorContents()) {
			if (cur != null) {
				if (cur.getType() != Material.AIR) {
					player.getWorld().dropItemNaturally(player.getLocation(), cur);
				}
			}
		}
		if (clear)
			Clear(player);
	}

	public static void Update(Entity player) {
		if (!(player instanceof Player)) {
			return;
		}
		((Player) player).updateInventory();
	}

	public static int removeAll(Player player, Material type, byte data) {
		HashSet<ItemStack> remove = new HashSet<>();
		int count = 0;

		for (ItemStack item : player.getInventory().getContents()) {
			if ((item != null) && (item.getType() == type) && ((data == -1) || (item.getData() == null)
					|| ((item.getData() != null) && (item.getData().getData() == data)))) {
				count += item.getAmount();
				remove.add(item);
			}
		}
		for (ItemStack item : remove) {
			player.getInventory().remove(item);
		}
		return count;
	}

	public static byte GetData(ItemStack stack) {
		if (stack == null) {
			return 0;
		}
		if (stack.getData() == null) {
			return 0;
		}
		return stack.getData().getData();
	}

	public static boolean IsItem(ItemStack item, Material type, byte data) {
		return IsItem(item, type.getId(), data);
	}

	public static boolean IsItem(ItemStack item, int id, byte data) {
		if (item == null) {
			return false;
		}
		if (item.getTypeId() != id) {
			return false;
		}
		if ((data != -1) && (GetData(item) != data)) {
			return false;
		}
		return true;
	}

	public static Integer FreeInventory(Player p) {
		int a = 0;

		for (ItemStack i : p.getInventory()) {

			if (i == null || i.getType() == Material.AIR) {
				a++;
			}

		}

		return a;
	}

	public static boolean isInventoryEmpty(Inventory inv) {
		boolean empty = true;
		for (ItemStack i : inv.getContents()) {
			if (i != null) {
				empty = false;
				break;
			}
		}
		return empty;
	}

	public static boolean isInInventory(Player p, Material m) {

		for (ItemStack i : p.getInventory()) {

			if (i != null && i.getType() == m) {
				return true;
			}

		}
		return false;
	}

	public static Integer AnzahlInInventory(Inventory inv, int id, byte data) {
		int a = 0;

		for (ItemStack i : inv) {
			if (i != null) {
				if ((i.getTypeId() == id && GetData(i) == data) || (id == 0)) {
					a = a + i.getAmount();
				}
			}
		}

		return a;
	}

	public static Integer AnzahlInInventory(Player p, int id, byte data) {
		int a = 0;

		for (ItemStack i : p.getInventory()) {
			if (i != null && i.getTypeId() == id && GetData(i) == data) {
				a = a + i.getAmount();
			}
		}

		return a;
	}

	public static Integer AnzahlInInventory(Player p, Material m, byte data) {
		return AnzahlInInventory(p, m.getId(), data);
	}

	public static boolean isInInventory(Player p, int id) {

		for (ItemStack i : p.getInventory()) {

			if (i != null && i.getTypeId() == id) {
				return true;
			}

		}
		return false;
	}

	public static void DisallowMovementOf(InventoryClickEvent event, String name, Material type, byte data,
			boolean inform) {
		DisallowMovementOf(event, name, type, data, inform, false);
	}

	public static void DisallowMovementOf(InventoryClickEvent event, String name, Material type, byte data,
			boolean inform, boolean allInventorties) {
		if ((!allInventorties) && (event.getInventory().getType() == InventoryType.CRAFTING)) {
			return;
		}

		if ((event.getAction() == InventoryAction.HOTBAR_SWAP)
				|| (event.getAction() == InventoryAction.HOTBAR_MOVE_AND_READD)) {
			boolean match = false;

			if (IsItem(event.getCurrentItem(), type, data)) {
				match = true;
			}
			if (IsItem(event.getWhoClicked().getInventory().getItem(event.getHotbarButton()), type, data)) {
				match = true;
			}
			if (!match) {
				return;
			}

			// UtilPlayer.message(event.getWhoClicked(), F.main("Inventory",
			// "You cannot hotbar swap " + F.item(name) + "."));
			event.setCancelled(true);
		} else {
			if (event.getCurrentItem() == null) {
				return;
			}
			IsItem(event.getCurrentItem(), type, data);

			if (!IsItem(event.getCurrentItem(), type, data)) {
				return;
			}
			// UtilPlayer.message(event.getWhoClicked(), F.main("Inventory",
			// "You cannot move " + F.item(name) + "."));
			event.setCancelled(true);
		}
	}

	public static void repairInventory(Player player, boolean body) {
		for (ItemStack item : player.getInventory().getContents()) {
			if (item != null && item.getType() != Material.AIR) {
				UtilItem.RepairItem(item);
			}
		}

		if (body) {
			for (ItemStack item : player.getInventory().getArmorContents()) {
				if (item != null && item.getType() != Material.AIR) {
					UtilItem.RepairItem(item);
				}
			}
		}
	}

	public static ItemStack searchInventoryItem(Player player, Material material, byte data, int max_anzahl) {
		for (ItemStack item : player.getInventory().getContents()) {
			if (item != null && item.getType() != Material.AIR) {
				if (item.getType() == material) {
					if (item.hasItemMeta()) {
						if (GetData(item) == data && item.getAmount() <= max_anzahl)
							return item;
					}
				}
			}
		}
		return null;
	}

	public static ItemStack searchInventoryItem(Player player, Material material, String displayName) {
		for (ItemStack item : player.getInventory().getContents()) {
			if (item != null && item.getType() != Material.AIR) {
				if (item.getType() == material) {
					if (item.hasItemMeta()) {
						if (item.getItemMeta().hasDisplayName()) {
							if (item.getItemMeta().getDisplayName().equalsIgnoreCase(displayName)) {
								return item;
							}
						}
					}
				}
			}
		}
		return null;
	}

	public static void refreshDurability(Player player, Material type) {
		for (ItemStack item : player.getInventory().getContents())
			if ((item != null) && (item.getType() == type)) {
				if (item.getDurability() == 0) {
					item.setDurability((short) 1);
				} else {
					item.setDurability((short) 0);
				}
			}
	}
}