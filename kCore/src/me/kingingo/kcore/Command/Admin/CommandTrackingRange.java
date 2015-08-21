package me.kingingo.kcore.Command.Admin;

import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Language.Language;
import me.kingingo.kcore.Permission.kPermission;
import me.kingingo.kcore.Util.UtilNumber;
import net.minecraft.server.v1_8_R3.EntityPlayer;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class CommandTrackingRange implements CommandExecutor{
	
	@me.kingingo.kcore.Command.CommandHandler.Command(command = "trackingrange", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		Player p = (Player)sender;
		if(!p.hasPermission(kPermission.COMMAND_MEM.getPermissionToString()))return false;
		
		EntityPlayer player = ((CraftPlayer)p).getHandle();
		if(args.length==0){
			p.sendMessage(Language.getText(p, "PREFIX")+" Player-Tracking-Range: "+player.world.spigotConfig.playerTrackingRange);
			p.sendMessage(Language.getText(p, "PREFIX")+" /trackingrange [RANGE/Normale Range ist 48]");
		}else{
			int i = UtilNumber.toInt(args[0]);
			if(i<=0)return false;
			player.world.spigotConfig.playerTrackingRange=i;
			p.sendMessage(Language.getText(p, "PREFIX")+Language.getText(p, "TRACKING_RANGE"));
		}
		return false;
	}
	
}
