package eu.epicpvp.kcore.Command.Commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Permission.PermissionType;
import eu.epicpvp.kcore.Translation.TranslationHandler;

public class CommandRemoveEnchantment implements CommandExecutor{

	private final String enchantments;
	
	public CommandRemoveEnchantment() {
		StringBuilder sb = new StringBuilder();
		for(Enchantment ench : Enchantment.values()) {
			sb.append(ench.getName()).append(", ");
		}
		this.enchantments = sb.substring(0, sb.length()-2);
	}
	
	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "removeEnchantment",alias={"re"}, sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		Player player = (Player) sender;
		
		if(player.hasPermission(PermissionType.REMOVE_ENCHANTMENT.getPermissionToString())){
			if(args.length==0){
				player.sendMessage(TranslationHandler.getText(player, "PREFIX")+"/removeEnchantment [ENCHANTMENT]");
				player.sendMessage(TranslationHandler.getText(player, "PREFIX")+enchantments);
			}else{
				Enchantment ench = Enchantment.getByName(args[0]);
				
				if(ench==null){
					player.sendMessage(TranslationHandler.getText(player, "PREFIX")+TranslationHandler.getText(player, "enchantmentNotFound"));
					return false;
				}
				
				if(player.getItemInHand()==null|| player.getItemInHand().getType()==Material.AIR){
					player.sendMessage(TranslationHandler.getText(player, "PREFIX")+TranslationHandler.getText(player, "HEAD_ITEM_EQUAL_NULL"));
					return false;
				}
				
				if(player.getItemInHand().getEnchantments().containsKey(ench)){
					player.getItemInHand().removeEnchantment(ench);
					player.sendMessage(TranslationHandler.getText(player, "PREFIX")+TranslationHandler.getText(player, "ENCHANTMENT_REMOVED"));
				}else{
					player.sendMessage(TranslationHandler.getText(player, "PREFIX")+TranslationHandler.getText(player, "ENCHANTMENT_NOT_FOUND_ITEM"));
				}
			}
		}
		return false;
	}

}
