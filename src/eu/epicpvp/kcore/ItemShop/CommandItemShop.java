package eu.epicpvp.kcore.ItemShop;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import dev.wolveringer.bukkit.permissions.PermissionType;
import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Enum.Zeichen;
import eu.epicpvp.kcore.Translation.TranslationHandler;
import eu.epicpvp.kcore.Util.UtilItem;
import eu.epicpvp.kcore.Util.UtilNumber;

public class CommandItemShop implements CommandExecutor{

	private ItemShop shop;
	
	public CommandItemShop(ItemShop shop){
		this.shop=shop;
	}
	
	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "shop",alias={"store"}, sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		Player player=(Player)sender;
		
		if(args.length==0){
			player.openInventory(shop.getShop());
		}else{
			if(player.hasPermission(PermissionType.ALL_PERMISSION.getPermissionToString())){
				if(args[0].equalsIgnoreCase("addc")){
					if(args.length>=2){
						player.sendMessage(TranslationHandler.getText(player, "PREFIX")+shop.addCategory(UtilNumber.toInt(args[1]), args[2], UtilItem.RenameItem(player.getItemInHand(), "§a"+args[2].replaceAll("_", " "))));
					}else{
						player.sendMessage(TranslationHandler.getText(player, "PREFIX")+"/shop addc [SLOT] [PageName]");
					}
				}else if(args[0].equalsIgnoreCase("delc")){
					if(args.length==2){
						shop.delCategory(UtilNumber.toInt(args[1]));
						player.sendMessage(TranslationHandler.getText(player, "PREFIX")+"§cCategory entfernt!");
					}else{
						player.sendMessage(TranslationHandler.getText(player, "PREFIX")+"/shop delc [CSLOT]");
					}
				}else if(args[0].equalsIgnoreCase("getc")){
					for(int i = 9; i < 35; i++){
						if(shop.getShop().getItem(i)!=null){
							player.sendMessage(TranslationHandler.getText(player, "PREFIX")+"ID: "+i+" ITEM:"+shop.getShop().getItem(i).getItemMeta().getDisplayName());
						}
					}
				}else if(args[0].equalsIgnoreCase("addi")){
					if(args.length==6){
						ItemStack hand = player.getItemInHand();
						hand=UtilItem.SetDescriptions(hand, new String[]{"§7"+Zeichen.DOUBLE_ARROWS_R.getIcon()+" §cBuy","§7-","§e1 "+Zeichen.DOUBLE_ARROWS_R.getIcon()+" §a"+UtilNumber.toInt(args[3])+" Epics","§e10 "+Zeichen.DOUBLE_ARROWS_R.getIcon()+" §a"+(UtilNumber.toInt(args[3])*10)+" Epics","§e64 "+Zeichen.DOUBLE_ARROWS_R.getIcon()+" §a"+(UtilNumber.toInt(args[3])*64)+" Epics","§7----------","§7"+Zeichen.DOUBLE_ARROWS_R.getIcon()+" §cSell","§7-","§e1 "+Zeichen.DOUBLE_ARROWS_R.getIcon()+" §a"+UtilNumber.toInt(args[4])+" Epics","§e10 "+Zeichen.DOUBLE_ARROWS_R.getIcon()+" §a"+(UtilNumber.toInt(args[4])*10)+" Epics","§e64 "+Zeichen.DOUBLE_ARROWS_R.getIcon()+" §a"+(UtilNumber.toInt(args[4])*64)+" Epics"});
						player.sendMessage(TranslationHandler.getText(player, "PREFIX")+shop.add(UtilNumber.toInt(args[1]), UtilNumber.toInt(args[2]), hand, UtilNumber.toInt(args[3]),UtilNumber.toInt(args[4]),UtilNumber.toInt(args[5])));
					}else if(args.length==5){
						ItemStack hand = player.getItemInHand();
						hand=UtilItem.SetDescriptions(hand, new String[]{"§7§ §cBuy","§7-","§e1 § §a"+UtilNumber.toInt(args[2])+" Epics","§e10 § §a"+(UtilNumber.toInt(args[2])*10)+" Epics","§e64 § §a"+(UtilNumber.toInt(args[2])*64)+" Epics","§7----------","§7§ §cSell","§7-","§e1 § §a"+UtilNumber.toInt(args[3])+" Epics","§e10 § §a"+(UtilNumber.toInt(args[3])*10)+" Epics","§e64 § §a"+(UtilNumber.toInt(args[3])*64)+" Epics"});
						player.sendMessage(TranslationHandler.getText(player, "PREFIX")+shop.add(UtilNumber.toInt(args[1]), -1, hand, UtilNumber.toInt(args[2]),UtilNumber.toInt(args[3]),UtilNumber.toInt(args[4])));
					}else{
						player.sendMessage(TranslationHandler.getText(player, "PREFIX")+"/shop addi [CSLOT] [SLOT] [BUY] [SELL] [PAGE]");
					}
				}else if(args[0].equalsIgnoreCase("deli")){
					if(args.length>=1){
						shop.del(UtilNumber.toInt(args[1]), UtilNumber.toInt(args[2]), UtilNumber.toInt(args[3]));
						player.sendMessage(TranslationHandler.getText(player, "PREFIX")+"§cItem entfernt!");
					}else{
						player.sendMessage(TranslationHandler.getText(player, "PREFIX")+"/shop deli [CSLOT] [SLOT] [PAGE]");
					}
				}else if(args[0].equalsIgnoreCase("reload")){
					if(shop.getShop()!=null)shop.getShop().remove();
					shop.setShop(null);
					shop.load();
					player.sendMessage(TranslationHandler.getText(player, "PREFIX")+"§aReloaded");
				}else if(args[0].equalsIgnoreCase("setloc")){
					if(shop.getListener()!=null&&shop.getListener().getEntity()!=null)shop.getListener().getEntity().remove();
					
					shop.setCreature(player.getLocation());
					player.sendMessage(TranslationHandler.getText(player, "PREFIX")+"§aLocation saved!");
				}else if(args[0].equalsIgnoreCase("move")){
					player.openInventory(shop.getMove());
				}else if(args[0].equalsIgnoreCase("edit")){
					player.openInventory(shop.getEdit());
				}else if(args[0].equalsIgnoreCase("fix")){
					shop.fix();
					player.sendMessage(TranslationHandler.getText(player, "PREFIX")+"§aFix all..");
				}
			}else{
				player.openInventory(shop.getShop());
			}
		}
		return false;
	}

}
