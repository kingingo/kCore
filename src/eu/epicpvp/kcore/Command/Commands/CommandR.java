package eu.epicpvp.kcore.Command.Commands;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Command.Commands.Events.PlayerMsgSendEvent;
import eu.epicpvp.kcore.Listener.kListener;
import eu.epicpvp.kcore.Translation.TranslationHandler;

public class CommandR extends kListener implements CommandExecutor{

	private HashMap<Player,Player> list = new HashMap<>();
	
	public CommandR(JavaPlugin instance) {
		super(instance, "CommandR");
	}
	
	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "r", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		Player player = (Player) sender;
		if(args.length==0){
			player.sendMessage(TranslationHandler.getText(player, "PREFIX")+"/r [Text]");
		}else{
			if(list.containsKey(player)){
				if(list.get(player).isOnline()){
					Player target = list.get(player);
					StringBuilder sb = new StringBuilder();
					for (int i = 0; i < args.length; i++) {
						sb.append(args[i]);
						sb.append(" ");
					}
					sb.setLength(sb.length() - 1);
					String msg = sb.toString();
					Bukkit.getPluginManager().callEvent(new PlayerMsgSendEvent(player, target, msg,false));
					target.sendMessage(TranslationHandler.getText(player, "PREFIX")+ player.getName()+"->"+TranslationHandler.getText(target, "ME")+":§b "+ msg);
					player.sendMessage(TranslationHandler.getText(player, "PREFIX")+TranslationHandler.getText(player, "ME")+"->"+ target.getName()+":§b "+ msg);
				}else{
					player.sendMessage(TranslationHandler.getText(player, "PREFIX")+TranslationHandler.getText(player, "PLAYER_IS_OFFLINE",list.get(player).getName()));
				}
			}else{
				player.sendMessage(TranslationHandler.getText(player, "PREFIX")+TranslationHandler.getText(player, "NO_ANSWER_PARTNER"));
			}
		}
		return false;
	}
	
	@EventHandler
	public void Msg(PlayerMsgSendEvent ev){
		if(!ev.isB())return;
		if(list.containsKey(ev.getPlayer()))list.remove(ev.getPlayer());
		if(list.containsKey(ev.getTarget()))list.remove(ev.getTarget());
		list.put(ev.getPlayer(), ev.getTarget());
		list.put(ev.getTarget(), ev.getPlayer());
	}
	
	@EventHandler
	public void Quit(PlayerQuitEvent ev){
		list.remove(ev.getPlayer());
	}

	
	
}
