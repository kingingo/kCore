package me.kingingo.kcore.Util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
 
public enum UtilParticle {
	
	HUGE_EXPLOSION("hugeexplosion"),
	LARGE_EXPLODE("largeexplode"),
	FIREWORKS_SPARK("fireworksSpark"),
	BUBBLE("bubble", true),
	SUSPEND("suspend", true),
	DEPTH_SUSPEND("depthSuspend"),
	TOWN_AURA("townaura"),
	CRIT("crit"),
	MAGIC_CRIT("magicCrit"),
	SMOKE("smoke"),
	MOB_SPELL("mobSpell"),
	MOB_SPELL_AMBIENT("mobSpellAmbient"),
	SPELL("spell"),
	INSTANT_SPELL("instantSpell"),
	WITCH_MAGIC("witchMagic"),
	NOTE("note"),
	PORTAL("portal"),
	ENCHANTMENT_TABLE("enchantmenttable"),
	EXPLODE("explode"),
	FLAME("flame"),
	LAVA("lava"),
	FOOTSTEP("footstep"),
	SPLASH("splash"),
	WAKE("wake"),
	LARGE_SMOKE("largesmoke"),
	CLOUD("cloud"),
	RED_DUST("reddust"),
	SNOWBALL_POOF("snowballpoof"),
	DRIP_WATER("dripWater"),
	DRIP_LAVA("dripLava"),
	SNOW_SHOVEL("snowshovel"),
	SLIME("slime"),
	HEART("heart"),
	ANGRY_VILLAGER("angryVillager"),
	HAPPY_VILLAGER("happyVillager");
 
	private static final Map<String, UtilParticle> NAME_MAP = new HashMap<String, UtilParticle>();
	private final String name;
	private final boolean requiresWater;
	
	static {
		for (UtilParticle effect : values()) {
			NAME_MAP.put(effect.name, effect);
		}
	}
 
	private UtilParticle(String name, boolean requiresWater) {
		this.name = name;
		this.requiresWater = requiresWater;
	}
 
	private UtilParticle(String name) {
		this(name, false);
	}
	
	public String getName() {
		return name;
	}
	
	public boolean getRequiresWater() {
		return requiresWater;
	}

	public static UtilParticle fromName(String name) {
		for (Entry<String, UtilParticle> entry : NAME_MAP.entrySet()) {
			if (!entry.getKey().equalsIgnoreCase(name)) {
				continue;
			}
			return entry.getValue();
		}
		return null;
	}
 
	private static boolean isWater(Location location) {
		Material material = location.getBlock().getType();
		return material == Material.WATER || material == Material.STATIONARY_WATER;
	}
 
	@SuppressWarnings("deprecation")
	private static boolean isBlock(int id) {
		Material material = Material.getMaterial(id);
		return material != null && material.isBlock();
	}
 
	public void display(float offsetX, float offsetY, float offsetZ, float speed, int amount, Location center, double range) throws IllegalArgumentException {
		if (requiresWater && !isWater(center)) {
			throw new IllegalArgumentException("There is no water at the center location");
		}
		new UtilParticlePacket(name, offsetX, offsetY, offsetZ, speed, amount).sendTo(center, range);
	}
 
	public void display(float offsetX, float offsetY, float offsetZ, float speed, int amount, Location center, List<Player> players) throws IllegalArgumentException {
		if (requiresWater && !isWater(center)) {
			throw new IllegalArgumentException("There is no water at the center location");
		}
		new UtilParticlePacket(name, offsetX, offsetY, offsetZ, speed, amount).sendTo(center, players);
	}
 
	public static void displayIconCrack(int id, byte data, float offsetX, float offsetY, float offsetZ, float speed, int amount, Location center, double range) {
		new UtilParticlePacket("iconcrack_" + id + "_" + data, offsetX, offsetY, offsetZ, speed, amount).sendTo(center, range);
	}
 
	public static void displayIconCrack(int id, byte data, float offsetX, float offsetY, float offsetZ, float speed, int amount, Location center, List<Player> players) {
		new UtilParticlePacket("iconcrack_" + id + "_" + data, offsetX, offsetY, offsetZ, speed, amount).sendTo(center, players);
	}
 
