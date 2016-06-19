package eu.epicpvp.kcore.Disguise.disguises.livings;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import eu.epicpvp.kcore.Disguise.disguises.DisguiseCreature;

public class DisguiseEnderdragon extends DisguiseCreature
{
  public DisguiseEnderdragon(Entity entity)
  {
    super(entity);
  }

  public EntityType GetEntityTypeId()
  {
    return EntityType.ENDER_DRAGON;
  }

  public String getHurtSound()
  {
    return "mob.enderdragon.growl";
  }

}