package me.kingingo.kcore.Inventory.Item;

import me.kingingo.kcore.Util.UtilEvent.ActionType;

import org.bukkit.entity.Player;

public interface BooleanClick {
	public boolean onBooleanClick(Player player, ActionType type,Object object);
}