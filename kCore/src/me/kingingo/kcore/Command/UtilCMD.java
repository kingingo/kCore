package me.kingingo.kcore.Command;

import java.lang.reflect.Field;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class UtilCMD {

	public static void registerCMDs(final Command c,JavaPlugin plugin) {
		try {
			try {
				Field f = plugin.getServer().getClass().getDeclaredField("commandMap");
				f.setAccessible(true);
				CommandMap cMap = (CommandMap) f.get(plugin.getServer());
				cMap.register(c.getCommand(), "/" + c.getCommand(), new org.bukkit.command.Command(c.getCommand()) {
					@Override
					public boolean execute(CommandSender arg0, String arg1,String[] arg2) {
						return c.onCommand(arg0, this, arg1, arg2);
					}
				});
				f.set(plugin.getServer(), cMap);
			} catch (Exception e) {
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
