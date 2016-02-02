package me.kingingo.kcore.Command.Commands;

import java.io.File;
import java.util.HashMap;

import lombok.Getter;
import me.kingingo.kcore.Command.CommandHandler;
import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Command.Commands.Events.AddKitEvent;
import me.kingingo.kcore.Command.Commands.Events.DeleteKitEvent;
import me.kingingo.kcore.Command.Commands.Events.ResetKitEvent;
import me.kingingo.kcore.Language.Language;
import me.kingingo.kcore.Permission.kPermission;
import me.kingingo.kcore.UserDataConfig.UserDataConfig;
import me.kingingo.kcore.Util.UtilTime;
import me.kingingo.kcore.kConfig.kConfig;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;

public class CommandKit implements CommandExecutor{

	private Player player;
	@Getter
	private kConfig config;
	private kConfig userconfig;
	@Getter
	private UserDataConfig userData;
	private String kit;
	@Getter
	private HashMap<String,ItemStack[]> kits = new HashMap<>();
	@Getter
	private HashMap<String,Long> kits_delay = new HashMap<>();
	
	public CommandKit(UserDataConfig userData,CommandHandler cmd){
		this.config=new kConfig(new File("plugins"+File.separator+userData.getInstance().getPlugin(userData.getInstance().getClass()).getName()+File.separator+"kits.yml"));
		this.userData=userData;
		for(String kit : config.getPathList("kits").keySet()){
			kits.put(kit, config.getInventory("kits."+kit+".Inventory").getContents());
			if(config.isSet("kits."+kit+".Delay"))kits_delay.put(kit, config.getLong("kits."+kit+".Delay"));
		}
		cmd.register(CommandDelKit.class, new CommandDelKit(config));
		cmd.register(CommandSetKit.class, new CommandSetKit(config));
		cmd.register(CommandResetKit.class, new CommandResetKit());
	}
	
	@EventHandler
	public void reset(ResetKitEvent ev){
		if(getKits().containsKey(ev.getKit())){
			//epicpvp.kit.use.starter
			if(ev.getPlayer().hasPermission(kPermission.KIT.getPermissionToString()+"."+ev.getKit())){
				if(getKits().containsKey(ev.getKit())&&!ev.getPlayer().hasPermission(kPermission.KIT_BYEPASS_DELAY.getPermissionToString())){
					getUserData().getConfig(ev.getPlayer()).set("timestamps.kits."+ev.getKit(),getKits_delay().get(ev.getKit())+System.currentTimeMillis());
					ev.getPlayer().sendMessage(Language.getText(ev.getPlayer(), "PREFIX")+Language.getText(ev.getPlayer(), "KIT_DELAY",UtilTime.formatMili(getUserData().getConfig(ev.getPlayer()).getLong("timestamps.kits."+ev.getKit())-System.currentTimeMillis())));
				}
			}
		}else{
			ev.setCancelled(true);
		}
	}
	
	@EventHandler
	public void added(AddKitEvent ev){
		if(ev.getDelay()!=0)getKits_delay().put(ev.getKit(), ev.getDelay());
		getKits().put(ev.getKit(), ev.getPlayer().getInventory().getContents().clone());
	}
	
	@EventHandler
	public void delete(DeleteKitEvent ev){
		if(getKits().containsKey(ev.getKit())){
			getKits().remove(ev.getKit());
			getKits_delay().remove(ev.getKit());
		}
	}
	
	@me.kingingo.kcore.Command.CommandHandler.Command(command = "kit", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		player=(Player)sender;
		
		if(player.hasPermission(kPermission.KIT.getPermissionToString())){
			if(args.length==0){
				player.sendMessage(Language.getText(player, "PREFIX")+"/kit [Name]");
				String kits="";
				for(String kit : this.kits.keySet())if(player.hasPermission(kPermission.KIT.getPermissionToString()+"."+kit))kits+=kit+",";
				player.sendMessage(Language.getText(player, "PREFIX")+"Kits: "+(kits.equalsIgnoreCase("") ? Language.getText(player, "KITS_EMPTY") : kits.substring(0, kits.length()-1)));
			}else{
				kit=args[0].toLowerCase();
				if(kits.containsKey(kit)){
					//epicpvp.kit.use.starter
					if(player.hasPermission(kPermission.KIT.getPermissionToString()+"."+kit)){
						this.userconfig=userData.getConfig(player);
						
						if(kits_delay.containsKey(kit)&&!player.hasPermission(kPermission.KIT_BYEPASS_DELAY.getPermissionToString())&&this.userconfig.isSet("timestamps.kits."+kit)){
							if(this.userconfig.getLong("timestamps.kits."+kit) >= System.currentTimeMillis()){
								player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "KIT_DELAY",UtilTime.formatMili(this.userconfig.getLong("timestamps.kits."+kit)-System.currentTimeMillis())));
								return false;
							}
						}
						
						for(ItemStack i : kits.get(kit)){
							if(i!=null&&i.getType()!=Material.AIR){
								player.getInventory().addItem(i.clone());
							}
						}
						player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "KIT_USE",kit));
						
						if(kits_delay.containsKey(kit)&&!player.hasPermission(kPermission.KIT_BYEPASS_DELAY.getPermissionToString())){
							this.userconfig.set("timestamps.kits."+kit, kits_delay.get(kit)+System.currentTimeMillis());
						}
					}
				}else{
					player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "KIT_EXIST"));
				}
			}
		}	
		return false;
	}

}
