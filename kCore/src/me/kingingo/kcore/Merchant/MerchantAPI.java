package me.kingingo.kcore.Merchant;
import net.minecraft.server.v1_7_R3.EntityVillager;
import net.minecraft.server.v1_7_R3.MerchantRecipeList;
import net.minecraft.server.v1_7_R3.NBTTagCompound;
import org.bukkit.craftbukkit.v1_7_R3.entity.CraftVillager;
import org.bukkit.entity.Villager;

public class MerchantAPI
{
  public static void setToVillager(Villager villager, Merchant merchant)
  {
    EntityVillager v = ((CraftVillager)villager).getHandle();

    NBTTagCompound t = new NBTTagCompound();
    v.b(t);

    MerchantRecipeList l = merchant.getOffers().getHandle();
    t.set("Offers", l.a());

    v.a(t);
  }
}