package eu.epicpvp.kcore.Command.Commands;

import java.util.HashMap;
import java.util.Locale;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.Potion;
import org.bukkit.potion.Potion.Tier;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Permission.Permission;
import eu.epicpvp.kcore.Permission.PermissionManager;
import eu.epicpvp.kcore.Permission.PermissionType;
import eu.epicpvp.kcore.Translation.TranslationHandler;
import eu.epicpvp.kcore.Util.UtilTime;

public class CommandPotion implements CommandExecutor{
	
	private Player player;
	private String s;
	private Long l;
	private String potions;
	private HashMap<PotionEffectType,Potion> types;
	private PermissionManager manager;
	
	public CommandPotion(PermissionManager manager){
		this.manager=manager;
		this.types=new HashMap<>();
		this.types.put(PotionEffectType.FIRE_RESISTANCE, new Potion(PotionType.FIRE_RESISTANCE, Tier.TWO, true));
		this.types.put(PotionEffectType.HEAL, new Potion(PotionType.INSTANT_HEAL, Tier.TWO, true));
		this.types.put(PotionEffectType.HARM, new Potion(PotionType.INSTANT_DAMAGE, Tier.TWO, true));
		this.types.put(PotionEffectType.INVISIBILITY, new Potion(PotionType.INVISIBILITY, Tier.TWO, true));
		this.types.put(PotionEffectType.JUMP, new Potion(PotionType.JUMP, Tier.TWO, true));
		this.types.put(PotionEffectType.POISON, new Potion(PotionType.POISON, Tier.TWO, true));
		this.types.put(PotionEffectType.REGENERATION, new Potion(PotionType.REGEN, Tier.TWO, true));
		this.types.put(PotionEffectType.SPEED, new Potion(PotionType.SPEED, Tier.TWO, true));
		this.types.put(PotionEffectType.DAMAGE_RESISTANCE, new Potion(PotionType.STRENGTH, Tier.TWO, true));
		this.types.put(PotionEffectType.SLOW, new Potion(PotionType.SLOWNESS, Tier.TWO, true));
		this.types.put(PotionEffectType.NIGHT_VISION, new Potion(PotionType.NIGHT_VISION, Tier.TWO, true));
		this.types.put(PotionEffectType.WEAKNESS, new Potion(PotionType.WEAKNESS, Tier.TWO, true));
		this.potions="";
		for(PotionEffectType type : types.keySet()){
			this.potions+=type.getName()+",";
		}
		
		this.potions=potions.substring(0, potions.length()-1);
	}
	
	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "potion", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		player = (Player)sender;
		if(player.hasPermission(PermissionType.POTION.getPermissionToString())){
			if(args.length==0){
				player.sendMessage(TranslationHandler.getText(player, "PREFIX")+"/potion [potion]");
				player.sendMessage(TranslationHandler.getText(player, "PREFIX")+potions);
			}else{
				s=UtilTime.getTimeManager().check(cmd.getName(), player);
				if(s!=null){
					player.sendMessage(TranslationHandler.getText(player, "PREFIX")+TranslationHandler.getText(player, "USE_BEFEHL_TIME",s));
				}else{
					PotionEffectType mob = PotionEffectType.getByName(args[0]);
					
					if(mob==null||!types.containsKey(mob)){
						player.sendMessage(TranslationHandler.getText(player, "PREFIX")+TranslationHandler.getText(player, "POTION_TYPE_NOT_FOUND"));
						return false;
					}
					if(!player.hasPermission(PermissionType.POTION.getPermissionToString()+"."+mob.getName().toLowerCase(Locale.ENGLISH))){
						if(!player.hasPermission(PermissionType.POTION_ALL.getPermissionToString())){
							player.sendMessage(TranslationHandler.getText(player, "PREFIX")+TranslationHandler.getText(player, "NO_PERMISSION"));
							return false;
						}
					}
					
					int a = 1;
					
					for(Permission perm : manager.getPermissionPlayer(player).getPermissions()){
						if(perm.getPermission().contains(PermissionType.POTION_AMOUNT.getPermissionToString()+".")){
							a = Integer.valueOf(perm.getPermission().substring((PermissionType.POTION_AMOUNT.getPermissionToString()+".").length(), perm.getPermission().length() ));
							break;
						}
					}

					l=UtilTime.getTimeManager().hasPermission(player, cmd.getName());
					if( l!=0 ){
						UtilTime.getTimeManager().add(cmd.getName(), player, l);
					}
					player.getInventory().addItem( this.types.get(mob).toItemStack(a) );
					player.sendMessage(TranslationHandler.getText(player, "PREFIX")+TranslationHandler.getText(player, "POTION_GOT"));
				}
			}
		}
		return false;
	}

}
