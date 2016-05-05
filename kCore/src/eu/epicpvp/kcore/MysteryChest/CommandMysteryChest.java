package eu.epicpvp.kcore.MysteryChest;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Translation.TranslationHandler;
import eu.epicpvp.kcore.Util.UtilNumber;

public class CommandMysteryChest implements CommandExecutor{
	
	private MysteryChestManager chestManager;
	
	public CommandMysteryChest(MysteryChestManager chestManager){
		this.chestManager=chestManager;
	}
	
	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "MysteryChest",alias={"mc","mchest"}, sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		Player player = (Player)sender;
		
		if(player.isOp()){
			if(args.length==0){
				player.sendMessage(TranslationHandler.getText(player, "PREFIX")+"/MysteryChest saveTemplate [Chest]");
				player.sendMessage(TranslationHandler.getText(player, "PREFIX")+"/MysteryChest createChest [Chest] [Template]");
				player.sendMessage(TranslationHandler.getText(player, "PREFIX")+"/MysteryChest addItem [Chest] [Chance] [CMD]");
				player.sendMessage(TranslationHandler.getText(player, "PREFIX")+"/MysteryChest start [Chest]");
			}else{
				if(args[0].equalsIgnoreCase("saveTemplate")){
					if(args.length==2){
						chestManager.addBuilding(player, args[1]);
						player.sendMessage(TranslationHandler.getText(player, "PREFIX")+"§aDein Template §e"+args[1]+"§a wurde gespeichert!");
					}else{
						player.sendMessage(TranslationHandler.getText(player, "PREFIX")+"/MysteryChest saveTemplate [Name]");
					}
				}else if(args[0].equalsIgnoreCase("createChest")){
					if(args.length==3){
						chestManager.addChest(player.getItemInHand(),args[2], args[1]);
						player.sendMessage(TranslationHandler.getText(player, "PREFIX")+"§aDie Treasure Chest "+args[1]+" mit dem Template "+args[2]+" wurde erstellt.");
					}else{
						player.sendMessage(TranslationHandler.getText(player, "PREFIX")+"/MysteryChest createChest [Chest] [Template]");
					}
				}else if(args[0].equalsIgnoreCase("addItem")){
					if(args.length==3){
						chestManager.getChest(args[1]).addItem(player.getItemInHand(), UtilNumber.toDouble(args[2]),args[3]);
						player.sendMessage(TranslationHandler.getText(player, "PREFIX")+"§aDas Item wurde zu der Chest hinzugefügt!");
					}else{
						player.sendMessage(TranslationHandler.getText(player, "PREFIX")+"/MysteryChest addItem [Chest] [Chance] [CMD]");
					}
				}else if(args[0].equalsIgnoreCase("start")){
					if(args.length==2){
						MysteryChest c = chestManager.getChest(args[1]);
						
						if(c!=null){
							c.start(player);
						}
					}else{
						player.sendMessage(TranslationHandler.getText(player, "PREFIX")+"/MysteryChest start [Name]");
					}
				}
			}
		}
		return false;
	}

}
