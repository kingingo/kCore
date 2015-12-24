package me.kingingo.kcore.Command.Admin;

import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Language.Language;
import me.kingingo.kcore.Packet.PacketManager;
import me.kingingo.kcore.Permission.kPermission;
import me.kingingo.kcore.Util.Coins;
import me.kingingo.kcore.Util.UtilInteger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandCoins implements CommandExecutor{
	
	private Coins coins;
	private PacketManager packetManager;
	
	public CommandCoins(Coins coins,PacketManager packetManager){
		this.coins=coins;
		this.packetManager=packetManager;
	}
	
	@me.kingingo.kcore.Command.CommandHandler.Command(command = "coins", sender = Sender.EVERYONE)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		if(sender instanceof Player){
			Player player = (Player)sender;
			if(player.hasPermission(kPermission.ALL_PERMISSION.getPermissionToString())){
				if(args.length==0){
					player.sendMessage(Language.getText(player, "PREFIX")+"§a/coins [Spieler] [+/- Coins]");
				}else if(args.length >= 2){
					String spieler = args[0];
					
					int c=UtilInteger.isNumber(args[1]);
					
					if(c==-1)return false;
					coins.giveCoins(packetManager, spieler, c);
					player.sendMessage(Language.getText(player, "PREFIX")+(c<0?Language.getText(player, "COINS_DEL_PLAYER",new String[]{spieler,String.valueOf(c)}):Language.getText(player, "COINS_ADD_PLAYER",new String[]{spieler,String.valueOf(c)})));
				}
			}
		}else{
			if(args.length==0){
				System.out.println("[EpicPvP]"+"/coins [Spieler] [+/- Coins]");
			}else if(args.length >= 2){
				String spieler = args[0];
					
				int c=UtilInteger.isNumber(args[1]);
					
				if(c==-1)return false;
				coins.giveCoins(packetManager, spieler, c);
				System.out.println("[EpicPvP]"+(c<0?"Den Spieler "+spieler+" wurden "+c+" abgezogen!":"Den Spieler "+spieler+" wurden "+c+" hinzugefügt!"));
			}
		}
		return false;
	}

}
