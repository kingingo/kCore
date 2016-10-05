package eu.epicpvp.kcore.Inventory.Inventory;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import eu.epicpvp.kcore.Inventory.InventoryPageBase;
import eu.epicpvp.kcore.Inventory.Item.Get;
import eu.epicpvp.kcore.Inventory.Item.Buttons.LottoPackage;
import eu.epicpvp.kcore.Update.UpdateType;
import eu.epicpvp.kcore.Update.Event.UpdateEvent;
import eu.epicpvp.kcore.Util.InventorySize;
import eu.epicpvp.kcore.Util.InventorySplit;
import eu.epicpvp.kcore.Util.UtilDebug;
import eu.epicpvp.kcore.Util.UtilEvent.ActionType;
import eu.epicpvp.kcore.Util.UtilItem;
import eu.epicpvp.kcore.Util.UtilMath;
import lombok.Getter;
import lombok.Setter;

public class InventoryLotto2 extends InventoryPageBase implements Listener{

	@Getter
	@Setter
	private LottoPackage[] list;
	@Getter
	@Setter
	private LottoPackage win;
	@Getter
	@Setter
	private int durchlauf;
	@Getter
	@Setter
	private int durchlauf_counter;
	@Getter
	@Setter
	private int durchlauf_min;
	@Getter
	@Setter
	private int durchlauf_max;
	private int list_i;
	@Getter
	private InventoryLotto2Status status;
	private Get get;
	
	public InventoryLotto2(String title, Get get, int durchlauf_min, int durchlauf_max, Plugin instance) {
		super("InventoryLotto2",InventorySize._27, title);
		this.durchlauf_min=durchlauf_min;
		this.durchlauf_max=durchlauf_max;
		this.get=get;
		clearLotto();
		Bukkit.getPluginManager().registerEvents(this, instance);
	}
	
	public void clearLotto(){
		this.status=InventoryLotto2Status.PLAY;
		this.win=null;
		this.list_i=0;
		this.durchlauf_counter=0;
		this.slot=0;
		clear();
		for(int i = 0; i<=8;i++)setItem(i, UtilItem.RenameItem(new ItemStack(Material.STAINED_GLASS_PANE), " "));
		for(int i = 18; i<=26;i++)setItem(i, UtilItem.RenameItem(new ItemStack(Material.STAINED_GLASS_PANE), " "));
		setItem(4, UtilItem.RenameItem(new ItemStack(Material.HOPPER), " "));
		closeInventory();
		HandlerList.unregisterAll(this); //Unused handlers
	}
	
	public void newRound(Player player){
		newRound(player,UtilMath.RandomInt(durchlauf_max, durchlauf_min));
	}
	
	public void newRound(Player player,LottoPackage[] list,int durchlauf){
		newRound(player, list, list[UtilMath.randomInteger(list.length)], durchlauf);
	}
	
	public void newRound(Player player,int durchlauf){
		newRound(player, ((LottoPackage[])this.get.onGet(player)), durchlauf);
	}
	
	public void newRound(Player player,LottoPackage[] list,LottoPackage win,int durchlauf){
		this.durchlauf=durchlauf;
		this.list = list;
		player.openInventory(this);
		this.win = win;
	}
	
	private ItemStack item;
	private ItemStack glas;
	
