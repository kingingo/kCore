package eu.epicpvp.kcore.AuktionsMarkt;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import eu.epicpvp.kcore.Inventory.InventoryPageBase;
import eu.epicpvp.kcore.Inventory.Item.Click;
import eu.epicpvp.kcore.Inventory.Item.Buttons.ButtonBase;
import eu.epicpvp.kcore.Util.InventorySize;
import eu.epicpvp.kcore.Util.InventorySplit;
import eu.epicpvp.kcore.Util.UtilEvent.ActionType;
import eu.epicpvp.kcore.Util.UtilItem;
import eu.epicpvp.kcore.Util.UtilPlayer;
import eu.epicpvp.kcore.Util.UtilServer;
import eu.epicpvp.kcore.Util.UtilTime;
import eu.epicpvp.kcore.kConfig.kConfig;
import lombok.Getter;

@Getter
public class Offer extends ButtonBase{

	private double price;
	private Material material;
	private byte data;
	private int amount;
	private int playerId;
	private long end;
	private String playerName;
	
	public Offer(int playerId,String playerName, Material material, byte data, int amount, double price,long time) {
		super(null,new ItemStack(material,1,data));
		
		Click click = new Click() {
			
			@Override
			public void onClick(Player player, ActionType type, Object object) {
				InventoryPageBase buy = new InventoryPageBase(InventorySize._36, "");
				update();
				buy.setItem(InventorySplit._18.getMiddle(), getItemStack());
				
				ButtonBase yes = new ButtonBase(new Click(){

					@Override
					public void onClick(Player player, ActionType type, Object object) {
						closeOffer(player);
					}
					
				}, UtilItem.RenameItem(new ItemStack(Material.STAINED_GLASS_PANE,1,((byte)5)), "§aYES"));
				
				buy.addButton(InventorySplit._9.getMin(), yes);
				buy.addButton(InventorySplit._9.getMin()+1, yes);
				buy.addButton(InventorySplit._9.getMin()+2, yes);
				buy.addButton(InventorySplit._18.getMin(), yes);
				buy.addButton(InventorySplit._18.getMin()+1, yes);
				buy.addButton(InventorySplit._18.getMin()+2, yes);
				buy.addButton(InventorySplit._27.getMin(), yes);
				buy.addButton(InventorySplit._27.getMin()+1, yes);
				buy.addButton(InventorySplit._27.getMin()+2, yes);
				
				ButtonBase no = new ButtonBase(new Click(){

					@Override
					public void onClick(Player player, ActionType type, Object object) {
						AuktionsMarkt.getAuktionsMarkt().open(player);
					}
					
				}, UtilItem.RenameItem(new ItemStack(Material.STAINED_GLASS_PANE,1,((byte)14)), "§cNO"));
				
				buy.addButton(InventorySplit._9.getMin(), no);
				buy.addButton(InventorySplit._9.getMin()+1, no);
				buy.addButton(InventorySplit._9.getMin()+2, no);
				buy.addButton(InventorySplit._18.getMin(), no);
				buy.addButton(InventorySplit._18.getMin()+1, no);
				buy.addButton(InventorySplit._18.getMin()+2, no);
				buy.addButton(InventorySplit._27.getMin(), no);
				buy.addButton(InventorySplit._27.getMin()+1, no);
				buy.addButton(InventorySplit._27.getMin()+2, no);
			}
		};
		
		setClick(click);
		
		this.price=price;
		this.material=material;
		this.data=data;
		this.amount=amount;
		this.playerId=playerId;
		this.playerName=playerName;
		this.end=System.currentTimeMillis()+time;
	}
	
	public ItemStack getItemWithDescription(){
		return UtilItem.setLore(new ItemStack(material,1,data), new String[]{
				" ",
				"§eAnzahl: §7"+amount,
				"§ePreis: §7"+price+(price==1?" Epic":" Epic's"),
				" ",
				"§eVerbleibende zeit im Markt: §7"+UtilTime.formatMili( (end-System.currentTimeMillis()) ),
				" ",
				"§7Rechtsklick, um zu §centfernen"});
	}

	public void closeOffer(Player buyer){
		
	}
	
	public void closeOffer(){
		Player player = UtilPlayer.searchExact(playerId);
		if(player!=null){
			player.sendMessage(""); //TODO AUKTION OVER
		}
		
		kConfig config = UtilServer.getUserData().getConfig(playerId);
		Inventory inv = config.getInventory("AuktionsMarkt.inv");
		
		//TODO ADD ITEMS!
		
		config.setInventory("AuktionsMarkt.inv", inv);
		config.save();
	}
	
	public void update(){
		if(end > System.currentTimeMillis()){
			setDescription(new String[]{
				" ",
				"§eAnzahl: §7"+amount,
				"§ePreis: §7"+price+(price==1?" Epic":" Epic's"),
				" ",
				"§eVerbleibende zeit im Markt: §7"+UtilTime.formatMili( (end-System.currentTimeMillis()) ),
				" ",
				"§7Angebot von §c"+playerName});
			refreshItemStack();
		}else{
			closeOffer();
		}
	}
}
