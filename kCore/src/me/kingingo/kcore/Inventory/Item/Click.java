package me.kingingo.kcore.Inventory.Item;

import me.kingingo.kcore.Util.UtilEvent.ActionType;

import org.bukkit.entity.Player;

public interface Click {
	public void onClick(Player player, ActionType type,Object object);
}