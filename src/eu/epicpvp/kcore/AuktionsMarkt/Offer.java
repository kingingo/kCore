package eu.epicpvp.kcore.AuktionsMarkt;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.epicpvp.kcore.Inventory.InventoryPageBase;
import eu.epicpvp.kcore.Inventory.Item.Click;
import eu.epicpvp.kcore.Inventory.Item.Buttons.ButtonBase;
import eu.epicpvp.kcore.Translation.TranslationHandler;
import eu.epicpvp.kcore.Util.InventorySize;
import eu.epicpvp.kcore.Util.InventorySplit;
import eu.epicpvp.kcore.Util.UtilEvent.ActionType;
import eu.epicpvp.kcore.Util.UtilInv;
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
				if(UtilPlayer.getPlayerId(player) != getPlayerId()){
					InventoryPageBase buy = new InventoryPageBase(InventorySize._27, "§aBuy Offer");
					update();
					buy.setItem(InventorySplit._18.getMiddle(), getItemStack());
					
					ButtonBase yes = new ButtonBase(new Click(){

						@Override
						public void onClick(Player player, ActionType type, Object object) {
							closeOffer(player);
						}
						
					}, UtilItem.RenameItem(new ItemStack(Material.STAINED_GLASS_PANE,1,((byte)5)), "§aYES"));
					
					buy.addButton(InventorySplit._9.getMax(), yes);
					buy.addButton(InventorySplit._9.getMax()-1, yes);
					buy.addButton(InventorySplit._9.getMax()-2, yes);
					buy.addButton(InventorySplit._18.getMax(), yes);
					buy.addButton(InventorySplit._18.getMax()-1, yes);
					buy.addButton(InventorySplit._18.getMax()-2, yes);
					buy.addButton(InventorySplit._27.getMax(), yes);
					buy.addButton(InventorySplit._27.getMax()-1, yes);
					buy.addButton(InventorySplit._27.getMax()-2, yes);
					
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
					
					buy.fill(Material.STAINED_GLASS_PANE, ((byte)7));
					
					player.openInventory(buy);
					UtilInv.getBase().addAnother(buy);
				}else{
					InventoryPageBase buy = new InventoryPageBase(InventorySize._27, "§cDelete Offer");
					update();
					buy.setItem(InventorySplit._18.getMiddle(), getItemStack());
					
					ButtonBase yes = new ButtonBase(new Click(){

						@Override
						public void onClick(Player player, ActionType type, Object object) {
							AuktionsMarkt.getAuktionsMarkt().open(player);
						}
						
					}, UtilItem.RenameItem(new ItemStack(Material.STAINED_GLASS_PANE,1,((byte)5)), "§aAngebot bestehen bleiben"));
					
					buy.addButton(InventorySplit._9.getMax(), yes);
					buy.addButton(InventorySplit._9.getMax()-1, yes);
					buy.addButton(InventorySplit._9.getMax()-2, yes);
					buy.addButton(InventorySplit._18.getMax(), yes);
					buy.addButton(InventorySplit._18.getMax()-1, yes);
					buy.addButton(InventorySplit._18.getMax()-2, yes);
					buy.addButton(InventorySplit._27.getMax(), yes);
					buy.addButton(InventorySplit._27.getMax()-1, yes);
					buy.addButton(InventorySplit._27.getMax()-2, yes);
					
					ButtonBase no = new ButtonBase(new Click(){

						@Override
						public void onClick(Player player, ActionType type, Object object) {
							closeOffer();
							AuktionsMarkt.getAuktionsMarkt().open(player);
						}
						
					}, UtilItem.RenameItem(new ItemStack(Material.STAINED_GLASS_PANE,1,((byte)14)), "§cAngebot zurück ziehen"));
					
					buy.addButton(InventorySplit._9.getMin(), no);
					buy.addButton(InventorySplit._9.getMin()+1, no);
					buy.addButton(InventorySplit._9.getMin()+2, no);
					buy.addButton(InventorySplit._18.getMin(), no);
					buy.addButton(InventorySplit._18.getMin()+1, no);
					buy.addButton(InventorySplit._18.getMin()+2, no);
					buy.addButton(InventorySplit._27.getMin(), no);
					buy.addButton(InventorySplit._27.getMin()+1, no);
					buy.addButton(InventorySplit._27.getMin()+2, no);
					
					buy.fill(Material.STAINED_GLASS_PANE, ((byte)7));
					
					player.openInventory(buy);
					UtilInv.getBase().addAnother(buy);
				}
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
		Player player = UtilPlayer.searchExact(playerId);
		if(player!=null){
			if(player.getOpenInventory()!=null
					&& player.getOpenInventory().getBottomInventory().getHolder() instanceof AuktionsInventoryHolder){
				player.closeInventory();
			}
			
			player.sendMessage(TranslationHandler.getPrefixAndText(player, "markt.offer.closed"));
		}
		
		kConfig config = UtilServer.getUserData().getConfig(UtilPlayer.getPlayerId(buyer));
		ItemStack[] items = config.getItemStackArray("auktionsMartk.items");
		ItemStack[] newItems;
		if(items!=null){
			newItems = new ItemStack[items.length+(amount/64)+((amount%64)!=0?1:0)];
			for(int i = 0; i < items.length; i++)newItems[i]=items[i];
		}else{
			newItems = new ItemStack[(amount/64)+((amount%64)!=0?1:0)];
		}
		
		for(int i = 0; i < (amount/64); i++)newItems[i]=new ItemStack(material,64,data);
		if((amount%64)!=0)newItems[newItems.length-1]=new ItemStack(material,(amount%64),data);
		
		config.setItemStackArray("auktionsMartk.items", newItems);
		config.save();
		AuktionsMarkt.getAuktionsMarkt().getOffers().remove(this);
		AuktionsMarkt.getAuktionsMarkt().open(buyer);
	}
	
	public void openOfferInv(Player player){
		
	}
	
	public void closeOffer(){
		Player player = UtilPlayer.searchExact(playerId);
		if(player!=null){
			if(player.getOpenInventory()!=null
					&& player.getOpenInventory().getBottomInventory().getHolder() instanceof AuktionsInventoryHolder){
				player.closeInventory();
			}

			player.sendMessage(TranslationHandler.getPrefixAndText(player, "markt.offer.closed"));
		}
		
		kConfig config = UtilServer.getUserData().getConfig(playerId);
		ItemStack[] items = config.getItemStackArray("auktionsMartk.items");
		
		ItemStack[] newItems;
		if(items!=null){
			newItems = new ItemStack[items.length+(amount/64)+((amount%64)!=0?1:0)];
			for(int i = 0; i < items.length; i++)newItems[i]=items[i];
		}else{
			newItems = new ItemStack[(amount/64)+((amount%64)!=0?1:0)];
		}
		
		for(int i = 0; i < (amount/64); i++)newItems[i]=new ItemStack(material,64,data);
		if((amount%64)!=0)newItems[newItems.length-1]=new ItemStack(material,(amount%64),data);
		
		config.setItemStackArray("auktionsMartk.items", newItems);
		config.save();
		AuktionsMarkt.getAuktionsMarkt().getOffers().remove(this);
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
