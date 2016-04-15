package eu.epicpvp.kcore.Pet.Commands;

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
import eu.epicpvp.kcore.Util.UtilEvent.ActionType;
import eu.epicpvp.kcore.Util.UtilItem;
import lombok.Getter;

public class CommandPet implements CommandExecutor{
	
	@Getter
	private PlayerPetHandler handler;
	@Getter
	private InventoryChoose choose;
	
	public CommandPet(PlayerPetHandler handler){
		this.handler=handler;
		
		this.choose=new InventoryChoose(new Click(){

			@Override
			public void onClick(Player player, ActionType type, Object object) {
				if(((ItemStack)object).getType()==Material.BARRIER){
					handler.getManager().RemovePet(player, true);
				} else if(player.hasPermission( handler.getPerm( ((ItemStack)object).getItemMeta().getDisplayName() ).getPermissionToString() )){
					handler.getManager().AddPetOwner(player, "§7"+player.getName()+"s Pet", handler.getEntityType(handler.getPerm( ((ItemStack)object).getItemMeta().getDisplayName() )), player.getLocation());
					if(!handler.getChange_settings().contains(player))handler.getChange_settings().add(player);
				}else{
					player.sendMessage(TranslationHandler.getText(player, "PREFIX")+TranslationHandler.getText(player, "PET_MUST_BUYED_IN_SHOP"));
				}
				player.closeInventory();
			}
			
		}, "§aPets",InventorySize.invSize(getItems().length), getItems());
		handler.getBase().addPage(choose);
	}

	public ItemStack[] getItems(){
		ItemStack[] items = new ItemStack[getHandler().getManager().getSetting_list().size()+1];
		int i = 0;	
		items[i]=UtilItem.RenameItem(new ItemStack(Material.BARRIER), "§cPet delete");
		
		for(PetSetting s : getHandler().getManager().getSetting_list().values()){
			i++;
			items[i]=s.getItem(1);
		}
		
		
		return items;
	}
	
	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "pet", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender cs, Command cmd, String arg2,String[] args) {
		Player p = (Player)cs;
			p.openInventory(choose);
		return false;
	}
	
}
