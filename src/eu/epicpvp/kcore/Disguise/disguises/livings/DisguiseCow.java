package eu.epicpvp.kcore.Disguise.disguises.livings;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import eu.epicpvp.kcore.Disguise.disguises.DisguiseAnimal;

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