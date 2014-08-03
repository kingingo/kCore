package me.kingingo.kcore.Disguise.disguises.livings;

import me.kingingo.kcore.Disguise.disguises.DisguiseAnimal;
import org.bukkit.entity.Entity;

public class DisguisePig extends DisguiseAnimal
{
  public DisguisePig(Entity entity)
  {
    super(entity);
  }

  protected int GetEntityTypeId()
  {
    return 90;
  }

  public String getHurtSound()
  {
    return "mob.pig.say";
  }
}