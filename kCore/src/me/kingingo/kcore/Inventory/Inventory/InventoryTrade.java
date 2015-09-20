package me.kingingo.kcore.Inventory.Inventory;

import me.kingingo.kcore.Inventory.InventoryPageBase;
import me.kingingo.kcore.Inventory.Item.ButtonBase;
import me.kingingo.kcore.Inventory.Item.Click;
import me.kingingo.kcore.Util.InventorySize;
import me.kingingo.kcore.Util.UtilEvent.ActionType;
import me.kingingo.kcore.Util.UtilItem;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class InventoryTrade extends InventoryPageBase{
	
	ButtonBase accept_t;
	ButtonBase accept_t1;
	
	public InventoryTrade(final Player t,final Player t1) {
		super("InventoryTrade",InventorySize._45.getSize(),t.getName()+" and "+t1.getName());
		
		for(int i = 4 ; i < (4+5*9); i+=9){
			setItem(i, UtilItem.RenameItem(new ItemStack(Material.STAINED_GLASS_PANE,1,(byte)15), " "));
		}
		
		accept_t=new ButtonBase(new Click(){

			@Override
			public void onClick(Player player, ActionType type, Object object) {
				if(player.getUniqueId()!=t.getUniqueId())return;
				if(accept_t.getItemStack().getType()==Material.REDSTONE){
					if(accept_t1.getItemStack().getType()==Material.REDSTONE){
						acceptHandle(t,t1);
					}else{
						accept_t.setItemStack(UtilItem.RenameItem(new ItemStack(Material.REDSTONE), "§cDeny"));
					}
				}else{
					accept_t.setItemStack(UtilItem.RenameItem(new ItemStack(Material.EMERALD), "§aAccept"));
				}
			}
			
		}, UtilItem.RenameItem(new ItemStack(Material.EMERALD), "§aAccept"));
		
		accept_t1=new ButtonBase(new Click(){

			@Override
			public void onClick(Player player, ActionType type, Object object) {
				if(player.getUniqueId()!=t1.getUniqueId())return;
				if(accept_t1.getItemStack().getType()==Material.REDSTONE){
					if(accept_t.getItemStack().getType()==Material.REDSTONE){
						acceptHandle(t,t1);
					}else{
						accept_t1.setItemStack(UtilItem.RenameItem(new ItemStack(Material.REDSTONE), "§cDeny"));
					}
				}else{
					accept_t1.setItemStack(UtilItem.RenameItem(new ItemStack(Material.EMERALD), "§aAccept"));
				}
			}
			
		}, UtilItem.RenameItem(new ItemStack(Material.EMERALD), "§aAccept"));
		
		addButton(44, accept_t);
		addButton(35, accept_t);
		for(int i = 35; i<44; i++)if(getItem(i)!=null&&getItem(i).getType()!=Material.AIR)setItem(i, UtilItem.RenameItem(new ItemStack(Material.STAINED_GLASS_PANE,1,(byte)160), " "));
		t.openInventory(this);
		t1.openInventory(this);
	}
	
	public void acceptHandle(Player t,Player t1){
		for(int i = 0; i<5;i++){
			for(int a = i; a>=i+4;a++){
				t.getInventory().addItem(getItem(a));
			}
		}
		
		for(int i = 5; i<(5+4*9);i=+9){
			for(int a = i; a>=i+4;a++){
				t1.getInventory().addItem(getItem(a));
			}
		}
		
		t.closeInventory();
		t1.closeInventory();
		accept_t.remove();
		accept_t1.remove();
		clear();
	}
}
