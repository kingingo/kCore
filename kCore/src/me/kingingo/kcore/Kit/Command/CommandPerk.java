package me.kingingo.kcore.Kit.Command;

import java.util.HashMap;

import lombok.Getter;
import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Inventory.InventoryBase;
import me.kingingo.kcore.Inventory.Inventory.InventoryChoose;
import me.kingingo.kcore.Inventory.Inventory.InventoryYesNo;
import me.kingingo.kcore.Inventory.Item.Click;
import me.kingingo.kcore.Kit.Perk;
import me.kingingo.kcore.Kit.PerkManager;
import me.kingingo.kcore.Update.UpdateType;
import me.kingingo.kcore.Update.Event.UpdateEvent;
import me.kingingo.kcore.Util.UtilEvent.ActionType;
import me.kingingo.kcore.Util.UtilItem;
import me.kingingo.kcore.Util.UtilList;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class CommandPerk  extends InventoryBase implements CommandExecutor{
	
	@Getter
	private PerkManager manager;
	InventoryYesNo or;
	HashMap<Player,String> perk = new HashMap<>();
	
	public CommandPerk(PerkManager manager){
		super(manager.getManager().getInstance(),"CommandPerk");
		this.manager=manager;
		
		or = new InventoryYesNo("Perk An/Aus", new Click(){

			@Override
			public void onClick(Player player, ActionType type,Object object) {
				getManager().addPlayer( perk.get(player) , player);
				perk.remove(player);
				player.closeInventory();
			}
			
		}, new Click(){

			@Override
			public void onClick(Player player, ActionType type,Object object) {
				getManager().removePlayer( perk.get(player) , player);
				perk.remove(player);
				player.closeInventory();
			}
			
		});
		
		addPage(or);
		
		setMain(new InventoryChoose(new Click(){

				@Override
				public void onClick(Player player, ActionType type,Object object) {
					perk.remove(player);
					perk.put(player, ((ItemStack)object).getItemMeta().getDisplayName());
					player.openInventory(or);
				}
				
			},"§aPerk Auswahl",18,getItems()));
	}

	@me.kingingo.kcore.Command.CommandHandler.Command(command = "perk", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender cs, Command cmd, String arg2,String[] args) {
		Player p = (Player)cs;
		if(manager.hasPlayer(p)){
			p.openInventory(getMain());
		}
		return false;
	}
	
	@EventHandler
	public void Update(UpdateEvent ev){
		if(ev.getType()==UpdateType.MIN_64){
			for(Player p : perk.keySet())p.closeInventory();
			UtilList.CleanList(perk);
		}
	}
	
	public ItemStack[] getItems(){
		ItemStack[] items = new ItemStack[getManager().getPlayers().size()];
		int perks = 0;
		for(Perk perk : getManager().getPlayers().keySet()){
			items[perks]=UtilItem.RenameItem(new ItemStack(Material.GOLD_BLOCK), perk.getName());
			perks++;
		}
		return items;
	}
	
}
