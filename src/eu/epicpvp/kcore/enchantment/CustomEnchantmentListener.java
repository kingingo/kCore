package eu.epicpvp.kcore.enchantment;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import eu.epicpvp.kcore.Listener.kListener;
import eu.epicpvp.kcore.Util.UtilServer;

public class CustomEnchantmentListener extends kListener{

	public static CustomEnchantmentListener listener;
	
	public static CustomEnchantmentListener getListener(){
		if(listener==null)listener=new CustomEnchantmentListener();
		return listener;
	}
	
	private HashMap<String, CustomEnchantment> enchantments = new HashMap<>();
	
	public CustomEnchantmentListener() {
		super(UtilServer.getPluginInstance(), "CustomEnchantmentListener");
	}
	
	public void register(CustomEnchantment ce){
		if(!enchantments.containsKey(ce.getEnchantment())){
			enchantments.put(ce.getEnchantment(), ce);
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