	private int slot=0;
	@EventHandler
	public void UpdateEvent(UpdateEvent ev){
		if(win!=null){
			if(getViewers().isEmpty()){
				clearLotto();
			}else if(status==InventoryLotto2Status.PLAY&&ev.getType()==UpdateType.TICK){
				for(int i = 17; i >= 9; i--){
					if(i==9){
						setItemWithPlane(list[list_i], i);
						list_i++;
						
						if(list_i>=list.length)list_i=0;
					}else{
						item=getItem(i);
						glas=getItem(i+9);
						
						if( ItemCheck(i-9, i+8) )setItem(i-9, getItem(i+8) );
						setItem(i, getItem(i-1) );
						if( ItemCheck(i+9, i+8) )setItem(i+9, getItem(i+8) );
						
						if(i-1!=9){
							if( glas!=null&&glas.getType()==Material.STAINED_GLASS_PANE ){
								if( ItemCheck(i+8) )setItem(i+8, glas );
								if( ItemCheck(i-10) )setItem(i-10, glas );
							}
							setItem(i-1, item);
						}
						
						if(i==13&&getItem(i)!=null
								&&getItem(i).hasItemMeta()
								&&getItem(i).getItemMeta().hasLore()
								&&getItem(i).getItemMeta().getLore().get(getItem(i).getItemMeta().getLore().size()-1).equalsIgnoreCase("§7N"+win.getId())){
							this.durchlauf_counter++;
							if(this.durchlauf_counter>=this.durchlauf){
								this.status=InventoryLotto2Status.GET;
								win.Clicked( ((Player)getViewers().toArray()[0]), ActionType.RIGHT, this.win);
								break;
							}
						}
					}
				}
			}else if(status==InventoryLotto2Status.GET&&ev.getType()==UpdateType.SEC){
				if(slot==InventorySplit._9.getMiddle()){
					item=UtilItem.RenameItem(new ItemStack(Material.STAINED_GLASS_PANE,1,(byte)15), " ");
					setItem(4, item);
					setItem(22, item);
					setItemReflect(12, item);
					status=InventoryLotto2Status.END;
				}else{
					item=(slot==0?UtilItem.RenameItem(new ItemStack(Material.STAINED_GLASS_PANE,1,(byte)win.getType().getData()), win.getType().getName()):getItem(slot-1));
					setItemReflect(slot, item);
					setItemReflect(slot+9, item);
					setItemReflect(slot+18, item);
					slot++;
					setItemReflect(slot+1, item);
					setItemReflect(slot+18, item);
				}
			}else if(status==InventoryLotto2Status.END&&ev.getType()==UpdateType.SLOWER){
				clearLotto();
			}
		}
	}
	
	public boolean ItemCheck(int set_slot,int get_slot){
		if(ItemCheck(set_slot)&&ItemCheck(get_slot)){
			return true;
		}
		return false;
	}
	
	public boolean ItemCheck(int slot){
		if(slot<=26&&slot>=0&&getItem(slot)!=null&&getItem(slot).getType()==Material.STAINED_GLASS_PANE){
			return true;
		}
		return false;
	}
	
	public void setItemWithPlane(LottoPackage l,int slot){
		if(l==null){
			UtilDebug.debug(this,"setItemWithPlane(LottoPackage,int)", new String[]{"Lottopacke == NULL","List: "+list.length+" List-Counter:"+list_i,"Durschlauf: "+durchlauf+" Durschlauf-Count: "+durchlauf_counter});
			return;
		}else if(!isSlot(slot, "setItemWithPlane(LottoPackage,int)"))return;
		
		if(ItemCheck(slot-9))setItem(slot-9, UtilItem.RenameItem(new ItemStack(l.getType().getMaterial(),1,(byte)l.getType().getData()), l.getType().getName()));
		setItem(slot, l.getItemStack());
		if(ItemCheck(slot+9))setItem(slot+9, UtilItem.RenameItem(new ItemStack(l.getType().getMaterial(),1,(byte)l.getType().getData()), l.getType().getName()));
	}
	
	public enum InventoryLotto2Status{
		PLAY,
		GET,
		END;
	}
	
	public enum InventoryLotto2Type{
		COMMON("§fCommon",Material.STAINED_GLASS_PANE,(byte)0,0),
		UNCOMMON("§7Uncommon",Material.STAINED_GLASS_PANE,(byte)7,1),
		RARE("§dRare",Material.STAINED_GLASS_PANE,(byte)6,2),
		LEGENDARY("§aLegendary",Material.STAINED_GLASS_PANE,(byte)5,3),
		DIVINE("§5Divine",Material.STAINED_GLASS_PANE,(byte)2,4);
		
		@Getter
		private Material material;
		@Getter
		private byte data;
		@Getter
		private int i;
		@Getter
		private String name;
		InventoryLotto2Type(String name,Material material,byte data,int i){
			this.material=material;
			this.data=data;
			this.i=i;
			this.name=name;
		}
	}
}
