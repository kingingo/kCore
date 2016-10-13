package eu.epicpvp.kcore.AuktionsMarkt;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import eu.epicpvp.kcore.Inventory.Item.Click;
import eu.epicpvp.kcore.Inventory.Item.Buttons.ButtonBase;
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
	private ItemStack item;
	private int playerId;
	private long end;
	private String playerName;
	private ItemKategorie kategorie;
	
	public Offer(ItemKategorie kategorie, int playerId, ItemStack item, double price,long time) {
		super(new Click() {
			
			@Override
			public void onClick(Player player, ActionType type, Object object) {
				//TODO BUY!
			}
		},UtilItem.setLore(item.clone(), new String[]{""}));
		
		this.price=price;
		this.item=item;
		this.playerId=playerId;
		this.playerName=UtilServer.getClient().getPlayer(playerId).getName();
		this.end=System.currentTimeMillis()+time;
		this.kategorie=kategorie;
	}
	
	public ItemStack getItemWithDescription(){
		return UtilItem.setLore(item.clone(), new String[]{
				"",
				"§ePreis: §7"+price+(price==1?" Epic":" Epic's"),
				"",
				"§eVerbleibende zeit im Markt: §7"+UtilTime.formatMili( (end-System.currentTimeMillis()) ),
				"",
				"§7Rechtsklick, um zu §centfernen"});
	}

	public void closeOffer(){
		Player player = UtilPlayer.searchExact(playerId);
		if(player!=null){
			player.sendMessage(""); //TODO AUKTION OVER
		}
		
		kConfig config = UtilServer.getUserData().getConfig(playerId);
		Inventory inv = config.getInventory("AuktionsMarkt.inv");
		inv.addItem(item);
		config.setInventory("AuktionsMarkt.inv", inv);
		config.save();
		kategorie.getOffers().remove(this);
	}
	
	public void update(){
		if(end > System.currentTimeMillis()){
			setDescription(new String[]{
				"",
				"§ePreis: §7"+price+(price==1?" Epic":" Epic's"),
				"",
				"§eVerbleibende zeit im Markt: §7"+UtilTime.formatMili( (end-System.currentTimeMillis()) ),
				"",
				"§7Angebot von §c"+playerName});
			
			refreshItemStack();
		}else{
			closeOffer();
		}
	}
}
