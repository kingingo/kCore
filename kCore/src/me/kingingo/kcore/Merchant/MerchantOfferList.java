package me.kingingo.kcore.Merchant;

import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.server.v1_7_R3.MerchantRecipe;
import net.minecraft.server.v1_7_R3.MerchantRecipeList;

public final class MerchantOfferList extends ArrayList<MerchantOffer>
{
  private static final long serialVersionUID = 7856998541433225645L;

  protected MerchantOfferList(MerchantRecipeList handle)
  {
    for (Iterator localIterator = handle.iterator(); localIterator.hasNext(); ) { Object r = localIterator.next();
      add(new MerchantOffer((MerchantRecipe)r));
    }
  }

  protected MerchantRecipeList getHandle()
  {
    MerchantRecipeList list = new MerchantRecipeList();

    for (MerchantOffer o : this) {
      list.add(o.getHandle());
    }

    return list;
  }
}