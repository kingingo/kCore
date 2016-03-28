package eu.epicpvp.kcore.Command.Admin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Language.Language;
import eu.epicpvp.kcore.Permission.PermissionType;
import eu.epicpvp.kcore.Util.UtilNumber;
import net.minecraft.server.v1_8_R3.EntityPlayer;

public class CommandTrackingRange implements CommandExecutor{
	
	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "trackingrange", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
			Player p = (Player)sender;
			if(p.hasPermission(PermissionType.COMMAND_MEM.getPermissionToString())){
				EntityPlayer player = ((CraftPlayer)p).getHandle();
				if(args.length==0){
					p.sendMessage(Language.getText(p, "PREFIX")+" Player-Tracking-Range: "+player.world.spigotConfig.playerTrackingRange);
					p.sendMessage(Language.getText(p, "PREFIX")+" /trackingrange [RANGE/Normale Range ist 48]");
				}else{
					int i = UtilNumber.toInt(args[0]);
					if(i<=0)return false;
					player.world.spigotConfig.playerTrackingRange=i;
					p.sendMessage(Language.getText(p, "PREFIX")+Language.getText(p, "TRACKING_RANGE",i));
				}
			}
		return false;
	}
	
}
