package me.kingingo.kcore.Merchant;
import java.io.Serializable;
import net.minecraft.server.v1_7_R4.MerchantRecipe;
import org.bukkit.craftbukkit.v1_7_R4.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public final class MerchantOffer
  implements Serializable
{
  private static final long serialVersionUID = -2230848596793706680L;
  private ItemStack[] items = new ItemStack[3];

  public MerchantOffer(ItemStack is1, ItemStack is2, ItemStack re)
  {
    this.items[0] = is1;
    this.items[1] = is2;
    this.items[2] = re;
  }

  public MerchantOffer(ItemStack is, ItemStack re)
  {
    this(is, null, re);
  }

  protected MerchantOffer(MerchantRecipe handle)
  {
    this.items[0] = CraftItemStack.asBukkitCopy(handle.getBuyItem1());
    this.items[1] = (handle.getBuyItem3() == null ? null : CraftItemStack.asBukkitCopy(handle.getBuyItem2()));
    this.items[2] = CraftItemStack.asBukkitCopy(handle.getBuyItem3());
  }

  protected MerchantRecipe getHandle()
  {
    if (this.items[1] != null) {
      return new MerchantRecipe(CraftItemStack.asNMSCopy(this.items[0]), CraftItemStack.asNMSCopy(this.items[1]), CraftItemStack.asNMSCopy(this.items[2]));
    }

    return new MerchantRecipe(CraftItemStack.asNMSCopy(this.items[0]), CraftItemStack.asNMSCopy(this.items[2]));
  }

  public ItemStack getFirstInput()
  {
    return this.items[0];
  }

  public ItemStack getSecondInput()
  {
    return this.items[1];
  }

  public ItemStack getOutput()
  {
    return this.items[2];
  }
}