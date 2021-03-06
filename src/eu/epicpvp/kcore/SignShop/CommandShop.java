package eu.epicpvp.kcore.SignShop;

import java.util.Set;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.java.JavaPlugin;

import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Listener.kListener;
import eu.epicpvp.kcore.Permission.PermissionType;
import eu.epicpvp.kcore.SignShop.Events.SignShopUseEvent;
import eu.epicpvp.kcore.Translation.TranslationHandler;

public class CommandShop extends kListener implements CommandExecutor{
	
	public CommandShop(JavaPlugin plugin) {
		super(plugin, "CommandShop");
	}

	private Player player;
	
	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "sshop", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		player = (Player)sender;
		if(player.hasPermission(PermissionType.ALL_PERMISSION.getPermissionToString())){
			if(args.length==0){
				player.sendMessage(TranslationHandler.getText(player, "PREFIX")+"/sshop sale");
				player.sendMessage(TranslationHandler.getText(player, "PREFIX")+"/sshop buy");
				player.sendMessage(TranslationHandler.getText(player, "PREFIX")+"/sshop all");
			}else{
				if(args[0].equalsIgnoreCase("sale")){
					Block b = player.getTargetBlock((Set<Material>) null, 100);
					
					if(b.getState() instanceof Sign){
						Sign s = (Sign)b.getState();
						s.setLine(0, "§b[Shop-Sale]");
						s.update();
					}
				}else if(args[0].equalsIgnoreCase("buy")){
					Block b = player.getTargetBlock((Set<Material>) null, 100);
					
					if(b.getState() instanceof Sign){
						Sign s = (Sign)b.getState();
						s.setLine(0, "§b[Shop-Buy]");
						s.update();
					}
				}else if(args[0].equalsIgnoreCase("all")){
					Block b = player.getTargetBlock((Set<Material>) null, 100);
					
					if(b.getState() instanceof Sign){
						Sign s = (Sign)b.getState();
						s.setLine(0, "§b[Shop]");
						s.update();
					}
				}
			}
		}
		return false;
	}
	
	@EventHandler
	public void Sign(SignShopUseEvent ev){
		if(ev.getAction()==SignShopAction.BUY){
			if(ev.getSign().getLine(0).equalsIgnoreCase("§b[Shop-Sale]")){
				ev.setCancelled(true);
			}
		}else if(ev.getAction()==SignShopAction.SALE){
			if(ev.getSign().getLine(0).equalsIgnoreCase("§b[Shop-Buy]")){
				ev.setCancelled(true);
			}
		}
	}

}
