package eu.epicpvp.kcore.Inventory.Inventory;

//Volatile
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import eu.epicpvp.kcore.Inventory.Inventory.Craft.CraftInventoryPlayer;
import eu.epicpvp.kcore.Util.UtilPlayer;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.ItemStack;
import net.minecraft.server.v1_8_R3.PlayerInventory;

public class InventoryPlayer extends PlayerInventory{

	@Getter
	private CraftPlayer craftPlayer;
	@Getter
	private int playerId;
	@Getter
	private CraftInventoryPlayer inventory;
    private final ItemStack[] extra;
	
	public InventoryPlayer(Player player){
		super(UtilPlayer.getCraftPlayer(player).getHandle());
		this.craftPlayer = UtilPlayer.getCraftPlayer(player);
		this.playerId = UtilPlayer.getPlayerId(player);
		this.extra = new ItemStack[5];
		this.inventory = new CraftInventoryPlayer(this);
		this.items = this.player.inventory.items;
        this.armor = this.player.inventory.armor;
	}
	
	@Override
    public void onClose(CraftHumanEntity who) {
        super.onClose(who);
		this.craftPlayer.saveData();
		this.craftPlayer=null;
		this.playerId=0;
		this.inventory=null;
    }
	
	@Override
    public ItemStack[] getContents() {
        ItemStack[] C = new ItemStack[getSize()];
        System.arraycopy(items, 0, C, 0, items.length);
        System.arraycopy(armor, 0, C, items.length, armor.length);
        return C;
    }

    @Override
    public int getSize() {
        return super.getSize() + 5;
    }

    @Override
    public ItemStack getItem(int i) {
        ItemStack[] is = this.items;

        if (i >= is.length) {
            i -= is.length;
            is = this.armor;
        }
        else {
            i = getReversedItemSlotNum(i);
        }

        if (i >= is.length) {
            i -= is.length;
            is = this.extra;
        }
        else if (is == this.armor) {
            i = getReversedArmorSlotNum(i);
        }

        return is[i];
    }
    
    @Override
    public boolean a(EntityHuman entityhuman) {
        return true;
    }

    @Override
    public ItemStack splitStack(int i, int j) {
        ItemStack[] is = this.items;

        if (i >= is.length) {
            i -= is.length;
            is = this.armor;
        }
        else {
            i = getReversedItemSlotNum(i);
        }

        if (i >= is.length) {
            i -= is.length;
            is = this.extra;
        }
        else if (is == this.armor) {
            i = getReversedArmorSlotNum(i);
        }

        if (is[i] != null) {
            ItemStack itemstack;

            if (is[i].count <= j) {
                itemstack = is[i];
                is[i] = null;
                return itemstack;
            }
            else {
                itemstack = is[i].cloneAndSubtract(j);
                if (is[i].count == 0) {
                    is[i] = null;
                }

                return itemstack;
            }
        }
        else {
            return null;
        }
    }

    @Override
    public ItemStack splitWithoutUpdate(int i) {
        ItemStack[] is = this.items;

        if (i >= is.length) {
            i -= is.length;
            is = this.armor;
        }
        else {
            i = getReversedItemSlotNum(i);
        }

        if (i >= is.length) {
            i -= is.length;
            is = this.extra;
        }
        else if (is == this.armor) {
            i = getReversedArmorSlotNum(i);
        }

        if (is[i] != null) {
            ItemStack itemstack = is[i];

            is[i] = null;
            return itemstack;
        }
        else {
            return null;
        }
    }

    @Override
    public void setItem(int i, ItemStack itemstack) {
        ItemStack[] is = this.items;

        if (i >= is.length) {
            i -= is.length;
            is = this.armor;
        }
        else {
            i = getReversedItemSlotNum(i);
        }

        if (i >= is.length) {
            i -= is.length;
            is = this.extra;
        }
        else if (is == this.armor) {
            i = getReversedArmorSlotNum(i);
        }

        // Effects
        if (is == this.extra) {
            this.craftPlayer.getHandle().drop(itemstack, true);
            itemstack = null;
        }

        is[i] = itemstack;

        this.craftPlayer.getHandle().defaultContainer.b();
    }

    private int getReversedItemSlotNum(int i) {
        if (i >= 27)
            return i - 27;
        else
            return i + 9;
    }

    private int getReversedArmorSlotNum(int i) {
        if (i == 0)
            return 3;
        if (i == 1)
            return 2;
        if (i == 2)
            return 1;
        if (i == 3)
            return 0;
        else
            return i;
    }

    @Override
    public String getName() {
        if (player.getName().length() > 16) {
            return player.getName().substring(0, 16);
        }
        return player.getName();
    }
}
