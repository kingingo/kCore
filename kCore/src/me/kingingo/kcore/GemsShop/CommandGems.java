package me.kingingo.kcore.GemsShop;

import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Inventory.Inventory.InventoryCopy;
import me.kingingo.kcore.Inventory.Item.Buttons.ButtonOpenInventoryCopy;
import me.kingingo.kcore.Language.Language;
import me.kingingo.kcore.Permission.kPermission;
import me.kingingo.kcore.Util.UtilItem;
import me.kingingo.kcore.Util.UtilNumber;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CommandGems implements CommandExecutor{

	private GemsShop shop;
	
	public CommandGems(GemsShop shop){
		this.shop=shop;
	}
	
	@me.kingingo.kcore.Command.CommandHandler.Command(command = "gems",alias={"gemsshop"}, sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		Player player=(Player)sender;
		
		if(args.length==0){
			player.openInventory(shop.getBase().getMain());
		}else{
			if(player.hasPermission(kPermission.ALL_PERMISSION.getPermissionToString())){
				if(args[0].equalsIgnoreCase("addc")){
					if(args.length>=2){
						player.sendMessage(Language.getText(player, "PREFIX")+shop.addCategory(UtilNumber.toInt(args[1]), args[2], player.getItemInHand()));
					}else{
						player.sendMessage(Language.getText(player, "PREFIX")+"/gems addc [SLOT] [PageName]");
					}
				}else if(args[0].equalsIgnoreCase("delc")){
					if(args.length>=1){
						shop.delCategory(UtilNumber.toInt(args[1]));
						player.sendMessage(Language.getText(player, "PREFIX")+"§cCategory entfernt!");
					}else{
						player.sendMessage(Language.getText(player, "PREFIX")+"/gems delc [SLOT]");
					}
				}else if(args[0].equalsIgnoreCase("getc")){
					for(int i = 9; i < 35; i++){
						if(shop.getBase().getMain().getItem(i)!=null){
							player.sendMessage(Language.getText(player, "PREFIX")+"ID: "+i+" ITEM:"+shop.getBase().getMain().getItem(i).getItemMeta().getDisplayName());
						}
					}
				}else if(args[0].equalsIgnoreCase("addp")){
					if(args.length>=4){
						ItemStack hand = player.getItemInHand();
						player.sendMessage(Language.getText(player, "PREFIX")+shop.add(UtilNumber.toInt(args[1]), UtilNumber.toInt(args[2]), hand, UtilNumber.toInt(args[3]), args[4]));
					}else{
						player.sendMessage(Language.getText(player, "PREFIX")+"/gems addp [CSLOT] [SLOT] [GEMS] [PERM]");
					}
				}else if(args[0].equalsIgnoreCase("addi")){
					if(args.length>=3){
						ItemStack hand = player.getItemInHand();
						ItemStack[] reward = player.getInventory().getContents();
						
						for(int i = 0; i<reward.length; i++){
							if(UtilItem.ItemNameEquals(reward[i], hand))reward[i]=null;
						}
						
						player.sendMessage(Language.getText(player, "PREFIX")+shop.add(UtilNumber.toInt(args[1]), UtilNumber.toInt(args[2]), hand, UtilNumber.toInt(args[3]), reward));
					}else{
						player.sendMessage(Language.getText(player, "PREFIX")+"/gems addi [CSLOT] [SLOT] [GEMS]");
					}
				}else if(args[0].equalsIgnoreCase("del")){
					if(args.length>=1){
						shop.del(UtilNumber.toInt(args[1]), UtilNumber.toInt(args[2]));
						player.sendMessage(Language.getText(player, "PREFIX")+"§cItem entfernt!");
					}else{
						player.sendMessage(Language.getText(player, "PREFIX")+"/gems del [CSLOT] [SLOT]");
					}
				}else if(args[0].equalsIgnoreCase("geti")){
					if(args.length>=2){
						InventoryCopy page = ((ButtonOpenInventoryCopy)shop.getBase().getMain().getButton(UtilNumber.toInt(args[1]))).getInventorySale();
						player.setItemInHand(page.getItem(UtilNumber.toInt(args[2])));
					}else{
						player.sendMessage(Language.getText(player, "PREFIX")+"/gems geti [CSLOT] [SLOT]");
					}
				}else if(args[0].equalsIgnoreCase("get")){
					if(args.length>=1){
						InventoryCopy page = ((ButtonOpenInventoryCopy)shop.getBase().getMain().getButton(UtilNumber.toInt(args[1]))).getInventorySale();
						
						for(int i = 9; i < 45; i++){
							if(page.getItem(i)!=null){
								player.sendMessage(Language.getText(player, "PREFIX")+"ID: "+i+" ITEM:"+page.getItem(i).getItemMeta().getDisplayName());
							}
						}
						
					}else{
						player.sendMessage(Language.getText(player, "PREFIX")+"/gems get [CSLOT]");
					}
				}else if(args[0].equalsIgnoreCase("reload")){
					if(shop.getBase().getMain()!=null)shop.getBase().getMain().remove();
					shop.getBase().setMain(null);
					
					for(int i = 0; i<shop.getBase().getPages().size(); i++){
						shop.getBase().getPages().get(i).remove();
					}
					shop.getBase().getPages().clear();
					
					shop.load();
					player.sendMessage(Language.getText(player, "PREFIX")+"§aReloaded");
				}else if(args[0].equalsIgnoreCase("setloc")){
					shop.setCreature(player.getLocation());
					player.sendMessage(Language.getText(player, "PREFIX")+"§aLocation saved!");
				}
			}else{
				player.openInventory(shop.getBase().getMain());
			}
		}
		return false;
	}

}
