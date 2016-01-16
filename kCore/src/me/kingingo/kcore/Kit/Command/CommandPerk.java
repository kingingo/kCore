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
import me.kingingo.kcore.Language.Language;
import me.kingingo.kcore.Update.UpdateType;
import me.kingingo.kcore.Update.Event.UpdateEvent;
import me.kingingo.kcore.Util.UtilEvent.ActionType;
import me.kingingo.kcore.Util.UtilItem;
import me.kingingo.kcore.Util.UtilList;
import me.kingingo.kcore.Util.UtilPlayer;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;

public class CommandPerk implements CommandExecutor{
	
	@Getter
	private PerkManager manager;
	private InventoryYesNo or;
	private InventoryChoose choose;
	private HashMap<Player,String> perk = new HashMap<>();
	
	public CommandPerk(final PerkManager manager,InventoryBase base){
		this.manager=manager;
		
		or = new InventoryYesNo("Perk An/Aus", new Click(){

			@Override
			public void onClick(Player player, ActionType type,Object object) {

				if(manager.getUserData()!=null&&manager.getUserData().getConfigs().containsKey(UtilPlayer.getRealUUID(player))){
					manager.getUserData().getConfig(player).set("perks."+perk.get(player), "true");
				}
				
				getManager().addPlayer( perk.get(player) , player);
				perk.remove(player);
				
				player.closeInventory();
			}
			
		}, new Click(){

			@Override
			public void onClick(Player player, ActionType type,Object object) {

				if(manager.getUserData()!=null&&manager.getUserData().getConfigs().containsKey(UtilPlayer.getRealUUID(player))){
					manager.getUserData().getConfig(player).set("perks."+perk.get(player), "false");
				}
				
				getManager().removePlayer( perk.get(player) , player);
				perk.remove(player);
				
				player.closeInventory();
			}
			
		});
		
		base.addPage(or);
		
		choose=new InventoryChoose(new Click(){

				@Override
				public void onClick(Player player, ActionType type,Object object) {
					if(object instanceof ItemStack){
						if(((ItemStack)object).hasItemMeta()){
							if(((ItemStack)object).getItemMeta().hasDisplayName()){
								if(manager.hasPlayer(((ItemStack)object).getItemMeta().getDisplayName(), player)){
									perk.remove(player);
									perk.put(player, ((ItemStack)object).getItemMeta().getDisplayName());
									player.openInventory(or);
								}
							}
						}
					}
				}
				
			},"§aPerk Auswahl",18,getItems());
		base.addPage(choose);
	}

	@me.kingingo.kcore.Command.CommandHandler.Command(command = "perk", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender cs, Command cmd, String arg2,String[] args) {
		Player p = (Player)cs;
		if(manager.hasPlayer(p)){
			p.openInventory(choose);
		}else{
			p.sendMessage(Language.getText(p, "PREFIX")+Language.getText(p, "PERK_NOT_BOUGHT"));
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
			items[perks]=UtilItem.RenameItem(new ItemStack(Material.EMERALD), perk.getName());
			perks++;
		}
		return items;
	}
	
}
