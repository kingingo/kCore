package eu.epicpvp.kcore.TreasureChest.NEW;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Language.Language;

public class CommandTreasureChest implements CommandExecutor{
	
	private TreasureChestManager treasureChest;
	
	public CommandTreasureChest(TreasureChestManager treasureChest){
		this.treasureChest=treasureChest;
	}
	
	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "tc", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		Player player = (Player)sender;
		
		if(player.isOp()){
			if(args.length==0){
				
			}else{
				if(args[0].equalsIgnoreCase("saveTemplate")){
					if(args.length==2){
						treasureChest.addBuilding(player, args[1]);
						player.sendMessage(Language.getText(player, "PREFIX")+"§aDein Template §e"+args[1]+"§a wurde gespeichert!");
					}else{
						player.sendMessage(Language.getText(player, "PREFIX")+"/tc saveTemplate [Name]");
					}
				}else if(args[0].equalsIgnoreCase("createChest")){
					if(args.length==3){
						treasureChest.addTreasureChest(player.getItemInHand(), args[1],  args[2]);
						player.sendMessage(Language.getText(player, "PREFIX")+"§aDie Treasure Chest "+args[2]+" mit dem Template "+args[1]+" wurde erstellt.");
					}else{
						player.sendMessage(Language.getText(player, "PREFIX")+"/tc createChest [Template] [Chest]");
					}
				}else if(args[0].equalsIgnoreCase("addItem")){
					if(args.length==3){
						treasureChest.getChest(args[1]).addItem(player.getItemInHand(), Integer.valueOf(args[2]));
						player.sendMessage(Language.getText(player, "PREFIX")+"§aDas Item wurde zu der Chest hinzugef§gt!");
					}else{
						player.sendMessage(Language.getText(player, "PREFIX")+"/tc addItem [Chest] [Nenner]");
					}
				}else if(args[0].equalsIgnoreCase("start")){
					if(args.length==2){
						TreasureChest c = treasureChest.getChest(args[1]);
						
						if(c!=null){
							c.start(player);
							player.sendMessage("C != NULL");
						}else{
							player.sendMessage("C == NULL");
						}
					}else{
						player.sendMessage(Language.getText(player, "PREFIX")+"/tc start [Name]");
					}
				}
			}
		}
		return false;
	}

}
