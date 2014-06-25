package me.kingingo.kcore.Merchant;
import java.io.Serializable;
import net.minecraft.server.v1_7_R3.EntityHuman;
import net.minecraft.server.v1_7_R3.EntityPlayer;
import net.minecraft.server.v1_7_R3.IMerchant;
import net.minecraft.server.v1_7_R3.ItemStack;
import net.minecraft.server.v1_7_R3.MerchantRecipe;
import net.minecraft.server.v1_7_R3.MerchantRecipeList;

public final class NMSMerchant
  implements IMerchant, Serializable
{
  private static final long serialVersionUID = -1580276616590310266L;
  private MerchantRecipeList o = new MerchantRecipeList();
  private transient EntityHuman c;

  public void a(MerchantRecipe recipe)
  {
    this.o.add(recipe);
  }

  public void b_(EntityHuman player)
  {
    this.c = player;
  }

  public MerchantRecipeList getOffers(EntityHuman player)
  {
    return this.o;
  }

  public EntityHuman m_()
  {
    return this.c;
  }

  public void setRecipes(MerchantRecipeList recipes)
  {
    this.o = recipes;
  }

  public void openTrading(EntityPlayer player, String m)
  {
    this.c = player;
    this.c.openTrade(this, m);
  }

  public void a_(EntityHuman arg0)
  {
  }

  public void a_(ItemStack arg0)
  {
  }

  public EntityHuman b()
  {
    return null;
  }
}