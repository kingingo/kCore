package eu.epicpvp.kcore.Kit.Command;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Inventory.Inventory.InventoryCopy;
import eu.epicpvp.kcore.Inventory.Item.Buttons.ButtonPerkOnOff;
import eu.epicpvp.kcore.Kit.Perk;
import eu.epicpvp.kcore.Kit.PerkManager;
import eu.epicpvp.kcore.Translation.TranslationHandler;
import eu.epicpvp.kcore.Util.InventorySize;
import eu.epicpvp.kcore.Util.InventorySplit;
import eu.epicpvp.kcore.Util.UtilInv;
import lombok.Getter;

public class CommandPerk implements CommandExecutor{
	
	@Getter
	private PerkManager manager;
	private HashMap<ItemStack,Perk> perklist;
	
	public CommandPerk(final PerkManager manager){
		this.manager=manager;
		this.perklist=new HashMap<>();
		UtilInv.getBase();
		manager.setPage(new InventoryCopy(InventorySize._45.getSize(), "Perk:"));
		
		int slot = 0;
		for (Perk perk : manager.getPlayers().keySet()) {
			manager.getPage().addButton(slot,new ButtonPerkOnOff(slot+9,slot, perk));
		    slot++;
		    
		    if(slot==(InventorySplit._9.getMax()+1)){
		    	slot=InventorySplit._36.getMin();
		    }
		}
		manager.getPage().fill(Material.STAINED_GLASS_PANE,7);
		((InventoryCopy)manager.getPage()).setCreate_new_inv(true);
		UtilInv.getBase().addPage(manager.getPage());
	}
	
	public ItemStack[] getItems(){
		ItemStack[] items = new ItemStack[getManager().getPlayers().size()];
		int perks = 0;
		for(Perk perk : getManager().getPlayers().keySet()){
			items[perks]=perk.getItem();
			perklist.put(perk.getItem(), perk);
			perks++;
		}
		return items;
	}
	

	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "perk", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender cs, Command cmd, String arg2,String[] args) {
		Player p = (Player)cs;
		if(manager.hasPlayer(p)){
			((InventoryCopy)manager.getPage()).open(p, UtilInv.getBase());
		}else{
			p.sendMessage(TranslationHandler.getText(p, "PREFIX")+TranslationHandler.getText(p, "PERK_NOT_BOUGHT"));
		}
		return false;
	}
}
