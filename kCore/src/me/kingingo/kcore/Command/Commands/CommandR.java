package me.kingingo.kcore.Command.Commands;

import java.util.HashMap;

import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Command.Commands.Events.PlayerMsgSendEvent;
import me.kingingo.kcore.Enum.Text;
import me.kingingo.kcore.Listener.kListener;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandR extends kListener implements CommandExecutor{

	private Player player;
	private Player target;
	private StringBuilder sb;
	private String msg;
	private HashMap<Player,Player> list = new HashMap<>();
	
	public CommandR(JavaPlugin instance) {
		super(instance, "CommandR");
	}
	
	@me.kingingo.kcore.Command.CommandHandler.Command(command = "r", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		player=(Player)sender;
		if(args.length==0){
			player.sendMessage(Text.PREFIX.getText()+"/r [Text]");
		}else{
			if(list.containsKey(player)){
				if(list.get(player).isOnline()){
					target=list.get(player);
					sb = new StringBuilder();
					for (int i = 0; i < args.length; i++) {
						sb.append(args[i]);
						sb.append(" ");
					}
					sb.setLength(sb.length() - 1);
					msg = sb.toString();
					Bukkit.getPluginManager().callEvent(new PlayerMsgSendEvent(player, target, msg,false));
					target.sendMessage(Text.PREFIX.getText()+player.getName()+"->mir:§b "+msg);
					player.sendMessage(Text.PREFIX.getText()+"mir->"+target.getName()+":§b "+msg);
				}else{
					player.sendMessage(Text.PREFIX.getText()+Text.PLAYER_IS_OFFLINE.getText(list.get(player).getName()));
				}
			}else{
				player.sendMessage(Text.PREFIX.getText()+Text.NO_ANSWER_PARTNER.getText());
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
		if(list.containsKey(ev.getPlayer()))list.remove(ev.getPlayer());
	}

	
	
}
