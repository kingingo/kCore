package me.kingingo.kcore.Command.Admin;

import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Language.Language;
import me.kingingo.kcore.Packet.PacketManager;
import me.kingingo.kcore.Permission.kPermission;
import me.kingingo.kcore.Util.Gems;
import me.kingingo.kcore.Util.UtilInteger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandGiveGems implements CommandExecutor{
	
	private Gems gems;
	private PacketManager packetManager;
	
	public CommandGiveGems(Gems gems,PacketManager packetManager){
		this.gems=gems;
		this.packetManager=packetManager;
	}
	
	@me.kingingo.kcore.Command.CommandHandler.Command(command = "givegems", sender = Sender.EVERYONE)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		if(sender instanceof Player){
			Player player = (Player)sender;
			if(player.hasPermission(kPermission.ALL_PERMISSION.getPermissionToString())){
				if(args.length==0){
					player.sendMessage(Language.getText(player, "PREFIX")+"§a/GiveGems [Spieler] [+/- Gems]");
				}else if(args.length >= 2){
					String spieler = args[0];
					int c=UtilInteger.isNumber(args[1]);
					
					if(c==-1)return false;
					gems.giveGems(packetManager, spieler, c);
					player.sendMessage(Language.getText(player, "PREFIX")+(c<0?Language.getText(player, "GEMS_DEL_PLAYER",new String[]{player.getName(),String.valueOf(c)}):Language.getText(player, "GEMS_ADD_PLAYER",new String[]{player.getName(),String.valueOf(c)})));
				}
			}
		}else if(sender instanceof CommandSender){
			if(args.length==0){
				System.out.println("[EpicPvP:] /GiveGems [Spieler] [+/- Gems]");
			}else if(args.length >= 2){
				String spieler = args[0];
				
				int c=UtilInteger.isNumber(args[1]);
				
				if(c==-1)return false;
				gems.giveGems(packetManager, spieler, c);
				System.out.println("[EpicPvP]: Der Spieler "+spieler+" hat die Gems erhalten!");
			}
		}
		return false;
	}
	
}
