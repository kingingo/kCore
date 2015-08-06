package me.kingingo.kcore.Command;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.SimplePluginManager;

public class CommandHandler {
private Plugin plugin;

	public CommandHandler(Plugin p){
		this.plugin = p;
	}
	
	public void register(Class clazz,CommandExecutor cExe){
		for(Method m:clazz.getDeclaredMethods())register(m,m.getAnnotation(Command.class),cExe);
	}
	
	private void register(Method m, Command cmd,CommandExecutor cExe){
		if(cmd == null)return;
		System.out.println("Register Command " + cmd.command());
		String name = cmd.command();
		List<String> alias = new ArrayList<String>();
		alias.add(name); 
		if(!ArrayUtils.isEmpty(cmd.alias()))Collections.addAll(alias, cmd.alias());
		CommandMap cm = getCommandMap();
		cm.register(name,new DynamicCommand(plugin,cExe,name, name, m, alias.toArray(new String[alias.size()]), "/" + name, cmd.permissions(), cmd.permissionMessage(), cmd.sender()));
		setCommandMap(cm);
	}
	
    public CommandMap getCommandMap() {
        Field map;
        try {
            map = plugin.getServer().getClass().getDeclaredField("commandMap");
            map.setAccessible(true);
            return (CommandMap) map.get(plugin.getServer());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public void setCommandMap(CommandMap c) {
        Field map;
        try {
            map = plugin.getServer().getClass().getDeclaredField("commandMap");
            map.setAccessible(true);
             map.set(plugin.getServer(),c);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
	public enum Sender {
		CONSOLE(ConsoleCommandSender.class),
		PLAYER(Player.class),
		EVERYONE(CommandSender.class);
		Class w;
		Sender(Class who){
			w = who;
		}
		
		public Class getSenderClass(){
			return w;
		}
	}
	
	@Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
	public @interface Command{
		public String command();
		public String usage() default "/<command>";
		public String[] alias() default {};
		public Sender sender() default Sender.EVERYONE;
		public String[] permissions() default {};
		public String permissionMessage() default "�3�cDu hast nicht die Rechte, dies zu tun!";
	}
	
	public class DynamicCommand extends org.bukkit.command.Command implements PluginIdentifiableCommand{
		String[] permission;
		String permissionMessage;
		Sender s;
		Method owner;
		Plugin ownerPlugin;
		CommandExecutor cExe;
		
		public DynamicCommand(Plugin ownerPlugin,CommandExecutor cExe,String name,String description,Method owner,String[] aliases, String usage, String[] permissions, String permMessage,Sender sender) {
			super(name,"Description: " + name,usage,Arrays.asList(aliases));
			permissionMessage = permMessage;
			this.permission = permissions;
			this.s = sender;
			this.owner = owner;
			this.ownerPlugin = ownerPlugin;
			this.cExe = cExe;
		}

		@Override
		public Plugin getPlugin() {
			return ownerPlugin;
		}

		@Override
		public boolean execute(CommandSender sender, String commandLabel, String[] args) {
			try{
				if(!isValidExecutor(sender)){sender.sendMessage("�c�lDu bist kein �3" + s.getSenderClass().getSimpleName());return false;}
				if(permission.length != 0 && !hasAnyPerm(sender)){sender.sendMessage(permissionMessage);return false;}
				return (boolean)owner.invoke(cExe, sender,this,commandLabel,args);
			}catch(Exception e){
				e.printStackTrace();
				return false;
			}
		}
		
		private boolean hasAnyPerm(CommandSender cs){
			boolean hasPerm = false;
	        for (String perm : permission) {
	        	if (cs.hasPermission(perm) || cs.isOp() || cs instanceof ConsoleCommandSender)hasPerm = true;
	        }
	        return hasPerm;
		}
		
		private boolean isValidExecutor(CommandSender cs){
			return (cs instanceof Player && s == Sender.PLAYER) || (cs instanceof ConsoleCommandSender && s == Sender.CONSOLE) || s == Sender.EVERYONE;
		}
	}
}
