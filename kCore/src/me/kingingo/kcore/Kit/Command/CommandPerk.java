package me.kingingo.kcore.Kit.Command;

import java.util.HashMap;

import lombok.Getter;
import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Inventory.Inventory.InventoryCopy;
import me.kingingo.kcore.Inventory.Item.Buttons.ButtonPerkOnOff;
import me.kingingo.kcore.Kit.Perk;
import me.kingingo.kcore.Kit.PerkManager;
import me.kingingo.kcore.Language.Language;
import me.kingingo.kcore.Util.InventorySize;
import me.kingingo.kcore.Util.InventorySplit;
import me.kingingo.kcore.Util.UtilInv;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CommandPerk implements CommandExecutor{
	
	@Getter
	private PerkManager manager;
	private HashMap<ItemStack,Perk> perklist;
	
	public CommandPerk(final PerkManager manager){
		this.manager=manager;
		this.perklist=new HashMap<>();
		UtilInv.getBase(manager.getInstance());
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
	

	@me.kingingo.kcore.Command.CommandHandler.Command(command = "perk", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender cs, Command cmd, String arg2,String[] args) {
		Player p = (Player)cs;
		if(manager.hasPlayer(p)){
			((InventoryCopy)manager.getPage()).open(p, UtilInv.getBase());
		}else{
			p.sendMessage(Language.getText(p, "PREFIX")+Language.getText(p, "PERK_NOT_BOUGHT"));
		}
		return false;
	}
}