	public static void displayBlockCrack(int id, byte data, float offsetX, float offsetY, float offsetZ, int amount, Location center, double range) throws IllegalArgumentException {
		if (!isBlock(id)) {
			throw new IllegalArgumentException("Invalid block id");
		}
		new UtilParticlePacket("blockcrack_" + id + "_" + data, offsetX, offsetY, offsetZ, 0, amount).sendTo(center, range);
	}
 
	public static void displayBlockCrack(int id, byte data, float offsetX, float offsetY, float offsetZ, int amount, Location center, List<Player> players) throws IllegalArgumentException {
		if (!isBlock(id)) {
			throw new IllegalArgumentException("Invalid block id");
		}
		new UtilParticlePacket("blockcrack_" + id + "_" + data, offsetX, offsetY, offsetZ, 0, amount).sendTo(center, players);
	}
 
	public static void displayBlockDust(int id, byte data, float offsetX, float offsetY, float offsetZ, float speed, int amount, Location center, double range) throws IllegalArgumentException {
		if (!isBlock(id)) {
			throw new IllegalArgumentException("Invalid block id");
		}
		new UtilParticlePacket("blockdust_" + id + "_" + data, offsetX, offsetY, offsetZ, speed, amount).sendTo(center, range);
	}
 
	public static void displayBlockDust(int id, byte data, float offsetX, float offsetY, float offsetZ, float speed, int amount, Location center, List<Player> players) throws IllegalArgumentException {
		if (!isBlock(id)) {
			throw new IllegalArgumentException("Invalid block id");
		}
		new UtilParticlePacket("blockdust_" + id + "_" + data, offsetX, offsetY, offsetZ, speed, amount).sendTo(center, players);
	}
 
	public static final class UtilParticlePacket {
		private static Constructor<?> packetConstructor;
		private static Method getHandle;
		private static Field playerConnection;
		private static Method sendPacket;
		private static boolean initialized;
		private final String name;
		private final float offsetX;
		private final float offsetY;
		private final float offsetZ;
		private final float speed;
		private final int amount;
		private Object packet;
 
		public UtilParticlePacket(String name, float offsetX, float offsetY, float offsetZ, float speed, int amount) throws IllegalArgumentException {
			initialize();
			if (speed < 0) {
				throw new IllegalArgumentException("The speed is lower than 0");
			}
			if (amount < 1) {
				throw new IllegalArgumentException("The amount is lower than 1");
			}
			this.name = name;
			this.offsetX = offsetX;
			this.offsetY = offsetY;
			this.offsetZ = offsetZ;
			this.speed = speed;
			this.amount = amount;
		}
 
		public enum PackageType {
			MINECRAFT_SERVER("net.minecraft.server." + getServerVersion()),
			CRAFTBUKKIT("org.bukkit.craftbukkit." + getServerVersion()),
			CRAFTBUKKIT_BLOCK(CRAFTBUKKIT, "block"),
			CRAFTBUKKIT_CHUNKIO(CRAFTBUKKIT, "chunkio"),
			CRAFTBUKKIT_COMMAND(CRAFTBUKKIT, "command"),
			CRAFTBUKKIT_CONVERSATIONS(CRAFTBUKKIT, "conversations"),
			CRAFTBUKKIT_ENCHANTMENS(CRAFTBUKKIT, "enchantments"),
			CRAFTBUKKIT_ENTITY(CRAFTBUKKIT, "entity"),
			CRAFTBUKKIT_EVENT(CRAFTBUKKIT, "event"),
			CRAFTBUKKIT_GENERATOR(CRAFTBUKKIT, "generator"),
			CRAFTBUKKIT_HELP(CRAFTBUKKIT, "help"),
			CRAFTBUKKIT_INVENTORY(CRAFTBUKKIT, "inventory"),
			CRAFTBUKKIT_MAP(CRAFTBUKKIT, "map"),
			CRAFTBUKKIT_METADATA(CRAFTBUKKIT, "metadata"),
			CRAFTBUKKIT_POTION(CRAFTBUKKIT, "potion"),
			CRAFTBUKKIT_PROJECTILES(CRAFTBUKKIT, "projectiles"),
			CRAFTBUKKIT_SCHEDULER(CRAFTBUKKIT, "scheduler"),
			CRAFTBUKKIT_SCOREBOARD(CRAFTBUKKIT, "scoreboard"),
			CRAFTBUKKIT_UPDATER(CRAFTBUKKIT, "updater"),
			CRAFTBUKKIT_UTIL(CRAFTBUKKIT, "util");
	 
