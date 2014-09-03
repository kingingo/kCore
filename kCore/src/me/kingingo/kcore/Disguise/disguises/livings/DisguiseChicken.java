package me.kingingo.kcore.Disguise.disguises.livings;
import me.kingingo.kcore.Disguise.disguises.DisguiseAnimal;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

public class DisguiseChicken extends DisguiseAnimal
{
  public DisguiseChicken(Entity entity)
  {
    super(entity);
  }

  protected EntityType GetEntityTypeId()
  {
    return EntityType.CHICKEN;
  }

  public String getHurtSound()
  {
    return "mob.chicken.hurt";
  }
}