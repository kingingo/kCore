package me.kingingo.kcore.Inventory;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.Inventory.Item.IButton;
import me.kingingo.kcore.Inventory.Item.IButtonMultiSlot;
import me.kingingo.kcore.Inventory.Item.IButtonOneSlot;
import me.kingingo.kcore.Inventory.Item.Buttons.ButtonBase;
import me.kingingo.kcore.Inventory.Item.Buttons.ButtonCopy;
import me.kingingo.kcore.Inventory.Item.Buttons.ButtonForMultiButtonsCopy;
import me.kingingo.kcore.Inventory.Item.Buttons.ButtonMultiCopy;
import me.kingingo.kcore.Inventory.Item.Buttons.ButtonMultiSlotBase;
import me.kingingo.kcore.Inventory.Item.Buttons.SalesPackageBase;
import me.kingingo.kcore.Util.InventorySize;
import me.kingingo.kcore.Util.InventorySplit;
import me.kingingo.kcore.Util.UtilDebug;
import me.kingingo.kcore.Util.UtilEvent.ActionType;
import me.kingingo.kcore.Util.UtilString;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftInventoryCustom;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


public class InventoryPageBase extends CraftInventoryCustom{
 
	@Getter
	@Setter
	private ArrayList<IButton> buttons;
	@Getter
	private String inventoryType;
	@Getter
	@Setter
	private boolean clickPlayerInventory = false;
	
	public InventoryPageBase(int size,String title){
		this(null,size,title);
	}
	
	public InventoryPageBase(InventorySize size,String title){
		this(null,size.getSize(),title);
	}
	
	public InventoryPageBase(String inventoryType,InventorySize size,String title){
		this(inventoryType,size.getSize(),title);
	}
	
	public InventoryPageBase(String inventoryType,int size,String title){
		super(null,InventorySize.invSize(size).getSize(),(title==null?"Inventory": UtilString.cut(title, 32) ));
		this.buttons=new ArrayList<>();
		this.inventoryType=inventoryType;
	}
	
	public boolean useButton(Player player,ActionType type,ItemStack item,int slot){
		if(!isSlot(slot,"useButton(Player,ActionType,ItemStack,int)"))return true;
		for(IButton button : buttons){
			if(button instanceof IButtonOneSlot){
				if(button.isSlot(slot)){
					((IButtonOneSlot)button).Clicked(player, type,item);
					return button.isCancelled();
				}
			}else if(button instanceof IButtonMultiSlot){
//				if(player.getName().contains("kingingo")){
//					System.out.println("BUTTO 1");
//					IButtonMultiSlot b = (IButtonMultiSlot)button;
//					for(ButtonBase bas : b.getButtons()){
//						if(bas!=null){
//							if(bas.getItemStack()!=null){
//								System.out.println("BUTTO "+bas.getSlot()+" "+bas.getItemStack().getType());
//							}else{
//								System.out.println("BUTTO "+bas.getSlot()+" ITEM==NULL");
//							}
//						}else{
//							System.out.println("BUTTO NULL");
//						}
//					}
//				}
				if(button.isSlot(slot)){
					if(player.getName().contains("kingingo"))System.out.println("CLICK "+slot);
					((IButtonMultiSlot)button).Clicked(slot,player, type,item);
					return button.isCancelled();
				}
			}
		}
		return true;
	}
	
	public void remove(){
		for(IButton b : this.buttons)b.remove();
		this.buttons.clear();
		this.buttons=null;
		this.inventoryType=null;
		clear();
	}
	
	public boolean isSlot(int index, String methode){
		if( index<0 ){
			UtilDebug.debug(this,methode, "index("+index+"<0) ist zu klein.");
			return false;
		}else if( index>getSize() ){
			UtilDebug.debug(this,methode, "index("+index+">"+getSize()+") ist zu gro�.");
			return false;
		}
		return true;
	}
	
	public org.bukkit.inventory.ItemStack getItem(int index) {
		if(!isSlot(index,"getItem(int)"))return null;
	    net.minecraft.server.v1_8_R3.ItemStack item = getInventory().getItem(index);
	    return item == null ? null : CraftItemStack.asCraftMirror(item);
	}
	
	public void setItemReflect(int index, org.bukkit.inventory.ItemStack item){
		setItem(index,item);
		setItem(InventorySplit.getSlotRelfect(index), item);
	}
	
	public void setItem(int index, org.bukkit.inventory.ItemStack item){
		if(!isSlot(index,"setItem(int,ItemStack)"))return;
	    getInventory().setItem(index, (item == null) || (item.getTypeId() == 0) ? null : CraftItemStack.asNMSCopy(item));
	}
	
