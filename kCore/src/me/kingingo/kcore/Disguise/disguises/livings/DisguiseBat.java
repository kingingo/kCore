package me.kingingo.kcore.Disguise.disguises.livings;
import me.kingingo.kcore.Disguise.disguises.DisguiseAnimal;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

public class DisguiseBat extends DisguiseAnimal
{
  public DisguiseBat(Entity entity)
  {
    super(entity);

    this.DataWatcher.a(16, new Byte((byte)0));
  }

  public boolean isAsleep()
  {
    return (this.DataWatcher.getByte(16) & 0x1) != 0;
  }

  public void setAsleep(boolean paramBoolean)
  {
	byte i = this.DataWatcher.getByte(16);
    if (paramBoolean)
      this.DataWatcher.watch(16, Byte.valueOf((byte)(i | 0x1)));
    else
      this.DataWatcher.watch(16, Byte.valueOf((byte)(i & 0xFFFFFFFE)));
  }

  public EntityType GetEntityTypeId()
  {
    return EntityType.BAT;
  }

  public String getHurtSound()
  {
    return "mob.bat.hurt";
  }
}