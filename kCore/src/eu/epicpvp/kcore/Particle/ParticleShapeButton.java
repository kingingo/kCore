package eu.epicpvp.kcore.Particle;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import dev.wolveringer.dataserver.gamestats.StatsKey;
import dev.wolveringer.nbt.NBTTagCompound;
import eu.epicpvp.kcore.Inventory.InventoryPageBase;
import eu.epicpvp.kcore.Inventory.Item.Click;
import eu.epicpvp.kcore.Inventory.Item.Buttons.ButtonCopy;
import eu.epicpvp.kcore.Util.UtilEvent.ActionType;
import eu.epicpvp.kcore.Util.UtilItem;
import eu.epicpvp.kcore.Util.UtilServer;

public class ParticleShapeButton extends ButtonCopy{

	public ParticleShapeButton(int slot, WingShop shop,ParticleShape particle) {
		super(new Click(){

			@Override
			public void onClick(Player player, ActionType type, Object object) {
				if(player.isOp() || UtilServer.getPermissionManager().hasPermission(player, particle.getPermission())){
					((InventoryPageBase)object).setItem(slot, UtilItem.addEnchantmentGlow(particle.getItem()));
				}else{
					((InventoryPageBase)object).setItem(slot, particle.getItem());
				}
			}
			
		}, new Click(){

			@Override
			public void onClick(Player player, ActionType type, Object object) {
				if(player.isOp() || UtilServer.getPermissionManager().hasPermission(player, particle.getPermission())){
					player.closeInventory();
					if(shop.getPlayers().containsKey(player)){
						PlayerParticle p = shop.getPlayers().get(player);
						shop.getPlayers().remove(player);
						p.stop();
						
						NBTTagCompound nbt = shop.getStatsManager().getNBTTagCompound(player, StatsKey.PROPERTIES);
						nbt.remove("wings");
						try {
							shop.getStatsManager().setNBTTagCompound(player, nbt, StatsKey.PROPERTIES);
						} catch (Exception e) {
							e.printStackTrace();
						}
						if(p.getShape().equals(particle))return;
					}
					shop.getPlayers().put(player, new PlayerParticle<>(player, particle));
					shop.getPlayers().get(player).start(shop.getInstance());
					
					NBTTagCompound nbt = shop.getStatsManager().getNBTTagCompound(player, StatsKey.PROPERTIES);
					nbt.setInt("wings", particle.getItem().getTypeId());
					try {
						shop.getStatsManager().setNBTTagCompound(player, nbt, StatsKey.PROPERTIES);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			
		}, particle.getItem());
	}

}
