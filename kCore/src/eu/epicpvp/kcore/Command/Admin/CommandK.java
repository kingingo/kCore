package eu.epicpvp.kcore.Command.Admin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import dev.wolveringer.client.LoadedPlayer;
import dev.wolveringer.client.connection.ClientType;
import dev.wolveringer.dataserver.protocoll.DataBuffer;
import dev.wolveringer.dataserver.protocoll.packets.PacketServerMessage;
import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Permission.Group.GroupTyp;
import eu.epicpvp.kcore.Util.TimeSpan;
import eu.epicpvp.kcore.Util.UtilNumber;
import eu.epicpvp.kcore.Util.UtilReflection;
import eu.epicpvp.kcore.Util.UtilServer;

public class CommandK implements CommandExecutor{
	
	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "k", alias={"group","perm"}, sender = Sender.CONSOLE)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		if(args.length==0){
			System.out.println("/k addg [Player] [TYP] [Group]");
			System.out.println("/k addgwithtime [Player] [TYP] [Group] [DAYs]");
		}else{
			if(args[0].equalsIgnoreCase("addg")){
				String player = args[1];
				String group = args[3];
				setGroup(player, group);
			}else if(args[0].equalsIgnoreCase("addgwithtime")){
				String player = args[1];
				String group = args[3];
				long time = System.currentTimeMillis()+(TimeSpan.DAY*UtilNumber.toInt(args[4]));
				setGroup(player, group);
				addPermission(player, "epicpvp.timer.group."+group+":"+time);
			}
		}
		return false;
	}
	
	public void addPermission(String player, String permission){
		LoadedPlayer loadedplayer = UtilServer.getClient().getPlayerAndLoad(player);
		DataBuffer buffer = new DataBuffer().writeByte(3).writeInt(loadedplayer.getPlayerId()).writeString(permission).writeInt(GroupTyp.ALL.ordinal());
		PacketServerMessage packet = new PacketServerMessage("permission", ClientType.BUNGEECORD, buffer);
		UtilReflection.setValue("targets", packet, new PacketServerMessage.Target[]{new PacketServerMessage.Target(ClientType.BUNGEECORD,"targetlimit;1")});
		UtilServer.getClient().writePacket(packet);
		System.out.println("add "+player+"("+loadedplayer.getPlayerId()+") the permission "+permission);
	}

	public void setGroup(String player, String toGroup){
		LoadedPlayer loadedplayer = UtilServer.getClient().getPlayerAndLoad(player);
		DataBuffer buffer = new DataBuffer();
		buffer.writeByte(2);
		buffer.writeInt(loadedplayer.getPlayerId());
		buffer.writeString(null);
		buffer.writeString(toGroup);
		PacketServerMessage packet = new PacketServerMessage("permission", ClientType.BUNGEECORD, buffer);

		UtilReflection.setValue("targets", packet, new PacketServerMessage.Target[]{new PacketServerMessage.Target(ClientType.BUNGEECORD,"targetlimit;1")});

		UtilServer.getClient().writePacket(packet);
		System.out.println("Set "+player+"("+loadedplayer.getPlayerId()+") to "+toGroup);
	}
	
}
