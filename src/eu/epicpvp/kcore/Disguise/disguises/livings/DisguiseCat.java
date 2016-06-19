package eu.epicpvp.kcore.Disguise.disguises.livings;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import eu.epicpvp.kcore.Disguise.disguises.DisguiseTameableAnimal;

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

  public EntityType GetEntityTypeId()
  {
    return EntityType.OCELOT;
  }

  protected String getHurtSound()
  {
    return "mob.cat.hitt";
  }
}