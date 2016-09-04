package eu.epicpvp.kcore.AACHack;

import java.lang.reflect.Method;
import java.util.UUID;

import me.konsolas.aac.AAC;
import me.konsolas.aac.api.HackType;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * This class uses reflection to access AAC.
 * 
 * Due to partly randomization of classes and packages from AAC per build, this only works for 1.9.10-b6
 */
public class AACAccessor {

	public static boolean isOnGround(Location loc) throws ReflectiveOperationException {
		//only works for 1.9.10-b6 !!!!!!!!!!!!!!!!
		Class<?> clazz = Class.forName("me.konsolas.aac.i.d.a");
		Method method = clazz.getDeclaredMethod("a", Location.class);
		return (boolean) method.invoke(null, loc);
	}
	
	public static boolean increaseAllViolationsAndNotify(UUID uuid, int amount, HackType hackType, String message) throws ReflectiveOperationException {
		AAC aac = JavaPlugin.getPlugin(AAC.class);
		Object checkManager = AAC.class.getField("l").get(aac);
		Method getDetector = checkManager.getClass().getMethod("a", HackType.class);
		Object detector = getDetector.invoke(checkManager, hackType);
		Method increaseAllViolationsAndNotify = detector.getClass().getMethod("a", UUID.class, int.class, HackType.class, String.class);
		return (boolean) increaseAllViolationsAndNotify.invoke(uuid, amount, hackType, message);
	}
}
