package eu.epicpvp.kcore.Util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64OutputStream;
import org.bukkit.entity.Player;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import eu.epicpvp.kcore.Inventory.InventoryBase;
import eu.epicpvp.kcore.Inventory.InventoryPageBase;

public class UtilList {

	public static HashMap<String, Integer> getRanked(HashMap<String, Integer> list, int ranked) {
		HashMap<String, Integer> rank = new HashMap<>();
		int a = 0;
		int value = 0;
		String key;
		boolean b = false;
		for (int i = 0; i < list.size(); i++) {
			key = (String) list.keySet().toArray()[i];
			value = list.get(key);
			b = true;
			for (String p1 : list.keySet()) {
				if (p1.equalsIgnoreCase(key))
					continue;
				if (list.get(p1) > value) {
					b = false;
					break;
				}
			}

			if (b) {
				rank.put(key, value);
				list.remove(key);
				a++;

				if (a == ranked) {
					break;
				}
			}
		}
		return rank;
	}

	//UtilList.serialize( HASHMAP ) || UtilList.serialize( String )
	public static String serialize(Object object) throws IOException {
		ByteArrayOutputStream byteaOut = new ByteArrayOutputStream();
		GZIPOutputStream gzipOut = null;
		try {
			gzipOut = new GZIPOutputStream(new Base64OutputStream(byteaOut));
			gzipOut.write(new Gson().toJson(object).getBytes("UTF-8"));
		} finally {
			if (gzipOut != null)
				try {
					gzipOut.close();
				} catch (IOException logOrIgnore) {
				}
		}
		return new String(byteaOut.toByteArray());
	}

	//UtilList.deserialize(STRING, new TypeToken<HashMap<Object,Object>(){}); || UtilList.deserialize(STRING, new TypeToken<HashMap<String,Player>(){});
	public static <T> T deserialize(String string, TypeToken type) throws IOException {
		ByteArrayOutputStream byteaOut = new ByteArrayOutputStream();
		GZIPInputStream gzipIn = null;
		try {
			gzipIn = new GZIPInputStream(new Base64InputStream(new ByteArrayInputStream(string.getBytes("UTF-8"))));
			for (int data; (data = gzipIn.read()) > -1;) {
				byteaOut.write(data);
			}
		} finally {
			if (gzipIn != null)
				try {
					gzipIn.close();
				} catch (IOException logOrIgnore) {
				}
		}
		return new Gson().fromJson(new String(byteaOut.toByteArray()), type.getType());
	}

	public static String decode(HashMap<?, ?> map) throws IllegalStateException {
		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

			// Write the size of the inventory
			dataOutput.writeInt(map.size() * 2);

			// Save every element in the list
			Object obj;
			for (int i = 0; i < map.size(); i++) {
				obj = map.keySet().toArray()[i];
				dataOutput.writeObject(obj);
				dataOutput.writeObject(map.get(i));
			}

			// Serialize that array
			dataOutput.close();
			return Base64Coder.encodeLines(outputStream.toByteArray());
		} catch (Exception e) {
			throw new IllegalStateException("Unable to save item stacks.", e);
		}
	}

	public static HashMap<?, ?> encode(String data) throws IOException {
		try {
			ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
			BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
			HashMap<Object, Object> map = new HashMap<>();

			// Read the serialized inventory
			for (int i = 0; i < dataInput.readInt(); i++) {
				map.put(dataInput.readObject(), dataInput.readObject());
			}

			dataInput.close();
			return map;
		} catch (ClassNotFoundException e) {
			throw new IOException("Unable to decode class type.", e);
		}
	}

	public static void CleanList(HashMap<?, ?> list) {
		if (list.isEmpty())
			return;
		if (list.keySet().toArray()[0] instanceof UUID) {
			boolean b = false;
			for (int i = 0; i < list.size(); i++) {
				b = false;
				for (Player player : UtilServer.getPlayers()) {
					if (player.getUniqueId().equals(((UUID) list.keySet().toArray()[i]))) {
						b = true;
						break;
					}
				}

				if (!b) {
					list.remove(i);
				}
			}
		} else if (list.keySet().toArray()[0] instanceof Player) {
			boolean b = false;
			for (int i = 0; i < list.size(); i++) {
				b = false;
				for (Player player : UtilServer.getPlayers()) {
					if (player.getName().equalsIgnoreCase(((Player) list.keySet().toArray()[i]).getName())) {
						b = true;
						break;
					}
				}

				if (!b) {
					list.remove(i);
				}
			}
		} else if (list.keySet().toArray()[0] instanceof String) {
			boolean b = false;
			for (int i = 0; i < list.size(); i++) {
				b = false;
				for (Player player : UtilServer.getPlayers()) {
					if (player.getName().equalsIgnoreCase(((String) list.keySet().toArray()[i]))) {
						b = true;
						break;
					}
				}

				if (!b) {
					list.remove(i);
				}
			}
		}
	}

	public static void CleanList(ArrayList<?> list) {
		if (list.isEmpty())
			return;
		if (list.get(0) instanceof UUID) {
			boolean b = false;
			for (int i = 0; i < list.size(); i++) {
				b = false;
				for (Player player : UtilServer.getPlayers()) {
					if (player.getUniqueId().equals(((UUID) list.get(0)))) {
						b = true;
						break;
					}
				}

				if (!b) {
					list.remove(i);
				}
			}
		} else if (list.get(0) instanceof Player) {
			boolean b = false;
			for (int i = 0; i < list.size(); i++) {
				b = false;
				for (Player player : UtilServer.getPlayers()) {
					if (player.getName().equalsIgnoreCase(((Player) list.get(0)).getName())) {
						b = true;
						break;
					}
				}

				if (!b) {
					list.remove(i);
				}
			}
		} else if (list.get(0) instanceof String) {
			boolean b = false;
			for (int i = 0; i < list.size(); i++) {
				b = false;
				for (Player player : UtilServer.getPlayers()) {
					if (player.getName().equalsIgnoreCase(((String) list.get(0)))) {
						b = true;
						break;
					}
				}

				if (!b) {
					list.remove(i);
				}
			}
		}
	}

	public static void CleanList(HashMap<?, ?> list, InventoryBase base) {
		if (list.isEmpty())
			return;
		if (list.keySet().toArray()[0] instanceof Player && list.values().toArray()[0] instanceof InventoryPageBase) {
			boolean b = false;
			for (int i = 0; i < list.size(); i++) {
				b = false;
				for (Player player : UtilServer.getPlayers()) {
					if (player.getName().equalsIgnoreCase(((Player) list.keySet().toArray()[i]).getName())) {
						b = true;
						break;
					}
				}

				if (!b) {
					base.getPages().remove(list.get(i));
					list.remove(i);
				}
			}
		}
	}

	public static String[] changeListType(List<String> l) {
		String[] s = new String[l.size()];

		for (int i = 0; i < l.size(); i++)
			s[i] = l.get(i);

		return s;
	}

}
