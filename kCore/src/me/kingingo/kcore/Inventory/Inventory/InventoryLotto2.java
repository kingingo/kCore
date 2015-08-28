package me.kingingo.kcore.Inventory.Inventory;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.Inventory.InventoryPageBase;
import me.kingingo.kcore.Inventory.Item.LottoPackage;
import me.kingingo.kcore.Update.UpdateType;
import me.kingingo.kcore.Update.Event.UpdateEvent;
import me.kingingo.kcore.Util.InventorySize;
import me.kingingo.kcore.Util.UtilEvent.ActionType;
import me.kingingo.kcore.Util.UtilItem;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.java.JavaPlugin;

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
	private int list_i;
	
	public InventoryLotto2(String title,JavaPlugin instance) {
		super(InventorySize._27, title);
		this.list_i=0;
		this.durchlauf=0;
		if(list!=null){
			setItem(4, UtilItem.RenameItem(new ItemStack(Material.HOPPER), " "));
			Bukkit.getPluginManager().registerEvents(this, instance);
		}
	}
	
	public void newRound(LottoPackage win){
		this.win=win;
		this.list_i=0;
		this.durchlauf=0;
	}
	
	@EventHandler
	public void UpdateEvent(UpdateEvent ev){
		if(ev.getType()!=UpdateType.TICK||win==null)return;
		for(int i = 9; i <= 17; i++){
			setItemWithPlane(list[list_i],i);
			list_i++;
			
			if(i==13&&UtilItem.ItemNameEquals(getItem(13), win.getItemStack())){
				durchlauf++;
				if(durchlauf>=2){
					win.Clicked(((Player)getViewers().toArray()[0]), ActionType.R, win);
					win=null;
				}
			}
			
			if(list.length<list_i||list.length==list_i){
				list_i=0;
			}
		}
	}
	
	public void setItemWithPlane(LottoPackage l,int slot){
		if(getItem(slot-9).getType()==Material.STAINED_GLASS_PANE)getItem(slot-9).setData(new MaterialData(l.getType().getMaterial(), l.getType().getData()));
		setItem(slot, l.getItemStack());
		if(getItem(slot+9).getType()==Material.STAINED_GLASS_PANE)getItem(slot+9).setData(new MaterialData(l.getType().getMaterial(), l.getType().getData()));
	}
	
	public enum InventoryLotto2Type{
		COMMON(Material.STAINED_GLASS_PANE,(byte)0,0),
		UNCOMMON(Material.STAINED_GLASS_PANE,(byte)7,1),
		RARE(Material.STAINED_GLASS_PANE,(byte)6,2),
		LEGENDARY(Material.STAINED_GLASS_PANE,(byte)5,3),
		DIVINE(Material.STAINED_GLASS_PANE,(byte)2,4);
		
		@Getter
		private Material material;
		@Getter
		private byte data;
		@Getter
		private int i;
		InventoryLotto2Type(Material material,byte data,int i){
			this.material=material;
			this.data=data;
			this.i=i;
		}
	}
}
