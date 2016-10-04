package eu.epicpvp.kcore.Command.Admin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import eu.epicpvp.datenclient.client.LoadedPlayer;
import dev.wolveringer.client.connection.ClientType;
import dev.wolveringer.dataserver.protocoll.DataBuffer;
import dev.wolveringer.dataserver.protocoll.packets.PacketServerMessage;
import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Permission.PermissionManager;
import eu.epicpvp.kcore.Util.UtilReflection;
import eu.epicpvp.kcore.Util.UtilServer;

public class CommandURang implements CommandExecutor{

//	private PermissionManager manager;

	public CommandURang(PermissionManager manager){
//		this.manager=manager;
	}

	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "urang", sender = Sender.CONSOLE)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {


		if(args.length == 0 || args.length == 1 || args.length == 2){
				System.out.println("[Befehl] /urang [Player] [Rang] [Upgrade Rang]");

		}

		if(args.length == 3){
			String player = args[0];
			String isGroup = args[1];
			String toGroup = args[2];
			setGroup(player,isGroup,toGroup);
		}
		return false;
	}

	public void setGroup(String player,String isGroup, String toGroup){
		LoadedPlayer loadedplayer = UtilServer.getClient().getPlayerAndLoad(player);
		DataBuffer buffer = new DataBuffer();
		buffer.writeByte(2);
		buffer.writeInt(loadedplayer.getPlayerId());
		buffer.writeString(isGroup);
		buffer.writeString(toGroup);
		PacketServerMessage packet = new PacketServerMessage("permission", ClientType.BUNGEECORD, buffer);

		UtilReflection.setValue("targets", packet, new PacketServerMessage.Target[]{new PacketServerMessage.Target(ClientType.BUNGEECORD,"targetlimit;1")});

		UtilServer.getClient().writePacket(packet);
		System.out.println("Set "+player+"("+loadedplayer.getPlayerId()+") from "+isGroup+" to "+toGroup);
	}

}
