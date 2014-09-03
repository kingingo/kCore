package me.kingingo.kcore.Disguise.disguises.livings;
import me.kingingo.kcore.Disguise.disguises.DisguiseTameableAnimal;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

public class DisguiseCat extends DisguiseTameableAnimal
{
  public DisguiseCat(Entity entity)
  {
    super(entity);

    this.DataWatcher.a(18, Byte.valueOf((byte)0));
  }

  public int getCatType()
  {
    return this.DataWatcher.getByte(18);
  }

  public void setCatType(int i)
  {
    this.DataWatcher.watch(18, Byte.valueOf((byte)i));
  }

  protected EntityType GetEntityTypeId()
  {
    return EntityType.OCELOT;
  }

  protected String getHurtSound()
  {
    return "mob.cat.hitt";
  }
}