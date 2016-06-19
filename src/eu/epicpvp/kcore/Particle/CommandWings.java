package eu.epicpvp.kcore.Particle;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Inventory.Inventory.InventoryChoose;
import eu.epicpvp.kcore.Inventory.Item.Click;
import eu.epicpvp.kcore.Pet.Setting.PetSetting;
import eu.epicpvp.kcore.Pet.Shop.PlayerPetHandler;
import eu.epicpvp.kcore.Translation.TranslationHandler;
import eu.epicpvp.kcore.Util.InventorySize;
import eu.epicpvp.kcore.Util.UtilInv;
import eu.epicpvp.kcore.Util.UtilEvent.ActionType;
import eu.epicpvp.kcore.Util.UtilItem;
import lombok.Getter;

public class CommandWings implements CommandExecutor{
	
	private WingShop wings;
	
	public CommandWings(WingShop wings){
		this.wings=wings;
	}
	
	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "wings", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender cs, Command cmd, String arg2,String[] args) {
		Player p = (Player)cs;
		wings.open(p, UtilInv.getBase());
		return false;
	}
	
}
