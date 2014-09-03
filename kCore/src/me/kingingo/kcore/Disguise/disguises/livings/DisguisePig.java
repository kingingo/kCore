package me.kingingo.kcore.Disguise.disguises.livings;

import me.kingingo.kcore.Disguise.disguises.DisguiseAnimal;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

public class DisguisePig extends DisguiseAnimal
{
  public DisguisePig(Entity entity)
  {
    super(entity);
  }

  protected EntityType GetEntityTypeId()
  {
    return EntityType.PIG;
  }

  public String getHurtSound()
  {
    return "mob.pig.say";
  }
}