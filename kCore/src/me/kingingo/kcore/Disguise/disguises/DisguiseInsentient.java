package me.kingingo.kcore.Disguise.disguises;
import java.util.ArrayList;
import java.util.List;

import me.kingingo.kcore.PacketAPI.kPacket;
import me.kingingo.kcore.PacketAPI.Packets.kPacketPlayOutEntityEquipment;
import net.minecraft.server.v1_8_R3.ItemStack;

public abstract class DisguiseInsentient extends DisguiseLiving
{
  private boolean _showArmor;

  public DisguiseInsentient(org.bukkit.entity.Entity entity)
  {
    super(entity);

//    this.DataWatcher.a(11, Byte.valueOf((byte)0));
//    this.DataWatcher.a(10, "");
    this.DataWatcher.a(15, Byte.valueOf((byte)0));
  }

  public String getName()
  {
	  return this.DataWatcher.getCustomName();
  }
  
  public void SetName(String name)
  {
    this.DataWatcher.setCustomName(name);
  }

  public boolean HasCustomName()
  {
    return this.DataWatcher.hasCustomName();
  }

  public void SetCustomNameVisible(boolean visible)
  {
    this.DataWatcher.setCustomNameVisible(visible);
  }

  public boolean GetCustomNameVisible()
  {
    return this.DataWatcher.isVisible();
  }

  public boolean armorVisible()
  {
    return this._showArmor;
  }

  public void showArmor()
  {
    this._showArmor = true;
  }

  public void hideArmor()
  {
    this._showArmor = false;
  }

  public List<kPacket> getArmorPackets()
  {
    List p5 = new ArrayList();
    ItemStack[] armorContents = this.Entity.getEquipment();

    for (short i = 0; i < armorContents.length; i = (short)(i + 1))
    {
      ItemStack armorSlot = armorContents[i];

      if (armorSlot != null)
      {
        p5.add(new kPacketPlayOutEntityEquipment(this.Entity.getId(), i, armorSlot));
      }
    }

    return null;
  }
}