package eu.epicpvp.kcore.Disguise.disguises.livings;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import eu.epicpvp.kcore.Disguise.disguises.DisguiseAnimal;

public class DisguiseChicken extends DisguiseAnimal
{
  public DisguiseChicken(Entity entity)
  {
    super(entity);
  }

  public EntityType GetEntityTypeId()
  {
    return EntityType.CHICKEN;
  }

  public String getHurtSound()
  {
    return "mob.chicken.hurt";
  }
}