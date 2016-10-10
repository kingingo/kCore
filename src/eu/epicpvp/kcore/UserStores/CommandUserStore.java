package eu.epicpvp.kcore.UserStores;

import java.io.File;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Util.UtilNumber;
import eu.epicpvp.kcore.kConfig.kConfig;
import lombok.Getter;

public class CommandUserStore implements CommandExecutor {

	@Getter
	private UserStores userStores;

	public CommandUserStore(UserStores userStores) {
		this.userStores = userStores;
	}

	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "mystore", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender cs, Command cmd, String arg2, String[] args) {
		Player p = (Player) cs;

		if(args.length==0){
			this.userStores.openInv(p);
		}else{
			switch(args[0]){
			case "clean":
				if (p.isOp()) {
					File[] userdatas = new File(userStores.getPlugin().getDataFolder(), "userdata").listFiles();
					if (userdatas == null) {
						p.sendMessage("Could not list files in dir.");
						return false;
					}
					int amount = userdatas.length;
					p.sendMessage("Found " + amount + " userdata configs. Start clearing shop delivery chest content");
					for (File userdata : userdatas) {
						try{
							kConfig config = new kConfig(userdata);
							if (!config.contains("UserStores")) {
								continue;
							}

							ArrayList<Location> list = new ArrayList<>();
							for (String loc : config.getPathList("UserStores").keySet()) {
								Location location = getStringLoc(loc);

								if (location.getBlock().getState() instanceof Sign) {
									Sign sign = (Sign) location.getBlock().getState();

									if (sign.getLine(0).startsWith(" §b[UserStore]  ")) {
										int playerId = getPlayerId(sign);
										
										if(playerId == UtilNumber.toInt(userdata.getName().replaceAll(".yml", ""))){
											list.add(location);
										}
									}
								}
							}

							config.setLocationList("UserStores", list);
							config.save();
							System.out.println("CHANGED -> "+userdata.getName());
						}catch(Exception e){
							System.out.println("FAIL "+userdata.getAbsolutePath());
						}
					}
					p.sendMessage("Done!");
				}
				return true;
			}
		}
		return false;
	}

	public int getPlayerId(Sign sign) {
		return UtilNumber.toInt(sign.getLine(0).split("/")[1]);
	}
	
	public Location getStringLoc(String s) {
		if (s == null)
			return null;
		String[] split = s.split(",");
		return new Location(Bukkit.getWorld(split[0]), Integer.valueOf(split[1]), Integer.valueOf(split[2]),
				Integer.valueOf(split[3]));
	}
}
