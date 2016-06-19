package eu.epicpvp.kcore.Inventory.Item;

import org.bukkit.entity.Player;

import eu.epicpvp.kcore.Util.UtilEvent.ActionType;

public interface Click {
	public void onClick(Player player, ActionType type,Object object);
}