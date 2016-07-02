package eu.epicpvp.kcore.Inventory.Item.Buttons;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.epicpvp.kcore.Inventory.InventoryPageBase;
import eu.epicpvp.kcore.Inventory.Item.Click;
import eu.epicpvp.kcore.Util.UtilEvent.ActionType;
import eu.epicpvp.kcore.Util.UtilItem;
import eu.epicpvp.kcore.kConfig.kConfig;
import lombok.Getter;
import lombok.Setter;

public class ButtonItemShopMove extends ButtonBase{
	
	private static HashMap<Player,ButtonItemShopMove> players = new HashMap<>();
	
	@Getter
	@Setter
	private int c_slot;
	@Getter
	@Setter
	private int page_slot;
	@Getter
	@Setter
	private int i_slot;
	
	public ButtonItemShopMove(kConfig config,int c_slot, int page_slot,int i_slot, InventoryPageBase page, ItemStack itemStack) {
		super(new Click(){

			@Override
			public void onClick(Player player, ActionType type, Object object) {
				if(type == ActionType.LEFT){
					players.remove(player);
				}else{
					if(players.containsKey(player)){
						ButtonItemShopMove move = players.get(player);
						if(move.getC_slot()==c_slot&&move.getPage_slot()==page_slot&&move.getI_slot()==i_slot){
							players.remove(player);
							return;
						}
						ButtonItemShopMove move1 = (ButtonItemShopMove)page.getButton(i_slot);

						ItemStack it = UtilItem.RenameItem(new ItemStack(Material.STAINED_GLASS_PANE,1,(byte)8), "LEER");
						int bprice = config.getInt("Main."+c_slot+"."+page_slot+"."+i_slot+".buy");
						int sprice = config.getInt("Main."+c_slot+"."+page_slot+"."+i_slot+".sell");
						if(config.contains("Main."+c_slot+"."+page_slot+"."+i_slot)){
							it = config.getItemStack("Main."+c_slot+"."+page_slot+"."+i_slot+".Item");
						}
						
						int bprice2 = config.getInt("Main."+move.getC_slot()+"."+move.getPage_slot()+"."+move.getI_slot()+".buy");
						int sprice2 = config.getInt("Main."+move.getC_slot()+"."+move.getPage_slot()+"."+move.getI_slot()+".sell");
						ItemStack it2=UtilItem.RenameItem(new ItemStack(Material.STAINED_GLASS_PANE,1,(byte)8), "LEER");
						if(config.contains("Main."+move.getC_slot()+"."+move.getPage_slot()+"."+move.getI_slot())){
							it2 = config.getItemStack("Main."+move.getC_slot()+"."+move.getPage_slot()+"."+move.getI_slot()+".Item");
						}
						
						if((it.getType()!=Material.STAINED_GLASS_PANE && it.getData().getData() != 8) || (it.getType()!=Material.STAINED_GLASS_PANE && it.getData().getData() == 8)){
							config.set("Main."+move.getC_slot()+"."+move.getPage_slot()+"."+move.getI_slot()+".buy", bprice);
							config.set("Main."+move.getC_slot()+"."+move.getPage_slot()+"."+move.getI_slot()+".sell", sprice);
							config.setItemStack("Main."+move.getC_slot()+"."+move.getPage_slot()+"."+move.getI_slot()+".Item", it);
						}else{
							config.set("Main."+move.getC_slot()+"."+move.getPage_slot()+"."+move.getI_slot(), null);
						}
						

						if((it2.getType()!=Material.STAINED_GLASS_PANE && it2.getData().getData() != 8) || (it2.getType()!=Material.STAINED_GLASS_PANE && it2.getData().getData() == 8)){
							config.set("Main."+c_slot+"."+page_slot+"."+i_slot+".buy", bprice2);
							config.set("Main."+c_slot+"."+page_slot+"."+i_slot+".sell", sprice2);
							config.setItemStack("Main."+c_slot+"."+page_slot+"."+i_slot+".Item", it2);
						}else{
							config.set("Main."+c_slot+"."+page_slot+"."+i_slot, null);
						}
						config.save();
						
						move1.setItemStack(it2);
						move1.refreshItemStack();
						
						move.setItemStack(it);
						move.refreshItemStack();
						
						players.remove(player);
					}else{
						players.put(player, ((ButtonItemShopMove)page.getButton(i_slot)));
					}
				}
			}
			
		},itemStack);
		this.c_slot=c_slot;
		this.page_slot=page_slot;
		this.i_slot=i_slot;
	}
}
