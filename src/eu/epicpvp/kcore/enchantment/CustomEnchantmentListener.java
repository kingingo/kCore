package eu.epicpvp.kcore.enchantment;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.Ambient;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BlockVector;

import eu.epicpvp.kcore.Inventory.InventoryPageBase;
import eu.epicpvp.kcore.Inventory.Item.Click;
import eu.epicpvp.kcore.Inventory.Item.Buttons.ButtonBase;
import eu.epicpvp.kcore.Listener.kListener;
import eu.epicpvp.kcore.Util.InventorySize;
import eu.epicpvp.kcore.Util.InventorySplit;
import eu.epicpvp.kcore.Util.UtilInv;
import eu.epicpvp.kcore.Util.UtilItem;
import eu.epicpvp.kcore.Util.UtilEvent.ActionType;
import eu.epicpvp.kcore.deliverychest.CommandSetDelivery;
import eu.epicpvp.kcore.Util.UtilServer;
import lombok.Getter;

public class CustomEnchantmentListener extends kListener{

	public static CustomEnchantmentListener listener;
	
	public static CustomEnchantmentListener getListener(){
		if(listener==null)listener=new CustomEnchantmentListener();
		return listener;
	}
	
	private HashMap<String, CustomEnchantment> enchantments = new HashMap<>();
	@Getter
	private BlockVector anvil;
	
	public CustomEnchantmentListener() {
		super(UtilServer.getPluginInstance(), "CustomEnchantmentListener");
		
		if(getPlugin().getConfig().contains("customenchantments.anvil"))
			this.anvil = getPlugin().getConfig().getVector("customenchantments.anvil").toBlockVector();
		UtilServer.getCommandHandler().register(CommandSetAnvil.class, new CommandSetAnvil());
	}
	
	public void setAnvil(BlockVector block){
		anvil=block;
		getPlugin().getConfig().set("customenchantments.anvil", block);
		getPlugin().saveConfig();
	}
	
	public void register(CustomEnchantment ce){
		if(!enchantments.containsKey(ce.getEnchantment())){
			enchantments.put(ce.getEnchantment(), ce);
		}
	}
	
	@EventHandler
	public void click(PlayerInteractEvent ev){
		if(anvil!=null && ev.getAction() == Action.RIGHT_CLICK_BLOCK){
			if(ev.getClickedBlock()!=null&&ev.getClickedBlock().getType()==Material.ANVIL){
				if(ev.getClickedBlock().getLocation().toVector().toBlockVector().equals(anvil)){
					InventoryPageBase anvil = new InventoryPageBase(InventorySize._27, "§7Anvil");
					anvil.fill(Material.STAINED_GLASS_PANE,((byte)15));
					
					anvil.setItem(InventorySplit._18.getMiddle()-2, null);
					anvil.setItem(InventorySplit._18.getMiddle()+2, null);
					
					anvil.addButton(InventorySplit._18.getMiddle(), new ButtonBase(new Click(){

						@Override
						public void onClick(Player player, ActionType type, Object object) {
							
						}
						
					}, UtilItem.RenameItem(new ItemStack(Material.EXP_BOTTLE), "§aenchant")));
					
					UtilInv.getBase().addAnother(anvil);
					ev.getPlayer().openInventory(anvil);
					ev.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler
	public void hit(EntityDamageByEntityEvent ev){
		if(ev.getDamager() instanceof Player){
			Player damager = (Player)ev.getDamager();
			
			if(damager.getItemInHand()!=null){
				for(CustomEnchantment ce : enchantments.values()){
					if(ce.contains(damager.getItemInHand())){
						ce.getEdo().hit(ev.getEntity());
					}
				}
			}
		}
	}

}
