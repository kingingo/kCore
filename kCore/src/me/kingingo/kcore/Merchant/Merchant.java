package me.kingingo.kcore.Merchant;
import java.io.Serializable;
import net.minecraft.server.v1_7_R3.EntityHuman;
import org.bukkit.craftbukkit.v1_7_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class Merchant
  implements Serializable
{
  private static final long serialVersionUID = -7657859047817139872L;
  private NMSMerchant h;

  public Merchant()
  {
    this.h = new NMSMerchant();
  }

  public Merchant addOffer(MerchantOffer offer)
  {
    this.h.a(offer.getHandle());
    return this;
  }

  public Merchant addOffers(MerchantOffer[] offers)
  {
    for (MerchantOffer o : offers) {
      addOffer(o);
    }

    return this;
  }

  public Merchant setOffers(MerchantOfferList offers)
  {
    this.h.setRecipes(offers.getHandle());
    return this;
  }

  public MerchantOfferList getOffers()
  {
    return new MerchantOfferList(this.h.getOffers(null));
  }

  public boolean hasCustomer()
  {
    return this.h.m_() != null;
  }

  public Player getCustomer()
  {
    return (Player)(this.h.m_() == null ? null : this.h.m_().getBukkitEntity());
  }

  public Merchant setCustomer(Player player)
  {
    this.h.b_(player == null ? null : ((CraftPlayer)player).getHandle());
    return this;
  }

  public void openTrading(Player player, String m)
  {
    this.h.openTrading(((CraftPlayer)player).getHandle(), m);
  }

  protected NMSMerchant getHandle()
  {
    return this.h;
  }

  public Merchant clone()
  {
    return new Merchant().setOffers(getOffers()).setCustomer(getCustomer());
  }
}