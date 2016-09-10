package eu.epicpvp.kcore.AACHack;

import me.konsolas.aac.api.AACAPIProvider;
import me.konsolas.aac.api.HackType;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class ScaffoldWalkCheck implements Listener {
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		Player plr = event.getPlayer();
		if (!event.getBlockPlaced().getType().isSolid()) {
			return;
		}
		Block plrBlock = plr.getLocation().getBlock().getRelative(BlockFace.DOWN);
		if (!plrBlock.getType().isSolid()) {
			return;
		}
		if (!AACAPIProvider.getAPI().isEnabled(HackType.IMPOSSIBLEINTERACT)) {
			return;
		}
		if (event.getBlockAgainst().equals(plrBlock)) { //if he sneaked / moved so he is above the edge, his center is not the block he place his block against
			try {
				System.out.println("scaffoldwalk detected");
				if (AACAccessor.increaseAllViolationsAndNotify(plr.getUniqueId(), 4, HackType.IMPOSSIBLEINTERACT, "(Custom) (ScaffoldWalk) " + plr.getName() + " is suspected for ImpossibleInteraction with the block at his feed")) {
					System.out.println("scaffoldwalk blocked");
					event.setCancelled(true);
				}
			} catch (ReflectiveOperationException e) {
				e.printStackTrace();
			}
		}
	}
}