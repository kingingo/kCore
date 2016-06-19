package eu.epicpvp.kcore.Inventory.Item;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.epicpvp.kcore.Util.UtilEvent.ActionType;

public interface IButtonOneSlot extends IButton{
	  public String getName();
	  public void setName(String name);
	  public void setDescription(String[] desc);
	  public String[] getDescription();
	  public void refreshItemStack();
	  public void setMaterial(Material material);
	  public void setMaterial(Material material,byte data);
	  public int getSlot();
	  public Click getClick();
	  public void Clicked(Player player,ActionType type,Object object);
	  public void setSlot(int slot);
	  public ItemStack getItemStack();
	  public void setItemStack(ItemStack item);
	}
