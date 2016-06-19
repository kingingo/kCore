package eu.epicpvp.kcore.GemsShop;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Inventory.Inventory.InventoryCopy;
import eu.epicpvp.kcore.Inventory.Item.Buttons.ButtonOpenInventoryCopy;
import eu.epicpvp.kcore.Permission.PermissionType;
import eu.epicpvp.kcore.Translation.TranslationHandler;
import eu.epicpvp.kcore.Util.UtilItem;
import eu.epicpvp.kcore.Util.UtilNumber;

public class CommandGems implements CommandExecutor{

	private GemsShop shop;
	
	public CommandGems(GemsShop shop){
		this.shop=shop;
	}
	
	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "gems",alias={"gemsshop"}, sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		Player player=(Player)sender;
		
		if(args.length==0){
			shop.openInv(player);
		}else{
			if(player.hasPermission(PermissionType.ALL_PERMISSION.getPermissionToString())){
				if(args[0].equalsIgnoreCase("setSale")){
					if(args.length>=1){
						shop.getConfig().set("Main.Sale", UtilNumber.toDouble(args[1]));
						shop.getConfig().save();
						player.sendMessage(TranslationHandler.getText(player, "PREFIX")+"§a Der Sale wurde auf "+args[1]+"% gesetzt.");
					}else{
						player.sendMessage(TranslationHandler.getText(player, "PREFIX")+"/gems setSale [Rabatt %]");
					}
				}else if(args[0].equalsIgnoreCase("addc")){
					if(args.length>=2){
						player.sendMessage(TranslationHandler.getText(player, "PREFIX")+shop.addCategory(UtilNumber.toInt(args[1]), args[2], player.getItemInHand()));
					}else{
						player.sendMessage(TranslationHandler.getText(player, "PREFIX")+"/gems addc [SLOT] [PageName]");
					}
				}else if(args[0].equalsIgnoreCase("delc")){
					if(args.length>=1){
						shop.delCategory(UtilNumber.toInt(args[1]));
						player.sendMessage(TranslationHandler.getText(player, "PREFIX")+"§cCategory entfernt!");
					}else{
						player.sendMessage(TranslationHandler.getText(player, "PREFIX")+"/gems delc [SLOT]");
					}
				}else if(args[0].equalsIgnoreCase("getc")){
					for(int i = 9; i < 35; i++){
						if(shop.getMain().getItem(i)!=null){
							player.sendMessage(TranslationHandler.getText(player, "PREFIX")+"ID: "+i+" ITEM:"+shop.getMain().getItem(i).getItemMeta().getDisplayName());
						}
					}
				}else if(args[0].equalsIgnoreCase("addp")){
					if(args.length>=4){
						ItemStack hand = player.getItemInHand();
						player.sendMessage(TranslationHandler.getText(player, "PREFIX")+shop.add(UtilNumber.toInt(args[1]), UtilNumber.toInt(args[2]), hand, UtilNumber.toInt(args[3]), args[4]));
					}else{
						player.sendMessage(TranslationHandler.getText(player, "PREFIX")+"/gems addp [CSLOT] [SLOT] [GEMS] [PERM]");
					}
				}else if(args[0].equalsIgnoreCase("addcmd")){
					if(args.length>=3){
						ItemStack hand = player.getItemInHand();
						
						player.sendMessage(TranslationHandler.getText(player, "PREFIX")+shop.addcmd(UtilNumber.toInt(args[1]), UtilNumber.toInt(args[2]), hand, UtilNumber.toInt(args[3]), args[4]));
					}else{
						player.sendMessage(TranslationHandler.getText(player, "PREFIX")+"/gems addcmd [CSLOT] [SLOT] [GEMS] [CMD]");
					}
				}else if(args[0].equalsIgnoreCase("addi")){
					if(args.length>=3){
						ItemStack hand = player.getItemInHand();
						ItemStack[] reward = player.getInventory().getContents();
						
						for(int i = 0; i<reward.length; i++){
							if(UtilItem.ItemNameEquals(reward[i], hand))reward[i]=null;
						}
						
						player.sendMessage(TranslationHandler.getText(player, "PREFIX")+shop.add(UtilNumber.toInt(args[1]), UtilNumber.toInt(args[2]), hand, UtilNumber.toInt(args[3]), reward));
					}else{
						player.sendMessage(TranslationHandler.getText(player, "PREFIX")+"/gems addi [CSLOT] [SLOT] [GEMS]");
					}
				}else if(args[0].equalsIgnoreCase("del")){
					if(args.length>=1){
						shop.del(UtilNumber.toInt(args[1]), UtilNumber.toInt(args[2]));
						player.sendMessage(TranslationHandler.getText(player, "PREFIX")+"§cItem entfernt!");
					}else{
						player.sendMessage(TranslationHandler.getText(player, "PREFIX")+"/gems del [CSLOT] [SLOT]");
					}
				}else if(args[0].equalsIgnoreCase("geti")){
					if(args.length>=2){
						InventoryCopy page = ((ButtonOpenInventoryCopy)shop.getMain().getButton(UtilNumber.toInt(args[1]))).getInventorySale();
						player.setItemInHand(page.getItem(UtilNumber.toInt(args[2])));
					}else{
						player.sendMessage(TranslationHandler.getText(player, "PREFIX")+"/gems geti [CSLOT] [SLOT]");
					}
				}else if(args[0].equalsIgnoreCase("get")){
					if(args.length>=1){
						InventoryCopy page = ((ButtonOpenInventoryCopy)shop.getMain().getButton(UtilNumber.toInt(args[1]))).getInventorySale();
						
						for(int i = 9; i < 45; i++){
							if(page.getItem(i)!=null){
								player.sendMessage(TranslationHandler.getText(player, "PREFIX")+"ID: "+i+" ITEM:"+page.getItem(i).getItemMeta().getDisplayName());
							}
						}
						
					}else{
						player.sendMessage(TranslationHandler.getText(player, "PREFIX")+"/gems get [CSLOT]");
					}
				}else if(args[0].equalsIgnoreCase("reload")){
					shop.getMain().remove();
					shop.setMain(null);
					
					shop.load();
					player.sendMessage(TranslationHandler.getText(player, "PREFIX")+"§aReloaded");
				}else if(args[0].equalsIgnoreCase("setloc")){
					if(shop.getListener()!=null&&shop.getListener().getEntity()!=null)shop.getListener().getEntity().remove();
					
					shop.setCreature(player.getLocation());
					player.sendMessage(TranslationHandler.getText(player, "PREFIX")+"§aLocation saved!");
				}
			}else{
				player.openInventory(shop.getMain());
			}
		}
		return false;
	}

}