			private final String path;
	 
			private PackageType(String path) {
				this.path = path;
			}
	 
			private PackageType(PackageType parent, String path) {
				this(parent + "." + path);
			}
	 
			public String getPath() {
				return path;
			}
	 
			public Class<?> getClass(String className) throws ClassNotFoundException {
				return Class.forName(this + "." + className);
			}
	 
			// Override for convenience
			@Override
			public String toString() {
				return path;
			}
			
			public static String getServerVersion() {
				return Bukkit.getServer().getClass().getPackage().getName().substring(23);
			}
		}
		
		public enum PacketType {
			HANDSHAKING_IN_SET_PROTOCOL("PacketHandshakingInSetProtocol"),
			LOGIN_IN_ENCRYPTION_BEGIN("PacketLoginInEncryptionBegin"),
			LOGIN_IN_START("PacketLoginInStart"),
			LOGIN_OUT_DISCONNECT("PacketLoginOutDisconnect"),
			LOGIN_OUT_ENCRYPTION_BEGIN("PacketLoginOutEncryptionBegin"),
			LOGIN_OUT_SUCCESS("PacketLoginOutSuccess"),
			PLAY_IN_ABILITIES("PacketPlayInAbilities"),
			PLAY_IN_ARM_ANIMATION("PacketPlayInArmAnimation"),
			PLAY_IN_BLOCK_DIG("PacketPlayInBlockDig"),
			PLAY_IN_BLOCK_PLACE("PacketPlayInBlockPlace"),
			PLAY_IN_CHAT("PacketPlayInChat"),
			PLAY_IN_CLIENT_COMMAND("PacketPlayInClientCommand"),
			PLAY_IN_CLOSE_WINDOW("PacketPlayInCloseWindow"),
			PLAY_IN_CUSTOM_PAYLOAD("PacketPlayInCustomPayload"),
			PLAY_IN_ENCHANT_ITEM("PacketPlayInEnchantItem"),
			PLAY_IN_ENTITY_ACTION("PacketPlayInEntityAction"),
			PLAY_IN_FLYING("PacketPlayInFlying"),
			PLAY_IN_HELD_ITEM_SLOT("PacketPlayInHeldItemSlot"),
			PLAY_IN_KEEP_ALIVE("PacketPlayInKeepAlive"),
			PLAY_IN_LOOK("PacketPlayInLook"),
			PLAY_IN_POSITION("PacketPlayInPosition"),
			PLAY_IN_POSITION_LOOK("PacketPlayInPositionLook"),
			PLAY_IN_SET_CREATIVE_SLOT("PacketPlayInSetCreativeSlot "),
			PLAY_IN_SETTINGS("PacketPlayInSettings"),
			PLAY_IN_STEER_VEHICLE("PacketPlayInSteerVehicle"),
			PLAY_IN_TAB_COMPLETE("PacketPlayInTabComplete"),
			PLAY_IN_TRANSACTION("PacketPlayInTransaction"),
			PLAY_IN_UPDATE_SIGN("PacketPlayInUpdateSign"),
			PLAY_IN_USE_ENTITY("PacketPlayInUseEntity"),
			PLAY_IN_WINDOW_CLICK("PacketPlayInWindowClick"),
			PLAY_OUT_ABILITIES("PacketPlayOutAbilities"),
			PLAY_OUT_ANIMATION("PacketPlayOutAnimation"),
			PLAY_OUT_ATTACH_ENTITY("PacketPlayOutAttachEntity"),
			PLAY_OUT_BED("PacketPlayOutBed"),
			PLAY_OUT_BLOCK_ACTION("PacketPlayOutBlockAction"),
			PLAY_OUT_BLOCK_BREAK_ANIMATION("PacketPlayOutBlockBreakAnimation"),
			PLAY_OUT_BLOCK_CHANGE("PacketPlayOutBlockChange"),
			PLAY_OUT_CHAT("PacketPlayOutChat"),
			PLAY_OUT_CLOSE_WINDOW("PacketPlayOutCloseWindow"),
			PLAY_OUT_COLLECT("PacketPlayOutCollect"),
			PLAY_OUT_CRAFT_PROGRESS_BAR("PacketPlayOutCraftProgressBar"),
			PLAY_OUT_CUSTOM_PAYLOAD("PacketPlayOutCustomPayload"),
			PLAY_OUT_ENTITY("PacketPlayOutEntity"),
			PLAY_OUT_ENTITY_DESTROY("PacketPlayOutEntityDestroy"),
			PLAY_OUT_ENTITY_EFFECT("PacketPlayOutEntityEffect"),
			PLAY_OUT_ENTITY_EQUIPMENT("PacketPlayOutEntityEquipment"),
			PLAY_OUT_ENTITY_HEAD_ROTATION("PacketPlayOutEntityHeadRotation"),
			PLAY_OUT_ENTITY_LOOK("PacketPlayOutEntityLook"),
			PLAY_OUT_ENTITY_METADATA("PacketPlayOutEntityMetadata"),
			PLAY_OUT_ENTITY_STATUS("PacketPlayOutEntityStatus"),
			PLAY_OUT_ENTITY_TELEPORT("PacketPlayOutEntityTeleport"),
			PLAY_OUT_ENTITY_VELOCITY("PacketPlayOutEntityVelocity"),
			PLAY_OUT_EXPERIENCE("PacketPlayOutExperience"),
			PLAY_OUT_EXPLOSION("PacketPlayOutExplosion"),
			PLAY_OUT_GAME_STATE_CHANGE("PacketPlayOutGameStateChange"),
			PLAY_OUT_HELD_ITEM_SLOT("PacketPlayOutHeldItemSlot"),
			PLAY_OUT_KEEP_ALIVE("PacketPlayOutKeepAlive"),
			PLAY_OUT_KICK_DISCONNECT("PacketPlayOutKickDisconnect"),
			PLAY_OUT_LOGIN("PacketPlayOutLogin"),
			PLAY_OUT_MAP("PacketPlayOutMap"),
			PLAY_OUT_MAP_CHUNK("PacketPlayOutMapChunk"),
			PLAY_OUT_MAP_CHUNK_BULK("PacketPlayOutMapChunkBulk"),
			PLAY_OUT_MULTI_BLOCK_CHANGE("PacketPlayOutMultiBlockChange"),
			PLAY_OUT_NAMED_ENTITY_SPAWN("PacketPlayOutNamedEntitySpawn"),
			PLAY_OUT_NAMED_SOUND_EFFECT("PacketPlayOutNamedSoundEffect"),
			PLAY_OUT_OPEN_SIGN_EDITOR("PacketPlayOutOpenSignEditor"),
			PLAY_OUT_OPEN_WINDOW("PacketPlayOutOpenWindow"),
			PLAY_OUT_PLAYER_INFO("PacketPlayOutPlayerInfo"),
			PLAY_OUT_POSITION("PacketPlayOutPosition"),
			PLAY_OUT_REL_ENTITY_MOVE("PacketPlayOutRelEntityMove"),
			PLAY_OUT_REL_ENTITY_MOVE_LOOK("PacketPlayOutRelEntityMoveLook"),
			PLAY_OUT_REMOVE_ENTITY_EFFECT("PacketPlayOutRemoveEntityEffect"),
			PLAY_OUT_RESPAWN("PacketPlayOutRespawn"),
			PLAY_OUT_SCOREBOARD_DISPLAY_OBJECTIVE("PacketPlayOutScoreboardDisplayObjective"),
			PLAY_OUT_SCOREBOARD_OBJECTIVE("PacketPlayOutScoreboardObjective"),
			PLAY_OUT_SCOREBOARD_SCORE("PacketPlayOutScoreboardScore"),
			PLAY_OUT_SCOREBOARD_TEAM("PacketPlayOutScoreboardTeam"),
			PLAY_OUT_SET_SLOT("PacketPlayOutSetSlot"),
			PLAY_OUT_SPAWN_ENTITY("PacketPlayOutSpawnEntity"),
			PLAY_OUT_SPAWN_ENTITY_EXPERIENCE_ORB("PacketPlayOutSpawnEntityExperienceOrb"),
			PLAY_OUT_SPAWN_ENTITY_LIVING("PacketPlayOutSpawnEntityLiving"),
			PLAY_OUT_SPAWN_ENTITY_PAINTING("PacketPlayOutSpawnEntityPainting"),
			PLAY_OUT_SPAWN_ENTITY_WEATHER("PacketPlayOutSpawnEntityWeather"),
			PLAY_OUT_SPAWN_POSITION("PacketPlayOutSpawnPosition"),
			PLAY_OUT_STATISTIC("PacketPlayOutStatistic"),
			PLAY_OUT_TAB_COMPLETE("PacketPlayOutTabComplete"),
			PLAY_OUT_TILE_ENTITY_DATA("PacketPlayOutTileEntityData"),
			PLAY_OUT_TRANSACTION("PacketPlayOutTransaction"),
			PLAY_OUT_UPDATE_ATTRIBUTES("PacketPlayOutUpdateAttributes"),
			PLAY_OUT_UPDATE_HEALTH("PacketPlayOutUpdateHealth"),
			PLAY_OUT_UPDATE_SIGN("PacketPlayOutUpdateSign"),
			PLAY_OUT_UPDATE_TIME("PacketPlayOutUpdateTime"),
			PLAY_OUT_WINDOW_ITEMS("PacketPlayOutWindowItems"),
			PLAY_OUT_WORLD_EVENT("PacketPlayOutWorldEvent"),
			PLAY_OUT_WORLD_PARTICLES("PacketPlayOutWorldParticles"),
			STATUS_IN_PING("PacketStatusInPing"),
			STATUS_IN_START("PacketStatusInStart"),
			STATUS_OUT_PONG("PacketStatusOutPong"),
			STATUS_OUT_SERVER_INFO("PacketStatusOutServerInfo");
	 
