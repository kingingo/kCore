package me.kingingo.kcore.Disguise.disguises.livings;
import me.kingingo.kcore.Disguise.disguises.DisguiseAnimal;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

public class DisguiseCow extends DisguiseAnimal
{
  public DisguiseCow(Entity entity)
  {
    super(entity);
  }

  public EntityType GetEntityTypeId()
  {
    return EntityType.COW;
  }

  public String getHurtSound()
  {
    return "mob.cow.hurt";
  }
}