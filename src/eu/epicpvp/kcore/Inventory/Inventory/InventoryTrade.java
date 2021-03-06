package eu.epicpvp.kcore.Inventory.Inventory;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import eu.epicpvp.kcore.Inventory.InventoryPageBase;
import eu.epicpvp.kcore.Inventory.Item.Click;
import eu.epicpvp.kcore.Inventory.Item.Buttons.ButtonBase;
import eu.epicpvp.kcore.Util.InventorySize;
import eu.epicpvp.kcore.Util.UtilEvent.ActionType;
import eu.epicpvp.kcore.Util.UtilItem;
import lombok.Getter;

public class InventoryTrade extends InventoryPageBase{
	
	private ButtonBase accept_t;
	private ButtonBase accept_t1;
	@Getter
	private Player t;
	@Getter
	private Player t1;
	private ItemStack accept;
	private ItemStack deny;
	
	public InventoryTrade(final Player t,final Player t1) {
		super("InventoryTrade",InventorySize._45.getSize(),t.getName()+" and "+t1.getName());
		setClickPlayerInventory(true);
		this.t=t;
		this.t1=t1;
		this.accept=UtilItem.RenameItem(new ItemStack(Material.EMERALD), "§aAccept");
		this.deny=UtilItem.RenameItem(new ItemStack(Material.REDSTONE), "§cDeny");
		
		for(int i = 4 ; i < (4+5*9); i+=9){
			setItem(i, UtilItem.RenameItem(new ItemStack(Material.STAINED_GLASS_PANE,1,(byte)15), " "));
		}
		
		accept_t=new ButtonBase(new Click(){

			@Override
			public void onClick(Player player, ActionType type, Object object) {
				if(player.getUniqueId()!=t.getUniqueId())return;
				
				if(accept_t.getItemStack().getType()==Material.REDSTONE){
					accept_t.setItemStack(accept);
					accept_t.refreshItemStack();
				}else{
					accept_t.setItemStack(deny);
					accept_t.refreshItemStack();
					if(accept_t1.getItemStack().getType()==Material.REDSTONE){
						t.closeInventory();
					}
				}
			}
			
		}, accept);
		addButton(44, accept_t);
		
		accept_t1=new ButtonBase(new Click(){

			@Override
			public void onClick(Player player, ActionType type, Object object) {
				if(player.getUniqueId()!=t1.getUniqueId())return;
				if(accept_t1.getItemStack().getType()==Material.REDSTONE){
					accept_t1.setItemStack(accept);
					accept_t1.refreshItemStack();
				}else{
					accept_t1.setItemStack(deny);
					accept_t1.refreshItemStack();
					if(accept_t.getItemStack().getType()==Material.REDSTONE){
						t.closeInventory();
					}
				}
			}
			
		}, accept);
		addButton(36, accept_t1);
		
		for(int i = 36; i<40; i++){
			if(getItem(i)!=null&&getItem(i).getType()!=Material.AIR){
				continue;
			}
			
			setItem(i, UtilItem.RenameItem(new ItemStack(Material.STAINED_GLASS_PANE,1,(byte)14), "§c"+t1.getName()));
		}
		
		for(int i = 40; i<44; i++){
			if(getItem(i)!=null&&getItem(i).getType()!=Material.AIR){
				continue;
			}
			
			setItem(i, UtilItem.RenameItem(new ItemStack(Material.STAINED_GLASS_PANE,1,(byte)11), "§b"+t.getName()));
		}
		
		t.openInventory(this);
		t1.openInventory(this);
	}
	
	public boolean putItem(Player player,Inventory clickedInv,ItemStack item,int slot){
		if(clickedInv instanceof PlayerInventory){
			if(player.getUniqueId()==t1.getUniqueId()){
				if(nextFreePlaceT1(item))clickedInv.setItem(slot, null);
			}else if(player.getUniqueId()==t.getUniqueId()){
				if(nextFreePlaceT(item))clickedInv.setItem(slot, null);
			}
			
			accept_t.setItemStack(accept);
			accept_t.refreshItemStack();
			accept_t1.setItemStack(accept);
			accept_t1.refreshItemStack();
			
			return true;
		}else if(item!=null&&item.getType()!=Material.AIR){
			if(player.getUniqueId()==this.t1.getUniqueId()&&(slot>=0&&slot<=3
					|| slot>=9&&slot<=12
					|| slot>=18&&slot<=21
					|| slot>=27&&slot<=30) ){
				player.getInventory().addItem(item);
				setItem(slot, null);
				accept_t.setItemStack(accept);
				accept_t.refreshItemStack();
				accept_t1.setItemStack(accept);
				accept_t1.refreshItemStack();
				return true;
			}else if(player.getUniqueId()==this.t.getUniqueId()&&(slot>=5&&slot<=8
					|| slot>=14&&slot<=17
					|| slot>=23&&slot<=26
					|| slot>=32&&slot<=35)){
				player.getInventory().addItem(item);
				setItem(slot, null);
				
				accept_t.setItemStack(accept);
				accept_t.refreshItemStack();
				accept_t1.setItemStack(accept);
				accept_t1.refreshItemStack();
				
				return true;
			}
		}
		return false;
	}
	
	public boolean nextFreePlaceT1(ItemStack item){
		for(int i = 0; i<4;i++){
			for(int a = i; a< (i+9*4) ;a+=9){
				if(getItem(a)==null){
					setItem(a, item);
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean nextFreePlaceT(ItemStack item){
		for(int i = 5; i<9;i++){
			for(int a = i; a< (i+9*4) ;a+=9){
				if(getItem(a)==null){
					setItem(a, item);
					return true;
				}
			}
		}
		return false;
	}
	
	public void done(){
		if(getViewers().size()==2&&accept_t1.getItemStack().getType()==Material.REDSTONE&&accept_t.getItemStack().getType()==Material.REDSTONE){
			acceptHandle();
		}else{
			cancelHandle();
		}
	}

	public void cancelHandle(){

		if(t1.isOnline()){
			for(int i = 0; i<4;i++){
				for(int a = i; a< (i+9*4) ;a+=9){
					if(getItem(a)!=null&&getItem(a).getType()!=Material.AIR)t1.getInventory().addItem(getItem(a));
				}
			}
		}
		
		if(t.isOnline()){
			for(int i = 5; i<9;i++){
				for(int a = i; a< (i+9*4) ;a+=9){
					if(getItem(a)!=null&&getItem(a).getType()!=Material.AIR)t.getInventory().addItem(getItem(a));
				}
			}
		}

		removeLast();
	}
	
	public void removeLast(){
		if(t!=null&&t.isOnline())t.closeInventory();
		if(t1!=null&&t1.isOnline())t1.closeInventory();
		closeInventory();
		if(accept_t!=null)accept_t.remove();
		if(accept_t1!=null)accept_t1.remove();
		clear();
		t=null;
		t1=null;
		accept_t=null;
		accept_t1=null;
	}
	
	public void acceptHandle(){

		if(t.isOnline()){
			for(int i = 0; i<4;i++){
				for(int a = i; a< (i+9*4) ;a+=9){
					if(getItem(a)!=null&&getItem(a).getType()!=Material.AIR)t.getInventory().addItem(getItem(a));
				}
			}
		}
		
		if(t1.isOnline()){
			for(int i = 5; i<9;i++){
				for(int a = i; a< (i+9*4) ;a+=9){
					if(getItem(a)!=null&&getItem(a).getType()!=Material.AIR)t1.getInventory().addItem(getItem(a));
				}
			}
		}

		removeLast();
	}
}
