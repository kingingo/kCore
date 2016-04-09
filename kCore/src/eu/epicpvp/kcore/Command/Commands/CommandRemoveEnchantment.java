package eu.epicpvp.kcore.Command.Commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Permission.PermissionType;
import eu.epicpvp.kcore.Translation.TranslationManager;

public class CommandRemoveEnchantment implements CommandExecutor{
	
	private Player player;
	private String enchantments;
	
	public CommandRemoveEnchantment() {
		this.enchantments="";
		for(Enchantment ench : Enchantment.values())enchantments+=ench.getName()+",";
		this.enchantments=enchantments.substring(0, enchantments.length()-1);
	}
	
	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "removeEnchantment",alias={"re"}, sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		player = (Player)sender;
		
		if(player.hasPermission(PermissionType.REMOVE_ENCHANTMENT.getPermissionToString())){
			if(args.length==0){
				player.sendMessage(TranslationManager.getText(player, "PREFIX")+"/removeEnchantment [ENCHANTMENT]");
				player.sendMessage(TranslationManager.getText(player, "PREFIX")+enchantments);
			}else{
				Enchantment ench = Enchantment.getByName(args[0]);
				
				if(ench==null){
					player.sendMessage(TranslationManager.getText(player, "PREFIX")+TranslationManager.getText(player, "enchantmentNotFound"));
					return false;
				}
				
				if(player.getItemInHand()==null||player.getItemInHand().getType()==Material.AIR){
					player.sendMessage(TranslationManager.getText(player, "PREFIX")+TranslationManager.getText(player, "HEAD_ITEM_EQUAL_NULL"));
					return false;
				}
				
				if(player.getItemInHand().getEnchantments().containsKey(ench)){
					player.getItemInHand().removeEnchantment(ench);
					player.sendMessage(TranslationManager.getText(player, "PREFIX")+TranslationManager.getText(player, "ENCHANTMENT_REMOVED"));
				}else{
					player.sendMessage(TranslationManager.getText(player, "PREFIX")+TranslationManager.getText(player, "ENCHANTMENT_NOT_FOUND_ITEM"));
				}
			}
		}
		return false;
	}

}