			private static final Map<String, PacketType> NAME_MAP = new HashMap<String, PacketType>();
			private final String name;
			private Class<?> packet;
	 
			static {
				for (PacketType type : values()) {
					NAME_MAP.put(type.name, type);
				}
			}
	 
			private PacketType(String name) {
				this.name = name;
			}
	 
			public String getName() {
				return name;
			}
	 
			public Class<?> getPacket() throws ClassNotFoundException {
				return packet == null ? (packet = PackageType.MINECRAFT_SERVER.getClass(name)) : packet;
			}
		}
		
		public enum DataType {
			BYTE(byte.class, Byte.class),
			SHORT(short.class, Short.class),
			INTEGER(int.class, Integer.class),
			LONG(long.class, Long.class),
			CHARACTER(char.class, Character.class),
			FLOAT(float.class, Float.class),
			DOUBLE(double.class, Double.class),
			BOOLEAN(boolean.class, Boolean.class);
	 
			private static final Map<Class<?>, DataType> CLASS_MAP = new HashMap<Class<?>, DataType>();
			private final Class<?> primitive;
			private final Class<?> reference;
	 
			static {
				for (DataType type : values()) {
					CLASS_MAP.put(type.primitive, type);
					CLASS_MAP.put(type.reference, type);
				}
			}
	 
