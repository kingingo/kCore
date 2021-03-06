package eu.epicpvp.kcore.Disguise.disguises.livings;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import eu.epicpvp.kcore.Disguise.disguises.DisguiseTameableAnimal;
import net.minecraft.server.v1_8_R3.EnumColor;

public class DisguiseWolf extends DisguiseTameableAnimal
{
  public DisguiseWolf(Entity entity)
  {
    super(entity);

    this.DataWatcher.a(18, new Float(getHealth()));
    this.DataWatcher.a(19, new Byte((byte)0));
    this.DataWatcher.a(20, new Byte((byte)EnumColor.RED.getColorIndex()));
  }

  public boolean isAngry()
  {
    return (this.DataWatcher.getByte(16) & 0x2) != 0;
  }

  public void setAngry(boolean angry)
  {
    byte b0 = this.DataWatcher.getByte(16);

    if (angry)
      this.DataWatcher.watch(16, Byte.valueOf((byte)(b0 | 0x2)));
    else
      this.DataWatcher.watch(16, Byte.valueOf((byte)(b0 & 0xFFFFFFFD)));
  }

  public int getCollarColor()
  {
    return this.DataWatcher.getByte(20) & 0xF;
  }

  public void setCollarColor(int i)
  {
    this.DataWatcher.watch(20, Byte.valueOf((byte)(i & 0xF)));
  }

  public void p(boolean flag)
  {
    if (flag)
      this.DataWatcher.watch(19, Byte.valueOf((byte)1));
    else
      this.DataWatcher.watch(19, Byte.valueOf((byte)0));
  }

  public boolean cx()
  {
    return this.DataWatcher.getByte(19) == 1;
  }

  public EntityType GetEntityTypeId()
  {
    return EntityType.WOLF;
  }

  protected String getHurtSound()
  {
    return "mob.wolf.hurt";
  }
}