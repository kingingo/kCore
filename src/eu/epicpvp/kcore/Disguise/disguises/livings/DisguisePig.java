package eu.epicpvp.kcore.Disguise.disguises.livings;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import eu.epicpvp.kcore.Disguise.disguises.DisguiseAnimal;

public class DisguisePig extends DisguiseAnimal
{
  public DisguisePig(Entity entity)
  {
    super(entity);
    this.DataWatcher.a(16, Byte.valueOf((byte)0));
  }

  public EntityType GetEntityTypeId()
  {
    return EntityType.PIG;
  }

  public String getHurtSound()
  {
    return "mob.pig.say";
  }
  
  public boolean hasSaddle()
  {
    return (this.DataWatcher.getByte(16) & 0x1) != 0;
  }

  public void setSaddle(boolean flag) {
    if (flag)
      this.DataWatcher.watch(16, Byte.valueOf((byte)1));
    else
      this.DataWatcher.watch(16, Byte.valueOf((byte)0));
  }
}