			private DataType(Class<?> primitive, Class<?> reference) {
				this.primitive = primitive;
				this.reference = reference;
			}
	 
			public Class<?> getPrimitive() {
				return primitive;
			}
	 
			public Class<?> getReference() {
				return reference;
			}
	 
			public static DataType fromClass(Class<?> clazz) {
				return CLASS_MAP.get(clazz);
			}
	 
			public static Class<?> getPrimitive(Class<?> clazz) {
				DataType type = fromClass(clazz);
				return type == null ? clazz : type.getPrimitive();
			}
	 
			public static Class<?> getReference(Class<?> clazz) {
				DataType type = fromClass(clazz);
				return type == null ? clazz : type.getReference();
			}
	 
			public static Class<?>[] getPrimitive(Class<?>[] classes) {
				int length = classes == null ? 0 : classes.length;
				Class<?>[] types = new Class<?>[length];
				for (int index = 0; index < length; index++) {
					types[index] = getPrimitive(classes[index]);
				}
				return types;
			}
	 
			public static Class<?>[] getReference(Class<?>[] classes) {
				int length = classes == null ? 0 : classes.length;
				Class<?>[] types = new Class<?>[length];
				for (int index = 0; index < length; index++) {
					types[index] = getReference(classes[index]);
				}
				return types;
			}
	 
