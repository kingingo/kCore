package me.kingingo.kcore.Command.Commands;

import java.io.File;
import java.util.HashMap;

import lombok.Getter;
import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Enum.Text;
import me.kingingo.kcore.Permission.kPermission;
import me.kingingo.kcore.UserDataConfig.UserDataConfig;
import me.kingingo.kcore.Util.UtilTime;
import me.kingingo.kcore.kConfig.kConfig;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CommandKit implements CommandExecutor{

	private Player player;
	@Getter
	private kConfig config;
	private UserDataConfig userData;
	private String kit;
	@Getter
	private HashMap<String,ItemStack[]> kits = new HashMap<>();
	@Getter
	private HashMap<String,Long> kits_delay = new HashMap<>();
	
	public CommandKit(UserDataConfig userData){
		this.config=new kConfig(new File("plugins"+File.separator+userData.getInstance().getPlugin(userData.getInstance().getClass()).getName()+File.separator+"kits.yml"));
		this.userData=userData;
		for(String kit : config.getPathList("kits").keySet()){
			kits.put(kit, config.getInventory("kits."+kit+".Inventory").getContents());
			if(config.isSet("kits."+kit+".Delay"))kits_delay.put(kit, config.getLong("kits."+kit+".Delay"));
		}
	}
	
	@me.kingingo.kcore.Command.CommandHandler.Command(command = "kit", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		player=(Player)sender;
		
		if(player.hasPermission(kPermission.KIT.getPermissionToString())){
			if(args.length==0){
				player.sendMessage(Text.PREFIX.getText()+"/kit [Name]");
				String kits="";
				for(String kit : this.kits.keySet())if(player.hasPermission(kPermission.KIT.getPermissionToString()+"."+kit))kits+=kit+",";
				player.sendMessage("Kits: "+(kits.equalsIgnoreCase("") ? "Du hast keine Kits" : kits.substring(0, kits.length()-1)));
			}else{
				kit=args[0].toLowerCase();
				if(kits.containsKey(kit)){
					if(player.hasPermission(kPermission.KIT.getPermissionToString()+"."+kit)){
						this.config=userData.getConfig(player);
						
						if(kits_delay.containsKey(kit)&&this.config.isSet("timestamps.kits."+kit)){
							if(this.config.getLong("timestamps.kits."+kit) >= System.currentTimeMillis()){
								player.sendMessage(Text.PREFIX.getText()+Text.KIT_DELAY.getText(UtilTime.formatMili(this.config.getLong("timestamps.kits."+kit)-System.currentTimeMillis())));
								return false;
							}
						}
						
						for(ItemStack i : kits.get(kit)){
							if(i!=null&&i.getType()!=Material.AIR){
								player.getInventory().addItem(i.clone());
							}
						}
						player.sendMessage(Text.PREFIX.getText()+Text.KIT_USE.getText(kit));
						
						if(kits_delay.containsKey(kit)){
							this.config.set("timestamps.kits."+kit, kits_delay.get(kit)+System.currentTimeMillis());
						}
					}
				}else{
					player.sendMessage(Text.PREFIX.getText()+Text.KIT_EXIST.getText());
				}
			}
		}	
		return false;
	}

}
