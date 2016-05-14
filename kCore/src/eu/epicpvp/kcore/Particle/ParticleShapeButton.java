package eu.epicpvp.kcore.Particle;

import dev.wolveringer.dataserver.gamestats.StatsKey;
import dev.wolveringer.nbt.NBTTagCompound;
import eu.epicpvp.kcore.Inventory.InventoryPageBase;
import eu.epicpvp.kcore.Inventory.Item.Buttons.ButtonCopy;
import eu.epicpvp.kcore.Translation.TranslationHandler;
import eu.epicpvp.kcore.Inventory.Item.Click;
import eu.epicpvp.kcore.Util.UtilEvent.ActionType;
import eu.epicpvp.kcore.Util.UtilItem;
import eu.epicpvp.kcore.Util.UtilServer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ParticleShapeButton extends ButtonCopy {

	public ParticleShapeButton(int slot, WingShop shop, ParticleShape<?, ?> particle) {
		super(new Click() {

			@Override
			public void onClick(Player player, ActionType type, Object object) {
				if (player.isOp() || UtilServer.getPermissionManager().hasPermission(player, particle.getPermission())) {
					if(shop.getParticleDisplayers().containsKey(player.getUniqueId()) && shop.getParticleDisplayers().get(player.getUniqueId()).getShape().equals(particle)){
						((InventoryPageBase) object).setItem(slot, UtilItem.addEnchantmentGlow(UtilItem.RenameItem(new ItemStack(351, 1, (byte) 10), "§7" + particle.getName())));
					} else {
						((InventoryPageBase) object).setItem(slot, UtilItem.addEnchantmentGlow(UtilItem.RenameItem(new ItemStack(351, 1, (byte) 8), "§7" + particle.getName())));
					}
				}
			}
		}, new Click() {

			@Override
			public void onClick(Player player, ActionType type, Object object) {
				player.closeInventory();
				if (player.isOp() || UtilServer.getPermissionManager().hasPermission(player, particle.getPermission())) {
					PlayerParticleDisplayer oldParticleDisplayer = shop.getParticleDisplayers().remove(player.getUniqueId());
					if (oldParticleDisplayer != null) {
						oldParticleDisplayer.stop();

						NBTTagCompound nbt = shop.getStatsManager().getNBTTagCompound(player, StatsKey.PROPERTIES);
						nbt.remove("wings");
						try {
							shop.getStatsManager().setNBTTagCompound(player, nbt, StatsKey.PROPERTIES);
						} catch (Exception e) {
							e.printStackTrace();
						}
						if (oldParticleDisplayer.getShape().equals(particle)) {
							return;
						}
					}
					PlayerParticleDisplayer particleDisplayer = new PlayerParticleDisplayer<>(player, particle);
					particleDisplayer.start(shop.getInstance());
					shop.getParticleDisplayers().put(player.getUniqueId(), particleDisplayer);

					NBTTagCompound nbt = shop.getStatsManager().getNBTTagCompound(player, StatsKey.PROPERTIES);
					nbt.setString("wings", particle.getName());
					try {
						shop.getStatsManager().setNBTTagCompound(player, nbt, StatsKey.PROPERTIES);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}else{
					player.sendMessage(TranslationHandler.getPrefixAndText(player, "IN_MYSTERYBOXES"));
				}
			}
		}, UtilItem.RenameItem(new ItemStack(351, 1, (byte) 8), "§7" + particle.getName()));
	}
}