			public static Class<?>[] getPrimitive(Object[] objects) {
				int length = objects == null ? 0 : objects.length;
				Class<?>[] types = new Class<?>[length];
				for (int index = 0; index < length; index++) {
					types[index] = getPrimitive(objects[index].getClass());
				}
				return types;
			}
	 
			public static Class<?>[] getReference(Object[] objects) {
				int length = objects == null ? 0 : objects.length;
				Class<?>[] types = new Class<?>[length];
				for (int index = 0; index < length; index++) {
					types[index] = getReference(objects[index].getClass());
				}
				return types;
			}
	 
			public static boolean compare(Class<?>[] primary, Class<?>[] secondary) {
				if (primary == null || secondary == null || primary.length != secondary.length) {
					return false;
				}
				for (int index = 0; index < primary.length; index++) {
					Class<?> primaryClass = primary[index];
					Class<?> secondaryClass = secondary[index];
					if (primaryClass.equals(secondaryClass) || primaryClass.isAssignableFrom(secondaryClass)) {
						continue;
					}
					return false;
				}
				return true;
			}
		}
		
		public static Constructor<?> getConstructor(Class<?> clazz, Class<?>... parameterTypes) throws NoSuchMethodException {
			Class<?>[] primitiveTypes = DataType.getPrimitive(parameterTypes);
			for (Constructor<?> constructor : clazz.getConstructors()) {
				if (!DataType.compare(DataType.getPrimitive(constructor.getParameterTypes()), primitiveTypes)) {
					continue;
				}
				return constructor;
			}
			throw new NoSuchMethodException("There is no such constructor in this class with the specified parameter types");
		}
		
		public static Field getField(Class<?> clazz, boolean declared, String fieldName) throws NoSuchFieldException, SecurityException {
			Field field = declared ? clazz.getDeclaredField(fieldName) : clazz.getField(fieldName);
			field.setAccessible(true);
			return field;
		}
		
		public static Field getField(String className, PackageType packageType, boolean declared, String fieldName) throws NoSuchFieldException, SecurityException, ClassNotFoundException {
			return getField(packageType.getClass(className), declared, fieldName);
		}
		
		public static Method getMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) throws NoSuchMethodException {
			Class<?>[] primitiveTypes = DataType.getPrimitive(parameterTypes);
			for (Method method : clazz.getMethods()) {
				if (!method.getName().equals(methodName) || !DataType.compare(DataType.getPrimitive(method.getParameterTypes()), primitiveTypes)) {
					continue;
				}
				return method;
			}
			throw new NoSuchMethodException("There is no such method in this class with the specified name and parameter types");
		}
		
		public static Method getMethod(String className, PackageType packageType, String methodName, Class<?>... parameterTypes) throws NoSuchMethodException, ClassNotFoundException {
			return getMethod(packageType.getClass(className), methodName, parameterTypes);
		}
		