	public int getSlotSort(){
		if(getSize()==InventorySize._9.getSize()){
			if(getItem(4)==null)return 4;
			else if(getItem(5)==null)return 5;
			else if(getItem(3)==null)return 3;
			else if(getItem(6)==null)return 6;
			else if(getItem(2)==null)return 2;
			else if(getItem(7)==null)return 7;
			else if(getItem(1)==null)return 1;
			else if(getItem(8)==null)return 8;
			else if(getItem(0)==null)return 0;
		}
		return 0;
	}
	
	public void fill(Material material){
		for(int i = 0 ; i < getSize(); i++){
			if(getItem(i)==null||getItem(i).getType()==Material.AIR){
				if(getItem(i)==null)setItem(i, new ItemStack(Material.IRON_FENCE));
				getItem(i).setType(material);
				ItemMeta im = getItem(i).getItemMeta();
				im.setDisplayName(" ");
				getItem(i).setItemMeta(im);
			}
		}
	}
	
	public void fill(Material material,int data){
		for(int i = 0 ; i < getSize(); i++){
			if(getItem(i)==null||getItem(i).getType()==Material.AIR){
				if(getItem(i)==null)setItem(i, new ItemStack(Material.IRON_FENCE));
				getItem(i).setType(material);
				getItem(i).setDurability((short) data);
				ItemMeta im = getItem(i).getItemMeta();
				im.setDisplayName(" ");
				getItem(i).setItemMeta(im);
			}
		}
	}
	
	public IButtonOneSlot getButtonOneSlot(int slot){
		if(!isSlot(slot,"getButtonOneSlot(int)"))return null;
		for(IButton button : getButtons()){
			if(button instanceof IButtonOneSlot && button.isSlot(slot)){
				return (IButtonOneSlot)button;
			}
		}
		return null;
	}
	
	public IButton getButton(int slot){
		if(!isSlot(slot,"getButton(int)"))return null;
		for(IButton button : getButtons()){
			if(button.isSlot(slot)){
				return button;
			}
		}
		return null;
	}
	
	public void addButton(IButton button){
		
		button.setInventoryPageBase(this);
		
		if(button instanceof IButtonOneSlot){
			int slot=firstEmpty();
			IButtonOneSlot b = (IButtonOneSlot)button;
			b.setSlot(slot);
			setItem(slot, b.getItemStack());
		}
		
		this.buttons.add(button);
	}
	
	public void addButton(int slot,IButton button){
		if(!isSlot(slot,"addButton(int,IButton)"))return;
		
		if(button instanceof IButtonOneSlot){
			IButtonOneSlot b = (IButtonOneSlot)button;
			b.setSlot(slot);
			setItem(slot, b.getItemStack());
		}
		
		button.setInventoryPageBase(this);
		this.buttons.add(button);
	}
	
	public InventoryPageBase clone(){
		InventoryPageBase page = new InventoryPageBase(getInventoryType(),getSize(), getTitle());
		for(IButton b : getButtons()){
			if(b instanceof IButtonOneSlot){
				if(b instanceof SalesPackageBase){
					page.addButton(((IButtonOneSlot)b).getSlot(), new SalesPackageBase(((SalesPackageBase)b).getClick(), ((SalesPackageBase)b).getPermission(), ((IButtonOneSlot)b).getItemStack() ));
				}else if(b instanceof ButtonCopy){
					page.addButton(((IButtonOneSlot)b).getSlot(), new ButtonCopy(((ButtonCopy)b).getSet(),((ButtonCopy)b).getClick(), ((IButtonOneSlot)b).getItemStack() ));
				}else if(b instanceof ButtonBase){
					page.addButton(((IButtonOneSlot)b).getSlot(), new ButtonBase(((IButtonOneSlot)b).getClick(), ((IButtonOneSlot)b).getItemStack() ));
				} 
			}else if(b instanceof IButtonMultiSlot){
				if(b instanceof ButtonMultiCopy){
					page.addButton(new ButtonMultiCopy( ((ButtonForMultiButtonsCopy[])((ButtonMultiCopy)b).getButtons().clone()) ));
				}else if(b instanceof ButtonMultiSlotBase){
					page.addButton(new ButtonMultiSlotBase(((ButtonMultiCopy)b).getButtons().clone()));
				}	
			}
		}
		
		page.setContents(getContents());
		page.updateInventory();
		return page;
	}
	
	public void closeInventory(){
		if(!getViewers().isEmpty())
			for(int i = 0; i<getViewers().size(); i++)
					getViewers().get(i).closeInventory();
	}
	
	public void updateInventory(){
		if(!getViewers().isEmpty())
			for(HumanEntity h : getViewers())
				if(h!=null && h instanceof Player)
					((Player)h).updateInventory();
	}
	
}
