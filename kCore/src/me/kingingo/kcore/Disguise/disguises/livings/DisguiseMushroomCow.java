package me.kingingo.kcore.Disguise.disguises.livings;
import me.kingingo.kcore.Disguise.disguises.DisguiseAnimal;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

public class DisguiseMushroomCow extends DisguiseCow
{
  public DisguiseMushroomCow(Entity entity)
  {
    super(entity);
  }

  protected EntityType GetEntityTypeId()
  {
    return EntityType.MUSHROOM_COW;
  }

  public String getHurtSound()
  {
    return "mob.cow.hurt";
  }
}