		public static void initialize() throws VersionIncompatibleException {
			if (initialized) {
				return;
			}
			try {
				int version = Integer.parseInt(Character.toString(PackageType.getServerVersion().charAt(3)));
				Class<?> packetClass = PackageType.MINECRAFT_SERVER.getClass(version < 7 ? "Packet63WorldParticles" : PacketType.PLAY_OUT_WORLD_PARTICLES.getName());
				packetConstructor = getConstructor(packetClass);
				getHandle = getMethod("CraftPlayer", PackageType.CRAFTBUKKIT_ENTITY, "getHandle");
				playerConnection = getField("EntityPlayer", PackageType.MINECRAFT_SERVER, false, "playerConnection");
				sendPacket = getMethod(playerConnection.getType(), "sendPacket", PackageType.MINECRAFT_SERVER.getClass("Packet"));
			} catch (Exception exception) {
				throw new VersionIncompatibleException("Your current bukkit version seems to be incompatible with this library", exception);
			}
			initialized = true;
		}
		
		public static boolean isInitialized() {
			return initialized;
		}
 
		public void sendTo(Location center, Player player) throws PacketInstantiationException, PacketSendingException {
			if (packet == null) {
				try {
					packet = packetConstructor.newInstance();
					UtilReflection.setValue("a", packet, name);
					UtilReflection.setValue("b", packet, (float) center.getX());
					UtilReflection.setValue("c", packet, (float) center.getY());
					UtilReflection.setValue("d", packet, (float) center.getZ());
					UtilReflection.setValue("e", packet, offsetX);
					UtilReflection.setValue("f", packet, offsetY);
					UtilReflection.setValue("g", packet, offsetZ);
					UtilReflection.setValue("h", packet, speed);
					UtilReflection.setValue("i", packet, amount);
//					ReflectionUtils.setValue(packet, true, "a", name);
//					ReflectionUtils.setValue(packet, true, "b", (float) center.getX());
//					ReflectionUtils.setValue(packet, true, "c", (float) center.getY());
//					ReflectionUtils.setValue(packet, true, "d", (float) center.getZ());
//					ReflectionUtils.setValue(packet, true, "e", offsetX);
//					ReflectionUtils.setValue(packet, true, "f", offsetY);
//					ReflectionUtils.setValue(packet, true, "g", offsetZ);
//					ReflectionUtils.setValue(packet, true, "h", speed);
//					ReflectionUtils.setValue(packet, true, "i", amount);
				} catch (Exception exception) {
					throw new PacketInstantiationException("Packet instantiation failed", exception);
				}
			}
			try {
				sendPacket.invoke(playerConnection.get(getHandle.invoke(player)), packet);
			} catch (Exception exception) {
				throw new PacketSendingException("Failed to send the packet to player '" + player.getName() + "'", exception);
			}
		}
 
		public void sendTo(Location center, List<Player> players) throws IllegalArgumentException {
			if (players.isEmpty()) {
				throw new IllegalArgumentException("The player list is empty");
			}
			for (Player player : players) {
				sendTo(center, player);
			}
		}
 
		@SuppressWarnings("deprecation")
		public void sendTo(Location center, double range) throws IllegalArgumentException {
			if (range < 1) {
				throw new IllegalArgumentException("The range is lower than 1");
			}
			String worldName = center.getWorld().getName();
			double squared = range * range;
			for (Player player : Bukkit.getOnlinePlayers()) {
				if (!player.getWorld().getName().equals(worldName) || player.getLocation().distanceSquared(center) > squared) {
					continue;
				}
				sendTo(center, player);
			}
		}
 
		private static final class VersionIncompatibleException extends RuntimeException {
			private static final long serialVersionUID = 3203085387160737484L;
			public VersionIncompatibleException(String message, Throwable cause) {
				super(message, cause);
			}
		}
		
		private static final class PacketInstantiationException extends RuntimeException {
			private static final long serialVersionUID = 3203085387160737484L;
			public PacketInstantiationException(String message, Throwable cause) {
				super(message, cause);
			}
		}
 
		private static final class PacketSendingException extends RuntimeException {
			private static final long serialVersionUID = 3203085387160737484L;
			public PacketSendingException(String message, Throwable cause) {
				super(message, cause);
			}
		}
	}
}