package eu.epicpvp.kcore.GagdetShop.Gagdet;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import dev.wolveringer.dataserver.gamestats.StatsKey;
import dev.wolveringer.nbt.NBTTagCompound;
import eu.epicpvp.kcore.GagdetShop.GadgetHandler;
import eu.epicpvp.kcore.Listener.kListener;
import eu.epicpvp.kcore.Translation.TranslationHandler;
import eu.epicpvp.kcore.Util.TimeSpan;
import eu.epicpvp.kcore.Util.UtilInv;
import eu.epicpvp.kcore.Util.UtilItem;
import eu.epicpvp.kcore.Util.UtilTime;
import lombok.Getter;

public class Gadget extends kListener{

	@Getter
	private GadgetHandler handler;
	@Getter
	private int gems;
	@Getter
	private int coins;
	@Getter
	private int buyAmount;
	@Getter
	private String name;
	@Getter
	private ItemStack item;
	@Getter
	private HashMap<Player,Integer> active_player;

	public Gadget(GadgetHandler handler, String name,ItemStack item){
		this(handler,name,item,50,750,2000);
	}
	
	public Gadget(GadgetHandler handler, String name,ItemStack item,int buyAmount,int gems,int coins){
		super(handler.getInstance(),"Gadget - "+name);
		this.handler=handler;
		this.name=name;
		this.item=item;
		this.active_player=new HashMap<>();
		this.gems=gems;
		this.coins=coins;
		this.buyAmount=buyAmount;
	}
	
	public void setAmount(Player player){
		if(this.active_player.containsKey(player)){
			setAmount(player, this.active_player.get(player));
		}
	}
	
	public void setAmount(Player player,int amount){
		try {
			NBTTagCompound nbt = getHandler().getStatsManager().getNBTTagCompound(player, StatsKey.PROPERTIES);
			nbt.setInt("gadgets"+getName(), amount);
			getHandler().getStatsManager().setNBTTagCompound(player, nbt, StatsKey.PROPERTIES);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public int getAmount(Player player){
		NBTTagCompound nbt = getHandler().getStatsManager().getNBTTagCompound(player, StatsKey.PROPERTIES);
		
		if(nbt.hasKey("gadgets"+getName())){
			return nbt.getInt("gadgets"+getName());
		}
		return 0;
	}

	public void addPlayer(Player player){
		addPlayer(player, getAmount(player));
	}
	
	public void addPlayer(Player player, int amount){
		for(Gadget gadget : this.handler.getGadgets())gadget.removePlayer(player);
		
		this.active_player.put(player,amount);
		player.getInventory().addItem(getItem());
	}
	
	public void removePlayer(Player player){
		if(this.active_player.containsKey(player)){
			setAmount(player);
			UtilInv.remove(player, getItem().getType(), getItem().getData().getData(), 1);
			this.active_player.remove(player);
		}
	}
	
	public boolean use(Player player){
		String timer = UtilTime.getTimeManager().check("gagdet", player);
		
		if(this.active_player.containsKey(player) && timer == null){
			if(!player.isOp())UtilTime.getTimeManager().add("gagdet", player, TimeSpan.SECOND*10);
			this.active_player.put(player, this.active_player.get(player)-1);
			UtilItem.RenameItem(player.getItemInHand(), "§e"+getName()+" §7(§e"+this.active_player.get(player)+"§7)");
			setAmount(player);
			
			if(this.active_player.get(player)<=0){
				removePlayer(player);
			}
			return true;
		}
		
		if(timer!=null)player.sendMessage(TranslationHandler.getText(player, "PREFIX")+TranslationHandler.getText(player, "USE_BEFEHL_TIME",timer));
		return false;
	}
	
}
