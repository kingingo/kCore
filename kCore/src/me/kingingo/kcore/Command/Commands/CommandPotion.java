package me.kingingo.kcore.Command.Commands;

import java.util.Locale;

import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Enum.Mob;
import me.kingingo.kcore.Language.Language;
import me.kingingo.kcore.Permission.kPermission;
import me.kingingo.kcore.Util.UtilInv;
import me.kingingo.kcore.Util.UtilItem;
import me.kingingo.kcore.Util.UtilTime;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

public class CommandPotion implements CommandExecutor{
	
	private Player player;
	private String s;
	private Long l;
	private String potions;
	
	public CommandPotion(){
		this.potions=Language.getText(player, "PREFIX")+"";
		for(PotionType type : PotionType.values())potions+=type.getEffectType().getName()+",";
		this.potions=potions.substring(0, potions.length()-1);
	}
	
	@me.kingingo.kcore.Command.CommandHandler.Command(command = "spawnmob", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		player = (Player)sender;
		if(player.hasPermission(kPermission.POTION.getPermissionToString())){
			if(args.length==0){
				player.sendMessage(Language.getText(player, "PREFIX")+"/potion [potion]");
				player.sendMessage(potions);
			}else{
				s=UtilTime.getTimeManager().check(cmd.getName(), player);
				if(s!=null){
					player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "USE_BEFEHL_TIME",s));
				}else{
					PotionType mob = PotionType.valueOf(args[0]);
					
					if(mob==null){
						player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "POTION_TYPE_NOT_FOUND"));
						return false;
					}
					if(!player.hasPermission(kPermission.POTION.getPermissionToString()+"."+mob.getEffectType().getName().toLowerCase(Locale.ENGLISH))){
						if(!player.hasPermission(kPermission.POTION_ALL.getPermissionToString())){
							player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "NO_PERMISSION"));
							return false;
						}
					}
					
					int a = 0;
					
					for(int i = 64; i==1; i--){
						if(player.hasPermission(kPermission.POTION_AMOUNT.getPermissionToString()+"."+i)){
							a=i;
							break;
						}
					}

					l=UtilTime.getTimeManager().hasPermission(player, cmd.getName());
					if( l!=0 ){
						UtilTime.getTimeManager().add(cmd.getName(), player, l);
					}
					player.getInventory().addItem( new ItemStack(373,a,(byte)mob.getEffectType().getId()) );
					player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "POTION_GOT"));
				}
			}
		}
		return false;
	}

}
