package eu.epicpvp.kcore.Command.Admin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.wolveringer.bukkit.permissions.GroupTyp;
import eu.epicpvp.datenclient.client.LoadedPlayer;
import dev.wolveringer.client.connection.ClientType;
import dev.wolveringer.dataserver.protocoll.DataBuffer;
import dev.wolveringer.dataserver.protocoll.packets.PacketServerMessage;
import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Permission.Permission;
import eu.epicpvp.kcore.Permission.PermissionPlayer;
import eu.epicpvp.kcore.Permission.Group.Group;
import eu.epicpvp.kcore.Util.TimeSpan;
import eu.epicpvp.kcore.Util.UtilNumber;
import eu.epicpvp.kcore.Util.UtilReflection;
import eu.epicpvp.kcore.Util.UtilServer;

public class CommandK implements CommandExecutor{

	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "k", alias={"group","perm"}, sender = Sender.CONSOLE)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		if(args.length==0){
			System.out.println("/k info [Player]");
			System.out.println("/k addperm [Player] [Permission]");
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
				addPermission(player, "epicpvp.timer.group."+group+":"+time,GroupTyp.ALL);
			}else if(args[0].equalsIgnoreCase("addperm")){
				String player = args[1];
				String perm = args[2];
				GroupTyp group = GroupTyp.get(args[3]);

				addPermission(player, perm,group);
			}else if(args[0].equalsIgnoreCase("update")){
				UtilServer.getPermissionManager().updateGroup(args[1]);
				System.out.println("UPDATE");
			}else if(args[0].equalsIgnoreCase("info")){
				if(args.length==2){
					Player player = Bukkit.getPlayer(args[1]);

					if(player!=null){
						PermissionPlayer pplayer = UtilServer.getPermissionManager().getPermissionPlayer(player);
						System.out.println("Player:"+player.getName());
						if(!pplayer.getGroups().isEmpty())System.out.println("Rank"+pplayer.getGroups().get(0).getName());
						for(Permission perm : pplayer.getPermissions())System.out.println("P: "+perm.getPermissionToString());
						for(Group g : pplayer.getGroups()){
							System.out.println("G: "+g.getName());
							for(Permission perm : g.getPermissions()){
								System.out.println("G_P: "+perm.getPermissionToString());
							}
						}
					}
				}
			}
		}
		return false;
	}

	public void addPermission(String player, String permission,GroupTyp typ){
		LoadedPlayer loadedplayer = UtilServer.getClient().getPlayerAndLoad(player);
		DataBuffer buffer = new DataBuffer().writeByte(3).writeInt(loadedplayer.getPlayerId()).writeString(permission).writeInt(typ.ordinal());
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
