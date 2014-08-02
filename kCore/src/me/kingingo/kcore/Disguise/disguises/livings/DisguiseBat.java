package me.kingingo.kcore.Disguise.disguises.livings;
import me.kingingo.kcore.Disguise.disguises.DisguiseAnimal;

import org.bukkit.entity.Entity;

public class DisguiseBat extends DisguiseAnimal
{
  public DisguiseBat(Entity entity)
  {
    super(entity);

    this.DataWatcher.a(16, new Byte((byte)0));
  }

  public boolean isSitting()
  {
    return (this.DataWatcher.getByte(16) & 0x1) != 0;
  }

  public void setSitting(boolean paramBoolean)
  {
    int i = this.DataWatcher.getByte(16);
    if (paramBoolean)
      this.DataWatcher.watch(16, Byte.valueOf((byte)(i | 0x1)));
    else
      this.DataWatcher.watch(16, Byte.valueOf((byte)(i & 0xFFFFFFFE)));
  }

  protected int GetEntityTypeId()
  {
    return 65;
  }

  public String getHurtSound()
  {
    return "mob.bat.hurt";
  }
}