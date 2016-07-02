package eu.epicpvp.kcore.Command.Admin;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_8_R3.Block;
import net.minecraft.server.v1_8_R3.ItemStack;
import net.minecraft.server.v1_8_R3.MojangsonParser;
import net.minecraft.server.v1_8_R3.NBTTagCompound;

public class CommandAddItem implements CommandExecutor {

	@SuppressWarnings("deprecation")
	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "additem", permissions={"command.additem"}) //additem <Player> <Forceonline> <Item tags>
	public boolean onCommand(CommandSender cs, Command cmd, String lable, String[] args) {
		if(args.length >= 3){
			String target = args[0];
			Boolean forceOnline = Boolean.parseBoolean(args[1]);
			String itemNbt = StringUtils.join(Arrays.copyOfRange(args, 2, args.length)," ");
			if(itemNbt.length() == 0){
				cs.sendMessage("§cItem NBT cant be empty!");
				return true;
			}
			NBTTagCompound nbt = new NBTTagCompound();
			try{
				nbt = MojangsonParser.parse(ChatColor.translateAlternateColorCodes('&', itemNbt));
			}catch(Exception e){
				cs.sendMessage("§cNBTTag isnt valid! (Message: "+e.getMessage()+")");
				return true;
			}
			ItemStack item = new ItemStack(Block.getById(1));
			item.c(nbt);
			org.bukkit.inventory.ItemStack craftItem = CraftItemStack.asBukkitCopy(item);
			
			OfflinePlayer player = Bukkit.getOfflinePlayer(target);
			if(!player.isOnline() && forceOnline){
				cs.sendMessage("§cThe target player isnt online!");
				return true;
			}
			
			if(!player.hasPlayedBefore()){
				cs.sendMessage("§6Attantion this player havnt played yet!");
			}
			if(player.getPlayer() == null){
				cs.sendMessage("§cThe playerdata is null!");
				return true;
			}
			Player playerData = player.getPlayer();
			if(!playerData.isOnline())
				playerData.loadData();
			playerData.getInventory().addItem(craftItem);
			if(playerData.isOnline())
				playerData.updateInventory();
			else
				playerData.saveData();
		}
		return true;
	}
}
