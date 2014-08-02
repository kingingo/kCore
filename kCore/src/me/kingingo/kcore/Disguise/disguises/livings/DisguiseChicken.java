package me.kingingo.kcore.Disguise.disguises.livings;
import me.kingingo.kcore.Disguise.disguises.DisguiseAnimal;

import org.bukkit.entity.Entity;

public class DisguiseChicken extends DisguiseAnimal
{
  public DisguiseChicken(Entity entity)
  {
    super(entity);
  }

  protected int GetEntityTypeId()
  {
    return 93;
  }

  public String getHurtSound()
  {
    return "mob.chicken.hurt";
  }
}